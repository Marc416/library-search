package com.library.service

import com.library.controller.response.PageResult
import com.library.controller.response.SearchResponse
import com.library.service.event.SearchEvent
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.eq
import org.mockito.kotlin.isA
import org.springframework.context.ApplicationEventPublisher
import java.time.LocalDate

class BookApplicationServiceTest {

    lateinit var bookApplicationService: BookApplicationService
    val bookQueryService = mock(BookQueryService::class.java)
    val dailyStatQueryService = mock(DailyStatQueryService::class.java)
    val eventPublisher = mock(ApplicationEventPublisher::class.java)

    @BeforeEach
    fun init() {
        this.bookApplicationService =
            BookApplicationService(
                bookQueryService = bookQueryService,
                dailyStatQueryService = dailyStatQueryService,
                eventPublisher = eventPublisher
            )
    }

    @Test
    fun `search메서드 호출시 검색결과를 반환하면서 통계데이터를 저장한다`() {
        // Arrange
        val givenQuery = "HTTP"
        val givenPage = 1
        val givenSize = 10
        `when`(bookQueryService.search(query = eq(givenQuery), page = eq(givenPage), size = eq(givenSize))).thenReturn(
            PageResult(
                page = 1,
                size = 10,
                totalElements = 10,
                contents = listOf(mock(SearchResponse::class.java))
            )
        )

        // Action
        bookApplicationService.search(givenQuery, givenPage, givenSize)

        // Assert
        verify(bookQueryService).search(query = eq(givenQuery), page = eq(givenPage), size = eq(givenSize))
        verify(eventPublisher).publishEvent(isA<SearchEvent>())
    }

    @Test
    fun `findQueryCount메서드 호출시 인자를 그대로 넘긴다`() {
        // Arrange
        val givenQuery = "HTTP"
        val givenDate = LocalDate.of(2024, 5, 1)

        // Action
        bookApplicationService.findQueryCount(givenQuery, givenDate)

        // Assert
        verify(dailyStatQueryService).findQueryCount(givenQuery, givenDate)
    }

    @Test
    fun `findTop5Query메서드 호출시 dailyStatQueryService의 findTop5Query가 호출된다`() {
        // Arrange, Arrange
        bookApplicationService.findTop5Query()

        // Assert
        verify(dailyStatQueryService).findTop5Query()
    }
}