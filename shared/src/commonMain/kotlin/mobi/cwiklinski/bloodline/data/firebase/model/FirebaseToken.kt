package mobi.cwiklinski.bloodline.data.firebase.model

import kotlinx.serialization.Serializable

@Serializable
data class FirebaseToken(
    val id: String,
    val year: Int = 0,
    val month: Int = 0,
    val day: Int = 0
)