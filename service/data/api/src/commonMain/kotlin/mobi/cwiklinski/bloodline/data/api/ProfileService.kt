package mobi.cwiklinski.bloodline.data.api

import kotlinx.coroutines.flow.Flow
import mobi.cwiklinski.bloodline.common.Either
import mobi.cwiklinski.bloodline.domain.Sex
import mobi.cwiklinski.bloodline.domain.model.Profile

interface ProfileService {

    fun updateProfile(
        id: String,
        name: String,
        avatar: String,
        sex: Sex,
        notification: Boolean,
        starting: Int,
        centerId: String
    ): Flow<Either<Profile, Throwable>>

    fun getProfile(): Flow<Profile>
}