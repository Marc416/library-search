package com.library.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateUtils {
    companion object {
        private val YYYYMMDD_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd")


        fun parseYYYYMMDD(date: String): LocalDate {
            return LocalDate.parse(date, YYYYMMDD_FORMATTER)
        }

        fun parseOffsetDateTime(datetime: String): LocalDateTime {
            return LocalDateTime.parse(datetime, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        }
    }
}
