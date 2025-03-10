package mobi.cwiklinski.bloodline.data.firebase

import dev.gitlive.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.map
import mobi.cwiklinski.bloodline.data.api.CenterService
import mobi.cwiklinski.bloodline.data.firebase.model.FirebaseCenter
import mobi.cwiklinski.bloodline.domain.sortByRegion

class CenterServiceImplementation(db: FirebaseDatabase) : CenterService {

    private val mainRef = db.reference("center")

    override fun getCenters() = mainRef
        .valueEvents
        .map { events ->
            events.value<Map<String, FirebaseCenter>>().values.map { it.toCenter() }.toList().sortByRegion()
        }

    override fun getCenter(id: String) = mainRef
        .child(id)
        .valueEvents
        .map { events ->
            events.value<Map<String, FirebaseCenter>>().values.map { it.toCenter() }.first()
        }
}