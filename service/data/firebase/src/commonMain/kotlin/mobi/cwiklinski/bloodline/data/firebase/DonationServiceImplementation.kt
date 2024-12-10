package mobi.cwiklinski.bloodline.data.firebase

import dev.gitlive.firebase.FirebaseException
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.database.DatabaseException
import dev.gitlive.firebase.database.FirebaseDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import mobi.cwiklinski.bloodline.common.Either
import mobi.cwiklinski.bloodline.data.api.DonationService
import mobi.cwiklinski.bloodline.data.firebase.model.FirebaseCenter
import mobi.cwiklinski.bloodline.data.firebase.model.FirebaseDonation
import mobi.cwiklinski.bloodline.domain.DonationType
import mobi.cwiklinski.bloodline.domain.model.Center
import mobi.cwiklinski.bloodline.domain.model.Donation

@OptIn(ExperimentalCoroutinesApi::class)
class DonationServiceImplementation(db: FirebaseDatabase, val auth: FirebaseAuth) :
    DonationService {

    private val centerRef = db.reference("center")
    private val mainRef = db.reference("donation").child(auth.currentUser?.uid ?: "-")


    override fun getDonations() =
        combineTransform(
            centerRef
                .valueEvents
                .map {
                    it.value<Map<String, FirebaseCenter>>().values.toList()
                },
            mainRef
                .valueEvents
                .map {
                    if (it.value != null) {
                        it.value<Map<String, FirebaseDonation>>().values.toList()
                    } else {
                        emptyList()
                    }
                }
        ) { centers, donations ->
            try {
                if(donations.isEmpty()) {
                    emit(emptyList())
                } else {
                    emit(donations.map { donation ->
                        donation.toDonation(centers.first { it.id == donation.centerId }.toCenter())
                    }.sortedByDescending { it.date })
                }
            } catch (e: DatabaseException) {
                emit(emptyList())
            }
        }

    override fun getDonation(id: String) =
        mainRef
            .child(id)
            .valueEvents
            .map {
                it.value<Map<String, FirebaseDonation>>().values.first()
            }
            .flatMapConcat { fDonation ->
                centerRef
                    .child(fDonation.centerId)
                    .valueEvents
                    .map { fCenter ->
                        fCenter.value<Map<String, FirebaseCenter>>().values
                            .map { it.toCenter() }.first()
                    }
                    .map { fDonation.toDonation(it) }
            }

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
        try {
            val newRef = mainRef.push()
            val id = newRef.key
            if (id != null) {
                try {
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
                } finally {
                    val newDonation = getDonation(id)
                    emit(Either.Left(newDonation.first()))
                }
            } else {
                emit(Either.Right(Exception()))
            }
        } catch (e: FirebaseException) {
            emit(Either.Right(e))
        }
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
            emit(Either.Left(updatedDonation.first()))
        } catch (e: FirebaseException) {
            emit(Either.Right(e))
        }
    }

    override fun deleteDonation(id: String) = flow {
        try {
            mainRef
                .child(id)
                .removeValue()
            emit(Either.Left(true))
        } catch (e: FirebaseException) {
            emit(Either.Right(e))
        }
    }
}