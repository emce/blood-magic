package mobi.cwiklinski.bloodline.common.event

interface SideEffect

interface SideEffectWithEvent : SideEffect {
    val event: Event
}

class SideEffects : SideEffect {
    class DeleteAccountEffect : SideEffect
    open class SideEffectEvent(override val event: Event) : SideEffectWithEvent
    data class OpenBrowser(val url: String, val openSystemBrowser: Boolean = false) : SideEffect
    data class SnackBar(val text: String) : SideEffect
    data class ErrorSnackBar(val text: String) : SideEffect

    data class InformationDialog(
        val title: String,
        val message: String
    ) : SideEffect

    data class ShareText(val text: String) : SideEffect

    data class Redirect(val route: ScreenRoute) : SideEffect
}

enum class ScreenRoute {
    UnreadNotification,
    Donations
}
