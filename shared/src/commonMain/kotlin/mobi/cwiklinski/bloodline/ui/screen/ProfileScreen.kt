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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.lifecycle.LifecycleEffectOnce
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import mobi.cwiklinski.bloodline.common.Job
import mobi.cwiklinski.bloodline.common.event.SideEffects
import mobi.cwiklinski.bloodline.common.manager.CallbackManager
import mobi.cwiklinski.bloodline.data.Parcelize
import mobi.cwiklinski.bloodline.domain.Sex
import mobi.cwiklinski.bloodline.domain.model.Center
import mobi.cwiklinski.bloodline.getPlatform
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.button_edit
import mobi.cwiklinski.bloodline.resources.female
import mobi.cwiklinski.bloodline.resources.heroGenitive
import mobi.cwiklinski.bloodline.resources.heroinGenitive
import mobi.cwiklinski.bloodline.resources.male
import mobi.cwiklinski.bloodline.resources.profileAvatarTitle
import mobi.cwiklinski.bloodline.resources.profileDataEmailLabel
import mobi.cwiklinski.bloodline.resources.profileDataNameLabel
import mobi.cwiklinski.bloodline.resources.profileDataSubmitButton
import mobi.cwiklinski.bloodline.resources.profileDataTitle
import mobi.cwiklinski.bloodline.resources.profileDeleteTitle
import mobi.cwiklinski.bloodline.resources.profilePasswordChangeTitle
import mobi.cwiklinski.bloodline.resources.profileTitle
import mobi.cwiklinski.bloodline.resources.settingsDefaultCenterLabel
import mobi.cwiklinski.bloodline.resources.settingsLogoutTitle
import mobi.cwiklinski.bloodline.resources.settingsReminderLabel
import mobi.cwiklinski.bloodline.resources.settingsSexLabel
import mobi.cwiklinski.bloodline.resources.settingsStartingLabel
import mobi.cwiklinski.bloodline.ui.model.ProfileError
import mobi.cwiklinski.bloodline.ui.model.ProfileScreenModel
import mobi.cwiklinski.bloodline.ui.model.ProfileState
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors.topBarColors
import mobi.cwiklinski.bloodline.ui.theme.contentAction
import mobi.cwiklinski.bloodline.ui.theme.contentText
import mobi.cwiklinski.bloodline.ui.theme.contentTitle
import mobi.cwiklinski.bloodline.ui.theme.itemDelete
import mobi.cwiklinski.bloodline.ui.theme.itemSubTitle
import mobi.cwiklinski.bloodline.ui.util.Avatar
import mobi.cwiklinski.bloodline.ui.util.NavigationItem
import mobi.cwiklinski.bloodline.ui.util.avatarShadow
import mobi.cwiklinski.bloodline.ui.util.clearStack
import mobi.cwiklinski.bloodline.ui.util.filter
import mobi.cwiklinski.bloodline.ui.util.koinNavigatorScreenModel
import mobi.cwiklinski.bloodline.ui.widget.AutoCompleteTextView
import mobi.cwiklinski.bloodline.ui.widget.Break
import mobi.cwiklinski.bloodline.ui.widget.CenterSelectItem
import mobi.cwiklinski.bloodline.ui.widget.DesktopNavigationScaffold
import mobi.cwiklinski.bloodline.ui.widget.DesktopTitleBar
import mobi.cwiklinski.bloodline.ui.widget.FormProgress
import mobi.cwiklinski.bloodline.ui.widget.JustTextButton
import mobi.cwiklinski.bloodline.ui.widget.LogoutDialog
import mobi.cwiklinski.bloodline.ui.widget.MobileLandscapeNavigationLayout
import mobi.cwiklinski.bloodline.ui.widget.MobilePortraitNavigationLayout
import mobi.cwiklinski.bloodline.ui.widget.MobileTitleBar
import mobi.cwiklinski.bloodline.ui.widget.OutlinedInput
import mobi.cwiklinski.bloodline.ui.widget.SubmitButton
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Parcelize
class ProfileScreen(override val key: ScreenKey = Clock.System.now().toString()) :
    AppProfileScreen() {

    @Composable
    override fun defaultView() {
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val screenModel = navigator.koinNavigatorScreenModel<ProfileScreenModel>()
        MobilePortraitNavigationLayout(
            topBar = {
                MobileTitleBar(
                    title = stringResource(Res.string.profileTitle),
                    actions = {
                        if (getPlatform().isDebugBinary) {
                            IconButton(onClick = {
                                screenModel.screenModelScope.launch {
                                    Job.checkNotifications()
                                }
                            }) {
                                Icon(
                                    Icons.Filled.Notifications,
                                    contentDescription = "not",
                                    modifier = Modifier.padding(5.dp)
                                )
                            }
                            IconButton(onClick = {
                                screenModel.screenModelScope.launch {
                                    Job.checkPotentialDonation()
                                }
                            }) {
                                Icon(
                                    Icons.Filled.WaterDrop,
                                    contentDescription = "not",
                                    modifier = Modifier.padding(5.dp)
                                )
                            }
                        }
                        IconButton(onClick = {
                            screenModel.screenModelScope.launch {
                                navigator.push(AboutScreen())
                            }
                        }) {
                            Icon(
                                Icons.Outlined.Info,
                                contentDescription = "not",
                                modifier = Modifier.padding(5.dp)
                            )
                        }
                        IconButton(onClick = {
                            screenModel.loggingOut()
                        }) {
                            Icon(
                                Icons.AutoMirrored.Filled.Logout,
                                contentDescription = stringResource(Res.string.settingsLogoutTitle),
                                modifier = Modifier.padding(5.dp),
                                tint = AppThemeColors.red1
                            )
                        }
                    },
                    colors = topBarColors()
                )
            },
            selected = NavigationItem.PROFILE,
            navigationAction = { navigationItem ->
                when (navigationItem) {
                    NavigationItem.LIST -> {
                        navigator.push(DonationsScreen())
                    }
                    NavigationItem.CENTER -> {
                        navigator.push(CentersScreen())
                    }
                    NavigationItem.PROFILE -> {
                        navigator.push(ProfileScreen())
                    }
                    else -> {
                        navigator.push(HomeScreen())
                    }
                }
            },
            floatingAction = {
                bottomSheetNavigator.show(NewDonationScreen())
            },
            modifier = Modifier
                .drawBehind {
                    drawRect(
                        brush = AppThemeColors.homeGradient,
                        size = Size(width = 4000f, height = 400f)
                    )
                }
        ) { paddingValues ->
            InternalProfileView(paddingValues)
        }
    }

    @Composable
    override fun tabletView() {
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val screenModel = navigator.koinNavigatorScreenModel<ProfileScreenModel>()
        MobileLandscapeNavigationLayout(
            topBar = {
                MobileTitleBar(
                    title = stringResource(Res.string.profileTitle),
                    actions = {
                        TextButton(onClick = {
                            screenModel.loggingOut()
                        }) {
                            Text(
                                stringResource(Res.string.settingsLogoutTitle)
                            )
                        }
                    },
                    colors = topBarColors()
                )
            },
            selected = NavigationItem.PROFILE,
            navigationAction = { navigationItem ->
                when (navigationItem) {
                    NavigationItem.LIST -> {
                        navigator.push(DonationsScreen())
                    }
                    NavigationItem.CENTER -> {
                        navigator.push(CentersScreen())
                    }
                    NavigationItem.PROFILE -> {
                        navigator.push(ProfileScreen())
                    }
                    else -> {
                        navigator.push(HomeScreen())
                    }
                }
            },
            floatingAction = {
                bottomSheetNavigator.show(NewDonationScreen())
            },
            infoClicked = {
                navigator.push(AboutScreen())
            },
            desiredContent = {
                InternalProfileView(PaddingValues(0.dp))
            },
            modifier = Modifier
                .drawBehind {
                    drawRect(
                        brush = AppThemeColors.homeGradient,
                        size = Size(width = 4000f, height = 400f)
                    )
                }
        )
    }

    @Composable
    override fun desktopView() {
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val screenModel = navigator.koinNavigatorScreenModel<ProfileScreenModel>()
        DesktopNavigationScaffold(
            topBar = {
                DesktopTitleBar(
                    title = stringResource(Res.string.profileTitle),
                    actions = {
                        if (getPlatform().isDebugBinary) {
                            IconButton(onClick = {
                                screenModel.screenModelScope.launch {
                                    Job.checkNotifications()
                                }
                            }) {
                                Icon(
                                    Icons.Filled.Notifications,
                                    contentDescription = "not",
                                    modifier = Modifier.padding(5.dp)
                                )
                            }
                            IconButton(onClick = {
                                screenModel.screenModelScope.launch {
                                    Job.checkPotentialDonation()
                                }
                            }) {
                                Icon(
                                    Icons.Filled.WaterDrop,
                                    contentDescription = "not",
                                    modifier = Modifier.padding(5.dp)
                                )
                            }
                        }
                        TextButton(onClick = {
                            screenModel.loggingOut()
                        }) {
                            Text(
                                stringResource(Res.string.settingsLogoutTitle),
                                style = contentAction()
                            )
                        }
                    }
                )
            },
            selected = NavigationItem.PROFILE,
            navigationAction = { navigationItem ->
                when (navigationItem) {
                    NavigationItem.LIST -> {
                        navigator.push(DonationsScreen())
                    }
                    NavigationItem.CENTER -> {
                        navigator.push(CentersScreen())
                    }
                    NavigationItem.PROFILE -> {
                        navigator.push(ProfileScreen())
                    }
                    else -> {
                        navigator.push(HomeScreen())
                    }
                }
            },
            floatingAction = {
                bottomSheetNavigator.show(NewDonationScreen())
            },
            infoClicked = {
                navigator.push(AboutScreen())
            },
            desiredContent = {
                InternalProfileView(PaddingValues(0.dp))
            },
            modifier = Modifier
                .drawBehind {
                    drawRect(
                        brush = AppThemeColors.homeGradient,
                        size = Size(width = 4000f, height = 400f)
                    )
                }
        )
    }

    @OptIn(ExperimentalVoyagerApi::class)
    @Composable
    fun InternalProfileView(paddingValues: PaddingValues) {
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val screenModel = navigator.koinNavigatorScreenModel<ProfileScreenModel>()
        val centerList by screenModel.centers.collectAsStateWithLifecycle(emptyList())
        val state by screenModel.state.collectAsStateWithLifecycle(ProfileState.Idle)
        handleSideEffects<ProfileState, ProfileScreenModel>()
        var name by remember { mutableStateOf("") }
        var sex by remember { mutableStateOf(Sex.MALE) }
        var starting by remember { mutableStateOf(0) }
        var center by remember { mutableStateOf<Center?>(null) }
        var query by remember { mutableStateOf("") }
        var notification by remember { mutableStateOf(false) }
        var email by remember { mutableStateOf("") }
        val heroGenitive = stringResource(Res.string.heroGenitive)
        val heroinGenitive = stringResource(Res.string.heroinGenitive)
        var hero by remember { mutableStateOf(heroGenitive) }
        var avatar by remember { mutableStateOf(Avatar.WIZARD) }
        LifecycleEffectOnce {
            screenModel.screenModelScope.launch {
                screenModel.profile.collectLatest { latestProfile ->
                    name = latestProfile.name
                    email = latestProfile.email
                    sex = latestProfile.sex
                    starting = latestProfile.starting
                    notification = latestProfile.notification
                    if (latestProfile.centerId.isNotEmpty()) {
                        centerList.firstOrNull { it.id == latestProfile.centerId }?.let {
                            center = it
                            query = it.toSelection()
                        }
                    }
                    hero = if (latestProfile.sex.isFemale()) heroinGenitive else heroGenitive
                    avatar = Avatar.byName(latestProfile.avatar)
                }
            }
        }
        if (state == ProfileState.LoggingOut) {
            screenModel.resetState()
            navigator.clearStack()
            navigator.replaceAll(LogoutScreen())
        }
        if (state == ProfileState.ToLoggedOut) {
            LogoutDialog(state, { screenModel.cancelLogout() } ) {
                screenModel.logout()
            }
        }
        ProfileView(
            paddingValues = paddingValues,
            avatar = avatar,
            editAvatar = {
                bottomSheetNavigator.show(ProfileAvatarScreen())
            },
            avatarName = { avatarName ->
                getAvatarName(avatarName)
            },
            formEnabled = state != ProfileState.Saving,
            name = name,
            onNameChanged = { newName ->
                name = newName
            },
            nameError = state is ProfileState.Error
                    && (state as ProfileState.Error).errors.contains(ProfileError.DATA),
            email = email,
            onEmailChanged = { newEmail ->
                email = newEmail
            },
            emailError = state is ProfileState.Error &&
                    (state as ProfileState.Error).errors.contains(ProfileError.EMAIL),
            sex = sex,
            onSexChanged = { newSex ->
                sex = newSex
            },
            center = query,
            centerList = centerList,
            onCenterChanged = { newQuery ->
                query = newQuery
            },
            onCenterPicked = { newCenter ->
                center = newCenter
                query = newCenter.toSelection()
            },
            onCenterClear = {
                query = ""
            },
            starting = starting,
            onStartingChanged = { newStarting ->
                starting = newStarting
            },
            startingError = state is ProfileState.Error
                    && (state as ProfileState.Error).errors.contains(ProfileError.DATA),
            notification = notification,
            onNotificationChanged = { newNotification ->
                notification = newNotification
            },
            errorMessage = { errors ->
                getError(errors)
            },
            onProfileDelete = {
                bottomSheetNavigator.show(ProfileDeleteScreen())
            },
            onPasswordChange = {
                bottomSheetNavigator.show(ProfilePasswordScreen())
            },
            errors = if (state is ProfileState.Error) {
                (state as ProfileState.Error).errors
            } else {
                emptyList()
            },
            onProfileSave = {
                screenModel.onProfileDataUpdate(
                    name,
                    email,
                    avatar.name,
                    sex,
                    notification,
                    starting,
                    center?.id ?: ""
                )
            }
        )
    }
}

@Composable
fun ProfileView(
    paddingValues: PaddingValues = PaddingValues(0.dp),
    avatar: Avatar = Avatar.FENIX,
    editAvatar: () -> Unit = {},
    avatarName: @Composable (String) -> String = { avatar.name },
    formEnabled: Boolean = true,
    name: String = avatar.name,
    onNameChanged: (String) -> Unit = {},
    nameError: Boolean = false,
    email: String = "",
    onEmailChanged: (String) -> Unit = {},
    emailError: Boolean = false,
    sex: Sex = Sex.MALE,
    onSexChanged: (Sex) -> Unit = {},
    center: String = "center",
    centerList: List<Center> = emptyList(),
    onCenterChanged: (String) -> Unit = {},
    onCenterPicked: (Center) -> Unit = {},
    onCenterClear: () -> Unit = {},
    starting: Int = 0,
    onStartingChanged: (Int) -> Unit = {},
    startingError: Boolean = false,
    notification: Boolean = true,
    onNotificationChanged: (Boolean) -> Unit = {},
    errorMessage: @Composable (List<ProfileError>) -> String = { "" },
    onProfileDelete: () ->Unit = {},
    onPasswordChange: () -> Unit = {},
    errors: List<ProfileError> = emptyList(),
    onProfileSave: () -> Unit = {}
) {
    val heroGenitive = stringResource(Res.string.heroGenitive)
    val heroinGenitive = stringResource(Res.string.heroinGenitive)
    val hero = if (sex.isFemale()) heroinGenitive else heroGenitive
    val scrollState = rememberScrollState()
    if (errors.isNotEmpty()) {
        koinInject<CallbackManager>().postSideEffect(SideEffects.ErrorSnackBar(errorMessage.invoke(errors)))
    }
    Column(
        modifier = Modifier.padding(paddingValues)
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
                painterResource(avatar.icon),
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
                    editAvatar.invoke()
                }
            )
        }
        Text(
            name,
            style = contentTitle()
        )
        Text(
            "⎯⎯  ${avatarName.invoke(avatar.name)}  ⎯⎯",
            style = contentText(),
        )
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(Res.string.profileDataTitle).replace("%s", hero),
                style = contentText(),
                modifier = Modifier.fillMaxWidth()
            )
            Break()
            OutlinedInput(
                text = name,
                onValueChanged = onNameChanged,
                label = stringResource(Res.string.profileDataNameLabel),
                enabled = formEnabled,
                error = nameError,
                errorMessage = errorMessage(listOf(ProfileError.DATA))
            )
            Break()
            OutlinedInput(
                text = email,
                onValueChanged = onEmailChanged,
                label = stringResource(Res.string.profileDataEmailLabel),
                enabled = formEnabled,
                error = emailError,
                errorMessage = errorMessage(listOf(ProfileError.EMAIL))
            )
            Break()
            Row(
                modifier = Modifier.selectableGroup().padding(vertical = 10.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(Res.string.settingsSexLabel),
                    style = itemSubTitle()
                )
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
                                onSexChanged.invoke(Sex.FEMALE)
                            },
                            role = Role.RadioButton
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        Icons.Filled.Female,
                        stringResource(Res.string.female),
                        colorFilter = ColorFilter.tint(
                            if (sex.isFemale())
                                AppThemeColors.white
                            else
                                AppThemeColors.violet2
                        ),
                        modifier = Modifier.size(20.dp)
                    )
                }
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
                                onSexChanged.invoke(Sex.MALE)
                            },
                            role = Role.RadioButton
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        Icons.Filled.Male,
                        stringResource(Res.string.male),
                        colorFilter = ColorFilter.tint(
                            if (!sex.isFemale())
                                AppThemeColors.white
                            else
                                AppThemeColors.violet2
                        ),
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(Modifier.width(20.dp))
            }
            Break()
            AutoCompleteTextView(
                modifier = Modifier.fillMaxWidth(),
                query = center,
                enabled = formEnabled,
                queryLabel = stringResource(Res.string.settingsDefaultCenterLabel),
                onQueryChanged = onCenterChanged,
                predictions = centerList.filter(center),
                onClearClick = onCenterClear,
                onItemClick = onCenterPicked
            ) { center, index ->
                CenterSelectItem(center = center, previous = if (index > 0) centerList[index - 1] else null)
            }
            Break()
            OutlinedInput(
                text = starting.toString(),
                onValueChanged = {
                    onStartingChanged.invoke(it.toIntOrNull() ?: 0)
                },
                label = stringResource(Res.string.settingsStartingLabel),
                enabled = formEnabled,
                error = startingError,
                errorMessage = errorMessage(listOf(ProfileError.DATA)),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Break()
            Text(
                stringResource(Res.string.settingsReminderLabel),
                style = itemSubTitle()
            )
            Switch(
                checked = notification,
                onCheckedChange = onNotificationChanged,
                enabled = formEnabled,
                colors = AppThemeColors.switchColors()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                JustTextButton(
                    text = stringResource(Res.string.profileDeleteTitle),
                    onClicked = onProfileDelete,
                    enabled = formEnabled,
                    textStyle = itemDelete(),
                    textColor = AppThemeColors.alertRed
                )
                JustTextButton(
                    text = stringResource(Res.string.profilePasswordChangeTitle),
                    onClicked = onPasswordChange,
                    enabled = formEnabled
                )
            }
            Break()
            if (formEnabled) {
                SubmitButton(
                    onClick = onProfileSave,
                    text = stringResource(Res.string.profileDataSubmitButton),
                    enabled = formEnabled,
                )
            } else {
                FormProgress()
            }
            Spacer(Modifier.height(100.dp))
        }
    }
}