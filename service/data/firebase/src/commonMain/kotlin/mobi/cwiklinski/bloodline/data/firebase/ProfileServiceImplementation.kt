package mobi.cwiklinski.bloodline.data.firebase

import dev.gitlive.firebase.FirebaseException
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.database.FirebaseDatabase
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import mobi.cwiklinski.bloodline.common.Either
import mobi.cwiklinski.bloodline.common.isValidEmail
import mobi.cwiklinski.bloodline.data.api.ProfileService
import mobi.cwiklinski.bloodline.data.api.ProfileUpdate
import mobi.cwiklinski.bloodline.data.api.ProfileUpdateState
import mobi.cwiklinski.bloodline.data.firebase.model.FirebaseSettings
import mobi.cwiklinski.bloodline.domain.Sex

class ProfileServiceImplementation(
    db: FirebaseDatabase,
    private val auth: FirebaseAuth
) : ProfileService {

    private val mainRef = db.reference("settings").child(auth.currentUser?.uid ?: "-")

    private val _profileData = mainRef.valueEvents
            .map {
                Napier.d { it.toString() }
                it.value<FirebaseSettings>()
            }
            .map {
                Napier.d { it.toString() }
                it.toProfile(auth.currentUser?.uid ?: "")
            }

    override fun updateProfileData(
        name: String,
        email: String,
        avatar: String,
        sex: Sex,
        notification: Boolean,
        starting: Int,
        centerId: String
    ) = channelFlow<Either<ProfileUpdate, Throwable>> {
        val result = mutableListOf(ProfileUpdateState.NOTHING)
        try {
            _profileData.collectLatest { profile ->
                if (profile.differs(name, avatar, sex, notification, starting, centerId)) {
                    mainRef.setValue(
                        FirebaseSettings(
                            name,
                            email,
                            sex.sex,
                            if (notification) 1 else 0,
                            centerId,
                            starting,
                            avatar
                        )
                    )
                    result.remove(ProfileUpdateState.NOTHING)
                    result.add(ProfileUpdateState.DATA)
                }
                trySend(Either.Left(ProfileUpdate(result)))
            }
        } catch (e: FirebaseException) {
            trySend(Either.Right(e))
        }
    }

    override fun updateProfileEmail(email: String) = channelFlow<Either<ProfileUpdate, Throwable>> {
        val result = mutableListOf(ProfileUpdateState.NOTHING)
        try {
            _profileData.collectLatest { profile ->
                if (email.isValidEmail() && email != profile.email) {
                    auth.currentUser?.verifyBeforeUpdateEmail(newEmail = email)
                    result.remove(ProfileUpdateState.NOTHING)
                    result.add(ProfileUpdateState.PASSWORD)
                }
                trySend(Either.Left(ProfileUpdate(result)))
            }
        } catch (e: FirebaseException) {
            trySend(Either.Right(e))
        }
    }

    override fun updateProfilePassword(password: String) = channelFlow<Either<ProfileUpdate, Throwable>> {
        val result = mutableListOf(ProfileUpdateState.NOTHING)
        try {
            if (password.isNotEmpty() && password.length > 5) {
                auth.currentUser?.updatePassword(password)
                result.remove(ProfileUpdateState.NOTHING)
                result.add(ProfileUpdateState.PASSWORD)
            }
            trySend(Either.Left(ProfileUpdate(result)))
        } catch (e: FirebaseException) {
            trySend(Either.Right(e))
        }
    }

    override fun getProfile() = _profileData
}