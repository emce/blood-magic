package mobi.cwiklinski.bloodline.auth.filed

import mobi.cwiklinski.bloodline.domain.Sex
import mobi.cwiklinski.bloodline.domain.model.Profile

object AuthData {
    val users = mapOf(
        "user1@domain.com" to "8d9f7asd98&9",
        "user2@domain.com" to "8d9f7asd98&8",
        "user3@domain.com" to "8d9f7asd98&7",
        "user4@domain.com" to "8d9f7asd98&6",
        "user5@domain.com" to "8d9f7asd98&5",
        "user6@domain.com" to "8d9f7asd98&4",
        "user7@domain.com" to "8d9f7asd98&3",
        "user8@domain.com" to "8d9f7asd98&2",
        "user9@domain.com" to "8d9f7asd98&1",
        "user0@domain.com" to "8d9f7asd98&0",
    )

    val notUsers = mapOf(
        "23234RE@domain.com" to "34534A FFZSDGF",
        "sfsde5ye5ye@domain.com" to "dsfsdfsdfsdfsdf"
    )

    private fun generateString(amount: Int = 10) = (1..amount).map { (('A'..'Z') + ('a'..'z') + ('0'..'9')).random() }.joinToString("")

    fun generateProfile(
        id: String = generateString(),
        name: String = generateString(),
        email: String = generateString(),
        avatar: String = generateString(),
        sex: Sex = Sex.entries.random(),
        notification: Boolean = listOf(false, true).random(),
        starting: Int = (0..1000).random(),
        centerId: String = ""
    ) = Profile(
        id,
        name,
        email,
        avatar,
        sex,
        notification,
        starting,
        centerId
    )
}