package mobi.cwiklinski.bloodline.ui.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.serialization.json.Json
import mobi.cwiklinski.bloodline.auth.api.AuthResult
import mobi.cwiklinski.bloodline.auth.api.AuthenticationService
import mobi.cwiklinski.bloodline.auth.api.AuthenticationState
import mobi.cwiklinski.bloodline.common.today
import mobi.cwiklinski.bloodline.data.filed.DummyData
import mobi.cwiklinski.bloodline.data.filed.ProfileServiceImplementation
import mobi.cwiklinski.bloodline.domain.DonationType
import mobi.cwiklinski.bloodline.domain.Sex
import mobi.cwiklinski.bloodline.domain.model.Center
import mobi.cwiklinski.bloodline.domain.model.Donation
import mobi.cwiklinski.bloodline.domain.model.Profile
import mobi.cwiklinski.bloodline.storage.api.StorageService
import mobi.cwiklinski.bloodline.test.CommonTestTools

object UiTestTools {

    fun getAuthService(result: AuthResult, secondResult: Boolean = true) = object :
        AuthenticationService {
        override val authenticationState = MutableStateFlow<AuthenticationState>(AuthenticationState.Idle)
        override fun loginWithEmailAndPassword(email: String, password: String) = flowOf(result)
        override fun registerWithEmailAndPassWord(email: String, password: String) = flowOf(result)
        override fun logOut(): Flow<Boolean> = flowOf(secondResult)
        override fun resetPassword(email: String) = flowOf(result)
    }

    fun getStorageService()  = object : StorageService {

        private val storage = mutableMapOf<String, String>()

        override suspend fun storeString(key: String, objectToStore: String) {
            storage[key] = objectToStore
        }

        override suspend fun getString(key: String, defaultValue: String) =
            storage.getOrElse(key) { defaultValue }

        override suspend fun deleteString(key: String): Boolean {
            storage.remove(key)
            return exists(key)
        }

        override suspend fun storeInt(key: String, objectToStore: Int) {
            storage[key] = objectToStore.toString()
        }

        override suspend fun getInt(key: String, defaultValue: Int) =
            storage.getOrElse(key) { defaultValue.toString() }.toInt()

        override suspend fun deleteInt(key: String): Boolean {
            storage.remove(key)
            return exists(key)
        }

        override suspend fun storeBoolean(key: String, objectToStore: Boolean) {
            storage[key] = if (objectToStore) "1" else "0"
        }

        override suspend fun getBoolean(key: String, defaultValue: Boolean) =
            storage.getOrElse(key) { if (defaultValue) "1" else "0" } == "1"

        override suspend fun deleteBoolean(key: String): Boolean {
            storage.remove(key)
            return exists(key)
        }

        override suspend fun storeProfile(profile: Profile) {
            storage["profile"] = profile.toJson()
        }

        override suspend fun getProfile(): Profile?  =
            Profile.fromJson(storage.getOrElse("profile") { "" })

        override suspend fun deleteProfile(): Boolean {
            storage.remove("profile")
            return !storage.containsKey("profile")
        }

        override suspend fun exists(key: String) = storage.containsKey(key)

        override suspend fun clearAll() {
            storage.clear()
        }

    }

    fun getProfileService(storageService: StorageService, coroutineScope: CoroutineScope) =
        ProfileServiceImplementation(storageService, coroutineScope)

    fun generateProfile(
        id: String? = CommonTestTools.randomString(9),
        name: String = CommonTestTools.randomString(14),
        email: String = CommonTestTools.generateEmail(),
        avatar: String = Avatar.entries[CommonTestTools.randomInt(0, Avatar.entries.size - 1)].name,
        sex: Sex = Sex.entries[CommonTestTools.randomInt(0, 1)],
        notification: Boolean = CommonTestTools.randomInt(0, 1) == 1,
        starting: Int = CommonTestTools.randomInt(0, 1000),
        centerId: String = CommonTestTools.randomString(9)
    ) = Profile(
        id,
        name,
        email,
        avatar,
        sex,
        notification,
        starting,
        centerId
    )

    fun generateDonation(
        id: String = CommonTestTools.randomString(10),
        amount: Int = CommonTestTools.randomInt(200, 500),
        date: LocalDate = today().minus(CommonTestTools.randomInt(2, 50), DateTimeUnit.DAY),
        center: Center = DummyData.CENTERS.random(),
        type: Int = DonationType.entries.random().type,
        hemoglobin: Int = CommonTestTools.randomInt(200, 500),
        systolic: Int = CommonTestTools.randomInt(100, 150),
        diastolic: Int = CommonTestTools.randomInt(100, 150),
        disqualification: Boolean = listOf(true, false).random()
    ) = Donation(
        id,
        date,
        DonationType.byType(type),
        amount,
        hemoglobin.toFloat(),
        systolic,
        diastolic,
        disqualification,
        center
    )
}