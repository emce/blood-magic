package mobi.cwiklinski.bloodline.ui.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import mobi.cwiklinski.bloodline.auth.api.AuthResult
import mobi.cwiklinski.bloodline.data.filed.DummyData
import mobi.cwiklinski.bloodline.ui.manager.AppCallbackManager
import mobi.cwiklinski.bloodline.ui.util.UiTestTools
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ResetScreenModelTest {
    private val scheduler = TestCoroutineScheduler()
    private val dispatcher = UnconfinedTestDispatcher(scheduler)
    private val scope = CoroutineScope(dispatcher)
    private val callbackManager = AppCallbackManager(scope)

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun `returns error with incorrect email address`() = runTest {
        val model = getModel(AuthResult.Success(), false)
        val email = "asdas8d7&adasd"
        model.onPasswordReset(email)
        assertIs<ResetState.Error>(model.state.first())
        assertTrue {
            (model.state.first() as ResetState.Error).errors.contains(ResetError.EMAIL_ERROR)
        }
    }

    @Test
    fun `passes with correct email address`() = runTest {
        val model = getModel(AuthResult.Success(), false)
        val email = DummyData.ACCOUNTS.last().first
        model.onPasswordReset(email)
        assertIs<ResetState.Sent>(model.state.first())
    }

    private fun getModel(
        authResult: AuthResult = AuthResult.Success(),
        authSecond: Boolean = true
    ) = ResetScreenModel(
        callbackManager,
        UiTestTools.getAuthService(authResult, authSecond),
    )

}