package com.library.feign.response

import com.fasterxml.jackson.annotation.JsonProperty

class Item(
    val title: String,
    val link: String,
    val image: String,
    val author: String,
    val discount: String,
    val publisher: String,

    @JsonProperty("pubdate")
    val pubDate: String,
    val isbn: String,
    val description: String,
)
