package mobi.cwiklinski.bloodline.data.filed

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import mobi.cwiklinski.bloodline.common.Either
import mobi.cwiklinski.bloodline.data.api.ProfileService
import mobi.cwiklinski.bloodline.data.api.ProfileUpdate
import mobi.cwiklinski.bloodline.data.api.ProfileUpdateState
import mobi.cwiklinski.bloodline.domain.Sex
import mobi.cwiklinski.bloodline.domain.model.Profile
import mobi.cwiklinski.bloodline.storage.api.StorageService

class ProfileServiceImplementation(
    private val storageService: StorageService,
    coroutineScope: CoroutineScope
) : ProfileService {

    private lateinit var _memory: MutableStateFlow<Profile>

    init {
        coroutineScope.launch {
            storageService.getProfile()?.let{
                _memory = MutableStateFlow(it)
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
    ): Flow<Either<ProfileUpdate, Throwable>> = flow {
        val oldProfile = _memory.value
        val newProfile = oldProfile.withData(
            name,
            avatar,
            sex,
            notification,
            starting,
            centerId
        )
        storageService.storeProfile(newProfile)
        _memory.value = newProfile
        emit(Either.Left(ProfileUpdate(listOf(ProfileUpdateState.DATA))))
    }

    override fun updateProfileEmail(email: String): Flow<Either<ProfileUpdate, Throwable>> = flow {
        val oldProfile = _memory.value
        val newProfile = oldProfile.withEmail(email)
        storageService.storeProfile(newProfile)
        _memory.value = newProfile
        emit(Either.Left(ProfileUpdate(listOf(ProfileUpdateState.EMAIL))))
    }

    override fun updateProfilePassword(password: String): Flow<Either<ProfileUpdate, Throwable>> =
        flowOf(Either.Left(ProfileUpdate(listOf(ProfileUpdateState.PASSWORD))))

    override fun getProfile(): StateFlow<Profile> = _memory
}