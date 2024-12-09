package com.library.feign.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.library.ApiException
import com.library.ErrorType
import com.library.feign.response.KakaoBookResponse
import com.library.feign.response.KakaoErrorResponse
import feign.Response
import feign.codec.ErrorDecoder
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import java.io.IOException
import java.nio.charset.StandardCharsets

private val logger = KotlinLogging.logger {}

class KakaoErrorDecoder(private val objectMapper: ObjectMapper) : ErrorDecoder {
    override fun decode(methodKey: String, response: Response): Exception {
        try {
            val body = String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8)
            val errorResponse: KakaoErrorResponse = objectMapper.readValue(body, KakaoErrorResponse::class.java)
            throw ApiException(
                errorResponse.message,
                ErrorType.EXTERNAL_API_ERROR,
                HttpStatus.valueOf(response.status())
            )
        } catch (e: IOException) {
            logger.error(
                "[Kakao] 에러 메세지 파싱 에러 code={}, request={}, methodKey={}, errorMessage={}",
                response.status(),
                response.request(),
                methodKey,
                e.message
            )
            throw ApiException("카카오 메세지 파싱에러", ErrorType.EXTERNAL_API_ERROR, HttpStatus.valueOf(response.status()))
        }
    }
}
