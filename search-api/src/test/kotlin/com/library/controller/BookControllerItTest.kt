package com.library.controller

import com.library.controller.request.SearchRequest
import com.library.controller.response.PageResult
import com.library.controller.response.SearchResponse
import com.library.service.BookQueryService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class BookControllerItTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var bookQueryService: BookQueryService

    @Test
    fun `정상인자로 요청시 성공한다`() {
        // Arrange
        val request = SearchRequest("HTTP", 1, 10)
        `when`(bookQueryService.search(query = anyString(), page = anyInt(), size = anyInt())).thenReturn(
            PageResult(
                page = 1,
                size = 10,
                totalElements = 10,
                contents = listOf(mock(SearchResponse::class.java))
            )
        )

        // Action
        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/books")
                .param("query", request.query)
                .param("page", request.page.toString())
                .param("size", request.size.toString())
        )

        // Assert
        result.andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.page").value(1))
            .andExpect(jsonPath("$.size").value(10))
            .andExpect(jsonPath("$.totalElements").value(10))
            .andExpect(jsonPath("$.contents").isArray)

    }

    @Test
    fun `query가 비어있을때 BadRequest 응답반환된다`() {
        // Arrange
        val request = SearchRequest("", 1, 10)

        // Action
        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/books")
                .param("query", request.query)
                .param("page", request.page.toString())
                .param("size", request.size.toString())
        )

        // Assert
        result.andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.errorMessage").value("입력은 비어있을 수 없습니다."))
    }

    @Test
    fun `page가 음수일경우에 BadRequest 응답반환된다`() {
        // Arrange
        val request = SearchRequest("http", -1, 10)

        // Action
        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/books")
                .param("query", request.query)
                .param("page", request.page.toString())
                .param("size", request.size.toString())
        )

        // Assert
        result.andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.errorMessage").value("페이지번호는 1이상이어야 합니다."))
    }

    @Test
    fun `size가 50을 초과하면 BadRequest 응답반환된다`() {
        // Arrange
        val request = SearchRequest("http", 1, 51)

        // Action
        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/books")
                .param("query", request.query)
                .param("page", request.page.toString())
                .param("size", request.size.toString())
        )

        // Assert
        result.andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.errorMessage").value("페이지크기는 50이하여야 합니다."))
    }

}