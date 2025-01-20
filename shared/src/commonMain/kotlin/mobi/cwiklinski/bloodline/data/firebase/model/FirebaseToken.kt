package mobi.cwiklinski.bloodline.data.firebase.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import mobi.cwiklinski.bloodline.domain.model.Token

@Serializable
data class FirebaseToken(
    val id: String,
    val year: Int = 0,
    val month: Int = 0,
    val day: Int = 0
) {

    fun toToken() =
        Token(this.id, LocalDate(year = this.year, monthNumber = this.month, dayOfMonth = this.day))
}