package mobi.cwiklinski.bloodline.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mobi.cwiklinski.bloodline.domain.Constants.Sex

@Serializable
data class Profile(
    val name: String,
    val email: String,
    val avatar: String,
    val sex: Sex,
    val notification: Boolean,
    val starting: Int,
    val centerId: String
) {
    fun toJson() = Json.encodeToString(this)

    companion object {
        fun fromJson(data: String) = Json.decodeFromString<Profile>(data)
    }
}