package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import co.touchlab.kermit.Logger
import mobi.cwiklinski.bloodline.common.event.SideEffects
import mobi.cwiklinski.bloodline.common.isValidEmail
import mobi.cwiklinski.bloodline.common.manager.CallbackManager
import mobi.cwiklinski.bloodline.data.Parcelize
import mobi.cwiklinski.bloodline.domain.Sex
import mobi.cwiklinski.bloodline.domain.model.Center
import mobi.cwiklinski.bloodline.domain.model.Profile
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.female
import mobi.cwiklinski.bloodline.resources.ic_sex_female
import mobi.cwiklinski.bloodline.resources.ic_sex_male
import mobi.cwiklinski.bloodline.resources.male
import mobi.cwiklinski.bloodline.resources.profileDataEmailError
import mobi.cwiklinski.bloodline.resources.profileDataEmailLabel
import mobi.cwiklinski.bloodline.resources.profileDataNameError
import mobi.cwiklinski.bloodline.resources.profileDataNameLabel
import mobi.cwiklinski.bloodline.resources.profileDataSubmitButton
import mobi.cwiklinski.bloodline.resources.profileUpdateError
import mobi.cwiklinski.bloodline.resources.settingsDefaultCenterLabel
import mobi.cwiklinski.bloodline.resources.settingsReminderLabel
import mobi.cwiklinski.bloodline.resources.settingsSexLabel
import mobi.cwiklinski.bloodline.resources.settingsStartingLabel
import mobi.cwiklinski.bloodline.resources.setupInformation
import mobi.cwiklinski.bloodline.resources.setupTitle
import mobi.cwiklinski.bloodline.resources.skip
import mobi.cwiklinski.bloodline.ui.model.SetupError
import mobi.cwiklinski.bloodline.ui.model.SetupScreenModel
import mobi.cwiklinski.bloodline.ui.model.SetupState
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors.topBarColors
import mobi.cwiklinski.bloodline.ui.theme.contentText
import mobi.cwiklinski.bloodline.ui.theme.inputPlaceHolder
import mobi.cwiklinski.bloodline.ui.theme.itemSubTitle
import mobi.cwiklinski.bloodline.ui.theme.toolbarTitle
import mobi.cwiklinski.bloodline.ui.util.Avatar
import mobi.cwiklinski.bloodline.ui.util.avatarShadow
import mobi.cwiklinski.bloodline.ui.util.clearStack
import mobi.cwiklinski.bloodline.ui.util.filter
import mobi.cwiklinski.bloodline.ui.util.koinNavigatorScreenModel
import mobi.cwiklinski.bloodline.ui.widget.AutoCompleteTextView
import mobi.cwiklinski.bloodline.ui.widget.Break
import mobi.cwiklinski.bloodline.ui.widget.CenterSelectItem
import mobi.cwiklinski.bloodline.ui.widget.FormProgress
import mobi.cwiklinski.bloodline.ui.widget.MobileLayout
import mobi.cwiklinski.bloodline.ui.widget.OutlinedInput
import mobi.cwiklinski.bloodline.ui.widget.RichText
import mobi.cwiklinski.bloodline.ui.widget.SecondaryButton
import mobi.cwiklinski.bloodline.ui.widget.SubmitButton
import mobi.cwiklinski.bloodline.ui.widget.getAvatarName
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Parcelize
class SetupScreen : AppScreen() {

    @Composable
    override fun defaultView() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<SetupScreenModel>()
        val state by screenModel.state.collectAsStateWithLifecycle(SetupState.Loading)
        val behaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()
        when (state) {
            SetupState.Loading, SetupState.SavedData, SetupState.AlreadySetup -> {
                Logger.d("Redirecting to Home Screen")
                navigator.clearStack()
                navigator.replaceAll(HomeScreen())
                screenModel.resetState()
                Box(
                    modifier = Modifier.fillMaxSize()
                        .background(AppThemeColors.background),
                    contentAlignment = Alignment.Center
                ) {
                    FormProgress()
                }
            }
            else -> {
                MobileLayout(
                    modifier = Modifier
                        .nestedScroll(behaviour.nestedScrollConnection)
                        .drawBehind {
                            drawRect(
                                brush = AppThemeColors.homeGradient,
                                size = Size(width = 4000f, height = 400f)
                            )
                        },
                    backgroundColor = Color.Transparent,
                    topBar = @Composable {
                        CenterAlignedTopAppBar(
                            title = {
                                Text(
                                    stringResource(Res.string.setupTitle),
                                    style = toolbarTitle()
                                )
                            },
                            colors = topBarColors(),
                            scrollBehavior = behaviour
                        )
                    }
                ) { paddingValues ->
                    Box(
                        modifier = Modifier.background(AppThemeColors.homeGradient)
                    ) {
                        InternalSetupView(paddingValues)
                    }
                }
            }
        }
    }

    @Composable
    override fun tabletView() {
        portraitPhoneView()
    }

    @Composable
    fun getError(error: SetupError) = stringResource(
        when (error) {
            SetupError.NAME -> Res.string.profileDataNameError
            SetupError.EMAIL -> Res.string.profileDataEmailError
            else -> Res.string.profileUpdateError
        }
    )

    @Composable
    fun InternalSetupView(paddingValues: PaddingValues) {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<SetupScreenModel>()
        val centerList by screenModel.centers.collectAsStateWithLifecycle(emptyList())
        val state by screenModel.state.collectAsStateWithLifecycle(SetupState.Idle)
        val profile by screenModel.profile.collectAsStateWithLifecycle(Profile(""))
        val emailString by screenModel.email.collectAsStateWithLifecycle("")
        var name by remember { mutableStateOf("") }
        var sex by remember { mutableStateOf(Sex.MALE) }
        var starting by remember { mutableStateOf(0) }
        var center by remember { mutableStateOf<Center?>(null) }
        var query by remember { mutableStateOf("") }
        var notification by remember { mutableStateOf(false) }
        var email by remember { mutableStateOf("") }
        var avatar by remember { mutableStateOf(Avatar.WIZARD) }
        if (emailString.isNotEmpty() && emailString.isValidEmail()) {
            email = emailString
        }
        if (profile.id?.isNotEmpty() == true) {
            name = profile.name
            if (profile.email.isNotEmpty() && profile.email.isValidEmail()) {
                email = profile.email
            }
            avatar = Avatar.byName(profile.avatar)
            sex = profile.sex
            notification = profile.notification
            starting = profile.starting
            if (center == null && profile.centerId.isNotEmpty()) {
                center = centerList.firstOrNull { it.id == profile.centerId }
                query = center?.toSelection() ?: ""
            }
        }
        if (state == SetupState.SavingData) {
            BasicAlertDialog(
                onDismissRequest = { }
            ) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .background(
                            AppThemeColors.white,
                            shape = RoundedCornerShape(20.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    FormProgress()
                }
            }
        }
        SetupView(
            paddingValues = paddingValues,
            formEnabled = state != SetupState.SavingData,
            name = name,
            onNameChanged = { newName ->
                name = newName
            },
            nameError = state is SetupState.Error && (state as SetupState.Error).error == SetupError.NAME,
            email = email,
            avatar = avatar,
            avatarName = { namedAvatar ->
                getAvatarName(namedAvatar)
            },
            isAvatarSelected = { toCheck ->
                avatar == toCheck
            },
            onAvatarSelected = { newAvatar ->
                avatar = newAvatar
            },
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
            errorText = { error ->
                getError(error)
            },
            onSkip = {
                navigator.replaceAll(HomeScreen())
            },
            onSubmit = {
                screenModel.onSetup(
                    name,
                    email,
                    avatar,
                    sex,
                    notification,
                    starting,
                    center
                )
            }
        )
    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SetupView(
    paddingValues: PaddingValues = PaddingValues(0.dp),
    formEnabled: Boolean = true,
    name: String = "",
    onNameChanged: (String) -> Unit = {},
    nameError: Boolean = false,
    email: String = "",
    avatar: Avatar = Avatar.FENIX,
    avatarName: @Composable (Avatar) -> String = { avatar.name },
    isAvatarSelected: (Avatar) -> Boolean = { false },
    onAvatarSelected: (Avatar) -> Unit = {},
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
    isError: Boolean = false,
    error: SetupError = SetupError.ERROR,
    errorText: @Composable (SetupError) -> String = { "" },
    onSkip: () -> Unit = {},
    onSubmit: () -> Unit = {}
) {
    val cellWidth = 124.dp
    val cellHeight = 120.dp
    val cellPadding = 10.dp
    val scrollState = rememberScrollState()
    if (isError) {
        koinInject<CallbackManager>().postSideEffect(SideEffects.ErrorSnackBar(errorText.invoke(error)))
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
            }
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            modifier = Modifier.wrapContentSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painterResource(avatar.icon),
                getAvatarName(avatar),
                modifier = Modifier
                    .width(184.dp)
                    .height(184.dp)
                    .avatarShadow()
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RichText(
                stringResource(Res.string.setupInformation),
                centered = true
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedInput(
                text = name,
                onValueChanged = onNameChanged,
                label = stringResource(Res.string.profileDataNameLabel),
                enabled = formEnabled,
                error = nameError,
                errorMessage = errorText(SetupError.NAME)
            )
            OutlinedInput(
                text = email,
                onValueChanged = { },
                label = stringResource(Res.string.profileDataEmailLabel),
                enabled = false,
                placeholder = {
                    Text(
                        "email",
                        style = inputPlaceHolder().copy(color = Color.Transparent)
                    )
                },
                readOnly = true
            )
            FlowRow(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalArrangement = Arrangement.Top,
                maxItemsInEachRow = 3
            ) {
                Avatar.entries.forEach { listAvatar ->
                    Column(
                        modifier = Modifier.padding(cellPadding)
                            .width(cellWidth - cellPadding.times(2))
                            .height(cellHeight - cellPadding.times(2)),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier.size(80.dp)
                                .selectable(
                                    selected = isAvatarSelected(listAvatar),
                                    interactionSource = MutableInteractionSource(),
                                    indication = null,
                                    onClick = {
                                        onAvatarSelected.invoke(listAvatar)
                                    },
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isAvatarSelected(listAvatar)) {
                                Canvas(
                                    modifier = Modifier.size(70.dp).offset(y = 1.dp)
                                ) {
                                    drawCircle(AppThemeColors.violet2)
                                }
                            }
                            Image(
                                painterResource(listAvatar.icon),
                                listAvatar.name,
                                modifier = Modifier.size(80.dp)
                            )
                        }
                        Text(
                            avatarName(listAvatar),
                            style = itemSubTitle(),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
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
                        painterResource(Res.drawable.ic_sex_female),
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
                        painterResource(Res.drawable.ic_sex_male),
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
                errorMessage = errorText(SetupError.ERROR),
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
            Break()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SecondaryButton(
                    text = stringResource(Res.string.skip),
                    onClick = onSkip
                )
                SubmitButton(
                    onClick = onSubmit,
                    text = stringResource(Res.string.profileDataSubmitButton),
                    enabled = formEnabled,
                )
            }
            Spacer(Modifier.height(100.dp))
        }
    }
}