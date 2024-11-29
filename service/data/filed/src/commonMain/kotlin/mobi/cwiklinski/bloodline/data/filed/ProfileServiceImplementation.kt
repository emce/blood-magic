package mobi.cwiklinski.bloodline.data.filed

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mobi.cwiklinski.bloodline.common.Either
import mobi.cwiklinski.bloodline.data.api.ProfileService
import mobi.cwiklinski.bloodline.data.api.ProfileServiceState
import mobi.cwiklinski.bloodline.data.api.ProfileUpdate
import mobi.cwiklinski.bloodline.data.api.ProfileUpdateState
import mobi.cwiklinski.bloodline.domain.Sex
import mobi.cwiklinski.bloodline.domain.model.Profile
import mobi.cwiklinski.bloodline.storage.api.StorageService

class ProfileServiceImplementation(
    private val storageService: StorageService,
    coroutineScope: CoroutineScope
) : ProfileService {

    private val _memory = MutableStateFlow(DummyData.generateProfile())

    init {
        coroutineScope.launch {
            storageService.getProfile()?.let{
                _memory.value = it
            }
        }
    }

    override fun updateProfileData(
        name: String,
        email: String,
        avatar: String,
        sex: Sex,
        notification: Boolean,
        starting: Int,
        centerId: String
    ): Flow<ProfileServiceState>  = callbackFlow {
        try {
            withContext(Dispatchers.Default) {
                _memory.value.let {
                    val newProfile = it.withData(
                        name,
                        email,
                        avatar,
                        sex,
                        notification,
                        starting,
                        centerId
                    )
                    storageService.storeProfile(newProfile)
                    _memory.value = newProfile
                }
            }
            trySend(ProfileServiceState.Saved)
        } catch (exception: Exception) {
            trySend(ProfileServiceState.Error(exception))
        }
        awaitClose { }
    }

    override fun updateProfileEmail(email: String): Flow<Either<ProfileUpdate, Throwable>> = flow {
        _memory.value.let {
            val newProfile = it.withEmail(email)
            storageService.storeProfile(newProfile)
            _memory.value = newProfile
            emit(Either.Left(ProfileUpdate(listOf(ProfileUpdateState.EMAIL))))
        }
    }

    override fun updateProfilePassword(password: String): Flow<Either<ProfileUpdate, Throwable>> =
        flowOf(Either.Left(ProfileUpdate(listOf(ProfileUpdateState.PASSWORD))))

    override fun getProfile(): StateFlow<Profile> = _memory.asStateFlow()
}