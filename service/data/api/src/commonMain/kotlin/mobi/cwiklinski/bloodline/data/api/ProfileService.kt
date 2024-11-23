package mobi.cwiklinski.bloodline.data.api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import mobi.cwiklinski.bloodline.common.Either
import mobi.cwiklinski.bloodline.domain.Sex
import mobi.cwiklinski.bloodline.domain.model.Profile

interface ProfileService {

    fun updateProfileData(
        name: String,
        avatar: String,
        sex: Sex,
        notification: Boolean,
        starting: Int,
        centerId: String
    ): Flow<Either<ProfileUpdate, Throwable>>

    fun updateProfileEmail(
        email: String
    ): Flow<Either<ProfileUpdate, Throwable>>

    fun updateProfilePassword(
        password: String
    ): Flow<Either<ProfileUpdate, Throwable>>

    fun getProfile(): StateFlow<Profile>
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