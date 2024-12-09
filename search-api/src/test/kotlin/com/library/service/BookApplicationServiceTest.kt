package com.library.service

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.argThat

class BookApplicationServiceTest {

    lateinit var bookApplicationService: BookApplicationService
    val bookQueryService = mock(BookQueryService::class.java)
    val dailyStatCommandService = mock(DailyStatCommandService::class.java)

    @BeforeEach
    fun init() {
        this.bookApplicationService = BookApplicationService(bookQueryService, dailyStatCommandService)
    }

    @Test
    fun `search메서드 호출시 검색결과를 반환하면서 통계데이터를 저장한다`() {
        // Arrange
        val givenQuery = "HTTP"
        val givenPage = 1
        val givenSize = 10

        // Action
        bookApplicationService.search(givenQuery, givenPage, givenSize)

        // Assert
        verify(bookQueryService).search(query = eq(givenQuery), page = eq(givenPage), size = eq(givenSize))
        verify(dailyStatCommandService).save(argThat { it ->
            it.query == givenQuery
        })
    }
}