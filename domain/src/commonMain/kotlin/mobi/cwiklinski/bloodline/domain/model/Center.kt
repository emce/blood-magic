package mobi.cwiklinski.bloodline.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Center(
    val id: String = "",
    val name: String = "",
    val voivodeship: String = "",
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
    fun getFullAddress(): String {
        val builder = StringBuilder()
        if (zip.isNotEmpty()) {
            builder.append(zip)
        }
        if (city.isNotEmpty()) {
            if (zip.isNotEmpty()) {
                builder.append(" ")
            }
            builder.append(city)
        }
        if (street.isNotEmpty()) {
            if (city.isNotEmpty()) {
                builder.append(", ")
            }
            builder.append(street)
        }
        return builder.toString()
    }

    fun toSelection() = "$city: $name"
}
