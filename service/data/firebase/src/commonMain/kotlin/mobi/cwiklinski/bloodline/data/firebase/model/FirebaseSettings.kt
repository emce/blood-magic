package mobi.cwiklinski.bloodline.data.firebase.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import mobi.cwiklinski.bloodline.domain.Sex
import mobi.cwiklinski.bloodline.domain.model.Profile

@Serializable
class FirebaseSettings(
    val name: String = "",
    val email: String = "",
    val sex: String = "",
    val reminder: Int = 0,
    @SerialName("center_id")
    val centerId: String = "",
    val starting: Int = 0,
    val avatar: String = "",
) {

    fun toProfile(id: String) = Profile(
        id,
        name,
        email,
        avatar,
        Sex.fromSex(sex),
        reminder == 1,
        starting,
        centerId
    )
}