package com.library.service.event

import com.library.entity.DailyStat
import com.library.service.DailyStatCommandService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.isA
import org.mockito.kotlin.verify
import java.time.LocalDateTime

class SearchEventHandlerTest {
    @Test
    fun `handleEvent`() {
        // Arrange
        val commandService = mock(DailyStatCommandService::class.java)
        val eventHandler = SearchEventHandler(commandService)
        val event =  SearchEvent("HTTP", LocalDateTime.now())

        // Action
        eventHandler.handleEvent(event)

        // Assert
        verify(commandService).save(isA<DailyStat>())
    }
}