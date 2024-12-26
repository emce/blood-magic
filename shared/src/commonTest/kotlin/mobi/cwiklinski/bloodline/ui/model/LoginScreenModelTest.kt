package mobi.cwiklinski.bloodline.ui.model

import app.cash.turbine.test
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import mobi.cwiklinski.bloodline.auth.api.AuthResult
import mobi.cwiklinski.bloodline.ui.manager.AppCallbackManager
import mobi.cwiklinski.bloodline.ui.util.UiTestTools
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertIsNot
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class LoginScreenModelTest {

    private val profile = UiTestTools.generateProfile()
    private val storageService = UiTestTools.getStorageService()
    private val testDispatcher = UnconfinedTestDispatcher()
    private val scope = CoroutineScope(testDispatcher)
    private val callbackManager = AppCallbackManager(scope)

    @BeforeTest
    fun setUp() = runTest {
        Dispatchers.setMain(testDispatcher)
        storageService.storeProfile(profile)
    }

    @Test
    fun `return error with incorrect email address`() = runTest {
        val model = getDefaultModel()
        val email1 = "sfdd.afaf-io"
        val password = "5qe313434342"
        model.onLoginSubmit(email1, password)
        model.state.test {
            val state = awaitItem()
            assertIs<LoginState.Error>(state)
            assertTrue {
                state.errors.contains(LoginError.EMAIL_ERROR)
            }
        }
    }

    @Test
    fun `passing with correct email address`() = runTest {
        val model = getDefaultModel()
        val email1 = "sfdd@afaf.com"
        val password = "5qe313434342"
        model.onLoginSubmit(email1, password)
        model.state.test {
            assertIsNot<LoginState.Error>(awaitItem())
        }
    }

    @Test
    fun `returns error with incorrect password length`() = runTest {
        val model = getDefaultModel()
        val email = "sfdd@afaf.com"
        val password = "5qe"
        model.onLoginSubmit(email, password)
        model.state.test {
            val state = awaitItem()
            assertIs<LoginState.Error>(state)
            assertTrue {
                state.errors.contains(LoginError.PASSWORD_ERROR)
            }
        }
    }

    @Test
    fun `passes with correct password length`() = runTest {
        val model = getDefaultModel()
        val email = "sfdd@afaf.com"
        val password = "5qe313434342"
        model.onLoginSubmit(email, password)
        model.state.test {
            assertIsNot<LoginState.Error>(awaitItem())
        }
    }

    @Test
    fun `returns error when credentials are incorrect`() = runTest {
        val model = getDefaultModel()
        val email = "sfdd@afaf.com"
        val password = "5qe"
        model.onLoginSubmit(email, password)
        model.state.test {
            val state = awaitItem()
            assertIs<LoginState.Error>(state)
            assertTrue {
                state.errors.contains(LoginError.PASSWORD_ERROR)
            }
        }
    }

    @Test
    fun `passes login process with success`() = runTest {
        val model = getDefaultModel()
        val email = "sfdd@afaf.com"
        val password = "5qe313434342"
        model.onLoginSubmit(email, password)
        model.state.test {
            assertIs<LoginState.LoggedIn>(awaitItem())
        }
    }

    private fun getDefaultModel() = getModel(
        AuthResult.Success(),
        false
    )

    private fun getModel(
        authResult: AuthResult = AuthResult.Success(),
        authSecond: Boolean = true
    ) = LoginScreenModel(
        callbackManager,
        UiTestTools.getAuthService(authResult, authSecond),
        UiTestTools.getStorageService()
    )
}