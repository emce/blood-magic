package mobi.cwiklinski.bloodline.data.filed

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import mobi.cwiklinski.bloodline.common.Either
import mobi.cwiklinski.bloodline.data.api.ProfileService
import mobi.cwiklinski.bloodline.data.api.ProfileServiceState
import mobi.cwiklinski.bloodline.data.api.ProfileUpdate
import mobi.cwiklinski.bloodline.data.api.ProfileUpdateState
import mobi.cwiklinski.bloodline.storage.api.StorageService
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileServiceImplementationTest {

    private val scheduler = TestCoroutineScheduler()
    private val testDispatcher = UnconfinedTestDispatcher(scheduler)
    private val scope = CoroutineScope(testDispatcher)
    private val storageService: StorageService = DataFiledTestTools.getStorageService()
    private lateinit var profileService: ProfileService

    @BeforeTest
    fun setUp() = runTest {
        Dispatchers.setMain(testDispatcher)
        storageService.storeProfile(DummyData.generateProfile())
        scheduler.advanceUntilIdle()
        profileService =
            ProfileServiceImplementation(storageService, CoroutineScope(testDispatcher))
    }

    @AfterTest
    fun finish() = runTest {
        storageService.clearAll()
    }

    @Test
    fun `returns user profile`() = runTest {
        assertNotNull(storageService.getProfile())
        profileService.getProfile().onEach {
            assertNotNull(it)
        }.launchIn(scope)

    }

    @Test
    fun `updates profile data`() = runTest {
        assertNotNull(storageService.getProfile())
        val newProfile = DummyData.generateProfile()
        profileService.updateProfileData(
            name = newProfile.name,
            email = newProfile.email,
            avatar = newProfile.avatar,
            sex = newProfile.sex,
            notification = newProfile.notification,
            starting = newProfile.starting,
            centerId = newProfile.centerId
        ).onEach { result ->
            assertIs<ProfileServiceState.Saved>(result)
            profileService.getProfile().onEach { profile ->
                assertNotNull(profile)
                assertEquals(newProfile.sex, profile.sex)
                assertEquals(newProfile.centerId, profile.centerId)
                assertEquals(newProfile.starting, profile.starting)
                assertEquals(newProfile.avatar, profile.avatar)
            }.launchIn(scope)
        }.launchIn(scope)
    }

    @Test
    fun `updates profile email`() = runTest {
        val newProfile = DummyData.generateProfile()
        profileService.updateProfileEmail(
            email = newProfile.email
        ).onEach { result ->
            assertIs<Either.Left<ProfileUpdate>>(result)
            assertEquals(ProfileUpdateState.EMAIL, result.value.updated.first())
            profileService.getProfile().onEach {
                assertNotNull(it)
                assertEquals(newProfile.email, it.email)
            }.launchIn(scope)
        }.launchIn(scope)
    }

    @Test
    fun `updates profile password`() = runTest {
        profileService.updateProfilePassword(DummyData.generateString()).onEach { result ->
            assertIs<Either.Left<ProfileUpdate>>(result)
            assertEquals(ProfileUpdateState.PASSWORD, result.value.updated.first())
        }.launchIn(scope)
    }
}