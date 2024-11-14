package mobi.cwiklinski.bloodline.storage.api

import mobi.cwiklinski.bloodline.domain.model.Profile

interface StorageService {

    suspend fun storeString(key: String, objectToStore: String)

    suspend fun getString(key: String, defaultValue: String): String

    suspend fun storeInt(key: String, objectToStore: Int)

    suspend fun getInt(key: String, defaultValue: Int): Int

    suspend fun storeBoolean(key: String, objectToStore: Boolean)

    suspend fun getBoolean(key: String, defaultValue: Boolean): Boolean

    suspend fun storeProfile(profile: Profile)

    suspend fun getProfile(): Profile

    suspend fun exists(key: String): Boolean

    suspend fun clearAll()

    companion object {
        const val FILE_NAME = "blood-magic.preferences_pb"
    }
}