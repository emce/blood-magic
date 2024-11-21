package mobi.cwiklinski.bloodline.ui.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import mobi.cwiklinski.bloodline.auth.api.AuthError
import mobi.cwiklinski.bloodline.auth.api.AuthResult
import mobi.cwiklinski.bloodline.auth.api.AuthenticationState
import mobi.cwiklinski.bloodline.ui.util.UiTestTools
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertIs

class SplashScreenModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `redirects to home is user is logged`() = runTest {
        val authService = UiTestTools.getAuthService(AuthResult.Success(), false)
        val model = SplashScreenModel(authService)
        model.onStart()
        model.state
            .take(1)
            .onEach { assertIs<AuthenticationState.Logged>(it) }
            .launchIn(backgroundScope)

    }

    @Test
    fun `redirects to login is user is not logged`() = runTest {
        val authService = UiTestTools.getAuthService(AuthResult.Failure(AuthError.INCORRECT_PASSWORD), false)
        val model = SplashScreenModel(authService)
        model.state
            .take(1)
            .onEach { assertIs<AuthenticationState.NotLogged>(it) }
            .launchIn(backgroundScope)
    }
}