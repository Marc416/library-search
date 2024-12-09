package com.library.repository

import com.library.controller.response.PageResult
import com.library.controller.response.SearchResponse
import com.library.feign.NaverClient
import com.library.feign.response.Item
import com.library.util.DateUtils
import org.springframework.stereotype.Repository

@Repository
class NaverBookRepository(
    private val naverClient: NaverClient
) : BookRepository {
    override fun search(query: String?, page: Int, size: Int): PageResult<SearchResponse> {
        val response = naverClient.search(query = query, start = page, display = size)
        val responses = response.items.map { createResponse(it) }
        return PageResult(
            page = page,
            size = size,
            contents = responses,
            totalElements = response.total
        )
    }

    private fun createResponse(item: Item): SearchResponse {
        return SearchResponse(
            title = item.title,
            author = item.author,
            publisher = item.publisher,
            pubDate = DateUtils.parseYYYYMMDD(item.pubDate),
            isbn = item.isbn
        )
    }
}