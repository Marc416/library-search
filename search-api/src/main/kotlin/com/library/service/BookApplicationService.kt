package com.library.service

import com.library.controller.response.PageResult
import com.library.controller.response.SearchResponse
import com.library.controller.response.StatResponse
import com.library.entity.DailyStat
import com.library.service.event.SearchEvent
import mu.KotlinLogging
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

private val logger = KotlinLogging.logger { }

@Service
class BookApplicationService(
    private val bookQueryService: BookQueryService,
    private val dailyStatQueryService: DailyStatQueryService,
    private val eventPublisher: ApplicationEventPublisher,
) {
    fun search(query: String, page: Int, size: Int): PageResult<SearchResponse> {
        val response = bookQueryService.search(query, page, size)
        if (response.contents.isNotEmpty()) {
            logger.info("검색결과 개수: {}", response.size)
            eventPublisher.publishEvent(SearchEvent(query, LocalDateTime.now()))
        }
        return response
    }

    fun findQueryCount(query: String, date: LocalDate): StatResponse {
        return dailyStatQueryService.findQueryCount(query = query, date = date)
    }

    fun findTop5Query(): List<StatResponse> {
        return dailyStatQueryService.findTop5Query()
    }
}