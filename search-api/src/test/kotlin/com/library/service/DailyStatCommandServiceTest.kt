package com.library.service

import com.library.entity.DailyStat
import com.library.repository.DailyStatRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.time.LocalDateTime

class DailyStatCommandServiceTest {

    val dailyStatRepository = mock(DailyStatRepository::class.java)
    lateinit var dailyStatCommandService: DailyStatCommandService

    @BeforeEach
    fun init() {
        this.dailyStatCommandService = DailyStatCommandService(dailyStatRepository)
    }

    @Test
    fun `저장시 넘어온 인자 그대로 호출된다`() {
        // Arrange
        val givenDailyStat = DailyStat(query = "HTTP", eventDateTime = LocalDateTime.now())
        // Action
        dailyStatCommandService.save(givenDailyStat)
        // Assert
        val captor = ArgumentCaptor.forClass(DailyStat::class.java)
        verify(dailyStatRepository).save(captor.capture()) // Captures the argument passed to save
        val capturedDailyStat = captor.value

        assertThat(capturedDailyStat.query).isEqualTo(givenDailyStat.query)
        assertThat(capturedDailyStat.eventDateTime).isEqualTo(givenDailyStat.eventDateTime)
    }

}