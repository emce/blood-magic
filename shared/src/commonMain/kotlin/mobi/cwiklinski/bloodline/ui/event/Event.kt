package mobi.cwiklinski.bloodline.ui.event

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import mobi.cwiklinski.bloodline.ui.model.AppModel

interface Event

interface EventWithSideEffect : Event {
    val sideEffect: SideEffect
}

class Events : Event {
    open class EventSideEffect(override val sideEffect: SideEffect) : EventWithSideEffect
    open class OpenBrowser(private val url: String) : EventWithSideEffect {
        override val sideEffect
            get() = SideEffects.OpenBrowser(url)
    }

}

@Composable
fun <S> HandleEvent(
    viewModel: AppModel<S>,
    handler: suspend CoroutineScope.(event: Event) -> Unit = {}
) {
    LaunchedEffect(Unit) {
        viewModel.event.collectLatest {
            handler.invoke(this, it)
        }
    }
}