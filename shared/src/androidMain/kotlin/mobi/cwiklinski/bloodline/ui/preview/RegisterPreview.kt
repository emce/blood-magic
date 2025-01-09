package mobi.cwiklinski.bloodline.ui.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import mobi.cwiklinski.bloodline.ui.screen.LoginView
import mobi.cwiklinski.bloodline.ui.screen.RegisterView

@Preview(locale = "pl")
@Composable
fun RegisterPreview() {
    RegisterView()
}

@Preview
@Composable
fun LoginForRegisterPreview() {
    LoginView()
}