package com.library

import org.springframework.http.HttpStatus

class ApiException(
    private val errorMessage: String, errorType: ErrorType,
    private val httpStatus: HttpStatus
) :
    RuntimeException() {
    private val errorType: ErrorType = errorType
}
