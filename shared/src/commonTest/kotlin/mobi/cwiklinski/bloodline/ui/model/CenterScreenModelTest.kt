package mobi.cwiklinski.bloodline.ui.model

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import mobi.cwiklinski.bloodline.data.filed.CenterServiceImplementation
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class CenterScreenModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun `loads data on bootstrap`() = runTest {
        val model = CenterScreenModel(CenterServiceImplementation())
        model.centers.test {
            assertEquals(193, awaitItem().size)
        }
    }

    @Test
    fun `searches centers based on query`() = runTest {
        val model = CenterScreenModel(CenterServiceImplementation())
        model.centers.test {
            assertEquals(193, awaitItem().size)
        }
        model.query.value = "krak"
        model.filteredCenters.test {
            assertEquals(20, awaitItem().size)
        }
    }
}