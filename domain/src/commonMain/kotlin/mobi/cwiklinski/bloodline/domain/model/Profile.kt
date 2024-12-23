package mobi.cwiklinski.bloodline.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mobi.cwiklinski.bloodline.domain.Sex

@Serializable
data class Profile(
    val id: String?,
    val name: String = "",
    val email: String = "",
    val avatar: String = "WIZARD",
    val sex: Sex = Sex.MALE,
    val notification: Boolean = true,
    val starting: Int = 0,
    val centerId: String = ""
) {
    fun toJson() = Json.encodeToString(this)

    fun withEmail(newEmail: String) = copy(email = newEmail)

    companion object {
        fun fromJson(data: String) =
            if (data != "profile") Json.decodeFromString<Profile>(data) else null
    }
}