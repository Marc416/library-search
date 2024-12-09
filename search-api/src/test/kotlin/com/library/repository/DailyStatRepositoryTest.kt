package com.library.repository

import com.library.entity.DailyStat
import com.library.feign.NaverClient
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.LocalDateTime

@ActiveProfiles("test")
@DataJpaTest
class DailyStatRepositoryTest {
    @Autowired
    private lateinit var dailyStatRepository: DailyStatRepository

    @Autowired
    private lateinit var entityManager: EntityManager

    @MockitoBean
    private lateinit var naverClient: NaverClient

    @Test
    fun `저장후 조회가된다`() {
        // Arrange
        val givenQuery = "HTTP"

        // Action
        val dailyStat = DailyStat(givenQuery, LocalDateTime.now())
        val saved = dailyStatRepository.saveAndFlush(dailyStat)
        assertThat(saved.id).isNotNull()

        // entityManager를 clear하고 재조회한다.
        entityManager.clear()
        val result = dailyStatRepository.findById(saved.id)

        // 캐시가아닌 실제 DB쿼리로 데이터를 가져온다.
        assertThat(result.isPresent).isTrue()
        assertThat(result.get().query).isEqualTo(givenQuery)

    }

    @Test
    fun `쿼리의 카운트를 조회한다`() {
        // Arrange
        val givenQuery = "HTTP"
        val now = LocalDateTime.of(2024, 5, 2, 0, 0, 0)
        val stat1 = DailyStat(givenQuery, now.plusMinutes(10))
        val stat2 = DailyStat(givenQuery, now.minusMinutes(1))
        val stat3 = DailyStat(givenQuery, now.plusMinutes(10))
        val stat4 = DailyStat("JAVA", now.plusMinutes(10))

        dailyStatRepository.saveAll(listOf(stat1, stat2, stat3, stat4))

        // Action
        val result = dailyStatRepository.countByQueryAndEventDateTimeBetween(givenQuery, now, now.plusDays(1))

        // Assert
        assertThat(result).isEqualTo(2)
    }

    @Test
    fun `가장 많이 검색된 쿼리 키워드를 개수와 함께 상위 3개반환한다`() {
        // Arrange
        val now = LocalDateTime.now()
        val stat1 = DailyStat("HTTP", now.plusMinutes(10))
        val stat2 = DailyStat("HTTP", now.plusMinutes(10))
        val stat3 = DailyStat("HTTP", now.plusMinutes(10))
        val stat4 = DailyStat("JAVA", now.plusMinutes(10))
        val stat5 = DailyStat("JAVA", now.plusMinutes(10))
        val stat6 = DailyStat("JAVA", now.plusMinutes(10))
        val stat7 = DailyStat("JAVA", now.plusMinutes(10))
        val stat8 = DailyStat("SPRING", now.plusMinutes(10))
        val stat9 = DailyStat("SPRING", now.plusMinutes(10))
        val stat10 = DailyStat("OS", now.plusMinutes(10))

        dailyStatRepository.saveAll(listOf(stat1, stat2, stat3, stat4, stat5, stat6, stat7, stat8, stat9, stat10))

        // Action
        val request = PageRequest.of(0, 3)
        val response = dailyStatRepository.findTopQuery(request)

        // Assert
        assertThat(response.size).isEqualTo(3)
        assertThat(response[0].query).isEqualTo("JAVA")
        assertThat(response[0].count).isEqualTo(4)
        assertThat(response[1].query).isEqualTo("HTTP")
        assertThat(response[1].count).isEqualTo(3)
        assertThat(response[2].query).isEqualTo("SPRING")
        assertThat(response[2].count).isEqualTo(2)
    }
}