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
import mobi.cwiklinski.bloodline.data.filed.CenterServiceImplementation
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class CenterScreenModelTest {

    private val scheduler = TestCoroutineScheduler()
    private val dispatcher = UnconfinedTestDispatcher(scheduler)
    private val scope = CoroutineScope(dispatcher)

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @Test
    fun `loads data on bootstrap`() = runTest {
        val model = CenterScreenModel(CenterServiceImplementation())
        model.centers.stateIn(scope, SharingStarted.Lazily, emptyList()).test {
            assertEquals(193, awaitItem().size)
        }
    }

    @Test
    fun `searches centers based on query`() = runTest {
        val model = CenterScreenModel(CenterServiceImplementation())
        model.centers.stateIn(scope, SharingStarted.Lazily, emptyList()).test {
            assertEquals(193, awaitItem().size)
        }
        model.query.value = "krak"
        model.filteredCenters.stateIn(scope, SharingStarted.Lazily, emptyList()).test {
            assertEquals(20, awaitItem().size)
        }
    }
}