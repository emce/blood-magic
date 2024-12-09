package mobi.cwiklinski.bloodline.data.firebase

import dev.gitlive.firebase.FirebaseException
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.database.DatabaseException
import dev.gitlive.firebase.database.FirebaseDatabase
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import mobi.cwiklinski.bloodline.common.Either
import mobi.cwiklinski.bloodline.common.isValidEmail
import mobi.cwiklinski.bloodline.data.api.ProfileService
import mobi.cwiklinski.bloodline.data.api.ProfileServiceState
import mobi.cwiklinski.bloodline.data.api.ProfileUpdate
import mobi.cwiklinski.bloodline.data.api.ProfileUpdateState
import mobi.cwiklinski.bloodline.data.firebase.model.FirebaseSettings
import mobi.cwiklinski.bloodline.domain.Sex
import mobi.cwiklinski.bloodline.domain.model.Profile

class ProfileServiceImplementation(
    db: FirebaseDatabase,
    private val auth: FirebaseAuth
) : ProfileService {

    private val mainRef = db.reference("settings").child(auth.currentUser?.uid ?: "-")

    private val _profileData = try {
        mainRef.valueEvents
            .map {
                Napier.d { it.toString() }
                it.value<FirebaseSettings>()
            }
            .map {
                Napier.d { it.toString() }
                it.toProfile(auth.currentUser?.uid ?: "")
            }
        } catch (e: DatabaseException) {
            flowOf(Profile(""))
    }

    override fun updateProfileData(
        name: String,
        email: String,
        avatar: String,
        sex: Sex,
        notification: Boolean,
        starting: Int,
        centerId: String
    ): Flow<ProfileServiceState> = callbackFlow {
        try {
            withContext(Dispatchers.Default) {
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
            }
            trySend(ProfileServiceState.Saved)
        } catch (exception: FirebaseException) {
            trySend(ProfileServiceState.Error(exception))
        }
        awaitClose { }
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