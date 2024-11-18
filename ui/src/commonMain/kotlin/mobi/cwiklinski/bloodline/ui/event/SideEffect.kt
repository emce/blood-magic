package mobi.cwiklinski.bloodline.ui.event

import mobi.cwiklinski.bloodline.ui.screen.AppScreen
import org.jetbrains.compose.resources.DrawableResource

interface SideEffect

interface SideEffectWithEvent : SideEffect {
    val event: Event
}

class SideEffects : SideEffect {
    open class SideEffectEvent(override val event: Event) : SideEffectWithEvent

    data class OpenDialog(val id: Int = 0) : SideEffect
    data class OpenBrowser(val url: String, val openSystemBrowser: Boolean = false) : SideEffect
    data class Snackbar(val text: String) : SideEffect
    data class ErrorSnackbar(val error: Throwable, val errorReport: String? = null) :
        SideEffect

    data class Dialog(
        val title: String? = null,
        val message: String,
        val icon: DrawableResource? = null
    ) : SideEffect

    data class ErrorDialog constructor(val error: Throwable, val errorReport: String? = null) :
        SideEffect

    data class Navigate(val data: Any? = null) : SideEffect
    data class NavigateTo(val destination: AppScreen) : SideEffect
    data class NavigateBack(
        val title: String? = null,
        val message: String? = null,
        val error: Throwable? = null,
        val errorReport: String? = null,
    ) : SideEffect
    class NavigateToRoot : SideEffect
}