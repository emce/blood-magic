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
import androidx.compose.material.SnackbarHostState
import androidx.compose.material3.Text
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
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import mobi.cwiklinski.bloodline.getScreenWidth
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.icon_apple
import mobi.cwiklinski.bloodline.resources.icon_eye_closed
import mobi.cwiklinski.bloodline.resources.icon_eye_opened
import mobi.cwiklinski.bloodline.resources.icon_facebook
import mobi.cwiklinski.bloodline.resources.icon_google
import mobi.cwiklinski.bloodline.resources.icon_login
import mobi.cwiklinski.bloodline.resources.loginEmailError
import mobi.cwiklinski.bloodline.resources.loginEmailLabel
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
import mobi.cwiklinski.bloodline.ui.event.SideEffects
import mobi.cwiklinski.bloodline.ui.model.LoginError
import mobi.cwiklinski.bloodline.ui.model.LoginScreenModel
import mobi.cwiklinski.bloodline.ui.model.LoginState
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.contentText
import mobi.cwiklinski.bloodline.ui.theme.hugeTitle
import mobi.cwiklinski.bloodline.ui.theme.itemSubTitle
import mobi.cwiklinski.bloodline.ui.theme.itemTrailing
import mobi.cwiklinski.bloodline.ui.widget.FormProgress
import mobi.cwiklinski.bloodline.ui.widget.JustTextButton
import mobi.cwiklinski.bloodline.ui.widget.MobileLayout
import mobi.cwiklinski.bloodline.ui.widget.OutlinedInput
import mobi.cwiklinski.bloodline.ui.widget.SocialIconButton
import mobi.cwiklinski.bloodline.ui.widget.SubmitButton
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class LoginScreen(val state: LoginScreenState = LoginScreenState.Idle) : AppScreen() {

    @Composable
    override fun Content() {
        super.Content()
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<LoginScreenModel>()
        if (state is LoginScreenState.Info) {
            screenModel.postSideEffect(SideEffects.SnackBar(state.text))
        }
    }

    @Composable
    override fun defaultView() {
        val snackBarHostState = remember { SnackbarHostState() }
        handleSnackBars<LoginState, LoginScreenModel>(snackBarHostState)
        MobileLayout(
            snackBarState = snackBarHostState,
            desiredContent = { paddingValues ->
                LoginView(paddingValues)
            }
        )
    }

    @Composable
    override fun tabletView() {
        val width = getScreenWidth()
        val snackBarHostState = remember { SnackbarHostState() }
        handleSnackBars<LoginState, LoginScreenModel>(snackBarHostState)
        MobileLayout(
            snackBarState = snackBarHostState,
            desiredContent = { paddingValues ->
                val newPaddingValues = PaddingValues(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                    start = width / 4,
                    end = width / 4
                )
                LoginView(newPaddingValues)
            }
        )
    }

    @Composable
    fun LoginView(paddingValues: PaddingValues) {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<LoginScreenModel>()
        val state by screenModel.state.collectAsStateWithLifecycle()
        if (state == LoginState.LoggedIn) {
            navigator.replaceAll(SetupScreen())
        }
        val focusManager = LocalFocusManager.current
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        val showPassword = remember { mutableStateOf(false) }
        val soon = stringResource(Res.string.soon)
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
                modifier = Modifier.padding(20.dp)
            )
            Spacer(Modifier.height(40.dp))
            Text(
                stringResource(Res.string.loginTitle),
                style = hugeTitle()
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
                    enabled = state != LoginState.LoggingIn,
                    error = state is LoginState.Error && (state as LoginState.Error).errors.contains(
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
                    enabled = state != LoginState.LoggingIn,
                    error = state is LoginState.Error && (state as LoginState.Error).errors.contains(
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
                        if (state != LoginState.LoggingIn) {
                            Image(
                                painterResource(if (showPassword.value) Res.drawable.icon_eye_opened else Res.drawable.icon_eye_closed),
                                "password",
                                modifier = Modifier.clickable {
                                    showPassword.value = !showPassword.value
                                }
                            )
                        }
                    },
                )
                Spacer(Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth().align(Alignment.End)) {
                    JustTextButton(
                        text = stringResource(Res.string.loginPasswordReminderButton),
                        onClicked = {
                            navigator.push(ResetScreen())
                        },
                        enabled = state != LoginState.LoggingIn
                    )
                }
                Spacer(Modifier.height(30.dp))
                if (state is LoginState.Error) {
                    Text(
                        getErrorMessage(state as LoginState.Error),
                        style = contentText().copy(color = AppThemeColors.alertRed)
                    )
                    Spacer(Modifier.height(30.dp))
                }
                if (state == LoginState.LoggingIn) {
                    FormProgress()
                } else {
                    SubmitButton(
                        onClick = {
                            screenModel.onLoginSubmit(email, password)
                        },
                        text = stringResource(Res.string.loginSubmitButton),
                        enabled = state != LoginState.LoggingIn,
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
                        onClicked = {
                            screenModel.postSideEffect(SideEffects.SnackBar(soon))
                        },
                        enabled = state != LoginState.LoggingIn,
                    )
                    Spacer(Modifier.width(30.dp))
                    SocialIconButton(
                        icon = Res.drawable.icon_google,
                        iconDescription = "Google",
                        onClicked = {
                            screenModel.postSideEffect(SideEffects.SnackBar(soon))
                        },
                        enabled = state != LoginState.LoggingIn,
                    )
                    Spacer(Modifier.width(30.dp))
                    SocialIconButton(
                        icon = Res.drawable.icon_apple,
                        iconDescription = "Apple",
                        onClicked = {
                            screenModel.postSideEffect(SideEffects.SnackBar(soon))
                        },
                        enabled = state != LoginState.LoggingIn,
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
                        onClicked = {
                            navigator.replace(RegisterScreen())
                        },
                        enabled = state != LoginState.LoggingIn,
                        textDecoration = TextDecoration.None,
                    )
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
                    LoginError.LOGIN_ERROR -> stringResource(Res.string.loginError)
                    LoginError.PROFILE_ERROR -> stringResource(Res.string.loginError)
                }
            }
            .joinToString("\n")
}

sealed class LoginScreenState {
    data object Idle : LoginScreenState()
    data class Info(val text: String) : LoginScreenState()
}