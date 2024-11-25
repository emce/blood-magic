package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import mobi.cwiklinski.bloodline.resources.*
import mobi.cwiklinski.bloodline.ui.event.Events
import mobi.cwiklinski.bloodline.ui.widget.LinkedText
import mobi.cwiklinski.bloodline.ui.widget.TextWithLinks
import mobi.cwiklinski.bloodline.ui.widget.JustTextButton
import mobi.cwiklinski.bloodline.ui.widget.OutlinedInput
import mobi.cwiklinski.bloodline.ui.widget.SocialIconButton
import mobi.cwiklinski.bloodline.ui.widget.SubmitButton
import mobi.cwiklinski.bloodline.ui.model.RegisterError
import mobi.cwiklinski.bloodline.ui.model.RegisterScreenModel
import mobi.cwiklinski.bloodline.ui.model.RegisterState
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.getTypography
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

class RegisterScreen : AppScreen() {

    @Preview
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<RegisterScreenModel>()
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var repeat by remember { mutableStateOf("") }
        if (screenModel.state.value == RegisterState.Registered) {
            navigator.replaceAll(SplashScreen())
        }
        Column(
            Modifier.wrapContentHeight().fillMaxWidth().background(
                AppThemeColors.authGradient
            ).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painterResource(Res.drawable.icon_register),
                stringResource(Res.string.registerTitle),
                modifier = Modifier.padding(20.dp)
            )
            Spacer(Modifier.height(40.dp))
            Text(
                stringResource(Res.string.registerTitle),
                style = getTypography().displayMedium.copy(fontSize = 34.sp, color = AppThemeColors.violet4)
            )
            Spacer(Modifier.height(50.dp))
            Column(
                modifier = Modifier.padding(20.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                OutlinedInput(
                    text = email,
                    onValueChanged = { email = it },
                    label = stringResource(Res.string.loginEmailLabel),
                    enabled = screenModel.state.value != RegisterState.Registering,
                    error = screenModel.state.value is RegisterState.Error
                            && (screenModel.state.value as RegisterState.Error).errors.contains(RegisterError.EMAIL_ERROR),
                    errorMessage = getError(listOf(RegisterError.EMAIL_ERROR)),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                )
                Spacer(Modifier.height(20.dp))
                OutlinedInput(
                    text = password,
                    onValueChanged = { password = it },
                    label = stringResource(Res.string.loginPasswordLabel),
                    enabled = screenModel.state.value != RegisterState.Registering,
                    error = screenModel.state.value is RegisterState.Error
                            && (screenModel.state.value as RegisterState.Error).errors.contains(RegisterError.PASSWORD_ERROR),
                    errorMessage = getError(listOf(RegisterError.PASSWORD_ERROR))
                )
                Spacer(Modifier.height(20.dp))
                OutlinedInput(
                    text = repeat,
                    onValueChanged = { repeat = it },
                    label = stringResource(Res.string.registerRepeatLabel),
                    enabled = screenModel.state.value != RegisterState.Registering,
                    error = screenModel.state.value is RegisterState.Error
                            && (screenModel.state.value as RegisterState.Error).errors.contains(RegisterError.REPEAT_ERROR),
                    errorMessage = getError(listOf(RegisterError.REPEAT_ERROR))
                )
                Spacer(Modifier.height(30.dp))
                if (screenModel.state.value is RegisterState.Error) {
                    Text(
                        getError((screenModel.state.value as RegisterState.Error).errors),
                        style = getTypography().bodyLarge.copy(
                            color = AppThemeColors.red1
                        )
                    )
                    Spacer(Modifier.height(30.dp))
                }
                Spacer(Modifier.height(20.dp))
                TextWithLinks(
                    text = stringResource(Res.string.registerTerms),
                    linkedTexts = listOf(
                        LinkedText(
                            text = stringResource(Res.string.registerTermsPrivacyPolicy),
                            link = stringResource(Res.string.registerTermsPrivacyPolicyLink)
                        ),
                        LinkedText(
                            text = stringResource(Res.string.registerTermsRules),
                            link = stringResource(Res.string.registerTermsRulesLink)
                        )
                    )
                )
                Spacer(Modifier.height(20.dp))
                SubmitButton(
                    onClick = { screenModel.onRegisterSubmit(email, password, repeat) },
                    text = stringResource(Res.string.registerSubmitButton),
                    enabled = screenModel.state.value != RegisterState.Registering,
                )
                Spacer(Modifier.height(30.dp))
                BoxWithConstraints(
                    modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(1.dp)
                            .background(AppThemeColors.grey3)
                    )
                    Box(
                        modifier = Modifier.wrapContentSize()
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                            .background(AppThemeColors.white)
                    ) {
                        Text(
                            stringResource(Res.string.registerSocialSectionTitle),
                            modifier = Modifier.padding(horizontal = 10.dp),
                            style = getTypography().bodyMedium.copy(color = AppThemeColors.grey3)
                        )
                    }
                }
                Spacer(Modifier.height(30.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().wrapContentHeight()
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    SocialIconButton(
                        icon = Res.drawable.icon_facebook,
                        iconDescription = "Facebook",
                        onClicked = {},
                        enabled = false
                    )
                    Spacer(Modifier.width(30.dp))
                    SocialIconButton(
                        icon = Res.drawable.icon_google,
                        iconDescription = "Google",
                        onClicked = {},
                        enabled = false
                    )
                    Spacer(Modifier.width(30.dp))
                    SocialIconButton(
                        icon = Res.drawable.icon_apple,
                        iconDescription = "Apple",
                        onClicked = {},
                        enabled = false
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth().wrapContentHeight()
                        .padding(vertical = 20.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(Res.string.registerLoginText))
                    Spacer(Modifier.width(10.dp))
                    JustTextButton(
                        stringResource(Res.string.registerLoginButton),
                        onClicked = {
                            navigator.replace(LoginScreen())
                        },
                        enabled = screenModel.state.value != RegisterState.Registering,
                        textDecoration = TextDecoration.None
                    )
                }
            }
        }
    }

    @Composable
    fun getError(errors: List<RegisterError>) =
        errors.map {
            when(it) {
                RegisterError.EMAIL_ERROR -> stringResource(Res.string.loginEmailError)
                RegisterError.PASSWORD_ERROR -> stringResource(Res.string.loginEmailError)
                RegisterError.REPEAT_ERROR -> stringResource(Res.string.loginEmailError)
                RegisterError.REGISTER_ERROR -> stringResource(Res.string.registerError)
                RegisterError.PROFILE_ERROR -> stringResource(Res.string.registerError)
            }
        }
        .joinToString("\n")
}