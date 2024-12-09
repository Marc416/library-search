package com.library.service.event

import com.library.entity.DailyStat
import com.library.service.DailyStatCommandService
import mu.KotlinLogging
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

val logger = KotlinLogging.logger { }

@Component
class SearchEventHandler(
    private val dailyStatCommandService: DailyStatCommandService
) {
    @Async
    @EventListener
    fun handleEvent(event: SearchEvent) {
        Thread.sleep(5000)
        logger.info("[SearchEventHandler] handleEvent: {}", event)
        val dailyStat = DailyStat(event.query, event.timestamp)
        dailyStatCommandService.save(dailyStat)
    }
}