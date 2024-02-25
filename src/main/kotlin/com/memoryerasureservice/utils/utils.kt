package com.memoryerasureservice.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun parseLocalDateTimeFromString(dateTimeString: String): LocalDateTime {
    // Определите формат даты и времени, который соответствует вашей строке
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss") // Например, "2022-01-01 12:00:00"
    return LocalDateTime.parse(dateTimeString, formatter)
}
