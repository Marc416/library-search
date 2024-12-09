package com.library.feign

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.test.context.ActiveProfiles
import kotlin.test.Ignore


@Ignore
@SpringBootTest(classes = [KakaoClientIntegrationTest.TestConfig::class])
@ActiveProfiles("test")
class KakaoClientIntegrationTest {
    @EnableAutoConfiguration
    @EnableFeignClients(clients = [KakaoClient::class])
    class TestConfig

    @Autowired
    lateinit var kakaoClient: KakaoClient

    @Test
    fun `kakao 호출`() {
        // Arrange
        val response = kakaoClient.search("HTTP", 1, 10)
        // Action
        assertThat(response.meta.totalCount).isGreaterThan(75)
    }
}


