package mobi.cwiklinski.bloodline.data.firebase.model

import kotlinx.datetime.LocalDate
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
    
    fun toDonation(center: Center): Donation {
        var donation = Donation()
        if (id != null) {
            donation = donation.copy(id = id!!)
        }
        donation = donation.copy(date = LocalDate(year, month, day))
        donation = donation.copy(type = DonationType.byType(type))
        if (amount != 0) {
            donation = donation.copy(amount = amount)
        }
        if (hemoglobin != 0.0f) {
            donation = donation.copy(hemoglobin = hemoglobin)
        }
        if (systolic != 0) {
            donation = donation.copy(systolic = systolic)
        }
        if (diastolic != 0) {
            donation = donation.copy(diastolic = diastolic)
        }
        if (disqualification) {
            donation = donation.copy(disqualification = disqualification)
        }
        donation = donation.copy(center = center)
        return donation
    }
}
