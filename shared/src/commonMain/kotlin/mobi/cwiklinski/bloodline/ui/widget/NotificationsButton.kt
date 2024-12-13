package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.ic_bell
import mobi.cwiklinski.bloodline.resources.ic_bell_on
import mobi.cwiklinski.bloodline.resources.notificationsTitle
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun NotificationsButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    newNotifications: Boolean = false
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .shadow(
                elevation = 2.dp,
                shape = CircleShape,
            )) {
        Image(
            painterResource(if (newNotifications) Res.drawable.ic_bell_on else Res.drawable.ic_bell),
            stringResource(Res.string.notificationsTitle),
            colorFilter = ColorFilter.tint(AppThemeColors.white),
            modifier = Modifier
                .background(
                    AppThemeColors.notificationButtonGradient,
                    CircleShape
                )
                .clickable {
                    onClick.invoke()
                }
                .padding(10.dp)
        )
    }
}