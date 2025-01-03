package mobi.cwiklinski.bloodline.domain

import kotlinx.datetime.LocalDate
import mobi.cwiklinski.bloodline.data.Parceler
import mobi.cwiklinski.bloodline.domain.model.Center

fun List<Center>.sortByRegion(): List<Center> {
    val combinedComparator = compareBy(Center::voivodeship).thenBy(Center::city).thenBy(Center::name)
    return this.sortedWith(combinedComparator)
}

expect object LocalDateParceler : Parceler<LocalDate>