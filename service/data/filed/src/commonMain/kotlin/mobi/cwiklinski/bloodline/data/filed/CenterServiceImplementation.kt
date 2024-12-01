package mobi.cwiklinski.bloodline.data.filed

import kotlinx.coroutines.flow.flowOf
import mobi.cwiklinski.bloodline.data.api.CenterService
import mobi.cwiklinski.bloodline.domain.sortByRegion

class CenterServiceImplementation : CenterService {

    override fun getCenters() = flowOf(DummyData.CENTERS.sortByRegion())

    override fun getCenter(id: String) = flowOf(DummyData.CENTERS.single { it.id == id })
}