package mobi.cwiklinski.bloodline.ui.model

class CenterScreenModel : AppModel<CenterState>(CenterState.Idle) {
}

sealed class CenterState {
    object Idle : CenterState()
}