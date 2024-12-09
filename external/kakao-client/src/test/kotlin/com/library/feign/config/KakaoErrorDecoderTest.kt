package com.library.feign.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.library.ApiException
import com.library.ErrorType
import com.library.feign.response.KakaoErrorResponse
import feign.Request
import feign.Response
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.springframework.http.HttpStatus
import java.io.ByteArrayInputStream

class KakaoErrorDecoderTest {
    lateinit var objectMapper: ObjectMapper
    lateinit var errorDecoder: KakaoErrorDecoder

    @BeforeEach
    fun init() {
        this.objectMapper = mock(ObjectMapper::class.java)
        this.errorDecoder = KakaoErrorDecoder(objectMapper)
    }

    @Test
    fun `에러디코더에서 에러발생시 ApiException 예외가 throw 된다`(){
        // Arrange
        val responseBody = mock(Response.Body::class.java)
        val inputStream = ByteArrayInputStream("{}".toByteArray())
        Mockito.`when`(responseBody.asInputStream()).thenReturn(inputStream)
        Mockito.`when`(
            objectMapper.readValue(
                ArgumentMatchers.any(String::class.java),
                ArgumentMatchers.eq(KakaoErrorResponse::class.java)
            )
        ).thenReturn(KakaoErrorResponse("InvalidArgument", "size is more than max"))
        val response = Response.builder()
            .status(400)
            .request(
                Request.create(
                    Request.HttpMethod.GET,
                    "testUrl",
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
        Assertions.assertThat(exception.errorMessage).isEqualTo("size is more than max")
        Assertions.assertThat(exception.httpStatus).isEqualTo(HttpStatus.BAD_REQUEST)
        Assertions.assertThat(exception.errorType).isEqualTo(ErrorType.EXTERNAL_API_ERROR)
    }
}