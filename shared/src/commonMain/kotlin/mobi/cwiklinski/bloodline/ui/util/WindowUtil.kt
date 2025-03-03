package mobi.cwiklinski.bloodline.ui.util

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.toSize
import mobi.cwiklinski.bloodline.getOrientation
import mobi.cwiklinski.bloodline.isMobile
import mobi.cwiklinski.bloodline.isTablet
import mobi.cwiklinski.bloodline.ui.widget.isHorizontal

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun RenderLayout(
    default: @Composable () -> Unit,
    desktop: @Composable () -> Unit,
    tablet: @Composable () -> Unit,
    phonePortrait: (@Composable () -> Unit)? = null,
    phoneLandscape: (@Composable () -> Unit)? = null
) {
    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current
    val size = with(density) { windowInfo.containerSize.toSize().toDpSize() }
    val window =  WindowSizeClass.calculateFromSize(size)
    val isLandscape = getOrientation().isHorizontal()
    if (isMobile()) {
        if (isTablet()) {
            if (window.widthSizeClass == WindowWidthSizeClass.Expanded) {
                desktop()
            } else {
                tablet()
            }
        } else {
            if (isLandscape) {
                if (window.widthSizeClass == WindowWidthSizeClass.Compact) {
                    phoneLandscape?.invoke() ?: default()
                } else {
                    tablet()
                }
            } else {
                if (window.widthSizeClass == WindowWidthSizeClass.Expanded) {
                    tablet()
                } else {
                    phonePortrait?.invoke() ?: default()
                }
            }
        }
    } else {
        if (window.widthSizeClass == WindowWidthSizeClass.Expanded) {
            desktop()
        } else {
            tablet()
        }
    }
}