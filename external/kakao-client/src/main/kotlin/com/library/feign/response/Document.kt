package com.library.feign.response

data class Document(
    val title: String,
    val authors: List<String>,
    val isbn: String,
    val publisher: String,
    val datetime: String
)
