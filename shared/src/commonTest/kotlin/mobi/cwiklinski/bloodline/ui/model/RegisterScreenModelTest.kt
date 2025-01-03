package mobi.cwiklinski.bloodline.ui.model

import app.cash.turbine.test
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import mobi.cwiklinski.bloodline.auth.api.AuthResult
import mobi.cwiklinski.bloodline.common.manager.AppCallbackManager
import mobi.cwiklinski.bloodline.ui.util.UiTestTools
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertIsNot
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterScreenModelTest {

    private val profile = UiTestTools.generateProfile()
    private val testDispatcher = UnconfinedTestDispatcher()
    private val storageService = UiTestTools.getStorageService()
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
        val email1 = "sfdd-afaf"
        val password = "5qe313434342"
        model.onRegisterSubmit(email1, password, password)
        model.state.test {
            val state = awaitItem()
            assertIs<RegisterState.Error>(state)
            assertTrue {
                state.errors.contains(RegisterError.EMAIL_ERROR)
            }
        }
    }

    @Test
    fun `passing with correct email address`() = runTest {
        val model = getDefaultModel()
        val email1 = "sfdd@afaf.com"
        val password = "5qe313434342"
        model.onRegisterSubmit(email1, password, password)
        model.state.test {
            assertIsNot<RegisterState.Error>(awaitItem())
        }
    }

    @Test
    fun `return error with incorrect password length`() = runTest {
        val model = getDefaultModel()
        val email = "sfdd@afaf.com"
        val password = "5qe"
        model.onRegisterSubmit(email, password, password)
        model.state.test {
            val state = awaitItem()
            assertIs<RegisterState.Error>(state)
            assertTrue {
                state.errors.contains(RegisterError.PASSWORD_ERROR)
            }
        }
    }

    @Test
    fun `passing with correct password length`() = runTest {
        val model = getDefaultModel()
        val email = "sfdd@afaf.com"
        val password = "5qe313434342"
        val repeat = "5qe313434342"
        model.onRegisterSubmit(email, password, repeat)
        model.state.test {
            assertIsNot<RegisterState.Error>(awaitItem())
        }
    }

    @Test
    fun `return error with incorrect password repetition`() = runTest {
        val model = getDefaultModel()
        val email = "sfdd@afaf.com"
        val password = "5qe313434342"
        val repeat = "54356345345"
        model.onRegisterSubmit(email, password, repeat)
        model.state.test {
            val state = awaitItem()
            assertIs<RegisterState.Error>(state)
            assertTrue {
                state.errors.contains(RegisterError.REPEAT_ERROR)
            }
        }
    }

    @Test
    fun `passing with correct password repetition`() = runTest {
        val model = getDefaultModel()
        val email = "sfdd@afaf.com"
        val password = "5qe313434342"
        val repeat = "5qe313434342"
        model.onRegisterSubmit(email, password, repeat)
        model.state.test {
            assertIsNot<RegisterState.Error>(awaitItem())
        }
    }

    @Test
    fun `passes register process with success`() = runTest {
        val model = getDefaultModel()
        val email = "sfdd@afaf.com"
        val password = "5qe313434342"
        val repeat = "5qe313434342"
        model.onRegisterSubmit(email, password, repeat)
        model.state.test {
            assertIs<RegisterState.Registered>(awaitItem())
        }
    }

    private fun getDefaultModel() = getModel(
        AuthResult.Success(),
        false
    )

    private fun getModel(
        authResult: AuthResult = AuthResult.Success(),
        authSecond: Boolean = true
    ) = RegisterScreenModel(
        callbackManager,
        UiTestTools.getAuthService(authResult, authSecond),
        UiTestTools.getProfileService(storageService, CoroutineScope(testDispatcher)),
        UiTestTools.getStorageService()
    )
}