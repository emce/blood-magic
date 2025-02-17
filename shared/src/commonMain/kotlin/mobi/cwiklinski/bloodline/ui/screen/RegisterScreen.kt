package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import mobi.cwiklinski.bloodline.common.event.SideEffects
import mobi.cwiklinski.bloodline.common.manager.CallbackManager
import mobi.cwiklinski.bloodline.data.Parcelize
import mobi.cwiklinski.bloodline.getScreenWidth
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.icon_apple
import mobi.cwiklinski.bloodline.resources.icon_facebook
import mobi.cwiklinski.bloodline.resources.icon_google
import mobi.cwiklinski.bloodline.resources.icon_register
import mobi.cwiklinski.bloodline.resources.loginEmailError
import mobi.cwiklinski.bloodline.resources.loginEmailLabel
import mobi.cwiklinski.bloodline.resources.loginPasswordError
import mobi.cwiklinski.bloodline.resources.loginPasswordLabel
import mobi.cwiklinski.bloodline.resources.registerError
import mobi.cwiklinski.bloodline.resources.registerLoginButton
import mobi.cwiklinski.bloodline.resources.registerLoginText
import mobi.cwiklinski.bloodline.resources.registerRepeatError
import mobi.cwiklinski.bloodline.resources.registerRepeatLabel
import mobi.cwiklinski.bloodline.resources.registerSocialSectionTitle
import mobi.cwiklinski.bloodline.resources.registerSubmitButton
import mobi.cwiklinski.bloodline.resources.registerTerms
import mobi.cwiklinski.bloodline.resources.registerTitle
import mobi.cwiklinski.bloodline.resources.soon
import mobi.cwiklinski.bloodline.ui.model.RegisterError
import mobi.cwiklinski.bloodline.ui.model.RegisterScreenModel
import mobi.cwiklinski.bloodline.ui.model.RegisterState
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.getTypography
import mobi.cwiklinski.bloodline.ui.util.avatarShadow
import mobi.cwiklinski.bloodline.ui.util.koinNavigatorScreenModel
import mobi.cwiklinski.bloodline.ui.widget.FormProgress
import mobi.cwiklinski.bloodline.ui.widget.JustTextButton
import mobi.cwiklinski.bloodline.ui.widget.MobileLayout
import mobi.cwiklinski.bloodline.ui.widget.OutlinedInput
import mobi.cwiklinski.bloodline.ui.widget.RichText
import mobi.cwiklinski.bloodline.ui.widget.SocialIconButton
import mobi.cwiklinski.bloodline.ui.widget.SubmitButton
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Parcelize
class RegisterScreen : AppScreen() {

    @Composable
    override fun defaultView() {
        handleSideEffects<RegisterState, RegisterScreenModel>()
        MobileLayout(
            desiredContent = { paddingValues ->
                InternalRegisterView(paddingValues)
            }
        )
    }

    @Composable
    override fun tabletView() {
        val width = getScreenWidth()
        handleSideEffects<RegisterState, RegisterScreenModel>()
        MobileLayout(
            desiredContent = { paddingValues ->
                val newPaddingValues = PaddingValues(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                    start = width / 4,
                    end = width / 4
                )
                InternalRegisterView(newPaddingValues)
            }
        )
    }

    @Composable
    fun InternalRegisterView(paddingValues: PaddingValues) {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<RegisterScreenModel>()
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var repeat by remember { mutableStateOf("") }
        val showPassword = remember { mutableStateOf(false) }
        val showRepeat = remember { mutableStateOf(false) }
        val state by screenModel.state.collectAsStateWithLifecycle(RegisterState.Idle)
        if (state == RegisterState.Registered) {
            navigator.replaceAll(SplashScreen())
        }
        RegisterView(
            paddingValues = paddingValues,
            formEnabled = state != RegisterState.Registering,
            email = email,
            onEmailChange = { newEmail ->
                email = newEmail
            },
            emailError = state is RegisterState.Error && (state as RegisterState.Error).errors.contains(
                RegisterError.EMAIL_ERROR
            ),
            password = password,
            onPasswordChange = { newPassword ->
                password = newPassword
            },
            passwordError = state is RegisterState.Error && (state as RegisterState.Error).errors.contains(
                RegisterError.PASSWORD_ERROR
            ),
            showPassword = showPassword.value,
            passwordTransform = { show ->
                showPassword.value = show
            },
            repeat = repeat,
            onRepeatChange = { newRepeat ->
                repeat = newRepeat
            },
            repeatError = state is RegisterState.Error && (state as RegisterState.Error).errors.contains(
                RegisterError.REPEAT_ERROR
            ),
            showRepeat = showRepeat.value,
            repeatTransform = { show ->
                showRepeat.value = show
            },
            onSubmit = {
                screenModel.clearState()
                screenModel.onRegisterSubmit(email, password, repeat)
            },
            onLogin = {
                navigator.replace(LoginScreen())
            },
            onFacebook = {
                screenModel.clearState()
                screenModel.registerWithFacebook()
            },
            onGoogle = {
                screenModel.clearState()
                screenModel.registerWithGoogle()
            },
            onApple = {
                screenModel.clearState()
                screenModel.registerWithApple()
            },
            isError = state is RegisterState.Error,
            errorText = if (state is RegisterState.Error) getError((state as RegisterState.Error).errors) else "",
            isRegistering = state == RegisterState.Registering
        )
    }

    @Composable
    fun getError(errors: List<RegisterError>) =
        errors.map {
            when (it) {
                RegisterError.EMAIL_ERROR -> stringResource(Res.string.loginEmailError)
                RegisterError.PASSWORD_ERROR -> stringResource(Res.string.loginEmailError)
                RegisterError.REPEAT_ERROR -> stringResource(Res.string.loginEmailError)
                RegisterError.REGISTER_ERROR -> stringResource(Res.string.registerError)
                RegisterError.PROFILE_ERROR -> stringResource(Res.string.registerError)
                RegisterError.NOT_IMPLEMENTED -> stringResource(Res.string.soon)
            }
        }
            .joinToString("\n")
}

@Composable
fun RegisterView(
    paddingValues: PaddingValues = PaddingValues(0.dp),
    formEnabled: Boolean = true,
    email: String = "",
    onEmailChange: (String) -> Unit = {},
    emailError: Boolean = false,
    password: String = "",
    onPasswordChange: (String) -> Unit = {},
    passwordError: Boolean = false,
    showPassword: Boolean = false,
    passwordTransform: (Boolean) -> Unit = {},
    repeat: String = "",
    onRepeatChange: (String) -> Unit = {},
    repeatError: Boolean = false,
    showRepeat: Boolean = false,
    repeatTransform: (Boolean) -> Unit = {},
    onSubmit: () -> Unit = {},
    onLogin: () -> Unit = {},
    onFacebook: () -> Unit = {},
    onGoogle: () -> Unit = {},
    onApple: () -> Unit = {},
    isError: Boolean = false,
    errorText: String = "",
    isRegistering: Boolean = false
) {
    if (isError) {
        koinInject<CallbackManager>().postSideEffect(SideEffects.ErrorSnackBar(errorText))
    }
    Column(
        Modifier.wrapContentHeight().fillMaxWidth().background(
            AppThemeColors.authGradient
        ).padding(paddingValues).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Spacer(Modifier.height(30.dp))
        Image(
            painterResource(Res.drawable.icon_register),
            stringResource(Res.string.registerTitle),
            modifier = Modifier
                .avatarShadow(
                    color = AppThemeColors.white,
                    sizeAdjustment = 0.38f
                )
        )
        Spacer(Modifier.height(30.dp))
        Text(
            stringResource(Res.string.registerTitle),
            style = getTypography().displayMedium.copy(
                color = AppThemeColors.violet4
            )
        )
        Spacer(Modifier.height(5.dp))
        Column(
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            OutlinedInput(
                text = email,
                onValueChanged = onEmailChange,
                label = stringResource(Res.string.loginEmailLabel),
                enabled = formEnabled,
                error = emailError,
                errorMessage = stringResource(Res.string.loginEmailError),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            )
            OutlinedInput(
                text = password,
                onValueChanged = onPasswordChange,
                label = stringResource(Res.string.loginPasswordLabel),
                enabled = formEnabled,
                error = passwordError,
                errorMessage = stringResource(Res.string.loginPasswordError),
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    if (formEnabled) {
                        Image(
                            if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            "password",
                            modifier = Modifier.clickable {
                                passwordTransform(!showPassword)
                            },
                            colorFilter = ColorFilter.tint(AppThemeColors.grey3)
                        )
                    }
                },
            )
            OutlinedInput(
                text = repeat,
                onValueChanged = onRepeatChange,
                label = stringResource(Res.string.registerRepeatLabel),
                enabled = formEnabled,
                error = repeatError,
                errorMessage = stringResource(Res.string.registerRepeatError),
                visualTransformation = if (showRepeat) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    if (formEnabled) {
                        Image(
                            if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            "password",
                            modifier = Modifier.clickable {
                                repeatTransform(!showRepeat)
                            },
                            colorFilter = ColorFilter.tint(AppThemeColors.grey3)
                        )
                    }
                },
            )
            Spacer(Modifier.height(10.dp))
            RichText(
                stringResource(Res.string.registerTerms),
                centered = true
            )
            Spacer(Modifier.height(20.dp))
            if (isRegistering) {
                FormProgress()
            } else {
                SubmitButton(
                    onClick = onSubmit,
                    text = stringResource(Res.string.registerSubmitButton),
                    enabled = formEnabled,
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
                    onClicked = onFacebook,
                    enabled = false
                )
                Spacer(Modifier.width(30.dp))
                SocialIconButton(
                    icon = Res.drawable.icon_google,
                    iconDescription = "Google",
                    onClicked = onGoogle,
                    enabled = false
                )
                Spacer(Modifier.width(30.dp))
                SocialIconButton(
                    icon = Res.drawable.icon_apple,
                    iconDescription = "Apple",
                    onClicked = onApple,
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
                    onClicked = onLogin,
                    enabled = formEnabled,
                    textDecoration = TextDecoration.None
                )
            }
        }
    }
}