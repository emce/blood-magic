package mobi.cwiklinski.bloodline.ui.model

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import mobi.cwiklinski.bloodline.common.removeDiacritics
import mobi.cwiklinski.bloodline.data.api.CenterService
import mobi.cwiklinski.bloodline.domain.model.Center

class CenterScreenModel(centerService: CenterService) :
    AppModel<CenterState>(CenterState.Idle) {

    val query = MutableStateFlow("")
    val centers: StateFlow<List<Center>> = centerService.getCenters()
        .stateIn(screenModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val filteredCenters = combine(query, centers) { query, centers ->
            if (query.length > 2) {
                centers.filter {
                    it.name.lowercase().removeDiacritics()
                        .contains(query.lowercase().removeDiacritics()) or
                            it.city.lowercase().removeDiacritics()
                                .contains(query.lowercase().removeDiacritics()) or
                            it.street.lowercase().removeDiacritics().contains(
                                query.lowercase().removeDiacritics()
                            )
                }
            } else {
                centers
            }
        }
        .stateIn(screenModelScope, SharingStarted.WhileSubscribed(), emptyList())

    init {
        bootstrap()
    }
}

sealed class CenterState {
    data object Idle : CenterState()
}