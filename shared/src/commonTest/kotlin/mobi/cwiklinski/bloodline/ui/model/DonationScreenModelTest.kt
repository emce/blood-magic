package mobi.cwiklinski.bloodline.ui.model

import app.cash.turbine.test
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import mobi.cwiklinski.bloodline.common.manager.AppCallbackManager
import mobi.cwiklinski.bloodline.data.filed.CenterServiceImplementation
import mobi.cwiklinski.bloodline.data.filed.DonationServiceImplementation
import mobi.cwiklinski.bloodline.data.filed.DummyData
import mobi.cwiklinski.bloodline.data.filed.ProfileServiceImplementation
import mobi.cwiklinski.bloodline.ui.util.UiTestTools
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class DonationScreenModelTest {

    private val scheduler = TestCoroutineScheduler()
    private val dispatcher = UnconfinedTestDispatcher(scheduler)
    private val scope = CoroutineScope(dispatcher)
    private val callbackManager = AppCallbackManager(scope)

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @Test
    fun `loads data on bootstrap`() = runTest {
        val model = getModel()
        model.centers.stateIn(scope, SharingStarted.WhileSubscribed(), emptyList()).test {
            assertEquals(DummyData.CENTERS.size, awaitItem().size)
        }
        model.donations.stateIn(scope, SharingStarted.WhileSubscribed(), emptyList()).test {
            assertEquals(DummyData.DONATIONS.size, awaitItem().size)
        }
    }

    @Test
    fun `searches centers based on query`() = runTest {
        val model = getModel()
        model.centers.stateIn(scope, SharingStarted.WhileSubscribed(), emptyList()).test {
            assertEquals(DummyData.CENTERS.size, awaitItem().size)
        }
        model.centers.stateIn(scope, SharingStarted.WhileSubscribed(), emptyList()).test {
            assertEquals(DummyData.CENTERS.size, awaitItem().size)
        }
        model.query.value = "krak"
        model.filteredCenters.stateIn(scope, SharingStarted.WhileSubscribed(), emptyList()).test {
            assertEquals(20, awaitItem().size)
        }
    }

    @Test
    fun `adds new donation`() = runTest {
        val model = getModel()
        val state = model.state.value
        assertIs<DonationState.Idle>(state)
        model.donations.stateIn(scope, SharingStarted.WhileSubscribed(), emptyList()).test {
            assertEquals(DummyData.DONATIONS.size, awaitItem().size)
        }
        UiTestTools.generateDonation()
        val newDonation = UiTestTools.generateDonation()
        model.addDonation(
            newDonation.amount,
            newDonation.date,
            newDonation.center,
            newDonation.type.type,
            newDonation.hemoglobin,
            newDonation.systolic,
            newDonation.diastolic,
            newDonation.disqualification
        )
        model.state.stateIn(scope, SharingStarted.WhileSubscribed(), DonationState.Idle).test {
            assertIs<DonationState.Saved>(awaitItem())
        }
        model.donations.stateIn(scope, SharingStarted.WhileSubscribed(), emptyList()).test {
            val newList = awaitItem()
            assertEquals(DummyData.DONATIONS.size + 1, newList.size)
            val savedDonation = newList.firstOrNull {
                it.date == newDonation.date &&
                it.amount == newDonation.amount &&
                it.type == newDonation.type &&
                it.center.id == newDonation.center.id
            }
            assertNotNull(savedDonation)
        }
    }

    private fun getModel() = DonationScreenModel(
        callbackManager,
        DonationServiceImplementation(),
        CenterServiceImplementation(),
        ProfileServiceImplementation(UiTestTools.getStorageService(), scope)
    )
}