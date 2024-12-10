package mobi.cwiklinski.bloodline.ui.util

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import mobi.cwiklinski.bloodline.getOrientation
import mobi.cwiklinski.bloodline.getPlatform
import mobi.cwiklinski.bloodline.getScreenWidth
import mobi.cwiklinski.bloodline.isTablet
import mobi.cwiklinski.bloodline.ui.widget.isHorizontal

@Composable
fun RenderLayout(
    default: @Composable () -> Unit,
    desktop: @Composable () -> Unit,
    tablet: @Composable () -> Unit,
    phonePortrait: (@Composable () -> Unit)? = null,
    phoneLandscape: (@Composable () -> Unit)? = null
) {
    val isLandscape = getOrientation().isHorizontal()
    val isTablet = isTablet()
    if (getPlatform().isMobile()) {
        if (isTablet) {
            tablet()
        } else {
            if (isLandscape) {
                phoneLandscape?.invoke() ?: default()
            } else {
                phonePortrait?.invoke() ?: default()
            }
        }
    } else {
        desktop()
    }
}


enum class ScreenType {
    PhonePortrait,
    PhoneLandscape,
    Tablet,
    Desktop
}