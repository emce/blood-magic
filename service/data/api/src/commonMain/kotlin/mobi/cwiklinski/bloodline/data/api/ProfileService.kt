package mobi.cwiklinski.bloodline.data.api

import kotlinx.coroutines.flow.Flow
import mobi.cwiklinski.bloodline.common.Either
import mobi.cwiklinski.bloodline.domain.Sex
import mobi.cwiklinski.bloodline.domain.model.Profile

interface ProfileService {

    fun updateProfileData(
        name: String,
        email: String,
        avatar: String,
        sex: Sex,
        notification: Boolean,
        starting: Int,
        centerId: String
    ): Flow<ProfileServiceState>

    fun updateProfileEmail(
        email: String
    ): Flow<Either<ProfileUpdate, Throwable>>

    fun updateProfilePassword(
        password: String
    ): Flow<Either<ProfileUpdate, Throwable>>

    fun getProfile(): Flow<Profile>
}

data class ProfileUpdate(val updated: List<ProfileUpdateState>)

enum class ProfileUpdateState {
    NOTHING,
    NAME,
    EMAIL,
    PASSWORD,
    DATA,
    ALL
}

sealed class ProfileServiceState {
    data object Idle: ProfileServiceState()
    data object Saving: ProfileServiceState()
    data object Saved: ProfileServiceState()
    data class Error(val error: Throwable): ProfileServiceState()
}