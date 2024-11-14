package mobi.cwiklinski.bloodline.domain.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import mobi.cwiklinski.bloodline.domain.Sex
import mobi.cwiklinski.bloodline.domain.DonationType
import mobi.cwiklinski.bloodline.domain.util.isAfter
import mobi.cwiklinski.bloodline.domain.util.isBefore
import kotlin.math.roundToInt

@Serializable
data class Donation(
    var id: String,
    var date: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
    var type: DonationType = DonationType.FULL_BLOOD,
    var amount: Int = 0,
    var hemoglobin: Float = 0.0f,
    var systolic: Int = 0,
    var diastolic: Int = 0,
    var disqualification: Boolean = false,
    var center: Center
) {

    fun isFullBlood() = type == DonationType.FULL_BLOOD

    fun isPlasma() = type == DonationType.PLASMA

    fun isPlatelets() = type == DonationType.PLATELETS

    fun isPacked() = type == DonationType.PACKED_CELLS

    fun convertToFullBlood(sex: Sex): Int {
        return when {
            isPlasma() -> when {
                date.isBefore(PERIOD_FIRST) -> {
                    amount.toDouble()
                }

                date.isAfter(PERIOD_FIRST) && date.isBefore(PERIOD_SECOND) -> {
                    if (sex.isFemale()) amount.times(200.0).div(650.0)
                    else amount.times(180.0).div(650.0)
                }

                else -> {
                    amount.times(216.0).div(650.0)
                }
            }.roundToInt()

            isPlatelets() -> when {
                date.isBefore(PERIOD_FIRST) -> amount.toDouble()
                date.isAfter(PERIOD_FIRST) && date.isBefore(PERIOD_SECOND) -> {
                    if (sex.isFemale()) amount.times(830.0).div(250.0)
                    else amount.times(670.0).div(250.0)
                }

                else -> amount.times(500.0).div(250.0)
            }.roundToInt()

            isPacked() -> when {
                date.isBefore(PERIOD_FIRST) -> amount
                else -> amount.times(1000.0).div(600.0).roundToInt()
            }

            else -> amount
        }
    }
    
    companion object {
        private val PERIOD_FIRST: LocalDate = LocalDate(1998, 10, 14)
        private val PERIOD_SECOND: LocalDate = LocalDate(2006, 2, 23)
    }

}
