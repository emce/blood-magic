package mobi.cwiklinski.bloodline.data.filed

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import mobi.cwiklinski.bloodline.domain.DonationType
import mobi.cwiklinski.bloodline.domain.model.Center
import mobi.cwiklinski.bloodline.domain.model.Donation
import mobi.cwiklinski.bloodline.domain.model.Profile
import mobi.cwiklinski.bloodline.storage.api.StorageService

object DataFiledTestTools {

    fun generateDonation(
        id: String = DummyData.generateString(),
        date: LocalDate = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault()).date,
        type: DonationType = DonationType.entries.random(),
        amount: Int = (41..50).random() * 10,
        hemoglobin: Float = (0..100).random().toFloat(),
        systolic: Int = (90..150).random(),
        diastolic: Int = (60..90).random(),
        disqualification: Boolean = listOf(true, false).random(),
        center: Center = DummyData.CENTERS.random()
    ) = Donation(
        id = id,
        date = date,
        type = type,
        amount = amount,
        hemoglobin = hemoglobin,
        systolic = systolic,
        diastolic = diastolic,
        disqualification = disqualification,
        center = center
    )

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

        override suspend fun getProfile(): Profile? =
            if (storage.containsKey("profile")) Profile.fromJson(storage.getOrElse("profile") { "{}" }) else null

        override suspend fun deleteProfile(): Boolean {
            storage.remove("profile")
            return !storage.containsKey("profile")
        }

        override suspend fun exists(key: String) = storage.containsKey(key)

        override suspend fun clearAll() {
            storage.clear()
        }

    }
}