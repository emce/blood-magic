package mobi.cwiklinski.bloodline.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import mobi.cwiklinski.bloodline.data.Parcelable
import mobi.cwiklinski.bloodline.data.Parcelize
import mobi.cwiklinski.bloodline.data.TypeParceler
import mobi.cwiklinski.bloodline.domain.LocalDateParceler

@Serializable
@Parcelize
data class Notification(
    val id: String,
    val type: Int,
    @TypeParceler<LocalDate, LocalDateParceler>()
    val date: LocalDate,
    val title: String,
    val message: String,
    val location: String,
    val read: Boolean = false
) : Parcelable