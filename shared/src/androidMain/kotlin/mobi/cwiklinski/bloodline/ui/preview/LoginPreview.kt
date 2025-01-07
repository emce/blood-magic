package mobi.cwiklinski.bloodline.ui.preview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.icon_eye_closed
import mobi.cwiklinski.bloodline.resources.icon_eye_opened
import mobi.cwiklinski.bloodline.ui.screen.LoginView
import org.jetbrains.compose.ui.tooling.preview.Preview

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
        passwordTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
        passwordIcon = if (showPassword.value) Res.drawable.icon_eye_opened else Res.drawable.icon_eye_closed,
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