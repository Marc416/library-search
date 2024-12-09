package com.library.controller.response

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock

class PageResultTest {
    @Test
    fun `pageResult 객체 생성된다`() {
        // Arrange
        val givenPage = 1
        val givenSize = 10
        val givenTotalElements = 2
        val searchResponse1 = mock(SearchResponse::class.java)
        val searchResponse2 = mock(SearchResponse::class.java)

        // Action
        val result = PageResult(
            page = givenPage,
            size = givenSize,
            totalElements = givenTotalElements,
            contents = listOf(searchResponse1, searchResponse2)
        )

        // Assert
        assertThat(result.page).isEqualTo(givenPage)
        assertThat(result.size).isEqualTo(givenSize)
        assertThat(result.totalElements).isEqualTo(givenTotalElements)
        assertThat(result.contents.size).isEqualTo(givenTotalElements)
    }
}