package mobi.cwiklinski.bloodline.ui.model

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import mobi.cwiklinski.bloodline.auth.api.AuthenticationState
import mobi.cwiklinski.bloodline.auth.filed.AuthData
import mobi.cwiklinski.bloodline.auth.filed.AuthenticationServiceImpl
import mobi.cwiklinski.bloodline.ui.util.UiTestTools
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class SplashScreenModelTest {

    private val scheduler = TestCoroutineScheduler()
    private val dispatcher = StandardTestDispatcher(scheduler)

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `redirects to home is user is logged`() = runTest {
        val storage = UiTestTools.getStorageService()
        val authService = AuthenticationServiceImpl(storage)
        val credentials = AuthData.users.entries.last()
        authService.loginWithEmailAndPassword(credentials.key, credentials.value)
        testScheduler.advanceUntilIdle()
        val model = SplashScreenModel(authService)
        model.onStart()
        testScheduler.advanceTimeBy(SplashScreenModel.SPLASH_DELAY * 2)
        model.state.test {
            assertIs<AuthenticationState.Logged>(awaitItem())
        }
    }

    @Test
    fun `redirects to login is user is not logged`() = runTest {
        val storage = UiTestTools.getStorageService()
        val authService = AuthenticationServiceImpl(storage)
        val model = SplashScreenModel(authService)
        testScheduler.advanceTimeBy(SplashScreenModel.SPLASH_DELAY * 2)
        model.state.test {
            assertIs<AuthenticationState.NotLogged>(awaitItem())
        }
    }
}