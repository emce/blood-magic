package mobi.cwiklinski.bloodline.ui.screen


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.datetime.Clock
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.close
import mobi.cwiklinski.bloodline.resources.icon_close
import mobi.cwiklinski.bloodline.resources.icon_eye_closed
import mobi.cwiklinski.bloodline.resources.icon_eye_opened
import mobi.cwiklinski.bloodline.resources.profileAvatarTitle
import mobi.cwiklinski.bloodline.resources.profileDataCurrentPasswordLabel
import mobi.cwiklinski.bloodline.resources.profileDataPasswordLabel
import mobi.cwiklinski.bloodline.resources.profileDataPasswordRepeatLabel
import mobi.cwiklinski.bloodline.resources.profileDataSubmitButton
import mobi.cwiklinski.bloodline.resources.profilePasswordChangeTitle
import mobi.cwiklinski.bloodline.ui.model.ProfileError
import mobi.cwiklinski.bloodline.ui.model.ProfileScreenModel
import mobi.cwiklinski.bloodline.ui.model.ProfileState
import mobi.cwiklinski.bloodline.ui.theme.AppFontFamily
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.getFontFamily
import mobi.cwiklinski.bloodline.ui.theme.getTypography
import mobi.cwiklinski.bloodline.ui.util.Avatar
import mobi.cwiklinski.bloodline.ui.util.avatarShadow
import mobi.cwiklinski.bloodline.ui.widget.FormProgress
import mobi.cwiklinski.bloodline.ui.widget.OutlinedInput
import mobi.cwiklinski.bloodline.ui.widget.SubmitButton
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class ProfilePasswordScreen : AppProfileScreen() {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val screenModel = navigator.koinNavigatorScreenModel<ProfileScreenModel>()
        val profile = screenModel.profile.collectAsStateWithLifecycle()
        val focusManager = LocalFocusManager.current
        if (screenModel.state.value == ProfileState.Saved) {
            bottomSheetNavigator.hide()
        }
        var currentPassword by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var repeat by remember { mutableStateOf("") }
        val showCurrentPassword = remember { mutableStateOf(false) }
        val showNewPassword = remember { mutableStateOf(false) }
        val showRepeatPassword = remember { mutableStateOf(false) }
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
                    painterResource(Avatar.byName(screenModel.profile.value?.avatar).icon),
                    stringResource(Res.string.profileAvatarTitle),
                    modifier = Modifier.width(184.dp).height(184.dp).avatarShadow()
                )
                Button(
                    onClick = { bottomSheetNavigator.hide() },
                    colors = AppThemeColors.textButtonColors(),
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Image(
                        painterResource(Res.drawable.icon_close),
                        stringResource(Res.string.close)
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth().wrapContentHeight()
                    .background(AppThemeColors.white)
                    .scrollable(
                        rememberScrollState(), Orientation.Vertical
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                Text(
                    profile.value?.name ?: "",
                    style = getTypography().displayMedium.copy(color = AppThemeColors.black)
                )
                Text(
                    "⎯⎯  ${getAvatarName(screenModel.profile.value?.avatar)}  ⎯⎯",
                    style = getTypography().displaySmall.copy(
                        color = AppThemeColors.black70,
                        fontFamily = getFontFamily(AppFontFamily.REGULAR)
                    ),
                )
                Text(
                    stringResource(Res.string.profilePasswordChangeTitle),
                    style = getTypography().displaySmall.copy(
                        color = AppThemeColors.black,
                        textAlign = TextAlign.Start
                    ),
                    modifier = Modifier.fillMaxWidth().padding(20.dp)
                )
                Spacer(Modifier.height(20.dp))
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
                        onValueChanged = { currentPassword = it },
                        label = stringResource(Res.string.profileDataCurrentPasswordLabel),
                        enabled = screenModel.state.value != ProfileState.Saving,
                        error = screenModel.state.value is ProfileState.Error &&
                                (screenModel.state.value as ProfileState.Error).errors.contains(
                                    ProfileError.PASSWORD
                                ),
                        errorMessage = getError(listOf(ProfileError.PASSWORD)),
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
                        visualTransformation = if (showCurrentPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            Image(
                                painterResource(if (showCurrentPassword.value) Res.drawable.icon_eye_opened else Res.drawable.icon_eye_closed),
                                "password",
                                modifier = Modifier.clickable {
                                    showCurrentPassword.value = !showCurrentPassword.value
                                }
                            )
                        },
                    )
                    Spacer(Modifier.height(20.dp))
                    OutlinedInput(
                        text = password,
                        onValueChanged = { password = it },
                        label = stringResource(Res.string.profileDataPasswordLabel),
                        enabled = screenModel.state.value != ProfileState.Saving,
                        error = screenModel.state.value is ProfileState.Error &&
                                (screenModel.state.value as ProfileState.Error).errors.contains(
                                    ProfileError.REPEAT
                                ),
                        errorMessage = getError(listOf(ProfileError.REPEAT)),
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
                        visualTransformation = if (showNewPassword.value) VisualTransformation.None
                            else PasswordVisualTransformation(),
                        trailingIcon = {
                            Image(
                                painterResource(if (showNewPassword.value) Res.drawable.icon_eye_opened else Res.drawable.icon_eye_closed),
                                "password",
                                modifier = Modifier.clickable {
                                    showNewPassword.value = !showNewPassword.value
                                }
                            )
                        },
                    )
                    Spacer(Modifier.height(20.dp))
                    OutlinedInput(
                        text = repeat,
                        onValueChanged = { repeat = it },
                        label = stringResource(Res.string.profileDataPasswordRepeatLabel),
                        enabled = screenModel.state.value != ProfileState.Saving,
                        error = screenModel.state.value is ProfileState.Error,
                        errorMessage = getError(listOf(ProfileError.REPEAT)),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                screenModel.onProfilePasswordUpdate(
                                    currentPassword,
                                    password,
                                    repeat
                                )
                            }
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            autoCorrectEnabled = true,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        visualTransformation = if (showRepeatPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            Image(
                                painterResource(if (showRepeatPassword.value) Res.drawable.icon_eye_opened else Res.drawable.icon_eye_closed),
                                "password",
                                modifier = Modifier.clickable {
                                    showRepeatPassword.value = !showRepeatPassword.value
                                }
                            )
                        },
                    )
                    Spacer(Modifier.height(30.dp))
                    if (screenModel.state.value is ProfileState.Error) {
                        Text(
                            getError((screenModel.state.value as ProfileState.Error).errors),
                            style = getTypography().displaySmall.copy(
                                color = AppThemeColors.red2
                            )
                        )
                        Spacer(Modifier.height(30.dp))
                    }
                    if (screenModel.state.value == ProfileState.Saved) {
                        SubmitButton(
                            onClick = {
                                bottomSheetNavigator.hide()
                            },
                            text = stringResource(Res.string.close),
                        )
                    } else {
                        if (screenModel.state.value != ProfileState.Saving) {
                            SubmitButton(
                                onClick = {
                                    screenModel.onProfilePasswordUpdate(
                                        currentPassword,
                                        password,
                                        repeat
                                    )
                                },
                                text = stringResource(Res.string.profileDataSubmitButton),
                                enabled = screenModel.state.value != ProfileState.Saving,
                            )
                        } else {
                            FormProgress()
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                }
            }
        }
    }
}