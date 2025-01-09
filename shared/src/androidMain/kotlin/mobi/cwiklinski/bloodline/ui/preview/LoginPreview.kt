package mobi.cwiklinski.bloodline.ui.preview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.ui.screen.LoginView

@Preview
@Composable
fun LoginPreview() {
    val showPassword = remember { mutableStateOf(false) }
    LoginView(
        paddingValues = PaddingValues(0.dp),
        formEnabled = true,
        email = "",
        onEmailChange = { },
        emailError = false,
        password = "",
        onPasswordChange = { },
        passwordError = false,
        showPassword = false,
        passwordTransform = {
            showPassword.value = !showPassword.value
        },
        onSubmit = { },
        onRegister = { },
        onReset = { },
        onFacebook = { },
        onGoogle = { },
        onApple = { },
        isError = false,
        errorText = "",
        isLogging = false
    )
}