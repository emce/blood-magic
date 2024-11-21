package mobi.cwiklinski.bloodline.ui.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import mobi.cwiklinski.bloodline.auth.api.AuthError
import mobi.cwiklinski.bloodline.auth.api.AuthResult
import mobi.cwiklinski.bloodline.data.api.ProfileUpdate
import mobi.cwiklinski.bloodline.data.api.ProfileUpdateState
import mobi.cwiklinski.bloodline.domain.model.Profile
import mobi.cwiklinski.bloodline.ui.util.UiTestTools
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertIsNot
import kotlin.test.assertTrue

class LoginScreenModelTest {

    private val profile = UiTestTools.generateProfile()

    private val update = ProfileUpdate(listOf(ProfileUpdateState.ALL))

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun `return error with incorrect email address`() = runTest {
        val model = getDefaultModel()
        val email1 = "sfdd@afaf"
        val password = "5qe313434342"
        model.onLoginSubmit(email1, password)
        assertIs<LoginState.Error>(model.state.first())
        assertTrue {
            (model.state.first() as LoginState.Error).errors.contains(LoginError.EMAIL_ERROR)
        }
    }

    @Test
    fun `passing with correct email address`() = runTest {
        val model = getDefaultModel()
        val email1 = "sfdd@afaf.com"
        val password = "5qe313434342"
        model.onLoginSubmit(email1, password)
        assertIsNot<LoginState.Error>(model.state.first())
    }

    @Test
    fun `return error with incorrect password length`() = runTest {
        val model = getDefaultModel()
        val email = "sfdd@afaf.com"
        val password = "5qe"
        model.onLoginSubmit(email, password)
        assertIs<LoginState.Error>(model.state.first())
        assertTrue {
            (model.state.first() as LoginState.Error).errors.contains(LoginError.PASSWORD_ERROR)
        }
    }

    @Test
    fun `passing with correct password length`() = runTest {
        val model = getDefaultModel()
        val email = "sfdd@afaf.com"
        val password = "5qe313434342"
        model.onLoginSubmit(email, password)
        assertIsNot<LoginState.Error>(model.state.first())
    }

    @Test
    fun `return error when credentials are incorrect`() = runTest {
        val model = getDefaultModel()
        val email = "sfdd@afaf.com"
        val password = "5qe"
        model.onLoginSubmit(email, password)
        assertIs<LoginState.Error>(model.state.first())
        assertTrue {
            (model.state.first() as LoginState.Error).errors.contains(LoginError.PASSWORD_ERROR)
        }
    }

    @Test
    fun `passes login process with success`() = runTest {
        val model = getDefaultModel()
        val email = "sfdd@afaf.com"
        val password = "5qe313434342"
        model.onLoginSubmit(email, password)
        assertIs<LoginState.LoggedIn>(model.state.first())
    }

    private fun getDefaultModel() = getModel(
        AuthResult.Success(),
        false,
        profile,
        update
    )

    private fun getModel(
        authResult: AuthResult = AuthResult.Success(),
        authSecond: Boolean = true,
        profile: Profile? = null,
        profileUpdate: ProfileUpdate
    ) = LoginScreenModel(
        UiTestTools.getAuthService(authResult, authSecond),
        UiTestTools.getProfileService(profile, profileUpdate),
        UiTestTools.getStorageService()
    )
}