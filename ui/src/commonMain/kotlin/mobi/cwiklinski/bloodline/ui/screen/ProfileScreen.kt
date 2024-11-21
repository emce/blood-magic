package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.datetime.Clock
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.button_edit
import mobi.cwiklinski.bloodline.resources.heroGenitive
import mobi.cwiklinski.bloodline.resources.heroinGenitive
import mobi.cwiklinski.bloodline.resources.profileAvatarTitle
import mobi.cwiklinski.bloodline.resources.profileDataEmailLabel
import mobi.cwiklinski.bloodline.resources.profileDataNameLabel
import mobi.cwiklinski.bloodline.resources.profileDataSubmitButton
import mobi.cwiklinski.bloodline.resources.profileDataTitle
import mobi.cwiklinski.bloodline.resources.profilePasswordChangeTitle
import mobi.cwiklinski.bloodline.resources.profileTitle
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
import mobi.cwiklinski.bloodline.ui.widget.JustTextButton
import mobi.cwiklinski.bloodline.ui.widget.OutlinedInput
import mobi.cwiklinski.bloodline.ui.widget.SubmitButton
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class ProfileScreen(override val key: ScreenKey = Clock.System.now().toString()) : AppProfileScreen() {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val screenModel = navigator.koinNavigatorScreenModel<ProfileScreenModel>()
        val profile = screenModel.profile.collectAsStateWithLifecycle()
        val behaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()
        var name by remember { mutableStateOf(profile.value?.name) }
        var email by remember { mutableStateOf(profile.value?.email) }
        val hero = if (profile.value?.sex?.isFemale() == true)
                stringResource(Res.string.heroinGenitive)
            else
                stringResource(Res.string.heroGenitive)
        val scrollState = rememberScrollState()
        Box(
            modifier = Modifier.background(AppThemeColors.homeGradient)
        ) {
            Scaffold(
                modifier = Modifier.nestedScroll(behaviour.nestedScrollConnection),
                backgroundColor = Color.Transparent,
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                stringResource(Res.string.profileTitle),
                                style = getTypography().displaySmall.copy(color = AppThemeColors.black)
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                            titleContentColor = AppThemeColors.black,
                            navigationIconContentColor = AppThemeColors.black
                        ),
                        scrollBehavior = behaviour
                    )
                },
                floatingActionButton = { getFAB() },
                floatingActionButtonPosition = FabPosition.Center,
                isFloatingActionButtonDocked = true,
                bottomBar = { getBottomBar() }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .drawBehind {
                            val start = 100.dp.value
                            val middle = 450.dp.value
                            drawPath(
                                color = AppThemeColors.background,
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
                                },
                            )
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Box(
                        modifier = Modifier.wrapContentSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painterResource(Avatar.byName(profile.value?.avatar).icon),
                            stringResource(Res.string.profileAvatarTitle),
                            modifier = Modifier
                                .width(184.dp)
                                .height(184.dp)
                                .avatarShadow()
                        )
                        Image(
                            painterResource(Res.drawable.button_edit),
                            stringResource(Res.string.profileAvatarTitle),
                            modifier = Modifier.offset(52.dp, 52.dp).clickable {
                                bottomSheetNavigator.show(ProfileAvatarScreen())
                            }
                        )
                    }
                    Text(
                        screenModel.profile.value?.name ?: "",
                        style = getTypography().displayMedium.copy(color = AppThemeColors.black)
                    )
                    Text(
                        "⎯⎯  ${getAvatarName(screenModel.profile.value?.avatar)}  ⎯⎯",
                        style = getTypography().displaySmall.copy(
                            color = AppThemeColors.black70,
                            fontFamily = getFontFamily(AppFontFamily.REGULAR)
                        ),
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            stringResource(Res.string.profileDataTitle).replace("%s", hero),
                            style = getTypography().bodyMedium.copy(
                                color = AppThemeColors.black,
                                textAlign = TextAlign.Start
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(20.dp))
                        OutlinedInput(
                            text = name ?: "",
                            onValueChanged = { name = it },
                            label = stringResource(Res.string.profileDataNameLabel),
                            enabled = screenModel.state.value != ProfileState.Saving,
                            error = screenModel.state.value is ProfileState.Error
                                    && (screenModel.state.value as ProfileState.Error).errors.contains(ProfileError.DATA),
                            errorMessage = getError(listOf(ProfileError.DATA))
                        )
                        Spacer(Modifier.height(20.dp))
                        OutlinedInput(
                            text = email ?: "",
                            onValueChanged = { email = it },
                            label = stringResource(Res.string.profileDataEmailLabel),
                            enabled = screenModel.state.value != ProfileState.Saving,
                            error = screenModel.state.value is ProfileState.Error &&
                                    (screenModel.state.value as ProfileState.Error).errors.contains(ProfileError.EMAIL),
                            errorMessage = getError(listOf(ProfileError.EMAIL))
                        )
                        Spacer(Modifier.height(20.dp))
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            JustTextButton(
                                text = stringResource(Res.string.profilePasswordChangeTitle),
                                onClicked = {
                                    bottomSheetNavigator.show(ProfilePasswordScreen())
                                },
                                enabled = screenModel.state.value != ProfileState.Saving
                            )
                        }
                        Spacer(Modifier.height(20.dp))
                        if (screenModel.state.value is ProfileState.Error) {
                            Text(
                                getError((screenModel.state.value as ProfileState.Error).errors),
                                style = getTypography().bodyMedium.copy(
                                    color = AppThemeColors.red2
                                )
                            )
                            Spacer(Modifier.height(30.dp))
                        }
                        if (screenModel.state.value != ProfileState.Saving) {
                            SubmitButton(
                                onClick = {

                                },
                                text = stringResource(Res.string.profileDataSubmitButton),
                                enabled = screenModel.state.value != ProfileState.Saving,
                            )
                        } else {
                            FormProgress()
                        }
                        Spacer(Modifier.height(100.dp))
                    }
                }
            }
        }
    }
}