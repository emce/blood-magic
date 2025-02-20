package mobi.cwiklinski.bloodline.analytics.api

interface Analytics {
    fun trackScreen(screenName: String) {
        trackScreen(Event.View(screenName))
    }

    fun trackScreen(view: Event.View)

    fun trackAction(actionName: String) {
        trackAction(Event.Action(actionName))
    }

    fun trackAction(action: Event.Action)
}