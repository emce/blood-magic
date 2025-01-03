package mobi.cwiklinski.bloodline.domain.model

import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.parcelize.Parcelize
import dev.icerock.moko.parcelize.TypeParceler
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import mobi.cwiklinski.bloodline.common.isAfter
import mobi.cwiklinski.bloodline.common.isBefore
import mobi.cwiklinski.bloodline.domain.DonationType
import mobi.cwiklinski.bloodline.domain.LocalDateParceler
import mobi.cwiklinski.bloodline.domain.Sex
import kotlin.math.roundToInt

@Serializable
@Parcelize
data class Donation(
    var id: String,
    @TypeParceler<LocalDate, LocalDateParceler>()
    var date: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
    var type: DonationType = DonationType.FULL_BLOOD,
    var amount: Int = 0,
    var hemoglobin: Float = 0.0f,
    var systolic: Int = 0,
    var diastolic: Int = 0,
    var disqualification: Boolean = false,
    var center: Center
) : Parcelable {

    fun convertToFullBlood(sex: Sex): Int {
        return when(type) {
            DonationType.PLASMA -> when {
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

            DonationType.PLATELETS -> when {
                date.isBefore(PERIOD_FIRST) -> amount.toDouble()
                date.isAfter(PERIOD_FIRST) && date.isBefore(PERIOD_SECOND) -> {
                    if (sex.isFemale()) amount.times(830.0).div(250.0)
                    else amount.times(670.0).div(250.0)
                }

                else -> amount.times(500.0).div(250.0)
            }.roundToInt()

            DonationType.PACKED_CELLS -> when {
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
