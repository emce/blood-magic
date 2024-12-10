package mobi.cwiklinski.bloodline.ui.util

import androidx.compose.runtime.Composable
import mobi.cwiklinski.bloodline.getOrientation
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
    val isLandscape = getOrientation().isHorizontal()
    if (isMobile()) {
        if (isTablet()) {
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