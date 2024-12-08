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
@SpringBootTest(classes = [NaverClientIntegrationTest.TestConfig::class])
@ActiveProfiles("test")
class NaverClientIntegrationTest {

    @EnableAutoConfiguration
    @EnableFeignClients(clients = [NaverClient::class])
    class TestConfig

    @Autowired
    lateinit var naverClient: NaverClient

    @Test
    fun `naver 호출`() {
        // Arrange
        val http = naverClient.search("HTTP", 1, 10)
        // Action
        assertThat(http.items.size).isEqualTo(10)
    }
}