package com.library.feign

import com.library.feign.config.KakaoClientConfiguration
import com.library.feign.response.KakaoBookResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "kakaoClient", url = "\${external.kakao.url}", configuration = [KakaoClientConfiguration::class])
interface KakaoClient {
    @GetMapping("/v3/search/book")
    fun search(
        @RequestParam("query") query: String?,
        @RequestParam("page") page: Int,
        @RequestParam("size") size: Int
    ): KakaoBookResponse
}
