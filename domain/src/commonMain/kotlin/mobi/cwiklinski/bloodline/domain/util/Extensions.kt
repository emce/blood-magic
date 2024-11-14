package mobi.cwiklinski.bloodline.domain.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.text.compareTo


fun LocalDate.isBefore(date: LocalDate) =
    LocalDateTime(this.year, this.monthNumber, this.dayOfMonth, 0, 0, 0)
        .toInstant(TimeZone.currentSystemDefault()) < LocalDateTime(
        date.year,
        date.monthNumber,
        date.dayOfMonth,
        0,
        0,
        0
    )
        .toInstant(TimeZone.currentSystemDefault())

fun LocalDate.isAfter(date: LocalDate) =
    LocalDateTime(this.year, this.monthNumber, this.dayOfMonth, 0, 0, 0)
        .toInstant(TimeZone.currentSystemDefault()) > LocalDateTime(
        date.year,
        date.monthNumber,
        date.dayOfMonth,
        0,
        0,
        0
    )
        .toInstant(TimeZone.currentSystemDefault())