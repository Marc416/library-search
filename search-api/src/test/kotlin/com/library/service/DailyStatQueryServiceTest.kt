package com.library.service

import com.library.repository.DailyStatRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.argThat
import java.time.LocalDate

class DailyStatQueryServiceTest {
    lateinit var dailyStatQueryService: DailyStatQueryService

    val dailyStatRepository = mock(DailyStatRepository::class.java)

    @BeforeEach
    fun init() {
        this.dailyStatQueryService = DailyStatQueryService(dailyStatRepository)
    }

    @Test
    fun `findQueryCount 조회시 하루치를 조회하면서 쿼리개수가 반환된다`() {
        // Arrange
        val givenQuery = "HTTP"
        val givenDate = LocalDate.of(2024, 5, 1)
        val expectedCount = 10L

        // Action
        `when`(
            dailyStatRepository.countByQueryAndEventDateTimeBetween(
                givenQuery,
                givenDate.atStartOfDay(),
                givenDate.atTime(23, 59, 59, 999999999)
            )
        ).thenReturn(expectedCount)
        val response = dailyStatQueryService.findQueryCount(givenQuery, givenDate)

        // Assert
        verify(dailyStatRepository).countByQueryAndEventDateTimeBetween(
            givenQuery,
            givenDate.atStartOfDay(),
            givenDate.atTime(23, 59, 59, 999999999)
        )
        assertThat(response.count).isEqualTo(expectedCount)

    }

    @Test
    fun `findTop5Query 조회시 상위 5개 반환 요청이 들어간다`() {
        // Arrange, Action
        val response = dailyStatQueryService.findTop5Query()

        // Assert
        verify(dailyStatRepository).findTopQuery(argThat { it ->
            it.pageSize == 5 && it.pageNumber == 0
        })

    }
}