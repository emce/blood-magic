package mobi.cwiklinski.bloodline.data.api

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import mobi.cwiklinski.bloodline.common.Either
import mobi.cwiklinski.bloodline.domain.DonationType
import mobi.cwiklinski.bloodline.domain.model.Center
import mobi.cwiklinski.bloodline.domain.model.Donation

interface DonationService {

    fun getDonations(): Flow<List<Donation>>

    fun getDonation(id: String): Flow<Donation>

    fun addDonation(
        date: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
        type: DonationType = DonationType.FULL_BLOOD,
        amount: Int = 0,
        hemoglobin: Float = 0.0f,
        systolic: Int = 0,
        diastolic: Int = 0,
        disqualification: Boolean = false,
        center: Center
    ): Flow<Either<Donation, Throwable>>

    fun updateDonation(
        id: String,
        date: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
        type: DonationType = DonationType.FULL_BLOOD,
        amount: Int = 0,
        hemoglobin: Float = 0.0f,
        systolic: Int = 0,
        diastolic: Int = 0,
        disqualification: Boolean = false,
        center: Center
    ): Flow<Either<Donation, Throwable>>

    fun deleteDonation(id: String): Flow<Either<Boolean, Throwable>>
}