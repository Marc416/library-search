package com.library.controller.response

import com.library.ErrorType

data class ErrorResponse(
    val errorMessage: String,
    val errorType: ErrorType
)