package com.library.service

import com.library.controller.response.PageResult
import com.library.controller.response.SearchResponse
import com.library.repository.BookRepository
import io.github.resilience4j.circuitbreaker.CallNotPermittedException
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class BookQueryService(
    @Qualifier("naverBookRepository")
    private val naverBookRepository: BookRepository,
    @Qualifier("kakaoBookRepository")
    private val kakaoBookRepository: BookRepository,
) {
    @CircuitBreaker(name = "naverSearch", fallbackMethod = "searchFallBack")
    fun search(query: String, page: Int, size: Int): PageResult<SearchResponse> {
        logger.info("[BookQueryService] naver query = {}, page = {}, size = {}", query, page, size)
        return naverBookRepository.search(query, page, size)
    }

    fun searchFallBack(query: String, page: Int, size: Int, throwable: Throwable): PageResult<SearchResponse> {
        if (throwable is CallNotPermittedException) {
            return handleOpenCircuit(query, page, size)
        }
        return handleException(query, page, size, throwable)
    }

    private fun handleOpenCircuit(query: String, page: Int, size: Int): PageResult<SearchResponse> {
        logger.warn("[BookQueryService] Circuit Breaker is open! Fallback to kakao search.")
        return kakaoBookRepository.search(query, page, size)
    }

    private fun handleException(query: String, page: Int, size: Int, throwable: Throwable): PageResult<SearchResponse> {
        logger.error("[BookQueryService] An error occurred! Fallback to kakao search. errorMessage={}", throwable.message)
        return kakaoBookRepository.search(query, page, size)
    }
}