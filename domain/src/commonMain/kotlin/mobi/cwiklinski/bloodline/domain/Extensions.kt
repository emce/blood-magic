package mobi.cwiklinski.bloodline.domain

import mobi.cwiklinski.bloodline.domain.model.Center

fun List<Center>.sortByRegion(): List<Center> {
    val combinedComparator = compareBy(Center::voivodeship).thenBy(Center::city).thenBy(Center::name)
    return this.sortedWith(combinedComparator)
}