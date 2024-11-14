package mobi.cwiklinski.bloodline.data.firebase

import dev.gitlive.firebase.FirebaseException
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.map
import mobi.cwiklinski.bloodline.data.api.Either
import mobi.cwiklinski.bloodline.data.api.ProfileService
import mobi.cwiklinski.bloodline.data.firebase.model.FirebaseSettings
import mobi.cwiklinski.bloodline.domain.Constants.Sex
import mobi.cwiklinski.bloodline.domain.util.flattenToList

class ProfileServiceImplementation(val db: FirebaseDatabase, val auth: FirebaseAuth) : ProfileService {

    private val mainRef = db.reference("settings").child(auth.currentUser?.uid ?: "-")

    override suspend fun updateProfile(
        newName: String,
        newAvatar: String,
        newSex: Sex,
        newNotification: Boolean,
        newStarting: Int,
        newCenterId: String
    ) =
        try {
            auth.currentUser?.updateProfile(newName)
            mainRef.setValue(FirebaseSettings(newSex.sex, if (newNotification) 1 else 0, newCenterId, newStarting, newAvatar))
            val newProfile = getProfile()
            Either.Left(newProfile)
        } catch (e: FirebaseException) {
            Either.Right(e)
        }

    override suspend fun getProfile() = mainRef
        .valueEvents
        .map { it.value<Map<String, FirebaseSettings>>().values.toList() }
        .flattenToList()
        .map { it.toProfile(auth.currentUser?.displayName ?: "", auth.currentUser?.email ?: "") }
        .first()
}