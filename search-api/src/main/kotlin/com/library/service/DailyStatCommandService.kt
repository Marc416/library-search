package com.library.service

import com.library.entity.DailyStat
import com.library.repository.DailyStatRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val logger = KotlinLogging.logger { }

@Service
class DailyStatCommandService(
    private val dailyStatRepository: DailyStatRepository
) {
    @Transactional
    fun save(dailyStat: DailyStat) {
        logger.info("save daily stats: {}", dailyStat)
        dailyStatRepository.save(dailyStat)
    }
}
