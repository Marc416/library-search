package com.library.controller

import com.library.controller.request.SearchRequest
import com.library.controller.response.ErrorResponse
import com.library.controller.response.PageResult
import com.library.controller.response.SearchResponse
import com.library.controller.response.StatResponse
import com.library.service.BookApplicationService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/v1/books")
@Tag(name = "book", description = "book api")
class BookController(
    private val bookApplicationService: BookApplicationService
) {
    @Operation(summary = "search API", description = "도서 검색결과 제공 ", tags = ["book"])
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            content = arrayOf(Content(schema = Schema(implementation = PageResult::class)))
        ), ApiResponse(
            responseCode = "400",
            content = arrayOf(Content(schema = Schema(implementation = ErrorResponse::class)))
        )]
    )
    @GetMapping("")
    fun search(@Valid request: SearchRequest): PageResult<SearchResponse> {
        logger.info("[BookController] search={}", request)
        return bookApplicationService.search(request.query, request.page, request.size)
    }

    @Operation(summary = "stats API", description = "쿼리 통계결과 제공 ", tags = ["book"])
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            content = arrayOf(Content(schema = Schema(implementation = StatResponse::class)))
        ), ApiResponse(
            responseCode = "400",
            content = arrayOf(Content(schema = Schema(implementation = ErrorResponse::class)))
        )]
    )
    @GetMapping("/stats")
    fun findQueryStats(
        @RequestParam(name = "query") query: String,
        @RequestParam(name = "date") date: LocalDate,
    ): StatResponse {
        logger.info("[BookController] find stats query={}, date={}", query, date)
        return bookApplicationService.findQueryCount(query = query, date = date)
    }

    @Operation(summary = "stats ranking API", description = "상위 쿼리 통계결과 제공 ", tags = ["book"])
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            content = arrayOf(Content(array = ArraySchema(schema = Schema(implementation = StatResponse::class))))
        ), ApiResponse(
            responseCode = "400",
            content = arrayOf(Content(schema = Schema(implementation = ErrorResponse::class)))
        )]
    )
    @GetMapping("/stats/ranking")
    fun findTop5Stats(): List<StatResponse> {
        logger.info("[BookController] find top 5 stats")
        return bookApplicationService.findTop5Query()
    }
}