package com.library.controller.response

import com.library.ErrorType
import io.swagger.v3.oas.annotations.media.Schema

data class ErrorResponse(
    @Schema(description = "에러 메세지", example = "잘못된 요청입니다")
    val errorMessage: String,
    @Schema(description = "에러 타입", example = "INVALID_PARAMETER")
    val errorType: ErrorType
)