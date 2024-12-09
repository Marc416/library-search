package com.library.service

import com.library.controller.response.PageResult
import com.library.controller.response.SearchResponse
import com.library.repository.KakaoBookRepository
import com.library.repository.NaverBookRepository
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@SpringBootTest
class BookQueryServiceItTest {
    @Autowired
    lateinit var bookQueryService: BookQueryService

    @Autowired
    lateinit var circuitBreakerRegistry: CircuitBreakerRegistry

    @MockitoBean
    val kakaoBookRepository = mock(KakaoBookRepository::class.java)

    @MockitoBean
    val naverBookRepository = mock(NaverBookRepository::class.java)

    @Test
    fun `정상상황에서는 Circuit의 상태가 CLOSED이고 naver쪽으로 호출이 들어간다`() {
        // Arrange
        val keyword = "HTTP"
        val page = 1
        val size = 10

        // Action
        bookQueryService.search(keyword, page, size)
        verify(naverBookRepository, times(1)).search(keyword, page, size)

        // Assert

        val circuitBreaker = circuitBreakerRegistry.allCircuitBreakers.stream().findFirst().get()
        assertThat(circuitBreaker.state).isEqualTo(CircuitBreaker.State.CLOSED)
        verify(kakaoBookRepository, times(0)).search(keyword, page, size)
    }

    @Test
    fun `circuit이 open되면 kakao쪽으로 요청을 한다`() {
        // Arrange
        val keyword = "HTTP"
        val page = 1
        val size = 10
        val kakaoResponse = PageResult(1, 10, 0, emptyList<SearchResponse>())

        val config = CircuitBreakerConfig.custom()
            .slidingWindowSize(1)
            .minimumNumberOfCalls(1)
            .failureRateThreshold(50F)
            .build()
        circuitBreakerRegistry.circuitBreaker("naverSearch", config)

        // Action
        // naver쪽은 항상 예외가 발생한다.
        `when`(naverBookRepository.search(keyword, page, size)).thenThrow(RuntimeException("error!"))
        `when`(kakaoBookRepository.search(keyword, page, size)).thenReturn(kakaoResponse)
        val result = bookQueryService.search(keyword, page, size)
        // kakao쪽으로 Fallback 된다.
        verify(kakaoBookRepository, times(1)).search(keyword, page, size)

        // Assert
        // circuit이 OPEN된다.
        val circuitBreaker = circuitBreakerRegistry.getAllCircuitBreakers().stream().findFirst().get()
        assertThat(circuitBreaker.state).isEqualTo(CircuitBreaker.State.OPEN)
        assertThat(result).isEqualTo(kakaoResponse)
    }
}