package com.library.controller

import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestClient
import java.util.concurrent.CompletableFuture

private val logger = KotlinLogging.logger { }

@RestController
@RequestMapping("/v1/test")
class TestController {
    private val restClient = RestClient.create()

    @GetMapping("")
    fun test() {
        val futures = mutableListOf<CompletableFuture<Void>>()
        for (i in 0 until 10) {
            val requestId = i
            val future = CompletableFuture.runAsync {
                try {
                    val result = restClient.get()
                        .uri("http://localhost:8080/v1/books?query=java&page=1&size=2")
                        .retrieve()
                        .toEntity(String::class.java)
                    logger.info("result = {} requestId = {}", result, requestId)
                } catch (e: Exception) {
                    logger.error("실패!!! requestId={}", requestId, e)
                }
            }
            futures.add(future)
        }
        CompletableFuture.allOf(*futures.toTypedArray()).join()
    }
}
