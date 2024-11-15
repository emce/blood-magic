package mobi.cwiklinski.bloodline.data.api

import mobi.cwiklinski.bloodline.domain.Sex
import mobi.cwiklinski.bloodline.domain.model.Profile

interface ProfileService {

    suspend fun updateProfile(
        id: String,
        newName: String,
        newAvatar: String,
        sex: Sex,
        notification: Boolean,
        starting: Int,
        centerId: String
    ): Either<Profile, Throwable>

    suspend fun getProfile(): Profile
}