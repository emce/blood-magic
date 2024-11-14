package mobi.cwiklinski.bloodline.data.firebase.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import mobi.cwiklinski.bloodline.domain.Constants.Sex
import mobi.cwiklinski.bloodline.domain.model.Profile

@Serializable
class FirebaseSettings(
    val sex: String = "",
    val reminder: Int = 0,
    @SerialName("center_id")
    val centerId: String = "",
    val starting: Int = 0,
    val avatar: String = "",
) {

    fun toProfile(name: String, email: String) = Profile(
        name,
        email,
        avatar,
        Sex.fromSex(sex),
        reminder == 1,
        starting,
        centerId
    )
}