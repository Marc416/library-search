package com.library.repository

import com.library.entity.DailyStat
import com.library.feign.NaverClient
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
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
}