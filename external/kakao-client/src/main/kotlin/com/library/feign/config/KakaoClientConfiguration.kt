package com.library.feign.config

import com.fasterxml.jackson.databind.ObjectMapper
import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean

class KakaoClientConfiguration {
    @Bean
    fun requestInterceptor(@Value("\${external.kakao.headers.rest-api-key}") restApiKey: String): RequestInterceptor {
        return RequestInterceptor { requestTemplate: RequestTemplate ->
            requestTemplate.header(
                "Authorization",
                "KakaoAK $restApiKey"
            )
        }
    }

    @Bean
    fun kakaoErrorDecoder(objectMapper: ObjectMapper): KakaoErrorDecoder {
        return KakaoErrorDecoder(objectMapper)
    }
}