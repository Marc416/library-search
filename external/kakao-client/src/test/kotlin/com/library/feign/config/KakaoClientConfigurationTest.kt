package com.library.feign.config

import feign.RequestTemplate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class KakaoClientConfigurationTest {
    lateinit var configuration : KakaoClientConfiguration

    @BeforeEach
    fun setup() {
        configuration = KakaoClientConfiguration()
    }
    @Test
    fun `requestInterceptor의 header에 key값들이 적용된다`(){
        // Arrange
        val template =  RequestTemplate()
        val restApiKey = "key"

        // interceptor를 타기전에 header가 존재하지 않는다.
        template.headers()["Authorization"] == null

        // Action
        // interceptor를 탄다.
        val interceptor = configuration.requestInterceptor(restApiKey)
        interceptor.apply(template)

        // Assert
        // interceptor를 탄 이후에는 header가 추가된다.
        template.headers()["Authorization"]?.contains("KakaoAK " + restApiKey)
    }
}