package mobi.cwiklinski.bloodline.ui.event

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import mobi.cwiklinski.bloodline.ui.manager.PlatformManager

interface SideEffect

interface SideEffectWithEvent : SideEffect {
    val event: Event
}

class SideEffects : SideEffect {
    open class SideEffectEvent(override val event: Event) : SideEffectWithEvent
    data class OpenBrowser(val url: String, val openSystemBrowser: Boolean = false) : SideEffect
    data class SnackBar(val text: String) : SideEffect
    data class ErrorSnackBar(val error: Throwable, val errorReport: String? = null) :
        SideEffect

    data class InformationDialog(
        val title: String,
        val message: String
    ) : SideEffect

    data class ErrorDialog constructor(val error: Throwable, val errorReport: String? = null) :
        SideEffect

    data class ShareText(val text: String) : SideEffect
}

suspend fun openBrowser(platformManager: PlatformManager, url: String, openSystemBrowser: Boolean = false) =
    platformManager.openBrowser(url = url, openSystemBrowser = openSystemBrowser)

suspend fun shareText(platformManager: PlatformManager, text: String) =
    platformManager.shareText(content = text)

@Composable
fun HandleSideEffect(
    sideEffects: Flow<SideEffect>,
    handler: suspend CoroutineScope.(sideEffect: SideEffect) -> Unit = {}
) {
    LaunchedEffect(Unit) {
        sideEffects.collectLatest {
            handler.invoke(this, it)
        }
    }
}