package mobi.cwiklinski.bloodline.domain


enum class NotificationType(val type: Int) {
    STANDARD(1),
    URGENT(2);

    companion object {
        fun byType(type: Int) =
            NotificationType.entries.firstOrNull { it.type == type } ?: STANDARD
    }
}