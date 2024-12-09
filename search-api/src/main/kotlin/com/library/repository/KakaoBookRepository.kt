package com.library.repository

import com.library.controller.response.PageResult
import com.library.controller.response.SearchResponse
import com.library.feign.KakaoClient
import com.library.feign.response.Document
import com.library.feign.response.KakaoBookResponse
import com.library.util.DateUtils
import org.springframework.stereotype.Repository

@Repository
class KakaoBookRepository(
    private val kakaoClient: KakaoClient
) : BookRepository {

    override fun search(query: String, page: Int, size: Int): PageResult<SearchResponse> {
        val response: KakaoBookResponse = kakaoClient.search(query, page, size)
        val responses: List<SearchResponse> = response.documents.stream()
            .map { document: Document -> this.createResponse(document) }.toList()
        return PageResult(page, size, response.meta.totalCount, responses)
    }

    private fun createResponse(document: Document): SearchResponse {
        return SearchResponse(
            title = document.title,
            author = document.authors[0],
            publisher = document.publisher,
            pubDate = DateUtils.parseOffsetDateTime(document.datetime).toLocalDate(),
            isbn = document.isbn
        )
    }
}