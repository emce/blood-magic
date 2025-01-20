package mobi.cwiklinski.bloodline.domain.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import mobi.cwiklinski.bloodline.data.Parcelable
import mobi.cwiklinski.bloodline.data.Parcelize
import mobi.cwiklinski.bloodline.data.TypeParceler
import mobi.cwiklinski.bloodline.domain.LocalDateParceler


@Serializable
@Parcelize
data class Token(
    val id: String = "",
    @TypeParceler<LocalDate, LocalDateParceler>()
    val date: LocalDate = Clock.System.now().toLocalDateTime(
        TimeZone.currentSystemDefault()
    ).date
): Parcelable
