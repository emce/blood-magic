package mobi.cwiklinski.bloodline.data.firebase.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import mobi.cwiklinski.bloodline.domain.Sex
import mobi.cwiklinski.bloodline.domain.model.Profile

@Serializable
class FirebaseSettings(
    val name: String = "",
    val email: String = "",
    val sex: String = "m",
    private val reminder: Int = 0,
    @SerialName("center_id")
    val centerId: String = "",
    val starting: Int = 0,
    val avatar: String = "WIZARD",
) {

    fun toProfile(id: String, email: String): Profile {
        var profile = Profile()
        if (id.isNotEmpty()) {
            profile = profile.copy(id = id)
        }
        if (name.isNotEmpty()) {
            profile = profile.copy(name = name)
        }
        if (email.isNotEmpty()) {
            profile = profile.copy(email = email)
        }
        if (avatar.isNotEmpty()) {
            profile = profile.copy(avatar = avatar)
        }
        profile = profile.copy(sex = Sex.fromSex(this.sex))
        profile = profile.copy(notification = this.reminder == 1)
        profile = profile.copy(starting = this.starting)
        if (centerId.isNotEmpty()) {
            profile = profile.copy(centerId = this.centerId)
        }
        return profile
    }
}