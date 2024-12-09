package com.library

import com.library.entity.DailyStat
import com.library.repository.DailyStatRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class ApplicationRunner(
    val dailyStatRepository: DailyStatRepository
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val stat1 = DailyStat("HTTP", LocalDateTime.now())
        val stat2 = DailyStat("HTTP", LocalDateTime.now())
        val stat3 = DailyStat("HTTP", LocalDateTime.now())
        val stat4 = DailyStat("HTTP", LocalDateTime.now())
        val stat5 = DailyStat("HTTP", LocalDateTime.now())
        val stat6 = DailyStat("HTTP", LocalDateTime.now())
        val stat7 = DailyStat("HTTP", LocalDateTime.now())

        val stat8 = DailyStat("JAVA", LocalDateTime.now())
        val stat9 = DailyStat("JAVA", LocalDateTime.now())

        val stat10 = DailyStat("Kotlin", LocalDateTime.now())

        val stat11 = DailyStat("Database", LocalDateTime.now())

        val stat12 = DailyStat("OS", LocalDateTime.now())

        dailyStatRepository.saveAll(
            listOf(
                stat1, stat2, stat3, stat4, stat5, stat6, stat7, stat8, stat9,
                stat10, stat11, stat12, stat3
            )
        )
    }
}