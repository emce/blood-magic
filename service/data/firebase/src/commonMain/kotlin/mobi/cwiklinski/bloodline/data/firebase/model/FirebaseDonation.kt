package mobi.cwiklinski.bloodline.data.firebase.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import mobi.cwiklinski.bloodline.data.firebase.util.IntToBooleanSerializer
import mobi.cwiklinski.bloodline.domain.DonationType
import mobi.cwiklinski.bloodline.domain.model.Center
import mobi.cwiklinski.bloodline.domain.model.Donation

@Serializable
data class FirebaseDonation(
    var id: String? = null,
    var year: Int = 0,
    var month: Int = 0,
    var day: Int = 0,
    var type: Int = 0,
    var amount: Int = 0,
    var hemoglobin: Float = 0.0f,
    @SerialName("center_id")
    var centerId: String = "",
    var systolic: Int = 0,
    var diastolic: Int = 0,
    @Serializable(with = IntToBooleanSerializer::class)
    var disqualification: Boolean = false
) {
    
    fun toDonation(center: Center) = Donation(
        id.toString(),
        LocalDate(year, month, day),
        DonationType.byType(type),
        amount,
        hemoglobin,
        systolic,
        diastolic,
        disqualification,
        center
    ) 
}
