package mobi.cwiklinski.bloodline.data.filed

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.LocalDate
import mobi.cwiklinski.bloodline.common.Either
import mobi.cwiklinski.bloodline.data.api.DonationService
import mobi.cwiklinski.bloodline.domain.DonationType
import mobi.cwiklinski.bloodline.domain.model.Center
import mobi.cwiklinski.bloodline.domain.model.Donation

class DonationServiceImplementation() : DonationService {

    private val _memory = mutableListOf<Donation>()

    init {
        _memory.addAll(DummyData.DONATIONS)
    }

    override fun getDonations() = flowOf(_memory)

    override fun getDonation(id: String) = flowOf(_memory.first { it.id == id })

    override fun addDonation(
        date: LocalDate,
        type: DonationType,
        amount: Int,
        hemoglobin: Float,
        systolic: Int,
        diastolic: Int,
        disqualification: Boolean,
        center: Center
    ) = flow<Either<Donation, Throwable>> {
        val id = DummyData.generateString()
        val newDonation = Donation(
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
        _memory.add(newDonation)
        emit(Either.Left(newDonation))
    }

    override fun updateDonation(
        id: String,
        date: LocalDate,
        type: DonationType,
        amount: Int,
        hemoglobin: Float,
        systolic: Int,
        diastolic: Int,
        disqualification: Boolean,
        center: Center
    ) = flow<Either<Donation, Throwable>> {
        val donation = _memory.firstOrNull { it.id == id }
        if (donation == null) {
            emit(Either.Right(RuntimeException()))
        } else {
            val updatedDonation = Donation(
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
            _memory.removeAll { it.id == id }
            _memory.add(updatedDonation)
            emit(Either.Left(updatedDonation))
        }
    }

    override fun deleteDonation(id: String) = flow<Either<Boolean, Throwable>> {
        _memory.removeAll { it.id == id }
        emit(Either.Left(true))
    }
}