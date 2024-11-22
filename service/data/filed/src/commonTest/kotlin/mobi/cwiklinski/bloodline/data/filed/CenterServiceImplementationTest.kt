package mobi.cwiklinski.bloodline.data.filed

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import mobi.cwiklinski.bloodline.data.api.CenterService
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class CenterServiceImplementationTest {

    private val centerService: CenterService = CenterServiceImplementation()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun `returns center list`() = runTest {
        val centers = centerService.getCenters().first()
        assertEquals(DummyData.CENTERS.size, centers.size)
    }

    @Test
    fun `returns center`() = runTest {
        val desiredCenter = DummyData.CENTERS.toList().random()
        val center = centerService.getCenter(desiredCenter.id).first()
        assertEquals(desiredCenter.id, center.id)
        assertEquals(desiredCenter.name, center.name)
        assertEquals(desiredCenter.street, center.street)
        assertEquals(desiredCenter.info, center.info)
    }
}