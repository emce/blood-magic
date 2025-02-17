package mobi.cwiklinski.bloodline.ui.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.tooling.preview.Preview
import mobi.cwiklinski.bloodline.ui.screen.SetupView
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.util.Avatar
import mobi.cwiklinski.bloodline.ui.widget.getAvatarName

@Preview
@Composable
fun SetupPreview() {
    Column(
        modifier = Modifier
            .drawBehind {
                drawRect(
                    brush = AppThemeColors.homeGradient,
                    size = Size(width = 4000f, height = 400f)
                )
            }
    ) {
        SetupView(
            avatar = Avatar.WIZARD,
            avatarName = { desiredAvatar ->
                getAvatarName(desiredAvatar)
            }
        )
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240,orientation=portrait")
@Composable
fun SetupBigScreenPreview() {
    Column(
        modifier = Modifier
            .drawBehind {
                drawRect(
                    brush = AppThemeColors.homeGradient,
                    size = Size(width = 4000f, height = 400f)
                )
            }
    ) {
        SetupView(
            avatar = Avatar.WIZARD,
            avatarName = { desiredAvatar ->
                getAvatarName(desiredAvatar)
            }
        )
    }
}