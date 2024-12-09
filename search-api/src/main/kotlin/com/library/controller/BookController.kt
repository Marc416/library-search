package com.library.controller

import com.library.controller.request.SearchRequest
import com.library.controller.response.PageResult
import com.library.controller.response.SearchResponse
import com.library.service.BookApplicationService
import jakarta.validation.Valid
import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/v1/books")
class BookController(
    private val bookApplicationService: BookApplicationService
) {
    @GetMapping("")
    fun search(@Valid request: SearchRequest): PageResult<SearchResponse> {
        logger.info("[BookController] search={}", request)
        return bookApplicationService.search(request.query, request.page, request.size)
    }
}