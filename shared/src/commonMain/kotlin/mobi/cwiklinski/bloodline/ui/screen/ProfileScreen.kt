package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Checkbox
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material3.*
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.datetime.Clock
import mobi.cwiklinski.bloodline.data.filed.DummyData
import mobi.cwiklinski.bloodline.domain.Sex
import mobi.cwiklinski.bloodline.domain.model.Center
import mobi.cwiklinski.bloodline.domain.model.Profile
import mobi.cwiklinski.bloodline.resources.*
import mobi.cwiklinski.bloodline.ui.model.ProfileError
import mobi.cwiklinski.bloodline.ui.model.ProfileScreenModel
import mobi.cwiklinski.bloodline.ui.model.ProfileState
import mobi.cwiklinski.bloodline.ui.theme.*
import mobi.cwiklinski.bloodline.ui.util.Avatar
import mobi.cwiklinski.bloodline.ui.util.avatarShadow
import mobi.cwiklinski.bloodline.ui.util.filter
import mobi.cwiklinski.bloodline.ui.widget.AutoCompleteTextView
import mobi.cwiklinski.bloodline.ui.widget.FormProgress
import mobi.cwiklinski.bloodline.ui.widget.JustTextButton
import mobi.cwiklinski.bloodline.ui.widget.OutlinedInput
import mobi.cwiklinski.bloodline.ui.widget.SubmitButton
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

class ProfileScreen(override val key: ScreenKey = Clock.System.now().toString()) :
    AppProfileScreen() {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<ProfileScreenModel>()
        val profile by screenModel.profile.collectAsStateWithLifecycle()
        val centerList by screenModel.centers.collectAsStateWithLifecycle()
        val state by screenModel.state.collectAsStateWithLifecycle()
        if (state == ProfileState.LoggedOut) {
            navigator.replaceAll(SplashScreen())
        }
        ProfileView(
            profile,
            centerList,
            state,
            { newName, newSex, newNotification, newStarting, newCenterId ->
                screenModel.onProfileDataUpdate(
                    newName,
                    profile?.avatar ?: Avatar.WIZARD.name,
                    newSex,
                    newNotification,
                    newStarting,
                    newCenterId
                )
            }) { screenModel.logout() }
    }

    @Composable
    fun Break() = Spacer(Modifier.height(20.dp))

    @Preview
    @Composable
    fun ProfileView(
        profile: Profile? = DummyData.generateProfile(),
        centerList: List<Center> = DummyData.CENTERS,
        state: ProfileState = ProfileState.Idle,
        onProfileSave: (String, Sex, Boolean, Int, String) -> Unit,
        logout: () -> Unit
    ) {
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val behaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()
        var name by remember { mutableStateOf(profile?.name ?: "") }
        var sex by remember { mutableStateOf(profile?.sex ?: Sex.MALE) }
        var starting by remember { mutableStateOf(profile?.starting ?: 0) }
        var center by remember { mutableStateOf(centerList.firstOrNull { it.id == profile?.centerId }) }
        var query by remember { mutableStateOf("") }
        var notification by remember { mutableStateOf(profile?.notification ?: true) }
        var email by remember { mutableStateOf(profile?.email) }
        val hero = if (profile?.sex?.isFemale() == true)
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
                                style = toolbarTitle()
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                            titleContentColor = AppThemeColors.black,
                            navigationIconContentColor = AppThemeColors.black
                        ),
                        actions = {
                            Text(
                                stringResource(Res.string.settingsLogoutAction),
                                modifier = Modifier.padding(5.dp).clickable {
                                    logout.invoke()
                                }
                            )
                        },
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
                            painterResource(Avatar.byName(profile?.avatar).icon),
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
                        name,
                        style = contentTitle()
                    )
                    Text(
                        "⎯⎯  ${getAvatarName(profile?.avatar)}  ⎯⎯",
                        style = contentText(),
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
                        Break()
                        OutlinedInput(
                            text = name,
                            onValueChanged = { name = it },
                            label = stringResource(Res.string.profileDataNameLabel),
                            enabled = state != ProfileState.Saving,
                            error = state is ProfileState.Error
                                    && state.errors.contains(ProfileError.DATA),
                            errorMessage = getError(listOf(ProfileError.DATA))
                        )
                        Break()
                        OutlinedInput(
                            text = email ?: "",
                            onValueChanged = { email = it },
                            label = stringResource(Res.string.profileDataEmailLabel),
                            enabled = state != ProfileState.Saving,
                            error = state is ProfileState.Error &&
                                    state.errors.contains(ProfileError.EMAIL),
                            errorMessage = getError(listOf(ProfileError.EMAIL))
                        )
                        Break()
                        Text(
                            stringResource(Res.string.settingsSexLabel)
                        )
                        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                            Row(horizontalArrangement = Arrangement.Center) {
                                Image(
                                    painterResource(Res.drawable.avatar_fairy),
                                    stringResource(Res.string.female),
                                    modifier = Modifier.size(40.dp)
                                )
                                Checkbox(
                                    checked = sex == Sex.FEMALE,
                                    onCheckedChange = {
                                        sex = Sex.FEMALE
                                    },
                                    colors = AppThemeColors.checkboxColors()
                                )
                            }
                            Row(horizontalArrangement = Arrangement.Center) {
                                Image(
                                    painterResource(Res.drawable.avatar_wizard),
                                    stringResource(Res.string.male),
                                    modifier = Modifier.size(40.dp)
                                )
                                Checkbox(
                                    checked = sex == Sex.MALE,
                                    onCheckedChange = {
                                        sex = Sex.MALE
                                    },
                                    colors = AppThemeColors.checkboxColors()
                                )
                            }
                        }
                        Break()
                        AutoCompleteTextView(
                            modifier = Modifier.fillMaxWidth(),
                            query = query,
                            enabled = state != ProfileState.Saving,
                            queryLabel = stringResource(Res.string.settingsDefaultCenterLabel),
                            onQueryChanged = { newQuery ->
                                query = newQuery
                            },
                            predictions = centerList.filter(query),
                            onClearClick = {
                                query = ""
                            },
                            onItemClick = { selectedCenter ->
                                center = selectedCenter
                                query = selectedCenter.toSelection()
                            }
                        ) { center ->
                            Text(
                                center.toSelection(),
                                style = inputPlaceHolder(),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Break()
                        OutlinedInput(
                            text = starting.toString(),
                            onValueChanged = {
                                starting = it.toIntOrNull() ?: 0 },
                            label = stringResource(Res.string.settingsStartingLabel),
                            enabled = state != ProfileState.Saving,
                            error = state is ProfileState.Error
                                    && state.errors.contains(ProfileError.DATA),
                            errorMessage = getError(listOf(ProfileError.DATA)),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        Break()
                        Text(
                            stringResource(Res.string.settingsReminderLabel)
                        )
                        Switch(
                            checked = notification,
                            onCheckedChange = {
                                notification = it
                            },
                            enabled = state != ProfileState.Saving,
                            colors = AppThemeColors.switchColors()
                        )
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            JustTextButton(
                                text = stringResource(Res.string.profilePasswordChangeTitle),
                                onClicked = {
                                    bottomSheetNavigator.show(ProfilePasswordScreen())
                                },
                                enabled = state != ProfileState.Saving
                            )
                        }
                        Break()
                        if (state is ProfileState.Error) {
                            Text(
                                getError(state.errors),
                                style = getTypography().bodyMedium.copy(
                                    color = AppThemeColors.red2
                                )
                            )
                            Spacer(Modifier.height(30.dp))
                        }
                        if (state != ProfileState.Saving) {
                            SubmitButton(
                                onClick = {
                                    onProfileSave.invoke(name, sex, notification, starting, center?.id ?: "")
                                },
                                text = stringResource(Res.string.profileDataSubmitButton),
                                enabled = state != ProfileState.Saving,
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
