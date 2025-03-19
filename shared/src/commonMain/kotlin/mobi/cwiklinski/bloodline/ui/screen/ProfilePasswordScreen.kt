package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import mobi.cwiklinski.bloodline.Constants
import mobi.cwiklinski.bloodline.analytics.api.TrackScreen
import mobi.cwiklinski.bloodline.common.event.SideEffects
import mobi.cwiklinski.bloodline.common.manager.CallbackManager
import mobi.cwiklinski.bloodline.data.IgnoredOnParcel
import mobi.cwiklinski.bloodline.data.Parcelize
import mobi.cwiklinski.bloodline.domain.model.Profile
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.close
import mobi.cwiklinski.bloodline.resources.profileAvatarTitle
import mobi.cwiklinski.bloodline.resources.profileDataCurrentPasswordLabel
import mobi.cwiklinski.bloodline.resources.profileDataPasswordLabel
import mobi.cwiklinski.bloodline.resources.profileDataPasswordRepeatLabel
import mobi.cwiklinski.bloodline.resources.profileDataSubmitButton
import mobi.cwiklinski.bloodline.resources.profilePasswordChangeTitle
import mobi.cwiklinski.bloodline.ui.model.ProfileError
import mobi.cwiklinski.bloodline.ui.model.ProfileScreenModel
import mobi.cwiklinski.bloodline.ui.model.ProfileState
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.util.Avatar
import mobi.cwiklinski.bloodline.ui.util.avatarShadow
import mobi.cwiklinski.bloodline.ui.util.koinNavigatorScreenModel
import mobi.cwiklinski.bloodline.ui.widget.CloseButton
import mobi.cwiklinski.bloodline.ui.widget.FormProgress
import mobi.cwiklinski.bloodline.ui.widget.OutlinedInput
import mobi.cwiklinski.bloodline.ui.widget.ProfileModal
import mobi.cwiklinski.bloodline.ui.widget.SubmitButton
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Parcelize
class ProfilePasswordScreen : AppProfileScreen() {

    @IgnoredOnParcel
    override val supportDialogs = false

    @Composable
    override fun defaultView() {
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val screenModel = navigator.koinNavigatorScreenModel<ProfileScreenModel>()
        val state by screenModel.state.collectAsStateWithLifecycle(ProfileState.Idle)
        val profile by screenModel.profile.collectAsStateWithLifecycle(Profile(""))
        if (state == ProfileState.Saved) {
            bottomSheetNavigator.hide()
        }
        var currentPassword by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var repeat by remember { mutableStateOf("") }
        val showCurrentPassword = remember { mutableStateOf(false) }
        val showNewPassword = remember { mutableStateOf(false) }
        val showRepeatPassword = remember { mutableStateOf(false) }
        ChangePasswordView(
            profile = profile,
            onClose = {
                bottomSheetNavigator.hide()
            },
            formEnabled = state != ProfileState.Saving,
            currentPassword = currentPassword,
            showCurrentPassword = showCurrentPassword.value,
            showCurrentPasswordChanged = { show ->
                showCurrentPassword.value = show
            },
            onCurrentPasswordChanged = { newPassword ->
                currentPassword = newPassword
            },
            currentPasswordError = state is ProfileState.Error &&
                    (state as ProfileState.Error).errors.contains(
                        ProfileError.PASSWORD
                    ),
            password = password,
            showPassword = showNewPassword.value,
            showPasswordChanged = { show ->
                showNewPassword.value = show
            },
            onPasswordChanged = { newPassword ->
                password = newPassword
            },
            passwordError = state is ProfileState.Error &&
                    (state as ProfileState.Error).errors.contains(
                        ProfileError.REPEAT
                    ),
            repeat = repeat,
            showRepeat = showRepeatPassword.value,
            showRepeatChanged = { show ->
                showRepeatPassword.value = show
            },
            onRepeatChanged = { newRepeat ->
                repeat = newRepeat
            },
            repeatError = state is ProfileState.Error,
            onChangePassword = {
                screenModel.onProfilePasswordUpdate(
                    currentPassword,
                    password,
                    repeat
                )
            },
            isSaved = state == ProfileState.Saved
        )
    }
}

@Composable
fun ChangePasswordView(
    profile: Profile,
    onClose: () -> Unit = {},
    formEnabled: Boolean = true,
    currentPassword: String = "",
    showCurrentPassword: Boolean = false,
    showCurrentPasswordChanged: (Boolean) -> Unit = {},
    onCurrentPasswordChanged: (String) -> Unit = {},
    currentPasswordError: Boolean = false,
    password: String = "",
    showPassword: Boolean = false,
    showPasswordChanged: (Boolean) -> Unit = {},
    onPasswordChanged: (String) -> Unit = {},
    passwordError: Boolean = false,
    repeat: String = "",
    showRepeat: Boolean = false,
    showRepeatChanged: (Boolean) -> Unit = {},
    onRepeatChanged: (String) -> Unit = {},
    repeatError: Boolean = false,
    onChangePassword: () -> Unit = {},
    isSaved: Boolean = false,
    errorMessage: @Composable (List<ProfileError>) -> String = { "" },
    errors: List<ProfileError> = emptyList()
) {
    TrackScreen(Constants.ANALYTICS_SCREEN_PROFILE_PASSWORD)
    val focusManager = LocalFocusManager.current
    if (errors.isNotEmpty()) {
        koinInject<CallbackManager>().postSideEffect(SideEffects.ErrorSnackBar(errorMessage.invoke(errors)))
    }
    Column(
        modifier = Modifier.background(AppThemeColors.homeGradient)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth().wrapContentHeight()
                .background(Color.Transparent)
                .drawBehind {
                    val start = 100.dp.value
                    val middle = 450.dp.value
                    drawPath(
                        color = AppThemeColors.white,
                        path = Path().apply {
                            reset()
                            moveTo(0f, start)
                            cubicTo(
                                x1 = 0f,
                                y1 = start,
                                x2 = size.width / 2,
                                y2 = middle,
                                x3 = size.width,
                                y3 = start
                            )
                            lineTo(size.width, size.height)
                            lineTo(0f, size.height)
                            lineTo(0f, start)
                            close()
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier.size(146.dp)
                    .shadow(
                        10.dp,
                        shape = CircleShape,
                        ambientColor = AppThemeColors.white,
                        spotColor = AppThemeColors.white
                    ).offset(y = 4.dp)
            ) {
                drawCircle(AppThemeColors.white.copy(alpha = 0.2f))
            }
            Image(
                painterResource(Avatar.byName(profile.avatar).icon),
                stringResource(Res.string.profileAvatarTitle),
                modifier = Modifier.width(184.dp).height(184.dp).avatarShadow()
            )
            CloseButton(modifier = Modifier.align(Alignment.TopEnd)) {
                onClose.invoke()
            }
        }
        ProfileModal(
            profile = profile,
            title = stringResource(Res.string.profilePasswordChangeTitle)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                OutlinedInput(
                    text = currentPassword,
                    onValueChanged = onCurrentPasswordChanged,
                    label = stringResource(Res.string.profileDataCurrentPasswordLabel),
                    enabled = formEnabled,
                    error = currentPasswordError,
                    errorMessage = errorMessage(listOf(ProfileError.PASSWORD)),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        autoCorrectEnabled = true,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = if (showCurrentPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        Image(
                            if (showCurrentPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            "password",
                            modifier = Modifier.clickable {
                                showCurrentPasswordChanged.invoke(!showCurrentPassword)
                            },
                            colorFilter = ColorFilter.tint(AppThemeColors.grey3)
                        )
                    },
                )
                Spacer(Modifier.height(20.dp))
                OutlinedInput(
                    text = password,
                    onValueChanged = onPasswordChanged,
                    label = stringResource(Res.string.profileDataPasswordLabel),
                    enabled = formEnabled,
                    error = passwordError,
                    errorMessage = errorMessage(listOf(ProfileError.REPEAT)),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        autoCorrectEnabled = true,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = if (showPassword) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    trailingIcon = {
                        Image(
                            if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            "password",
                            modifier = Modifier.clickable {
                                showPasswordChanged.invoke(!showPassword)
                            },
                            colorFilter = ColorFilter.tint(AppThemeColors.grey3)
                        )
                    },
                )
                Spacer(Modifier.height(20.dp))
                OutlinedInput(
                    text = repeat,
                    onValueChanged = onRepeatChanged,
                    label = stringResource(Res.string.profileDataPasswordRepeatLabel),
                    enabled = formEnabled,
                    error = repeatError,
                    errorMessage = errorMessage(listOf(ProfileError.REPEAT)),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onChangePassword.invoke()
                        }
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        autoCorrectEnabled = true,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = if (showRepeat) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        Image(
                            if (showRepeat) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            "password",
                            modifier = Modifier.clickable {
                                showRepeatChanged.invoke(!showRepeat)
                            },
                            colorFilter = ColorFilter.tint(AppThemeColors.grey3)
                        )
                    },
                )
                Spacer(Modifier.height(30.dp))
                if (isSaved) {
                    SubmitButton(
                        onClick = onClose,
                        text = stringResource(Res.string.close),
                    )
                } else {
                    if (formEnabled) {
                        SubmitButton(
                            onClick = onChangePassword,
                            text = stringResource(Res.string.profileDataSubmitButton),
                            enabled = formEnabled,
                        )
                    } else {
                        FormProgress()
                    }
                }
                Spacer(Modifier.height(40.dp))
            }
        }
    }
}