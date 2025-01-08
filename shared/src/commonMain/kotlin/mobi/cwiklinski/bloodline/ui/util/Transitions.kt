package mobi.cwiklinski.bloodline.ui.util

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.transitions.ScreenTransition

@OptIn(ExperimentalVoyagerApi::class)
class FadeTransition : ScreenTransition {

    override fun enter(lastEvent: StackEvent): EnterTransition {
        return fadeIn(tween(500, delayMillis = 200))
    }

    override fun exit(lastEvent: StackEvent): ExitTransition {
        return fadeOut(tween(500))
    }
}