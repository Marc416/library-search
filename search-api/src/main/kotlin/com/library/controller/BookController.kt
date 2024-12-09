package com.library.controller

import com.library.controller.request.SearchRequest
import com.library.controller.response.PageResult
import com.library.controller.response.SearchResponse
import com.library.controller.response.StatResponse
import com.library.service.BookApplicationService
import jakarta.validation.Valid
import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

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

    @GetMapping("/stats")
    fun findQueryStats(
        @RequestParam(name = "query") query: String,
        @RequestParam(name = "date") date: LocalDate,
    ): StatResponse {
        logger.info("[BookController] find stats query={}, date={}", query, date)
        return bookApplicationService.findQueryCount(query = query, date = date)
    }

    @GetMapping("/stats/ranking")
    fun findTop5Stats(): List<StatResponse> {
        logger.info("[BookController] find top 5 stats")
        return bookApplicationService.findTop5Query()
    }
}