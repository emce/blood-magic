package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.runtime.Composable
import mobi.cwiklinski.bloodline.ui.util.RenderLayout


@Composable
fun MultiWindowSizeLayout(
    default: @Composable () -> Unit,
    desktop: @Composable () -> Unit,
    tablet: (@Composable () -> Unit)? = null,
    phonePortrait: (@Composable () -> Unit)? = null,
    phoneLandscape: (@Composable () -> Unit)? = null
) {
    RenderLayout(
        default,
        desktop,
        tablet ?: default,
        phonePortrait ?: tablet,
        phoneLandscape ?: tablet
    )
}