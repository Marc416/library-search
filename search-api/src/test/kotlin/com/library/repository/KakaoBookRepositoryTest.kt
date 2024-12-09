package com.library.repository

import com.library.feign.KakaoClient
import com.library.feign.response.*
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.time.LocalDate

class KakaoBookRepositoryTest {
    private lateinit var bookRepository: BookRepository
    private lateinit var kakaoClient: KakaoClient

    @BeforeEach
    fun setup() {
        this.kakaoClient = Mockito.mock(KakaoClient::class.java)
        this.bookRepository = KakaoBookRepository(kakaoClient)
    }

    @Test
    fun `search호출시 적절한 데이터형식으로 변환한다`() {
        // Arrange
        val documents = listOf(
            Document(
                title = "제목1",
                authors = listOf("저자1"),
                publisher = "출판사1",
                datetime = "2016-02-01T00:00:00.000+09:00",
                isbn = "isbn1"
            ),
            Document(
                title = "제목2",
                authors = listOf("저자2"),
                publisher = "출판사2",
                datetime = "2016-02-02T00:00:00.000+09:00",
                isbn = "isbn2"
            )
        )
        val givenPage = 1
        val givenSize = 2
        val totalCount = 10

        val meta = Meta(false, pageableCount = givenPage, totalCount = totalCount)
        val response = KakaoBookResponse(documents, meta)
        `when`(kakaoClient.search("HTTP", page = givenPage, size = givenSize)).thenReturn(response)

        // Action
        val result = bookRepository.search("HTTP", page = givenPage, size = givenSize)

        // Assert
        Assertions.assertThat(result.page).isEqualTo(givenPage)
        Assertions.assertThat(result.size).isEqualTo(givenSize)
        Assertions.assertThat(result.totalElements).isEqualTo(totalCount)
        Assertions.assertThat(result.contents.size).isEqualTo(givenSize)
        Assertions.assertThat(result.contents[0].pubDate).isEqualTo(LocalDate.of(2016, 2, 1))
    }
}