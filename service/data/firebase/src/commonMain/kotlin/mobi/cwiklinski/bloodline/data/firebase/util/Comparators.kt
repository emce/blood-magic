package mobi.cwiklinski.bloodline.data.firebase.util

import mobi.cwiklinski.bloodline.data.firebase.model.FirebaseCenter

class RegionCenterComparator : Comparator<FirebaseCenter> {

    private val polish = POLISH_DIACRITICS.toList()

    override fun compare(a: FirebaseCenter, b: FirebaseCenter): Int {
        if (a.voivodeship == b.voivodeship) {
            return compareWords(a.city, b.city)
        }
        return compareWords(a.voivodeship, b.voivodeship)
    }

    private fun compareWords(a: String, b: String): Int {
        for (i in 0..minOf(a.length, b.length)) {
            val aLetter = polish.indexOf(a[i].lowercaseChar())
            val bLetter = polish.indexOf(b[i].lowercaseChar())
            if (aLetter != bLetter) {
                return if (aLetter < bLetter) -1 else 1
            }
        }
        return 0
    }

    companion object {
        const val POLISH_DIACRITICS = "aąbcćdeęfghijklłmnńoópqrsśtvwxyzźż"
    }

}