package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import mobi.cwiklinski.bloodline.Constants
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.anim_confirm_0
import mobi.cwiklinski.bloodline.resources.anim_confirm_1
import mobi.cwiklinski.bloodline.resources.anim_confirm_2
import mobi.cwiklinski.bloodline.resources.anim_confirm_3
import mobi.cwiklinski.bloodline.resources.anim_confirm_4
import mobi.cwiklinski.bloodline.resources.anim_stars_0
import mobi.cwiklinski.bloodline.resources.anim_stars_1
import mobi.cwiklinski.bloodline.resources.anim_stars_2
import mobi.cwiklinski.bloodline.resources.anim_stars_3
import mobi.cwiklinski.bloodline.resources.anim_stars_4
import mobi.cwiklinski.bloodline.resources.anim_stars_5
import mobi.cwiklinski.bloodline.resources.anim_stars_6
import mobi.cwiklinski.bloodline.resources.anim_stars_7
import mobi.cwiklinski.bloodline.resources.appName
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun StarsAnimation(
    modifier: Modifier = Modifier,
) {
    val stars = listOf(
        Res.drawable.anim_stars_0,
        Res.drawable.anim_stars_1,
        Res.drawable.anim_stars_2,
        Res.drawable.anim_stars_3,
        Res.drawable.anim_stars_4,
        Res.drawable.anim_stars_5,
        Res.drawable.anim_stars_6,
        Res.drawable.anim_stars_7
    )
    val infiniteTransition = rememberInfiniteTransition("stars")
    val image = infiniteTransition.animateValue(
        initialValue = 0,
        targetValue = 7,
        typeConverter = Int.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(Constants.ANIMATION_SPEED),
            repeatMode = RepeatMode.Restart
        )
    )
    Image(
        painterResource(stars[image.value]),
        contentDescription = stringResource(Res.string.appName),
        modifier = modifier
    )
}
@Composable
fun ConfirmAnimation(
    modifier: Modifier = Modifier,
) {
    val frames = listOf(
        Res.drawable.anim_confirm_0,
        Res.drawable.anim_confirm_1,
        Res.drawable.anim_confirm_2,
        Res.drawable.anim_confirm_3,
        Res.drawable.anim_confirm_4,
    )
    val infiniteTransition = rememberInfiniteTransition("confirm")
    val image = infiniteTransition.animateValue(
        initialValue = 0,
        targetValue = 4,
        typeConverter = Int.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(Constants.ANIMATION_SPEED + 200),
            repeatMode = RepeatMode.Restart
        )
    )
    Image(
        painterResource(frames[image.value]),
        contentDescription = stringResource(Res.string.appName),
        modifier = modifier
    )
}