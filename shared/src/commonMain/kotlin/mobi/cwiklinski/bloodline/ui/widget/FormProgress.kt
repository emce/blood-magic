package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.icon_loading_large
import mobi.cwiklinski.bloodline.resources.icon_loading_small
import mobi.cwiklinski.bloodline.resources.loading
import mobi.cwiklinski.bloodline.resources.stillLoading
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun FormProgress(
    modifier: Modifier = Modifier,
    bigCircleColor: Color = AppThemeColors.red2,
    smallCircleColor: Color = AppThemeColors.accentRed1,
) {
    BoxWithConstraints(
        modifier = modifier
    ) {
        val infiniteTransition = rememberInfiniteTransition()
        val largeAngle by infiniteTransition.animateFloat(
            initialValue = 0F,
            targetValue = 360F,
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = LinearEasing)
            )
        )
        val smallAngle by infiniteTransition.animateFloat(
            initialValue = 360F,
            targetValue = 0F,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing)
            )
        )
        Image(
            painterResource(Res.drawable.icon_loading_large),
            stringResource(Res.string.loading),
            modifier = Modifier.size(48.dp).rotate(largeAngle),
            colorFilter = ColorFilter.tint(bigCircleColor)
        )
        Image(
            painterResource(Res.drawable.icon_loading_small),
            stringResource(Res.string.stillLoading),
            modifier = Modifier.size(48.dp).rotate(smallAngle),
            colorFilter = ColorFilter.tint(smallCircleColor)
        )
    }
}