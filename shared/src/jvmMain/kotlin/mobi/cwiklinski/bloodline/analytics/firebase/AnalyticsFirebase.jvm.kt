package mobi.cwiklinski.bloodline.analytics.firebase

import mobi.cwiklinski.bloodline.analytics.api.Analytics
import mobi.cwiklinski.bloodline.analytics.api.Event

actual class AnalyticsFirebase : Analytics {
    override fun trackScreen(view: Event.View) = Unit

    override fun trackAction(action: Event.Action) = Unit

    override fun setUserId(userId: String) = Unit

}