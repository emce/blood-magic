package mobi.cwiklinski.bloodline.data.api

import kotlinx.coroutines.flow.Flow
import mobi.cwiklinski.bloodline.domain.model.Center

interface CenterService {

    fun getCenters(): Flow<List<Center>>

    fun getCenter(id: String): Flow<Center>
}