package mobi.cwiklinski.bloodline.storage.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import mobi.cwiklinski.bloodline.storage.api.StorageService
import okio.Path.Companion.toPath
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class StorageTests {

    private val testDispatcher = UnconfinedTestDispatcher()
    private var preferencesScope: CoroutineScope = CoroutineScope(testDispatcher + Job())
    private val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.createWithPath(
        corruptionHandler = null,
        migrations = emptyList(),
        scope = preferencesScope,
        produceFile = { StorageService.FILE_NAME.toPath() },
    )
    private var storage: StorageService = StorageServiceImpl(dataStore)

    @AfterTest
    fun tearDown() {
        preferencesScope.cancel()
    }

    @Test
    fun `writes and reads String data`() = runTest {
        val key = "test_key "
        val value = "test_value"
        val defaultValue = "not_stored_value"

        storage.storeString(key, value)

        val storedValue = storage.getString(key, "")
        val noStoredValue = storage.getString("not_key", defaultValue)
        assertEquals(value, storedValue)
        assertEquals(noStoredValue, defaultValue)
    }

}