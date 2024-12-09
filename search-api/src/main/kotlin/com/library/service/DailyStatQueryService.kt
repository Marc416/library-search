package com.library.service

import com.library.controller.response.StatResponse
import com.library.repository.DailyStatRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime

@Service
class DailyStatQueryService(
    private val dailyStatRepository: DailyStatRepository
) {
    private val PAGE = 0
    private val SIZE = 5


    fun findQueryCount(query: String, date: LocalDate): StatResponse {
        val count = dailyStatRepository.countByQueryAndEventDateTimeBetween(
            query,
            date.atStartOfDay(),
            date.atTime(LocalTime.MAX)
        )
        return StatResponse(query, count)
    }

    fun findTop5Query(): List<StatResponse> {
        val pageable: Pageable = PageRequest.of(PAGE, SIZE)
        return dailyStatRepository.findTopQuery(pageable)
    }
}