package mobi.cwiklinski.bloodline.ui.model

import app.cash.turbine.test
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import mobi.cwiklinski.bloodline.auth.api.AuthResult
import mobi.cwiklinski.bloodline.data.filed.CenterServiceImplementation
import mobi.cwiklinski.bloodline.data.filed.DummyData
import mobi.cwiklinski.bloodline.test.CommonTestTools
import mobi.cwiklinski.bloodline.ui.util.UiTestTools
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileScreenModelTest {

    private val profile = UiTestTools.generateProfile()
    private val scheduler = TestCoroutineScheduler()
    private val dispatcher = UnconfinedTestDispatcher(scheduler)
    private val scope = CoroutineScope(dispatcher)
    private val storageService = UiTestTools.getStorageService()
    private lateinit var model: ProfileScreenModel

    @BeforeTest
    fun setUp() = runTest {
        Dispatchers.setMain(dispatcher)
        runBlocking {
            storageService.storeProfile(profile)
        }
        model = ProfileScreenModel(
            UiTestTools.getProfileService(storageService, scope),
            CenterServiceImplementation(),
            UiTestTools.getAuthService(AuthResult.Success(), false),
            storageService
        )
    }

    @Test
    fun `starts with profile`() = runTest {
        model.profile.test {
            val currentProfile = awaitItem()
            assertNotNull(currentProfile)
            assertEquals(profile.email, currentProfile.email)
        }
    }

    @Test
    fun `updates profile with data`() = runTest {
        val newProfile =
            UiTestTools.generateProfile(id = profile.id ?: CommonTestTools.randomString(9))
        runBlocking {
            model.onProfileDataUpdate(
                newName = newProfile.name,
                newEmail = newProfile.email,
                newAvatar = newProfile.avatar,
                newSex = newProfile.sex,
                newNotification = newProfile.notification,
                newStarting = newProfile.starting,
                newCenterId = newProfile.centerId
            )
        }
        scheduler.advanceUntilIdle()
        model.state.test {
            val state = awaitItem()
            assertIs<ProfileState.Saved>(state)
        }
        model.profile.test {
            val currentProfile = awaitItem()
            assertNotNull(currentProfile)
            assertEquals(newProfile.name, currentProfile.name)
            assertEquals(newProfile.sex, currentProfile.sex)
            assertEquals(newProfile.centerId, currentProfile.centerId)
        }
    }

    @Test
    fun `updates profile with email`() = runTest {
        val newEmail = DummyData.ACCOUNTS.last().first
        model.onProfileEmailUpdate(newEmail)
        model.state.test {
            val state = awaitItem()
            assertIs<ProfileState.Saved>(state)
        }
        model.profile.test {
            val profile = awaitItem()
            assertNotNull(profile)
            assertEquals(newEmail, profile.email)
        }
    }

    @Test
    fun `returns error when updates profile with incorrect email`() = runTest {
        val newEmail = "354234#adas"
        model.onProfileEmailUpdate(newEmail)
        model.state.test {
            val state = awaitItem()
            assertIs<ProfileState.Error>(state)
        }
    }

}