package com.library.service

import com.library.controller.response.PageResult
import com.library.controller.response.SearchResponse
import com.library.repository.BookRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service

val logger = KotlinLogging.logger {}

@Service
class BookQueryService(
    private val naverBookRepository: BookRepository,
) {

    fun search(query: String?, page: Int, size: Int): PageResult<SearchResponse> {
        logger.info("[BookQueryService] naver query = {}, page = {}, size = {}", query, page, size)
        return naverBookRepository.search(query, page, size)
    }
}