package com.library.controller.response

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

@Schema(description = "검색결과")
data class SearchResponse(
    @Schema(description = "제목", example = "HTTP완벽가이드")
    val title: String,
    @Schema(description = "저자", example = "데이빗고울리")
    val author: String,
    @Schema(description = "출판사", example = "인사이트")
    val publisher: String,
    @Schema(description = "출판일", example = "2015-01-01")
    val pubDate: LocalDate,
    @Schema(description = "isbn", example = "9788966261208")
    val isbn: String
)
