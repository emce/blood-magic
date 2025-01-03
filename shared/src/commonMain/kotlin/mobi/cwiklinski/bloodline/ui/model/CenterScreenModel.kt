package mobi.cwiklinski.bloodline.ui.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import mobi.cwiklinski.bloodline.data.api.CenterService
import mobi.cwiklinski.bloodline.common.manager.CallbackManager
import mobi.cwiklinski.bloodline.ui.util.filter

class CenterScreenModel(
    callbackManager: CallbackManager, centerService: CenterService
) :
    AppModel<CenterState>(CenterState.Idle, callbackManager) {

    val query = MutableStateFlow("")
    val centers = centerService.getCenters()

    val filteredCenters = query.combine(centers) { query, centers ->
        if (query.length > 2) {
            centers.filter(query)
        } else {
            centers
        }
    }

    init {
        bootstrap()
    }
}

sealed class CenterState {
    data object Idle : CenterState()
}