package com.library

import org.springframework.http.HttpStatus

class ApiException(
    val errorMessage: String, errorType: ErrorType,
    val httpStatus: HttpStatus
) :
    RuntimeException() {
    val errorType: ErrorType = errorType
}
