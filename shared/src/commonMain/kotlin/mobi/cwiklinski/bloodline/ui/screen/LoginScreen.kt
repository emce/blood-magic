package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable
import mobi.cwiklinski.bloodline.data.Parcelize
import mobi.cwiklinski.bloodline.getScreenWidth
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.icon_apple
import mobi.cwiklinski.bloodline.resources.icon_eye_closed
import mobi.cwiklinski.bloodline.resources.icon_eye_opened
import mobi.cwiklinski.bloodline.resources.icon_facebook
import mobi.cwiklinski.bloodline.resources.icon_google
import mobi.cwiklinski.bloodline.resources.icon_login
import mobi.cwiklinski.bloodline.resources.icon_question
import mobi.cwiklinski.bloodline.resources.loginEmailError
import mobi.cwiklinski.bloodline.resources.loginEmailLabel
import mobi.cwiklinski.bloodline.resources.loginEmailTip
import mobi.cwiklinski.bloodline.resources.loginError
import mobi.cwiklinski.bloodline.resources.loginPasswordError
import mobi.cwiklinski.bloodline.resources.loginPasswordLabel
import mobi.cwiklinski.bloodline.resources.loginPasswordReminderButton
import mobi.cwiklinski.bloodline.resources.loginRegisterButton
import mobi.cwiklinski.bloodline.resources.loginRegisterText
import mobi.cwiklinski.bloodline.resources.loginSocialSectionTitle
import mobi.cwiklinski.bloodline.resources.loginSubmitButton
import mobi.cwiklinski.bloodline.resources.loginTitle
import mobi.cwiklinski.bloodline.resources.soon
import mobi.cwiklinski.bloodline.ui.model.LoginError
import mobi.cwiklinski.bloodline.ui.model.LoginScreenModel
import mobi.cwiklinski.bloodline.ui.model.LoginState
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.contentText
import mobi.cwiklinski.bloodline.ui.theme.getTypography
import mobi.cwiklinski.bloodline.ui.theme.itemSubTitle
import mobi.cwiklinski.bloodline.ui.theme.itemTrailing
import mobi.cwiklinski.bloodline.ui.util.avatarShadow
import mobi.cwiklinski.bloodline.ui.util.koinNavigatorScreenModel
import mobi.cwiklinski.bloodline.ui.widget.FormProgress
import mobi.cwiklinski.bloodline.ui.widget.JustTextButton
import mobi.cwiklinski.bloodline.ui.widget.MobileLayout
import mobi.cwiklinski.bloodline.ui.widget.OutlinedInput
import mobi.cwiklinski.bloodline.ui.widget.SocialIconButton
import mobi.cwiklinski.bloodline.ui.widget.SubmitButton
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Parcelize
class LoginScreen(override val key: ScreenKey = Clock.System.now().toString()) : AppScreen() {

    @Composable
    override fun defaultView() {
        handleSideEffects<LoginState, LoginScreenModel>()
        MobileLayout(
            desiredContent = { paddingValues ->
                InternalLoginView(paddingValues)
            }
        )
    }

    @Composable
    override fun tabletView() {
        val width = getScreenWidth()
        handleSideEffects<LoginState, LoginScreenModel>()
        MobileLayout(
            desiredContent = { paddingValues ->
                val newPaddingValues = PaddingValues(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                    start = width / 4,
                    end = width / 4
                )
                InternalLoginView(newPaddingValues)
            }
        )
    }

    @Composable
    fun InternalLoginView(paddingValues: PaddingValues) {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<LoginScreenModel>()
        val state by screenModel.state.collectAsStateWithLifecycle(LoginState.Idle)
        if (state == LoginState.LoggedIn) {
            screenModel.resetState()
            navigator.replaceAll(SetupScreen())
        }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        val showPassword = remember { mutableStateOf(false) }
        handleSideEffects<LoginState, LoginScreenModel>()
        LoginView(
            paddingValues = paddingValues,
            formEnabled = state != LoginState.LoggingIn,
            email = email,
            onEmailChange = { newEmail ->
                email = newEmail
            },
            emailError = state is LoginState.Error && (state as LoginState.Error).errors.contains(
                LoginError.EMAIL_ERROR
            ),
            password = password,
            onPasswordChange = { newPassword ->
                password = newPassword
            },
            passwordError = state is LoginState.Error && (state as LoginState.Error).errors.contains(
                LoginError.PASSWORD_ERROR
            ),
            passwordTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
            passwordIcon = if (showPassword.value) Res.drawable.icon_eye_opened else Res.drawable.icon_eye_closed,
            passwordTransform = {
                showPassword.value = !showPassword.value
            },
            onSubmit = {
                screenModel.clearState()
                screenModel.onLoginSubmit(email, password)
            },
            onRegister = {
                navigator.replace(RegisterScreen())
            },
            onReset = {
                navigator.replace(ResetScreen())
            },
            onFacebook = {
                screenModel.clearState()
                screenModel.loginWithFacebook()
            },
            onGoogle = {
                screenModel.clearState()
                screenModel.loginWithGoogle()
            },
            onApple = {
                screenModel.clearState()
                screenModel.loginWithApple()
            },
            isError = state is LoginState.Error,
            errorText = if (state is LoginState.Error) getErrorMessage(state as LoginState.Error) else "",
            isLogging = state == LoginState.LoggingIn
        )
    }

    @Composable
    private fun getErrorMessage(state: LoginState.Error) = state.errors
            .map {
                when (it) {
                    LoginError.EMAIL_ERROR -> stringResource(Res.string.loginEmailError)
                    LoginError.PASSWORD_ERROR -> stringResource(Res.string.loginEmailError)
                    LoginError.LOGIN_ERROR -> stringResource(Res.string.loginError)
                    LoginError.PROFILE_ERROR -> stringResource(Res.string.loginError)
                    LoginError.NOT_IMPLEMENTED -> stringResource(Res.string.soon)
                }
            }
            .joinToString("\n")
}

@Serializable
sealed class LoginScreenState {
    data object Idle : LoginScreenState()
    data class Info(val text: String) : LoginScreenState()
}

@Composable
fun LoginView(
    paddingValues: PaddingValues,
    formEnabled: Boolean = true,
    email: String,
    onEmailChange: (String) -> Unit,
    emailError: Boolean = false,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordError: Boolean = false,
    passwordTransformation: VisualTransformation = VisualTransformation.None,
    passwordIcon: DrawableResource,
    passwordTransform: () -> Unit,
    onSubmit: () -> Unit,
    onReset: () -> Unit,
    onRegister: () -> Unit,
    onFacebook: () -> Unit,
    onGoogle: () -> Unit,
    onApple: () -> Unit,
    isError: Boolean = false,
    errorText: String = "",
    isLogging: Boolean = false
) {
    val focusManager = LocalFocusManager.current
    Column(
        Modifier.wrapContentHeight().fillMaxWidth().background(
            AppThemeColors.authGradient
        ).padding(paddingValues).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painterResource(Res.drawable.icon_login),
            stringResource(Res.string.loginTitle),
            modifier = Modifier.padding(vertical = 30.dp).avatarShadow(
                color = AppThemeColors.white,
                sizeAdjustment = 0.5f
            )
        )
        Text(
            stringResource(Res.string.loginTitle),
            style = getTypography().displayMedium
        )
        Spacer(Modifier.height(5.dp))
        Column(
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedInput(
                    text = email,
                    modifier = Modifier.weight(1f),
                    onValueChanged = onEmailChange,
                    label = stringResource(Res.string.loginEmailLabel),
                    enabled = formEnabled,
                    error = emailError,
                    errorMessage = stringResource(Res.string.loginEmailError),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    )
                )
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = { PlainTooltip { Text(stringResource(Res.string.loginEmailTip)) } },
                    state = rememberTooltipState(),
                    focusable = false,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Icon(
                        painterResource(Res.drawable.icon_question),
                        contentDescription = stringResource(Res.string.loginEmailTip)
                    )
                }
            }
            Row(
                modifier = Modifier.padding(end = 40.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedInput(
                    text = password,
                    onValueChanged = onPasswordChange,
                    label = stringResource(Res.string.loginPasswordLabel),
                    enabled = formEnabled,
                    error = passwordError,
                    errorMessage = stringResource(Res.string.loginPasswordError),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            onSubmit.invoke()
                        }
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        autoCorrectEnabled = true,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = passwordTransformation,
                    trailingIcon = {
                        if (formEnabled) {
                            Image(
                                painterResource(passwordIcon),
                                "password",
                                modifier = Modifier.clickable {
                                    passwordTransform.invoke()
                                }
                            )
                        }
                    },
                )
            }
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                JustTextButton(
                    text = stringResource(Res.string.loginPasswordReminderButton),
                    onClicked = onReset,
                    enabled = formEnabled
                )
            }
            Spacer(Modifier.height(20.dp))
            if (isError) {
                Text(
                    errorText,
                    style = contentText().copy(color = AppThemeColors.alertRed)
                )
                Spacer(Modifier.height(30.dp))
            }
            if (isLogging) {
                FormProgress()
            } else {
                SubmitButton(
                    onClick = onSubmit,
                    text = stringResource(Res.string.loginSubmitButton),
                    enabled = formEnabled,
                )
            }
            Spacer(Modifier.height(30.dp))
            Row(
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier.weight(1.0f).height(1.dp)
                        .background(AppThemeColors.grey3)
                )
                Text(
                    stringResource(Res.string.loginSocialSectionTitle),
                    modifier = Modifier.padding(horizontal = 10.dp),
                    style = itemSubTitle().copy(color = AppThemeColors.grey3)
                )
                Box(
                    modifier = Modifier.weight(1.0f).height(1.dp)
                        .background(AppThemeColors.grey3)
                )
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
                    onClicked = onFacebook,
                    enabled = formEnabled,
                )
                Spacer(Modifier.width(30.dp))
                SocialIconButton(
                    icon = Res.drawable.icon_google,
                    iconDescription = "Google",
                    onClicked = onGoogle,
                    enabled = formEnabled,
                )
                Spacer(Modifier.width(30.dp))
                SocialIconButton(
                    icon = Res.drawable.icon_apple,
                    iconDescription = "Apple",
                    onClicked = onApple,
                    enabled = formEnabled,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().wrapContentHeight()
                    .padding(vertical = 20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(Res.string.loginRegisterText),
                    style = itemTrailing().copy(color = AppThemeColors.black70)
                )
                Spacer(Modifier.width(10.dp))
                JustTextButton(
                    stringResource(Res.string.loginRegisterButton),
                    onClicked = onRegister,
                    enabled = formEnabled,
                    textDecoration = TextDecoration.None,
                )
            }
        }
    }
}