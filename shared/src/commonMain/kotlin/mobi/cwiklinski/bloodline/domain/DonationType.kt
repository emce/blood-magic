package mobi.cwiklinski.bloodline.domain

enum class DonationType(val type: Int) {
    FULL_BLOOD(1),
    PLASMA(2),
    PLATELETS(3),
    PACKED_CELLS(4);

    companion object {
        fun byType(type: Int) =
            entries.firstOrNull { it.type == type } ?: FULL_BLOOD
    }
}