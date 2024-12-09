package com.library.controller

import com.library.service.BookQueryService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class BookControllerTest {
    lateinit var bookQueryService: BookQueryService
    lateinit var bookController: BookController
    lateinit var mockMvc: MockMvc

    @BeforeEach
    fun init() {
        this.bookQueryService = mock(BookQueryService::class.java)
        this.bookController = BookController(bookQueryService)
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
        verify(bookQueryService, times(1)).search(givenQuery, givenPage, givenSize)
    }
}