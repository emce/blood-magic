package mobi.cwiklinski.bloodline.ui.manager

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import mobi.cwiklinski.bloodline.ui.event.Event
import mobi.cwiklinski.bloodline.ui.event.Events
import mobi.cwiklinski.bloodline.ui.event.SideEffect
import mobi.cwiklinski.bloodline.ui.event.SideEffects
import kotlin.time.Duration.Companion.seconds

interface CallbackManager {
    val sideEffect: Flow<SideEffect>
    val event: Flow<Event>
    fun bootstrap()
    fun postEvent(event: Event)
    fun postSideEffect(sideEffect: SideEffect)
    suspend fun handleEvent(event: Event)
}

class AppCallbackManager(private val coroutineScope: CoroutineScope) : CallbackManager {
    private var _bootstrapped: Boolean = false
    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    override val event = _event.asSharedFlow()
    private val _sideEffect: Channel<SideEffect> = Channel()
    override val sideEffect = _sideEffect.receiveAsFlow()

    override fun bootstrap() {
        _bootstrapped = true
        _event.onEach {
            handleEvent(it)
        }.launchIn(coroutineScope)
    }

    override fun postEvent(event: Event) {
        if (!_bootstrapped) {
            throw RuntimeException("ViewModel wasn't bootstrapped")
        }
        coroutineScope.launch { _event.emit(event) }
    }

    override fun postSideEffect(sideEffect: SideEffect) {
        coroutineScope.launch {
            delay(1.seconds.inWholeMilliseconds)
            _sideEffect.send(sideEffect)
        }
    }

    override suspend fun handleEvent(event: Event) = when (event) {
        is Events.OpenBrowser -> {
            _sideEffect.send(event.sideEffect)
        }
        is Events.EventSideEffect -> {
            when (event.sideEffect) {
                is SideEffects.ShareText -> {
                    _event.emit(event)
                }
                else -> {}
            }
        }
        else -> {}
    }
}