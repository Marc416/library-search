package com.library.config

import com.library.ApiException
import com.library.ErrorType
import com.library.controller.response.ErrorResponse
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.stream.Collectors

val logger = KotlinLogging.logger {}

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(ApiException::class)
    fun handleApiException(e: ApiException): ResponseEntity<ErrorResponse> {
        logger.error("Api Exception occurred. message={}, className={}", e.errorMessage, e.javaClass.simpleName)
        return ResponseEntity.status(e.httpStatus)
            .body(ErrorResponse(errorMessage = e.errorMessage, errorType = e.errorType))
    }

    @ExceptionHandler(BindException::class)
    fun handleBindException(e: BindException): ResponseEntity<ErrorResponse> {
        logger.error("Bind Exception occurred. message={}, className={}", e.message, e.javaClass.simpleName)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse(
                    errorMessage = createMessage(e),
                    errorType = ErrorType.INVALID_PARAMETER
                )
            )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
        logger.error("Exception occurred. message={}, className={}", e.message, e.javaClass.simpleName)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ErrorResponse(
                    errorMessage = ErrorType.UNKNOWN.description,
                    errorType = ErrorType.UNKNOWN
                )
            )
    }

    private fun createMessage(e: BindException): String {
        if (e.fieldError != null && e.fieldError!!.defaultMessage != null) {
            return e.fieldError!!.defaultMessage!!
        }

        return e.fieldErrors.stream()
            .map { obj: FieldError -> obj.field }
            .collect(Collectors.joining(", ")) + " 값들이 정확하지 않습니다."
    }
}