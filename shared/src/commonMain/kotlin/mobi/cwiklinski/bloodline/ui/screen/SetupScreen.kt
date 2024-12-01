package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import mobi.cwiklinski.bloodline.common.isValidEmail
import mobi.cwiklinski.bloodline.data.filed.DummyData
import mobi.cwiklinski.bloodline.domain.Sex
import mobi.cwiklinski.bloodline.domain.model.Center
import mobi.cwiklinski.bloodline.domain.model.Profile
import mobi.cwiklinski.bloodline.getScreenWidth
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
import mobi.cwiklinski.bloodline.ui.theme.contentText
import mobi.cwiklinski.bloodline.ui.theme.inputPlaceHolder
import mobi.cwiklinski.bloodline.ui.theme.itemSubTitle
import mobi.cwiklinski.bloodline.ui.theme.toolbarTitle
import mobi.cwiklinski.bloodline.ui.util.Avatar
import mobi.cwiklinski.bloodline.ui.util.avatarShadow
import mobi.cwiklinski.bloodline.ui.util.filter
import mobi.cwiklinski.bloodline.ui.widget.AutoCompleteTextView
import mobi.cwiklinski.bloodline.ui.widget.Break
import mobi.cwiklinski.bloodline.ui.widget.CenterSelectItem
import mobi.cwiklinski.bloodline.ui.widget.FormProgress
import mobi.cwiklinski.bloodline.ui.widget.OutlinedInput
import mobi.cwiklinski.bloodline.ui.widget.SecondaryButton
import mobi.cwiklinski.bloodline.ui.widget.SubmitButton
import mobi.cwiklinski.bloodline.ui.widget.camelCase
import mobi.cwiklinski.bloodline.ui.widget.getAvatarName
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

class SetupScreen : AppScreen() {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<SetupScreenModel>()
        val centerList by screenModel.centers.collectAsStateWithLifecycle()
        val profile by screenModel.profile.collectAsStateWithLifecycle(Profile(""))
        val state by screenModel.state.collectAsStateWithLifecycle()
        val email by screenModel.email.collectAsStateWithLifecycle()
        if (state == SetupState.SavedData || state == SetupState.AlreadySetup) {
            navigator.replaceAll(HomeScreen())
        }
        if (state == SetupState.Loading) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(AppThemeColors.background),
                contentAlignment = Alignment.Center
            ) {
                FormProgress()
            }
        } else {
            SetupView(screenModel, profile, centerList, state, email)
        }
    }

    @Preview
    @Composable
    fun SetupView(
        screenModel: SetupScreenModel,
        profile: Profile = DummyData.generateProfile(),
        centerList: List<Center> = DummyData.CENTERS,
        state: SetupState = SetupState.Idle,
        storedEmail: String = DummyData.ACCOUNTS.first().first
    ) {
        val avatarSize = 75.dp
        val navigator = LocalNavigator.currentOrThrow
        val behaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()
        var name by remember { mutableStateOf(profile.name) }
        var sex by remember { mutableStateOf(profile.sex) }
        var starting by remember { mutableStateOf(profile.starting) }
        var center by remember { mutableStateOf(centerList.firstOrNull { it.id == profile.centerId }) }
        var query by remember {
            mutableStateOf(
                centerList.firstOrNull { it.id == profile.centerId }?.toSelection() ?: ""
            )
        }
        var notification by remember { mutableStateOf(profile.notification) }
        var email by remember { mutableStateOf(profile.email.ifEmpty { storedEmail }) }
        var avatar by remember { mutableStateOf(Avatar.valueOf(profile.avatar)) }
        val scrollState = rememberScrollState()
        screenModel.screenModelScope.launch {
            screenModel.profile.collectLatest { fetchedProfile ->
                name = fetchedProfile.name
                if (fetchedProfile.email.isNotEmpty() && fetchedProfile.email.isValidEmail()) {
                    email = fetchedProfile.email
                }
                avatar = Avatar.byName(fetchedProfile.avatar)
                sex = fetchedProfile.sex
                notification = fetchedProfile.notification
                starting = fetchedProfile.starting
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
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                stringResource(Res.string.setupTitle),
                                style = toolbarTitle()
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                            scrolledContainerColor = Color.Transparent,
                            titleContentColor = AppThemeColors.black,
                            navigationIconContentColor = AppThemeColors.black
                        ),
                        scrollBehavior = behaviour
                    )
                }
            ) { paddingValues ->
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
                            FormProgress(filter = ColorFilter.tint(AppThemeColors.red2))
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
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
                            getAvatarName(avatar),
                            modifier = Modifier
                                .width(184.dp)
                                .height(184.dp)
                                .avatarShadow()
                        )
                    }
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            stringResource(Res.string.setupInformation),
                            style = contentText().copy(
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier.padding(horizontal = getScreenWidth() / 8)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        OutlinedInput(
                            text = name,
                            onValueChanged = { name = it },
                            label = stringResource(Res.string.profileDataNameLabel),
                            enabled = state != SetupState.SavingData,
                            error = state is SetupState.Error
                                    && state.error == SetupError.NAME,
                            errorMessage = getError(SetupError.NAME)
                        )
                        Break()
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
                        Break()
                        Row(
                            modifier = Modifier.selectableGroup()
                                .height(140.dp).wrapContentWidth()
                                .horizontalScroll(rememberScrollState())
                                .padding(start = 20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Avatar.entries.forEach { item ->
                                ConstraintLayout(
                                    modifier = Modifier.wrapContentSize()
                                        .selectable(
                                            selected = (item == avatar),
                                            onClick = {
                                                avatar = item
                                            },
                                            role = Role.RadioButton
                                        )
                                        .padding(horizontal = 16.dp),
                                ) {
                                    val (avatarImage, radioButton, title) = createRefs()
                                    Image(
                                        painterResource(item.icon),
                                        getAvatarName(item),
                                        modifier = Modifier
                                            .size(avatarSize)
                                            .avatarShadow()
                                            .constrainAs(avatarImage) {
                                                top.linkTo(parent.top)
                                                centerHorizontallyTo(parent)
                                            }
                                    )
                                    RadioButton(
                                        selected = (item == avatar),
                                        onClick = null,
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = AppThemeColors.violet4.copy(alpha = 0.4f),
                                            unselectedColor = AppThemeColors.violet1.copy(alpha = 0.4f),
                                        ),
                                        modifier = Modifier
                                            .size(20.dp)
                                            .constrainAs(radioButton) {
                                                top.linkTo(avatarImage.top)
                                                centerHorizontallyTo(parent)
                                            }
                                    )
                                    Text(
                                        text = getAvatarName(item).camelCase(),
                                        style = itemSubTitle().copy(
                                            color = AppThemeColors.black70,
                                            textAlign = TextAlign.Center
                                        ),
                                        modifier = Modifier.constrainAs(title) {
                                            top.linkTo(avatarImage.bottom, 10.dp)
                                            bottom.linkTo(parent.bottom)
                                            centerHorizontallyTo(parent)
                                        }
                                    )
                                }
                            }
                        }
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
                            enabled = state != SetupState.SavingData,
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
                        ) { center, index ->
                            CenterSelectItem(center, if (index > 0) centerList[index - 1] else null)
                        }
                        Break()
                        OutlinedInput(
                            text = starting.toString(),
                            onValueChanged = {
                                starting = it.toIntOrNull() ?: 0
                            },
                            label = stringResource(Res.string.settingsStartingLabel),
                            enabled = state != SetupState.SavingData,
                            error = state is SetupState.Error
                                    && state.error == SetupError.CENTER,
                            errorMessage = getError(SetupError.CENTER),
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
                            enabled = state != SetupState.SavingData,
                            colors = AppThemeColors.switchColors()
                        )
                        Break()
                        if (state is SetupState.Error) {
                            Text(
                                getError(state.error),
                                style = contentText().copy(
                                    color = AppThemeColors.red2
                                )
                            )
                            Spacer(Modifier.height(30.dp))
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            SecondaryButton(
                                text = stringResource(Res.string.skip),
                                onClick = {
                                    navigator.replaceAll(HomeScreen())
                                }
                            )
                            SubmitButton(
                                onClick = {
                                    screenModel.onSetup(
                                        name,
                                        email,
                                        avatar,
                                        sex,
                                        notification,
                                        starting,
                                        center
                                    )
                                },
                                text = stringResource(Res.string.profileDataSubmitButton),
                                enabled = state != SetupState.SavingData,
                            )
                        }
                        Spacer(Modifier.height(100.dp))
                    }
                }
            }
        }
    }

    @Composable
    fun getError(error: SetupError) = stringResource(
        when (error) {
            SetupError.NAME -> Res.string.profileDataNameError
            SetupError.EMAIL -> Res.string.profileDataEmailError
            else -> Res.string.profileUpdateError
        }
    )

}