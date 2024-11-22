package mobi.cwiklinski.bloodline.data.filed

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import mobi.cwiklinski.bloodline.common.Either
import mobi.cwiklinski.bloodline.data.api.ProfileService
import mobi.cwiklinski.bloodline.data.api.ProfileUpdate
import mobi.cwiklinski.bloodline.data.api.ProfileUpdateState
import mobi.cwiklinski.bloodline.domain.Sex
import mobi.cwiklinski.bloodline.domain.model.Profile

class ProfileServiceImplementation : ProfileService {

    private var _memory = DummyData.generateProfile()

    override fun updateProfileData(
        name: String,
        avatar: String,
        sex: Sex,
        notification: Boolean,
        starting: Int,
        centerId: String
    ): Flow<Either<ProfileUpdate, Throwable>> = flow {
        val oldProfile = _memory
        _memory = DummyData.generateProfile(
            oldProfile.id ?: DummyData.generateString(),
            name,
            oldProfile.email,
            avatar,
            sex,
            notification,
            starting,
            centerId
        )
        emit(Either.Left(ProfileUpdate(listOf(ProfileUpdateState.DATA))))
    }

    override fun updateProfileEmail(email: String): Flow<Either<ProfileUpdate, Throwable>> = flow {
        val oldProfile = _memory
        _memory = DummyData.generateProfile(
            oldProfile.id ?: DummyData.generateString(),
            oldProfile.name,
            email,
            oldProfile.avatar,
            oldProfile.sex,
            oldProfile.notification,
            oldProfile.starting,
            oldProfile.centerId
        )
        emit(Either.Left(ProfileUpdate(listOf(ProfileUpdateState.EMAIL))))
    }

    override fun updateProfilePassword(password: String): Flow<Either<ProfileUpdate, Throwable>> =
        flowOf(Either.Left(ProfileUpdate(listOf(ProfileUpdateState.PASSWORD))))

    override fun getProfile(): Flow<Profile> = flowOf(_memory)
}