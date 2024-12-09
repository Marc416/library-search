package com.library.feign.response

data class KakaoBookResponse(
    val documents: List<Document>,
    val meta: Meta
)