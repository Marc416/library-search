package com.library.repository

import com.library.feign.NaverClient
import com.library.feign.response.Item
import com.library.feign.response.NaverBookResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.time.LocalDate

class NaverBookRepositoryTest {

    private lateinit var bookRepository: BookRepository
    private lateinit var naverClient: NaverClient

    @BeforeEach
    fun setup() {
        this.naverClient = mock(NaverClient::class.java)
        this.bookRepository = NaverBookRepository(naverClient)
    }

    @Test
    fun `search호출시 적절한 데이터형식으로 변환한다`() {
        // Arrange
        val items = listOf(
            Item(
                title = "제목1",
                link = "http://link1",
                image = "http://image1",
                author = "저자1",
                discount = "1000",
                publisher = "출판사1",
                pubDate = "20240101",
                isbn = "isbn1",
                description = "설명1"

            ),
            Item(
                title = "제목2",
                link = "http://link2",
                image = "http://image2",
                author = "저자2",
                discount = "2000",
                publisher = "출판사2",
                pubDate = "20240102",
                isbn = "isbn2",
                description = "설명2"
            )
        )
        val givenPage = 1
        val givenSize = 2
        val totalElementSize = 2
        val startPage = 1


        val response = NaverBookResponse(
            lastBuildDate = "Wed, 29 May 2024 21:12:29 +0900",
            total = totalElementSize,
            start = startPage,
            display = givenSize,
            items = items
        )
        `when`(naverClient.search("HTTP", givenPage, givenSize)).thenReturn(response)

        // Action
        val result = bookRepository.search("HTTP", givenPage, givenSize)

        // Assert
        assertThat(result.page).isEqualTo(givenPage)
        assertThat(result.size).isEqualTo(givenSize)
        assertThat(result.totalElements).isEqualTo(totalElementSize)
        assertThat(result.contents.size).isEqualTo(totalElementSize)
        assertThat(result.contents[0].pubDate).isEqualTo(LocalDate.of(2024, 1, 1))
    }
}

