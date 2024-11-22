package mobi.cwiklinski.bloodline.data.filed

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import mobi.cwiklinski.bloodline.common.Either
import mobi.cwiklinski.bloodline.data.api.ProfileService
import mobi.cwiklinski.bloodline.data.api.ProfileUpdate
import mobi.cwiklinski.bloodline.data.api.ProfileUpdateState
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileServiceImplementationTest {

    private val profileService: ProfileService = ProfileServiceImplementation()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun `returns user profile`() = runTest {
        val profile = profileService.getProfile().first()
        assertNotNull(profile)
    }

    @Test
    fun `updates profile data`() = runTest {
        val newProfile = DummyData.generateProfile()
        val result = profileService.updateProfileData(
            name = newProfile.name,
            avatar = newProfile.avatar,
            sex = newProfile.sex,
            notification = newProfile.notification,
            starting = newProfile.starting,
            centerId = newProfile.centerId
        ).first()
        assertIs<Either.Left<ProfileUpdate>>(result)
        assertEquals(ProfileUpdateState.DATA, result.value.updated.first())
        val profile = profileService.getProfile().first()
        assertEquals(newProfile.sex, profile.sex)
        assertEquals(newProfile.centerId, profile.centerId)
        assertEquals(newProfile.starting, profile.starting)
        assertEquals(newProfile.avatar, profile.avatar)
    }

    @Test
    fun `updates profile email`() = runTest {
        val newProfile = DummyData.generateProfile()
        val result = profileService.updateProfileEmail(
            email = newProfile.email
        ).first()
        assertIs<Either.Left<ProfileUpdate>>(result)
        assertEquals(ProfileUpdateState.EMAIL, result.value.updated.first())
        val profile = profileService.getProfile().first()
        assertEquals(newProfile.email, profile.email)
    }

    @Test
    fun `updates profile password`() = runTest {
        val result = profileService.updateProfilePassword(DummyData.generateString()).first()
        assertIs<Either.Left<ProfileUpdate>>(result)
        assertEquals(ProfileUpdateState.PASSWORD, result.value.updated.first())
    }
}