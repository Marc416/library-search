package com.library.config

import com.library.ApiException
import com.library.ErrorType
import com.library.controller.response.ErrorResponse
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.resource.NoResourceFoundException
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

    @ExceptionHandler(NoResourceFoundException::class)
    fun handleNoResourceFoundException(e: NoResourceFoundException): ResponseEntity<ErrorResponse> {
        logger.error("NoResourceFound Exception occurred. message={}, className={}", e.message, e.javaClass.name)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse(
                    ErrorType.NO_RESOURCE.description,
                    ErrorType.NO_RESOURCE
                )
            )
    }

    // TODO 수정예정 - 동작하지 않음
    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingServletRequestParameterException(e: MissingServletRequestParameterException): ResponseEntity<ErrorResponse> {
        logger.error(
            "MissingServletRequestParameter Exception occurred. parameterName={}, message={}",
            e.parameterName,
            e.message
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse(
                    errorMessage = ErrorType.INVALID_PARAMETER.description,
                    errorType = ErrorType.INVALID_PARAMETER
                )
            )
    }

    // TODO 수정예정 - 동작하지 않음
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchException(e: MethodArgumentTypeMismatchException): ResponseEntity<ErrorResponse> {
        logger.error("MethodArgumentTypeMismatch Exception occurred. message={}", e.message)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse(
                    ErrorType.INVALID_PARAMETER.description,
                    ErrorType.INVALID_PARAMETER
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

    private fun createMessage(e: BindException): String {
        if (e.fieldError != null && e.fieldError!!.defaultMessage != null) {
            return e.fieldError!!.defaultMessage!!
        }

        return e.fieldErrors.stream()
            .map { obj: FieldError -> obj.field }
            .collect(Collectors.joining(", ")) + " 값들이 정확하지 않습니다."
    }
}