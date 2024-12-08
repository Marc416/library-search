package com.library.feign.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.library.ApiException
import com.library.ErrorType
import com.library.feign.response.NaverErrorResponse
import feign.Request
import feign.Response
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.http.HttpStatus
import java.io.ByteArrayInputStream

class NaverErrorDecoderTest {
    val objectMapper: ObjectMapper = mock()
    val errorDecoder = NaverErrorDecoder(objectMapper)

    @Test
    fun `에러디코더에서 에러발생시 ApiException 예외가 throw 된다`() {
        // Arrange
        val responseBody: Response.Body = mock()
        val inputStream = ByteArrayInputStream("{}".toByteArray())
        `when`(responseBody.asInputStream()).thenReturn(inputStream)
        `when`(objectMapper.readValue(any(String::class.java), eq(NaverErrorResponse::class.java)))
            .thenReturn(NaverErrorResponse("error!!", "SE03"))
        val response = Response.builder()
            .status(400)
            .request(
                Request.create(
                    Request.HttpMethod.GET,
                    "url",
                    mapOf(),
                    Request.Body.empty(),
                    null
                )
            )
            .body(responseBody)
            .build()
        // Action, Assert
        val exception = assertThrows<ApiException> {
            errorDecoder.decode("_", response)
        }
        assertThat(exception.errorMessage).isEqualTo("error!!")
        assertThat(exception.httpStatus).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(exception.errorType).isEqualTo(ErrorType.EXTERNAL_API_ERROR)

    }
}