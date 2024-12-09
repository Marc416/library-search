package com.library.controller.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "페이징 결과")
data class PageResult<T>(
    @Schema(description = "현재 페이지번호", example = "1")
    val page: Int,
    @Schema(description = "페이지 크기", example = "10")
    val size: Int,
    @Schema(description = "전체 요소수", example = "100")
    val totalElements: Int,
    @Schema(description = "본문")
    val contents: List<T>
)
