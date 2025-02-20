package mobi.cwiklinski.bloodline.analytics.firebase

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.analytics.FirebaseAnalyticsEvents
import dev.gitlive.firebase.analytics.FirebaseAnalyticsParam
import dev.gitlive.firebase.analytics.analytics
import mobi.cwiklinski.bloodline.analytics.api.Analytics
import mobi.cwiklinski.bloodline.analytics.api.Event
import mobi.cwiklinski.bloodline.getPlatform

class AnalyticsFirebase : Analytics {

    private val firebaseAnalytics = Firebase.analytics

    override fun trackScreen(view: Event.View) {
        if (!getPlatform().isDebugBinary) {
            firebaseAnalytics.logEvent(
                FirebaseAnalyticsEvents.SCREEN_VIEW,
                mapOf(
                    FirebaseAnalyticsParam.SCREEN_NAME to view.screenName,
                )
            )
        }
    }

    override fun trackAction(action: Event.Action) {
        if (!getPlatform().isDebugBinary) {
            val actionName = action.actionName.replace(" ", "_").lowercase()
            firebaseAnalytics.logEvent(
                actionName, mapOf(

                )
            )
        }
    }
}