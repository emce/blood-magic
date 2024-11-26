package mobi.cwiklinski.bloodline.ui.model

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import mobi.cwiklinski.bloodline.ui.event.Event
import mobi.cwiklinski.bloodline.ui.event.Events
import mobi.cwiklinski.bloodline.ui.event.SideEffect
import org.koin.core.component.KoinComponent

abstract class AppModel<S>(initialState: S) : StateScreenModel<S>(initialState), KoinComponent {

    private var _bootstrapped: Boolean = false

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    private val _sideEffect: Channel<SideEffect> = Channel()
    val sideEffect = _sideEffect.receiveAsFlow()


    protected open fun bootstrap() {
        _bootstrapped = true
        _event.onEach {
            handleEvent(it)
        }.launchIn(screenModelScope)
    }

    fun postEvent(event: Event) {
        if (!_bootstrapped) {
            throw RuntimeException("ViewModel wasn't bootstrapped")
        }
        screenModelScope.launch { _event.emit(event) }
    }

    protected fun postSideEffect(sideEffect: SideEffect) {
        screenModelScope.launch {
            _sideEffect.send(sideEffect)
        }
    }

    open suspend fun handleEvent(event: Event) = when (event) {
        is Events.OpenBrowser -> {

        }
        is Events.NavigateTo -> {

        }
        is Events.NavigateBack -> {

        }
        is Events.NotificationPermissionGiven -> {

        }

        else -> {}
    }
}