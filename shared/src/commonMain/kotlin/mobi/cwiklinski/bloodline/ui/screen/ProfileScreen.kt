package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Switch
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import mobi.cwiklinski.bloodline.common.isValidEmail
import mobi.cwiklinski.bloodline.data.filed.DummyData
import mobi.cwiklinski.bloodline.domain.Sex
import mobi.cwiklinski.bloodline.domain.model.Center
import mobi.cwiklinski.bloodline.domain.model.Profile
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.button_edit
import mobi.cwiklinski.bloodline.resources.female
import mobi.cwiklinski.bloodline.resources.heroGenitive
import mobi.cwiklinski.bloodline.resources.heroinGenitive
import mobi.cwiklinski.bloodline.resources.ic_sex_female
import mobi.cwiklinski.bloodline.resources.ic_sex_male
import mobi.cwiklinski.bloodline.resources.male
import mobi.cwiklinski.bloodline.resources.profileAvatarTitle
import mobi.cwiklinski.bloodline.resources.profileDataEmailLabel
import mobi.cwiklinski.bloodline.resources.profileDataNameLabel
import mobi.cwiklinski.bloodline.resources.profileDataSubmitButton
import mobi.cwiklinski.bloodline.resources.profileDataTitle
import mobi.cwiklinski.bloodline.resources.profilePasswordChangeTitle
import mobi.cwiklinski.bloodline.resources.profileTitle
import mobi.cwiklinski.bloodline.resources.settingsDefaultCenterLabel
import mobi.cwiklinski.bloodline.resources.settingsLogoutAction
import mobi.cwiklinski.bloodline.resources.settingsLogoutMessage
import mobi.cwiklinski.bloodline.resources.settingsLogoutTitle
import mobi.cwiklinski.bloodline.resources.settingsReminderLabel
import mobi.cwiklinski.bloodline.resources.settingsSexLabel
import mobi.cwiklinski.bloodline.resources.settingsStartingLabel
import mobi.cwiklinski.bloodline.resources.skip
import mobi.cwiklinski.bloodline.ui.model.ProfileError
import mobi.cwiklinski.bloodline.ui.model.ProfileScreenModel
import mobi.cwiklinski.bloodline.ui.model.ProfileState
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.contentText
import mobi.cwiklinski.bloodline.ui.theme.contentTitle
import mobi.cwiklinski.bloodline.ui.theme.getTypography
import mobi.cwiklinski.bloodline.ui.theme.hugeTitle
import mobi.cwiklinski.bloodline.ui.theme.inputPlaceHolder
import mobi.cwiklinski.bloodline.ui.theme.itemSubTitle
import mobi.cwiklinski.bloodline.ui.theme.toolbarTitle
import mobi.cwiklinski.bloodline.ui.util.Avatar
import mobi.cwiklinski.bloodline.ui.util.avatarShadow
import mobi.cwiklinski.bloodline.ui.util.filter
import mobi.cwiklinski.bloodline.ui.widget.AutoCompleteTextView
import mobi.cwiklinski.bloodline.ui.widget.Break
import mobi.cwiklinski.bloodline.ui.widget.FormProgress
import mobi.cwiklinski.bloodline.ui.widget.JustTextButton
import mobi.cwiklinski.bloodline.ui.widget.OutlinedInput
import mobi.cwiklinski.bloodline.ui.widget.SecondaryButton
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
        val profile by screenModel.profile.collectAsStateWithLifecycle(Profile(""))
        val centerList by screenModel.centers.collectAsStateWithLifecycle()
        val state by screenModel.state.collectAsStateWithLifecycle()
        if (state == ProfileState.LoggedOut) {
            navigator.replaceAll(SplashScreen())
        }
        ProfileView(
            screenModel,
            profile,
            centerList,
            state
        )
    }

    @Preview
    @Composable
    fun ProfileView(
        screenModel: ProfileScreenModel,
        profile: Profile = DummyData.generateProfile(),
        centerList: List<Center> = DummyData.CENTERS,
        state: ProfileState = ProfileState.Idle
    ) {
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val behaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()
        var name by remember { mutableStateOf(profile.name) }
        var sex by remember { mutableStateOf(profile.sex) }
        var starting by remember { mutableStateOf(profile.starting) }
        var center by remember { mutableStateOf(centerList.firstOrNull { it.id == profile.centerId }) }
        var query by remember { mutableStateOf("") }
        var notification by remember { mutableStateOf(profile.notification) }
        var email by remember { mutableStateOf(profile.email) }
        val hero = stringResource(
            if (profile.sex.isFemale()) Res.string.heroinGenitive else
                Res.string.heroGenitive
        )
        val scrollState = rememberScrollState()
        screenModel.screenModelScope.launch {
            screenModel.profile.collectLatest { fetchedProfile ->
                name = fetchedProfile.name
                if (fetchedProfile.email.isNotEmpty() && fetchedProfile.email.isValidEmail()) {
                    email = fetchedProfile.email
                }
                sex= fetchedProfile.sex
                notification = fetchedProfile.notification
                starting= fetchedProfile.starting
                if (center == null && fetchedProfile.centerId.isNotEmpty()) {
                    center = centerList.firstOrNull { it.id == fetchedProfile.centerId }
                    query = center?.toSelection() ?: ""
                }
            }
        }
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
                                    screenModel.loggingOut()
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
                if ((listOf(ProfileState.LoggingOut, ProfileState.ToLoggedOut).contains(state))) {
                    BasicAlertDialog(
                        onDismissRequest = {
                            screenModel.cancelLogout()
                        },
                        modifier = Modifier
                            .padding(40.dp)
                            .background(
                                color = AppThemeColors.white,
                                shape = RoundedCornerShape(20.dp)
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(50.dp)
                                .wrapContentSize(),
                        ) {
                            Text(
                                stringResource(Res.string.settingsLogoutTitle),
                                modifier = Modifier.fillMaxWidth(),
                                style = hugeTitle().copy(textAlign = TextAlign.Center)
                            )
                            Spacer(Modifier.height(10.dp))
                            Text(
                                stringResource(Res.string.settingsLogoutMessage),
                                style = contentText().copy(textAlign = TextAlign.Center)
                            )
                            Spacer(Modifier.height(30.dp))
                            if (state == ProfileState.LoggingOut) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    FormProgress(filter = ColorFilter.tint(AppThemeColors.red2))
                                }
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    SecondaryButton(
                                        text = stringResource(Res.string.skip),
                                        onClick = {
                                            screenModel.cancelLogout()
                                        }
                                    )
                                    SubmitButton(
                                        text = stringResource(Res.string.settingsLogoutAction),
                                        onClick = {
                                            screenModel.logout()
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
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
                            painterResource(Avatar.byName(profile.avatar).icon),
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
                        "⎯⎯  ${getAvatarName(profile.avatar)}  ⎯⎯",
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
                            text = email,
                            onValueChanged = { email = it },
                            label = stringResource(Res.string.profileDataEmailLabel),
                            enabled = state != ProfileState.Saving,
                            error = state is ProfileState.Error &&
                                    state.errors.contains(ProfileError.EMAIL),
                            errorMessage = getError(listOf(ProfileError.EMAIL))
                        )
                        Break()
                        Text(
                            stringResource(Res.string.settingsSexLabel),
                            style = itemSubTitle()
                        )
                        Row(
                            modifier = Modifier.selectableGroup().padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        if (sex.isFemale()) AppThemeColors.grey3 else AppThemeColors.background,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .selectable(
                                        selected = (sex == Sex.FEMALE),
                                        onClick = {
                                            sex = Sex.FEMALE
                                        },
                                        role = Role.RadioButton
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painterResource(Res.drawable.ic_sex_female),
                                    stringResource(Res.string.female),
                                    colorFilter = ColorFilter.tint(if (sex.isFemale()) AppThemeColors.white else AppThemeColors.violet2),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(Modifier.width(20.dp))
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        if (!sex.isFemale()) AppThemeColors.grey else AppThemeColors.background,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .selectable(
                                        selected = (sex == Sex.MALE),
                                        onClick = {
                                            sex = Sex.MALE
                                        },
                                        role = Role.RadioButton
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painterResource(Res.drawable.ic_sex_male),
                                    stringResource(Res.string.male),
                                    colorFilter = ColorFilter.tint(if (!sex.isFemale()) AppThemeColors.white else AppThemeColors.violet2),
                                    modifier = Modifier.size(20.dp)
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
                                starting = it.toIntOrNull() ?: 0
                            },
                            label = stringResource(Res.string.settingsStartingLabel),
                            enabled = state != ProfileState.Saving,
                            error = state is ProfileState.Error
                                    && state.errors.contains(ProfileError.DATA),
                            errorMessage = getError(listOf(ProfileError.DATA)),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        Break()
                        Text(
                            stringResource(Res.string.settingsReminderLabel),
                            style = itemSubTitle()
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
                                    screenModel.onProfileDataUpdate(
                                        name,
                                        email,
                                        profile.avatar,
                                        sex,
                                        notification,
                                        starting,
                                        center?.id ?: ""
                                    )
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
