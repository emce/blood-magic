package mobi.cwiklinski.bloodline.ui.event

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