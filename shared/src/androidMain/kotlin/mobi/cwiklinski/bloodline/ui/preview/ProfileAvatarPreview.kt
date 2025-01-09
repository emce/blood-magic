package mobi.cwiklinski.bloodline.ui.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import mobi.cwiklinski.bloodline.ui.screen.AvatarView
import mobi.cwiklinski.bloodline.ui.util.Avatar
import mobi.cwiklinski.bloodline.ui.widget.getAvatarName

@Preview
@Composable
fun ProfileAvatarPreview() {
    Column(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        AvatarView(
            avatarName = { avatarName ->
                getAvatarName(Avatar.byName(avatarName))
            },
            isAvatarSelected = { avatarName ->
                Avatar.byName(avatarName) == Avatar.FAIRY
            }
        )
    }
}