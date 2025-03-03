package mobi.cwiklinski.bloodline.ui.util

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import mobi.cwiklinski.bloodline.getOrientation
import mobi.cwiklinski.bloodline.getWindowSizeClass
import mobi.cwiklinski.bloodline.isMobile
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
    val window =  getWindowSizeClass()
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