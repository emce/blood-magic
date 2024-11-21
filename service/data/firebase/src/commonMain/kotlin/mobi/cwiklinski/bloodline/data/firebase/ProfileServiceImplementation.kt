package mobi.cwiklinski.bloodline.data.firebase

import dev.gitlive.firebase.FirebaseException
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import mobi.cwiklinski.bloodline.common.Either
import mobi.cwiklinski.bloodline.common.isValidEmail
import mobi.cwiklinski.bloodline.data.api.ProfileService
import mobi.cwiklinski.bloodline.data.api.ProfileUpdate
import mobi.cwiklinski.bloodline.data.api.ProfileUpdateState
import mobi.cwiklinski.bloodline.data.firebase.model.FirebaseSettings
import mobi.cwiklinski.bloodline.domain.Sex

class ProfileServiceImplementation(val db: FirebaseDatabase, val auth: FirebaseAuth) : ProfileService {

    private val mainRef = db.reference("settings").child(auth.currentUser?.uid ?: "-")

    override fun updateProfileData(
        name: String,
        avatar: String,
        sex: Sex,
        notification: Boolean,
        starting: Int,
        centerId: String
    ) = flow {
        val result = mutableListOf(ProfileUpdateState.NOTHING)
        try {
            val profile = getProfile().first()
            if (name != profile.name) {
                auth.currentUser?.updateProfile(name)
                result.remove(ProfileUpdateState.NOTHING)
                result.add(ProfileUpdateState.NAME)
            }
            if (profile.differs(avatar, sex, notification, starting, centerId)) {
                mainRef.setValue(
                    FirebaseSettings(
                        sex.sex,
                        if (notification) 1 else 0,
                        centerId,
                        starting,
                        avatar
                    )
                )
                result.remove(ProfileUpdateState.NOTHING)
                result.add(ProfileUpdateState.PASSWORD)
            }
            emit(Either.Left(ProfileUpdate(result)))
        } catch (e: FirebaseException) {
            emit(Either.Right(e))
        }
    }

    override fun updateProfileEmail(
        email: String
    ) = flow {
        val result = mutableListOf(ProfileUpdateState.NOTHING)
        try {
            val profile = getProfile().first()
            if (email.isValidEmail() && email != (profile.email)) {
                auth.currentUser?.verifyBeforeUpdateEmail(newEmail = email)
                result.remove(ProfileUpdateState.NOTHING)
                result.add(ProfileUpdateState.PASSWORD)
            }
            emit(Either.Left(ProfileUpdate(result)))
        } catch (e: FirebaseException) {
            emit(Either.Right(e))
        }
    }

    override fun updateProfilePassword(
        password: String
    ) = flow {
        val result = mutableListOf(ProfileUpdateState.NOTHING)
        try {
            if (password.isNotEmpty() && password.length > 5) {
                auth.currentUser?.updatePassword(password)
                result.remove(ProfileUpdateState.NOTHING)
                result.add(ProfileUpdateState.PASSWORD)
            }
            emit(Either.Left(ProfileUpdate(result)))
        } catch (e: FirebaseException) {
            emit(Either.Right(e))
        }
    }

    override fun getProfile() = mainRef
        .valueEvents
        .map { settings ->
            settings.value<Map<String, FirebaseSettings>>().values.toList().map {
                it.toProfile(auth.currentUser?.uid ?: "", auth.currentUser?.displayName ?: "", auth.currentUser?.email ?: "")
            }.first()
        }
}