package mobi.cwiklinski.bloodline.analytics.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.staticCompositionLocalOf

@Composable
fun TrackScreen(
    view: Event.View,
    analyticsInterface: Analytics = LocalAnalytics.current
) = LaunchedEffect(view.screenName) { analyticsInterface.trackScreen(view) }

@Composable
fun TrackScreen(
    screenName: String,
    analyticsInterface: Analytics = LocalAnalytics.current
) = LaunchedEffect(screenName) { analyticsInterface.trackScreen(screenName) }

@Composable
fun TrackAction(
    analyticsAction: Event.Action,
    analyticsInterface: Analytics = LocalAnalytics.current
) = LaunchedEffect(Unit) { analyticsInterface.trackAction(analyticsAction) }

@Composable
fun TrackAction(
    actionName: String,
    analyticsInterface: Analytics = LocalAnalytics.current
) = LaunchedEffect(Unit) { analyticsInterface.trackAction(actionName) }

/**
 * Global key used to obtain access to the Analytics Interface
 * through a CompositionLocal.
 */
val LocalAnalytics = staticCompositionLocalOf<Analytics> {
    NoOpAnalytics()
}