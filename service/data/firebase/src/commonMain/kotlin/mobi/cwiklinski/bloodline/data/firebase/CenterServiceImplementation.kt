package mobi.cwiklinski.bloodline.data.firebase

import dev.gitlive.firebase.database.FirebaseDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import mobi.cwiklinski.bloodline.data.api.CenterService
import mobi.cwiklinski.bloodline.data.firebase.model.FirebaseCenter
import mobi.cwiklinski.bloodline.data.firebase.util.flattenToList

@OptIn(ExperimentalCoroutinesApi::class)
class CenterServiceImplementation(val db: FirebaseDatabase) : CenterService {

    private val mainRef = db.reference("center")

    override suspend fun getCenters() = mainRef
        .valueEvents
        .map {
            it.value<Map<String, FirebaseCenter>>().values.toList()
        }
        .catch { e ->
            flowOf<List<FirebaseCenter>>(emptyList())
        }
        .flattenToList()
        .map { it.toCenter() }

    override suspend fun getCenter(id: String) = mainRef
        .child(id)
        .valueEvents
        .map { it.value<Map<String, FirebaseCenter>>().values.toList() }
        .flattenToList()
        .map { it.toCenter() }
        .first()
}