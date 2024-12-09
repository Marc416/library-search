package com.library.controller

import com.library.service.BookApplicationService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.time.LocalDate

class BookControllerTest {
    lateinit var bookApplicationService: BookApplicationService
    lateinit var bookController: BookController
    lateinit var mockMvc: MockMvc

    @BeforeEach
    fun init() {
        this.bookApplicationService = mock(BookApplicationService::class.java)
        this.bookController = BookController(bookApplicationService)
        this.mockMvc = MockMvcBuilders.standaloneSetup(bookController).build()
    }

    @Test
    fun `search`() {
        // Arrange
        val givenQuery = "HTTP"
        val givenPage = 1
        val givenSize = 10

        // Action
        val response =
            mockMvc.perform(MockMvcRequestBuilders.get("/v1/books?query=${givenQuery}&page=${givenPage}&size=${givenSize}"))
                .andReturn()
                .response

        // Assert
        assertThat(response.status).isEqualTo(HttpStatus.OK.value())
        verify(bookApplicationService, times(1)).search(givenQuery, givenPage, givenSize)
    }

    @Test
    fun `findStat`() {
        // Arrange
        val givenQuery = "HTTP"
        val givenDate = LocalDate.of(2024, 5, 1)

        // Action
        val response = mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/books/stats?query=${givenQuery}&date=${givenDate}")
        )
            .andReturn()
            .response

        // Assert
        assertThat(response.status).isEqualTo(HttpStatus.OK.value())
        verify(bookApplicationService, times(1)).findQueryCount(givenQuery, givenDate)

    }

    @Test
    fun `findStatRanking`() {
        // Arrange, Action
        val response = mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/books/stats/ranking")
        )
            .andReturn()
            .response

        // Assert
        assertThat(response.status).isEqualTo(HttpStatus.OK.value())
        verify(bookApplicationService, times(1)).findTop5Query()
    }

}