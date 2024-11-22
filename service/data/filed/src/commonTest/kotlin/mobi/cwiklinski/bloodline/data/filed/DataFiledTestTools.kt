package mobi.cwiklinski.bloodline.data.filed

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import mobi.cwiklinski.bloodline.domain.DonationType
import mobi.cwiklinski.bloodline.domain.model.Center
import mobi.cwiklinski.bloodline.domain.model.Donation

object DataFiledTestTools {

    fun generateDonation(
        id: String = DummyData.generateString(),
        date: LocalDate = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault()).date,
        type: DonationType = DonationType.entries.random(),
        amount: Int = (41..50).random() * 10,
        hemoglobin: Float = (0..100).random().toFloat(),
        systolic: Int = (90..150).random(),
        diastolic: Int = (60..90).random(),
        disqualification: Boolean = listOf(true, false).random(),
        center: Center = DummyData.CENTERS.random()
    ) = Donation(
        id = id,
        date = date,
        type = type,
        amount = amount,
        hemoglobin = hemoglobin,
        systolic = systolic,
        diastolic = diastolic,
        disqualification = disqualification,
        center = center
    )
}