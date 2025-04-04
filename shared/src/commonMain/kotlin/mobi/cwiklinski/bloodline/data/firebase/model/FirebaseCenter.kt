package mobi.cwiklinski.bloodline.data.firebase.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import mobi.cwiklinski.bloodline.domain.model.Center

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

    fun toCenter(): Center {
        var center = Center()
        if (id.isNotEmpty()) {
            center = center.copy(id = id)
        }
        if (name.isNotEmpty()) {
            center = center.copy(name = name)
        }
        if (voivodeship.isNotEmpty()) {
            center = center.copy(voivodeship = voivodeship)
        }
        if (voivodeshipId.isNotEmpty()) {
            center = center.copy(voivodeshipId = voivodeshipId)
        }
        if (phone.isNotEmpty()) {
            center = center.copy(phone = phone)
        }
        if (street.isNotEmpty()) {
            center = center.copy(street = street)
        }
        if (zip.isNotEmpty()) {
            center = center.copy(zip = zip)
        }
        if (city.isNotEmpty()) {
            center = center.copy(city = city)
        }
        if (latitude != 0.0) {
            center = center.copy(latitude = latitude)
        }
        if (longitude != 0.0) {
            center = center.copy(longitude = longitude)
        }
        if (site.isNotEmpty()) {
            center = center.copy(site = site)
        }
        if (info.isNotEmpty()) {
            center = center.copy(info = info)
        }
        return center
    }
}