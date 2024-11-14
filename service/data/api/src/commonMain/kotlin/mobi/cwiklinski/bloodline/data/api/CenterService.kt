package mobi.cwiklinski.bloodline.data.api

import mobi.cwiklinski.bloodline.domain.model.Center

interface CenterService {

    suspend fun getCenters(): List<Center>

    suspend fun getCenter(id: String): Center
}