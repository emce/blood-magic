package mobi.cwiklinski.bloodline.storage.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import mobi.cwiklinski.bloodline.domain.model.Profile
import mobi.cwiklinski.bloodline.storage.api.StorageService

class StorageServiceImpl(private val store: DataStore<Preferences>) : StorageService {
    
    private val _profileKey = "__profile" 

    override suspend fun storeString(key: String, objectToStore: String) {
        store.edit { preferences ->
            preferences[stringPreferencesKey(key)] = objectToStore
        }
    }

    override suspend fun getString(key: String, defaultValue: String) = store
        .data
        .map { preferences ->
            preferences[stringPreferencesKey(key)]
        }
        .first() ?: defaultValue

    override suspend fun deleteString(key: String): Boolean {
        store
            .edit { preferences ->
                preferences.remove(stringPreferencesKey(key))
                exists(key)
            }
        return exists(key)
    }

    override suspend fun storeInt(key: String, objectToStore: Int) {
        store.edit { preferences ->
            preferences[intPreferencesKey(key)] = objectToStore
        }
    }

    override suspend fun getInt(key: String, defaultValue: Int) = store
        .data
        .map { preferences ->
            preferences[intPreferencesKey(key)]
        }
        .first() ?: defaultValue

    override suspend fun deleteInt(key: String): Boolean {
        store
            .edit { preferences ->
                preferences.remove(intPreferencesKey(key))
                exists(key)
            }
        return exists(key)
    }

    override suspend fun storeBoolean(key: String, objectToStore: Boolean) {
        store.edit { preferences ->
            preferences[booleanPreferencesKey(key)] = objectToStore
        }
    }

    override suspend fun getBoolean(key: String, defaultValue: Boolean) = store
        .data
        .map { preferences ->
            preferences[booleanPreferencesKey(key)]
        }
        .first() ?: defaultValue

    override suspend fun deleteBoolean(key: String): Boolean {
        store
            .edit { preferences ->
                preferences.remove(booleanPreferencesKey(key))
                exists(key)
            }
        return exists(key)
    }

    override suspend fun storeProfile(profile: Profile) {
        store.edit { preferences ->
            preferences[stringPreferencesKey(_profileKey)] = profile.toJson()
        }
    }

    override suspend fun getProfile() = Profile.fromJson(store
        .data
        .map { preferences ->
            preferences[stringPreferencesKey(_profileKey)]
        }
        .first() ?: _profileKey)

    override suspend fun deleteProfile(): Boolean {
        store
            .edit { preferences ->
                preferences.remove(stringPreferencesKey(_profileKey))
                exists(_profileKey)
            }
        return exists(_profileKey)
    }

    override suspend fun exists(key: String) = store
        .data
        .map { preference ->
            preference.contains(stringPreferencesKey(key)) ||
                    preference.contains(intPreferencesKey(key)) ||
                    preference.contains(booleanPreferencesKey(key))
        }
        .first()

    override suspend fun clearAll() {
        store.edit { preferences ->
            preferences.clear()
        }
    }
}
