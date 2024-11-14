package mobi.cwiklinski.bloodline

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform