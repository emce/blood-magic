package mobi.cwiklinski.bloodline.auth.filed

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import mobi.cwiklinski.bloodline.auth.api.AuthError
import mobi.cwiklinski.bloodline.auth.api.AuthResult
import mobi.cwiklinski.bloodline.auth.api.AuthenticationService
import mobi.cwiklinski.bloodline.auth.api.AuthenticationState
import mobi.cwiklinski.bloodline.storage.api.StorageService
import mobi.cwiklinski.bloodline.storage.datastore.StorageServiceImpl
import okio.Path.Companion.toPath
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AuthenticationServiceTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private var preferencesScope: CoroutineScope = CoroutineScope(testDispatcher + Job())
    private val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.createWithPath(
        corruptionHandler = null,
        migrations = emptyList(),
        scope = preferencesScope,
        produceFile = { StorageService.FILE_NAME.toPath() },
    )
    private var storage: StorageService = StorageServiceImpl(dataStore)
    private val auth: AuthenticationService = AuthenticationServiceImpl(storage)

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        preferencesScope.cancel()
    }

    @Test
    fun `logs user with correct email and password`() = runTest {
        val credentials = AuthData.users.entries.last()
        val results = auth.loginWithEmailAndPassword(credentials.key, credentials.value).toList()
        assertTrue { results.isNotEmpty() }
        assertIs<AuthResult.Success>(results.first())
        assertIs<AuthenticationState.Logged>(auth.authenticationState.first())
    }

    @Test
    fun `not logs user with incorrect email`() = runTest {
        val credentials = AuthData.notUsers.entries.last()
        val results = auth.loginWithEmailAndPassword(credentials.key, credentials.value).toList()
        assertTrue { results.isNotEmpty() }
        val result = results.first()
        assertIs<AuthResult.Failure>(result)
        assertIs<AuthError>(result.error)
        assertEquals(AuthError.INCORRECT_EMAIL, result.error)
    }

    @Test
    fun `not logs user with incorrect password`() = runTest {
        val goodCredentials = AuthData.users.entries.last()
        val badCredentials = AuthData.notUsers.entries.last()
        val results = auth.loginWithEmailAndPassword(goodCredentials.key, badCredentials.value).toList()
        assertTrue { results.isNotEmpty() }
        val result = results.first()
        assertIs<AuthResult.Failure>(result)
        assertIs<AuthError>(result.error)
        assertEquals(AuthError.INCORRECT_PASSWORD, result.error)
    }

    @Test
    fun `registers user with correct email and password`() = runTest {
        val credentials = AuthData.notUsers.entries.last()
        val results = auth.registerWithEmailAndPassWord(credentials.key, credentials.value).toList()
        assertTrue { results.isNotEmpty() }
        assertIs<AuthResult.Success>(results.first())
    }

    @Test
    fun `logs user out`() = runTest {
        val credentials = AuthData.users.entries.last()
        auth.loginWithEmailAndPassword(credentials.key, credentials.value).toList()
        delay(1000L)
        val result = auth.logOut().first()
        assertEquals(true, result)
        assertIs<AuthenticationState.NotLogged>(auth.authenticationState.first())
    }
}