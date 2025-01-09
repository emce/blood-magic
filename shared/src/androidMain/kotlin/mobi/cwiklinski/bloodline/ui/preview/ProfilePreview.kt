package mobi.cwiklinski.bloodline.ui.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.tooling.preview.Preview
import mobi.cwiklinski.bloodline.data.filed.DummyData
import mobi.cwiklinski.bloodline.ui.model.ProfileState
import mobi.cwiklinski.bloodline.ui.screen.ProfileView
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.widget.LogoutDialog

@Preview(locale = "pl")
@Composable
fun ProfileLogoutDialogPreview() {
    Column {
        LogoutDialog(ProfileState.ToLoggedOut, {}) { }
    }
}

@Preview(locale = "pl")
@Composable
fun ProfilePreview() {
    val profile = DummyData.generateProfile()
    Column(
        modifier = Modifier
            .drawBehind {
                drawRect(
                    brush = AppThemeColors.homeGradient,
                    size = Size(width = 4000f, height = 400f)
                )
            }
    ) {
        ProfileView(
            name = profile.name,
            email = profile.email,
            sex = profile.sex,
            starting = profile.starting,
            notification = profile.notification,
            center = profile.centerId
        )
    }
}