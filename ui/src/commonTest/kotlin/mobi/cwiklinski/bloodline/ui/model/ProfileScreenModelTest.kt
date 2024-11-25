package mobi.cwiklinski.bloodline.ui.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import mobi.cwiklinski.bloodline.auth.api.AuthResult
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
    private val testDispatcher = UnconfinedTestDispatcher()
    private val storageService = UiTestTools.getStorageService()

    @BeforeTest
    fun setUp() = runTest {
        Dispatchers.setMain(testDispatcher)
        runBlocking {
            storageService.storeProfile(profile)
        }
    }

    @Test
    fun `starts with profile`() = runTest {
        val model = getDefaultModel()
        val currentProfile = model.profile.value
        assertNotNull(currentProfile)
        assertEquals(profile.email, currentProfile.email)
    }

    @Test
    fun `updates profile with data`() = runTest {
        val model = getDefaultModel()
        val newProfile =
            UiTestTools.generateProfile(id = profile.id ?: CommonTestTools.randomString(9))
        model.onProfileDataUpdate(
            newName = newProfile.name,
            newAvatar = newProfile.avatar,
            newSex = newProfile.sex,
            newNotification = newProfile.notification,
            newStarting = newProfile.starting,
            newCenterId = newProfile.centerId
        )
        val state = model.state.first()
        assertIs<ProfileState.Saved>(state)
        val currentProfile = model.profile.value
        assertNotNull(currentProfile)
        assertEquals(newProfile.name, currentProfile.name)
        assertEquals(newProfile.sex, currentProfile.sex)
        assertEquals(newProfile.centerId, currentProfile.centerId)
    }

    @Test
    fun `updates profile with email`() = runTest {
        val model = getDefaultModel()
        val newEmail = DummyData.ACCOUNTS.last().first
        model.onProfileEmailUpdate(newEmail)
        val state = model.state.value
        assertIs<ProfileState.Saved>(state)
        val currentProfile = model.profile.value
        assertEquals(newEmail, currentProfile.email)
    }

    @Test
    fun `returns error when updates profile with incorrect email`() = runTest {
        val model = getDefaultModel()
        val newEmail = "354234#adas"
        model.onProfileEmailUpdate(newEmail)
        val state = model.state.first()
        assertIs<ProfileState.Error>(state)
    }

    private fun getDefaultModel() = getModel(
        AuthResult.Success(),
        false,
    )

    private fun getModel(
        authResult: AuthResult = AuthResult.Success(),
        authSecond: Boolean = true,
    ) = ProfileScreenModel(
        UiTestTools.getProfileService(storageService, CoroutineScope(testDispatcher)),
        UiTestTools.getAuthService(authResult, authSecond),
        storageService
    )

}