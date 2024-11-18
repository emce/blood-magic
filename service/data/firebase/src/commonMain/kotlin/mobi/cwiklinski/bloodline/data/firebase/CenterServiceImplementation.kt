package mobi.cwiklinski.bloodline.data.firebase

import dev.gitlive.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.map
import mobi.cwiklinski.bloodline.data.api.CenterService
import mobi.cwiklinski.bloodline.data.firebase.model.FirebaseCenter

class CenterServiceImplementation(val db: FirebaseDatabase) : CenterService {

    private val mainRef = db.reference("center")

    override fun getCenters() = mainRef
        .valueEvents
        .map { events ->
            events.value<Map<String, FirebaseCenter>>().values.map { it.toCenter() }.toList()
        }

    override fun getCenter(id: String) = mainRef
        .child(id)
        .valueEvents
        .map { events ->
            events.value<Map<String, FirebaseCenter>>().values.map { it.toCenter() }.first()
        }
}