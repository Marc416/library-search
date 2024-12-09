package com.library.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class DailyStat (
    @Column(nullable = false)
    val query: String,
    @Column(nullable = false)
    val eventDateTime: LocalDateTime
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}