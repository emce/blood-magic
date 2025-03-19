package mobi.cwiklinski.bloodline.ui.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import mobi.cwiklinski.bloodline.ui.screen.ProfileEditForm


@Preview(
    locale = "pl",
    showSystemUi = true,
)
@Composable
fun ProfileDialogPreview() {
    ProfileEditForm()
}