package mobi.cwiklinski.bloodline.data.firebase.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import mobi.cwiklinski.bloodline.domain.model.Center
import kotlin.String

@Serializable
data class FirebaseCenter(
    val id: String = "",
    val name: String = "",
    val voivodeship: String = "",
    @SerialName("voivodeship_id")
    val voivodeshipId: String = "",
    val phone: String = "",
    val street: String = "",
    val zip: String = "",
    val city: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val site: String = "",
    val info: String = ""
) {
    
    fun toCenter(): Center = Center(
        id,
        name,
        voivodeship,
        voivodeshipId,
        phone,
        street,
        zip,
        city,
        latitude,
        longitude,
        site,
        info
    )
}
