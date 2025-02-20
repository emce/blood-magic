package mobi.cwiklinski.bloodline.analytics.api

class NoOpAnalytics : Analytics {
    override fun trackScreen(view: Event.View) = Unit

    override fun trackAction(action: Event.Action) = Unit
}