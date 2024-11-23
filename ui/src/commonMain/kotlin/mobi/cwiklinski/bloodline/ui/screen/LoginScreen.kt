package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import mobi.cwiklinski.bloodline.resources.*
import mobi.cwiklinski.bloodline.ui.model.LoginError
import mobi.cwiklinski.bloodline.ui.model.LoginScreenModel
import mobi.cwiklinski.bloodline.ui.model.LoginState
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.getTypography
import mobi.cwiklinski.bloodline.ui.widget.FormProgress
import mobi.cwiklinski.bloodline.ui.widget.JustTextButton
import mobi.cwiklinski.bloodline.ui.widget.OutlinedInput
import mobi.cwiklinski.bloodline.ui.widget.SocialIconButton
import mobi.cwiklinski.bloodline.ui.widget.SubmitButton
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

class LoginScreen : AppScreen() {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<LoginScreenModel>()
        if (screenModel.state.value == LoginState.LoggedIn) {
            navigator.replaceAll(HomeScreen())
        }
        MainWindow(screenModel)
    }

    @Preview
    @Composable
    fun MainWindow(screenModel: LoginScreenModel) {
        val navigator = LocalNavigator.currentOrThrow
        val focusManager = LocalFocusManager.current
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        val showPassword = remember { mutableStateOf(false) }
        Scaffold {
            Column(
                Modifier.wrapContentHeight().fillMaxWidth().background(
                    AppThemeColors.authGradient
                ).verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                ) {
                Image(
                    painterResource(Res.drawable.icon_login),
                    stringResource(Res.string.loginTitle),
                    modifier = Modifier.padding(20.dp)
                )
                    Spacer(Modifier.height(40.dp))
                    Text(
                        stringResource(Res.string.loginTitle),
                        style = getTypography().displayLarge.copy(
                            fontSize = 34.sp,
                            color = AppThemeColors.violet4
                        )
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
                            enabled = screenModel.state.value != LoginState.LoggingIn,
                            error = screenModel.state.value is LoginState.Error && (screenModel.state.value as LoginState.Error).errors.contains(
                                LoginError.EMAIL_ERROR
                            ),
                            errorMessage = stringResource(Res.string.loginEmailError),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.moveFocus(FocusDirection.Down)
                                }
                            )
                        )
                        Spacer(Modifier.height(20.dp))
                        OutlinedInput(
                            text = password,
                            onValueChanged = { password = it },
                            label = stringResource(Res.string.loginPasswordLabel),
                            enabled = screenModel.state.value != LoginState.LoggingIn,
                            error = screenModel.state.value is LoginState.Error && (screenModel.state.value as LoginState.Error).errors.contains(
                                LoginError.PASSWORD_ERROR
                            ),
                            errorMessage = stringResource(Res.string.loginPasswordError),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                    screenModel.onLoginSubmit(email, password)
                                }
                            ),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                autoCorrectEnabled = true,
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
                            visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                Image(
                                    painterResource(if (showPassword.value) Res.drawable.icon_eye_opened else Res.drawable.icon_eye_closed),
                                    "password",
                                    modifier = Modifier.clickable {
                                        showPassword.value = !showPassword.value
                                    }
                                )
                                           },
                            )
                        Spacer(Modifier.height(20.dp))
                        JustTextButton(
                            text = stringResource(Res.string.loginPasswordReminderButton),
                            onClicked = {
                                navigator.push(ResetScreen())
                                        },
                            enabled = screenModel.state.value != LoginState.LoggingIn
                        )
                        Spacer(Modifier.height(30.dp))
                        if (screenModel.state.value is LoginState.Error) {
                            Text(
                                getErrorMessage(screenModel.state.value as LoginState.Error),
                                style = getTypography().displaySmall.copy(
                                    color = AppThemeColors.red1
                                )
                            )
                            Spacer(Modifier.height(30.dp))
                        }
                        if (screenModel.state.value == LoginState.LoggingIn) {
                            FormProgress()
                        } else {
                            SubmitButton(
                                onClick = {
                                    screenModel.onLoginSubmit(email, password)
                                          },
                                text = stringResource(Res.string.loginSubmitButton),
                                enabled = screenModel.state.value != LoginState.LoggingIn,
                                )
                        }
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
                                    stringResource(Res.string.loginSocialSectionTitle),
                                    modifier = Modifier.padding(horizontal = 10.dp),
                                    style = getTypography().displayLarge.copy(color = AppThemeColors.grey3)
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
                            Text(stringResource(Res.string.loginRegisterText))
                            Spacer(Modifier.width(10.dp))
                            JustTextButton(
                                stringResource(Res.string.loginRegisterButton),
                                onClicked = {
                                    navigator.replace(RegisterScreen())
                                            },
                                enabled = screenModel.state.value != LoginState.LoggingIn,
                                textDecoration = TextDecoration.None
                            )
                        }
                    }
            }
        }
    }

    @Composable
    private fun getErrorMessage(state: LoginState.Error) = state.errors
            .map {
                when (it) {
                    LoginError.EMAIL_ERROR -> stringResource(Res.string.loginEmailError)
                    LoginError.PASSWORD_ERROR -> stringResource(Res.string.loginEmailError)
                    LoginError.LOGIN_ERROR -> stringResource(Res.string.loginEmailError)
                }
            }
            .joinToString("\n")
}