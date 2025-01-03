package mobi.cwiklinski.bloodline.ui.model

import app.cash.turbine.test
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import mobi.cwiklinski.bloodline.auth.api.AuthResult
import mobi.cwiklinski.bloodline.data.filed.CenterServiceImplementation
import mobi.cwiklinski.bloodline.data.filed.DummyData
import mobi.cwiklinski.bloodline.test.CommonTestTools
import mobi.cwiklinski.bloodline.common.manager.AppCallbackManager
import mobi.cwiklinski.bloodline.ui.util.UiTestTools
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileScreenModelTest {

    private val profile = UiTestTools.generateProfile(email = DummyData.ACCOUNTS.random().first)
    private val scheduler = TestCoroutineScheduler()
    private val dispatcher = UnconfinedTestDispatcher(scheduler)
    private val scope = CoroutineScope(dispatcher)
    private val storageService = UiTestTools.getStorageService()
    private lateinit var model: ProfileScreenModel
    private val profileService = UiTestTools.getProfileService(profile)
    private val callbackManager = AppCallbackManager(scope)

    @BeforeTest
    fun setUp() = runTest {
        Dispatchers.setMain(dispatcher)
        runBlocking {
            storageService.storeProfile(profile)
        }
        model = ProfileScreenModel(
            callbackManager,
            profileService,
            CenterServiceImplementation(),
            UiTestTools.getAuthService(AuthResult.Success(), false),
            storageService
        )
        advanceUntilIdle()
        profileService.getProfile().first().let {
            assertEquals(profile.email, it.email)
        }
    }

    @Test
    fun `starts with profile`() = runTest {
        model.profile.first().let { currentProfile ->
            assertNotNull(currentProfile)
            assertEquals(profile.email, currentProfile.email)
        }
    }

    @Test
    fun `updates profile with data`() = runTest {
        val newProfile =
            UiTestTools.generateProfile(id = profile.id ?: CommonTestTools.randomString(9))
        model.onProfileDataUpdate(
            newName = newProfile.name,
            newEmail = newProfile.email,
            newAvatar = newProfile.avatar,
            newSex = newProfile.sex,
            newNotification = newProfile.notification,
            newStarting = newProfile.starting,
            newCenterId = newProfile.centerId
        )
        model.state.test {
            assertIs<ProfileState.Saved>(awaitItem())
        }
        model.profile.test {
            val profile = awaitItem()
            assertNotNull(profile)
            assertEquals(newProfile.name, profile.name)
            assertEquals(newProfile.sex, profile.sex)
            assertEquals(newProfile.centerId, profile.centerId)
        }
    }

    @Test
    fun `updates profile with email`() = runTest {
        val newEmail = DummyData.ACCOUNTS.last().first
        model.onProfileEmailUpdate(newEmail)
        scheduler.advanceUntilIdle()
        model.state.test {
            assertIs<ProfileState.Saved>(awaitItem())
        }
        model.profile.test {
            awaitItem().let {
                assertNotNull(it)
                assertEquals(newEmail, it.email)
            }
        }
    }

    @Test
    fun `returns error when updates profile with incorrect email`() = runTest {
        val newEmail = "354234#adas"
        model.onProfileEmailUpdate(newEmail)
        model.state.test {
            assertIs<ProfileState.Error>(awaitItem())
        }
    }

}