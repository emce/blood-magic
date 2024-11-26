package mobi.cwiklinski.bloodline.data.firebase

import dev.gitlive.firebase.FirebaseException
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import mobi.cwiklinski.bloodline.common.Either
import mobi.cwiklinski.bloodline.common.isValidEmail
import mobi.cwiklinski.bloodline.data.api.ProfileService
import mobi.cwiklinski.bloodline.data.api.ProfileUpdate
import mobi.cwiklinski.bloodline.data.api.ProfileUpdateState
import mobi.cwiklinski.bloodline.data.firebase.model.FirebaseSettings
import mobi.cwiklinski.bloodline.domain.Sex
import mobi.cwiklinski.bloodline.domain.model.Profile

class ProfileServiceImplementation(
    db: FirebaseDatabase,
    private val auth: FirebaseAuth,
    scope: CoroutineScope
) : ProfileService {

    private val profileFlow = MutableStateFlow<Profile?>(null)
    private val mainRef = db.reference("settings").child(auth.currentUser?.uid ?: "-")

    init {
        scope.launch {
            combine(
                mainRef.valueEvents.map { it.value<Map<String, FirebaseSettings>>().values.toList() },
                auth.authStateChanged
            ) { settings, user ->
                    settings.map {
                        it.toProfile(
                            user?.uid ?: "",
                            user?.displayName ?: "",
                            user?.email ?: ""
                        )
                    }
                }
            .collectLatest {
                profileFlow.emit(it.first())
            }
        }
    }

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
            profileFlow.value?.let { profile ->
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
            }
        } catch (e: FirebaseException) {
            emit(Either.Right(e))
        }
    }

    override fun updateProfileEmail(email: String) = flow {
        val result = mutableListOf(ProfileUpdateState.NOTHING)
        try {
            profileFlow.value?.let { profile ->
                if (email.isValidEmail() && email != (profile.email)) {
                    auth.currentUser?.verifyBeforeUpdateEmail(newEmail = email)
                    result.remove(ProfileUpdateState.NOTHING)
                    result.add(ProfileUpdateState.PASSWORD)
                }
                emit(Either.Left(ProfileUpdate(result)))
            }
        } catch (e: FirebaseException) {
            emit(Either.Right(e))
        }
    }

    override fun updateProfilePassword(password: String) = flow {
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

    override fun getProfile() = profileFlow.asStateFlow()
}