package mobi.cwiklinski.bloodline.domain

import dev.icerock.moko.parcelize.Parcel
import dev.icerock.moko.parcelize.Parceler
import kotlinx.datetime.LocalDate
import mobi.cwiklinski.bloodline.domain.model.Center

fun List<Center>.sortByRegion(): List<Center> {
    val combinedComparator = compareBy(Center::voivodeship).thenBy(Center::city).thenBy(Center::name)
    return this.sortedWith(combinedComparator)
}

object LocalDateParceler :
    Parceler<LocalDate> {
    override fun create(parcel: Parcel): LocalDate {
        return parcel.readString()?.let {
            LocalDate.parse(it)
        } ?: LocalDate(0, 0, 0)
    }

    override fun LocalDate.write(parcel: Parcel, flags: Int) {
        parcel.writeString(this.toString())
    }
}