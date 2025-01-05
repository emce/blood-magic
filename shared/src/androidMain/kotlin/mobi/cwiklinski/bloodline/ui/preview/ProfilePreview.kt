package mobi.cwiklinski.bloodline.ui.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import mobi.cwiklinski.bloodline.ui.model.ProfileState
import mobi.cwiklinski.bloodline.ui.widget.LogoutDialog

@Preview(locale = "pl")
@Composable
fun ProfileLogoutDialogPreview() {
    Column {
        LogoutDialog(ProfileState.ToLoggedOut, {}) { }
    }
}