package mobi.cwiklinski.bloodline.common

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.toList
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant


@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T> Flow<List<T>>.flattenToList() =
    flatMapConcat { it.asFlow() }.toList()

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