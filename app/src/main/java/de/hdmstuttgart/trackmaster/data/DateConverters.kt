package de.hdmstuttgart.trackmaster.data

import java.time.LocalDate
import java.time.format.DateTimeFormatter


fun toDayAndMonth(date: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.")
    return date.format(formatter)
}

fun toDay(date: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("dd.")
    return date.format(formatter)
}

fun toGermanDate(date: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    return date.format(formatter)
}