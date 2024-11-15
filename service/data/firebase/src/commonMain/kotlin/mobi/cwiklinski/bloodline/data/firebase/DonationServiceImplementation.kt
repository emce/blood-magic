package mobi.cwiklinski.bloodline.data.firebase

import dev.gitlive.firebase.FirebaseException
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.database.FirebaseDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import mobi.cwiklinski.bloodline.data.api.DonationService
import mobi.cwiklinski.bloodline.data.api.Either
import mobi.cwiklinski.bloodline.data.firebase.model.FirebaseCenter
import mobi.cwiklinski.bloodline.data.firebase.model.FirebaseDonation
import mobi.cwiklinski.bloodline.data.firebase.util.flattenToList
import mobi.cwiklinski.bloodline.domain.DonationType
import mobi.cwiklinski.bloodline.domain.model.Center
import mobi.cwiklinski.bloodline.domain.model.Donation

class DonationServiceImplementation(val db: FirebaseDatabase, val auth: FirebaseAuth) :
    DonationService {

    private val centerRef = db.reference("center")
    private val mainRef = db.reference("donation").child(auth.currentUser?.uid ?: "-")


    override suspend fun getDonations() =
        combineTransform(
            centerRef
                .valueEvents
                .map {
                    it.value<Map<String, FirebaseCenter>>().values.toList()
                },
            mainRef
                .valueEvents
                .map {
                    it.value<Map<String, FirebaseDonation>>().values.toList()
                }
        ) { centers, donations ->
            emit(donations.map { donation ->
                donation.toDonation(centers.first { it.id == donation.centerId }.toCenter())
            })
        }
            .catch { e ->
                flowOf<List<Donation>>(emptyList())
            }
            .flattenToList()

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getDonation(id: String): Donation {
        val fDonation = mainRef
            .child(id)
            .valueEvents
            .map {
                it.value<Map<String, FirebaseDonation>>().values.toList()
            }
            .flattenToList()
            .first()
        val center = centerRef
            .child(fDonation.centerId)
            .valueEvents
            .map { it.value<Map<String, FirebaseCenter>>().values.toList() }
            .flattenToList()
            .map { it.toCenter() }
            .first()
        return fDonation.toDonation(center)
    }

    override suspend fun addDonation(
        date: LocalDate,
        type: DonationType,
        amount: Int,
        hemoglobin: Float,
        systolic: Int,
        diastolic: Int,
        disqualification: Boolean,
        center: Center
    ): Either<Donation, Throwable> {
        try {
            val newRef = mainRef.push()
            val id = newRef.key
            if (id != null) {
                newRef.setValue(
                    FirebaseDonation(
                        id,
                        date.year,
                        date.monthNumber,
                        date.dayOfMonth,
                        type.type,
                        amount,
                        hemoglobin,
                        center.id,
                        systolic,
                        diastolic,
                        disqualification
                    )
                )
                val newDonation = getDonation(id)
                return Either.Left(newDonation)
            } else {
                return Either.Right(Exception())
            }
        } catch (e: FirebaseException) {
            return Either.Right(e)
        }
    }

    override suspend fun updateDonation(
        id: String,
        date: LocalDate,
        type: DonationType,
        amount: Int,
        hemoglobin: Float,
        systolic: Int,
        diastolic: Int,
        disqualification: Boolean,
        center: Center
    ): Either<Donation, Throwable> {
        try {
            mainRef
                .child(id)
                .setValue(
                    FirebaseDonation(
                        id,
                        date.year,
                        date.monthNumber,
                        date.dayOfMonth,
                        type.type,
                        amount,
                        hemoglobin,
                        center.id,
                        systolic,
                        diastolic,
                        disqualification
                    )
                )
            val updatedDonation = getDonation(id)
            return Either.Left(updatedDonation)
        } catch (e: FirebaseException) {
            return Either.Right(e)
        }
    }

    override suspend fun deleteDonation(id: String): Either<Boolean, Throwable> {
        try {
            mainRef
                .child(id)
                .removeValue()
            return Either.Left(true)
        } catch (e: FirebaseException) {
            return Either.Right(e)
        }
    }
}