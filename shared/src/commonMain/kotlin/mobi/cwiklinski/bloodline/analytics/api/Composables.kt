package mobi.cwiklinski.bloodline.analytics.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import org.koin.compose.koinInject

@Composable
fun TrackScreen(
    view: Event.View,
    analyticsInterface: Analytics = koinInject<Analytics>()
) = LaunchedEffect(view.screenName) { analyticsInterface.trackScreen(view) }

@Composable
fun TrackScreen(
    screenName: String,
    analyticsInterface: Analytics = koinInject<Analytics>()
) = LaunchedEffect(screenName) { analyticsInterface.trackScreen(screenName) }

@Composable
fun TrackAction(
    analyticsAction: Event.Action,
    analyticsInterface: Analytics = koinInject<Analytics>()
) = LaunchedEffect(Unit) { analyticsInterface.trackAction(analyticsAction) }

@Composable
fun TrackAction(
    actionName: String,
    analyticsInterface: Analytics = koinInject<Analytics>()
) = LaunchedEffect(Unit) { analyticsInterface.trackAction(actionName) }
