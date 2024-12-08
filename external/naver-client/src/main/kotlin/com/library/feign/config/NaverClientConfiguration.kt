package com.library.feign.config

import com.fasterxml.jackson.databind.ObjectMapper
import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean

class NaverClientConfiguration {
    @Bean
    fun requestInterceptor(
        @Value("\${external.naver.headers.client-id}") clientId: String,
        @Value("\${external.naver.headers.client-secret}") clientSecret: String
    ): RequestInterceptor {
        return RequestInterceptor { requestTemplate: RequestTemplate ->
            requestTemplate.header("X-Naver-Client-Id", clientId)
                .header("X-Naver-Client-Secret", clientSecret)
        }
    }

    @Bean
    fun naverErrorDecoder(objectMapper: ObjectMapper): NaverErrorDecoder {
        return NaverErrorDecoder(objectMapper)
    }
}
