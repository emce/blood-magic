package mobi.cwiklinski.bloodline.ui.event

import mobi.cwiklinski.bloodline.ui.screen.AppScreen

interface Event

interface EventWithSideEffect : Event {
    val sideEffect: SideEffect
}

class Events : Event {
    open class EventSideEffect(override val sideEffect: SideEffect) : EventWithSideEffect
    open class OpenBrowser(val url: String) : EventWithSideEffect {
        override val sideEffect
            get() = SideEffects.OpenBrowser(url)
    }

    open class NavigateTo(val destination: AppScreen) : EventWithSideEffect {
        override val sideEffect
            get() = SideEffects.NavigateTo(destination)
    }

    object NavigateBack : EventSideEffect(SideEffects.NavigateBack())

    object NotificationPermissionGiven: Event

}