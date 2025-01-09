package mobi.cwiklinski.bloodline.ui.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import mobi.cwiklinski.bloodline.data.filed.DummyData
import mobi.cwiklinski.bloodline.ui.screen.ChangePasswordView

@Preview
@Composable
fun PasswordChangePreview() {
    ChangePasswordView(DummyData.generateProfile())
}