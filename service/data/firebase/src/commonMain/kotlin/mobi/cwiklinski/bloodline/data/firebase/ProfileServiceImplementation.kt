package mobi.cwiklinski.bloodline.data.firebase

import dev.gitlive.firebase.FirebaseException
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import mobi.cwiklinski.bloodline.common.Either
import mobi.cwiklinski.bloodline.data.api.ProfileService
import mobi.cwiklinski.bloodline.data.firebase.model.FirebaseSettings
import mobi.cwiklinski.bloodline.domain.Sex

class ProfileServiceImplementation(val db: FirebaseDatabase, val auth: FirebaseAuth) : ProfileService {

    private val mainRef = db.reference("settings").child(auth.currentUser?.uid ?: "-")

    override fun updateProfile(
        id: String,
        name: String,
        avatar: String,
        sex: Sex,
        notification: Boolean,
        starting: Int,
        centerId: String
    ) = flow {
        try {
            auth.currentUser?.updateProfile(name)
            mainRef.setValue(
                FirebaseSettings(sex.sex,
                    if (notification) 1 else 0,
                    centerId,
                    starting,
                    avatar
                )
            )
            val newProfile = getProfile()
            emit(Either.Left(newProfile.first()))
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