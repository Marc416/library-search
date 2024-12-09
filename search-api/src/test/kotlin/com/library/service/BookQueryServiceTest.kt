package com.library.service

import com.library.repository.BookRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.eq
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class BookQueryServiceTest {
    private val naverBookRepository = mock(BookRepository::class.java)
    private val kakaoBookRepository = mock(BookRepository::class.java)
    private lateinit var bookQueryService: BookQueryService

    @BeforeEach
    fun init() {
        this.bookQueryService = BookQueryService(naverBookRepository, kakaoBookRepository)
    }

    @Test
    fun `search시 인자가 그대로 넘어가고 naver쪽으로 호출한다 (feign으로 값이 올바르게 넘어갔는지 검증)`() {
        // Arrange
        val givenQuery = "HTTP완벽가이드"
        val givenPage = 1
        val givenSize = 10

        // Action
        bookQueryService.search(givenQuery, givenPage, givenSize)

        // Assert
        verify(naverBookRepository, times(1)).search(eq(givenQuery), eq(givenPage), eq(givenSize))
        verify(kakaoBookRepository, times(0))
    }
}