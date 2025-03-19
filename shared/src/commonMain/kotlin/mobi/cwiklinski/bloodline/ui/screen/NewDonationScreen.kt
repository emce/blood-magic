package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import co.touchlab.kermit.Logger
import kotlinx.datetime.Clock
import mobi.cwiklinski.bloodline.Constants
import mobi.cwiklinski.bloodline.analytics.api.TrackScreen
import mobi.cwiklinski.bloodline.common.event.SideEffects
import mobi.cwiklinski.bloodline.common.isAfter
import mobi.cwiklinski.bloodline.common.isValidPressure
import mobi.cwiklinski.bloodline.common.today
import mobi.cwiklinski.bloodline.data.IgnoredOnParcel
import mobi.cwiklinski.bloodline.data.Parcelize
import mobi.cwiklinski.bloodline.domain.DonationType
import mobi.cwiklinski.bloodline.domain.model.Center
import mobi.cwiklinski.bloodline.domain.model.Profile
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.donationNewAmountLabel
import mobi.cwiklinski.bloodline.resources.donationNewCenterLabel
import mobi.cwiklinski.bloodline.resources.donationNewDisqualificationLabel
import mobi.cwiklinski.bloodline.resources.donationNewHemoglobin
import mobi.cwiklinski.bloodline.resources.donationNewInformationMessage
import mobi.cwiklinski.bloodline.resources.donationNewInformationTitle
import mobi.cwiklinski.bloodline.resources.donationNewPressure
import mobi.cwiklinski.bloodline.resources.donationNewSubmit
import mobi.cwiklinski.bloodline.resources.donationNewTitle
import mobi.cwiklinski.bloodline.resources.donationNewTypeLabel
import mobi.cwiklinski.bloodline.resources.goBack
import mobi.cwiklinski.bloodline.ui.model.DonationError
import mobi.cwiklinski.bloodline.ui.model.DonationScreenModel
import mobi.cwiklinski.bloodline.ui.model.DonationState
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.contentText
import mobi.cwiklinski.bloodline.ui.util.koinNavigatorScreenModel
import mobi.cwiklinski.bloodline.ui.widget.AutoCompleteTextView
import mobi.cwiklinski.bloodline.ui.widget.CenterSelectItem
import mobi.cwiklinski.bloodline.ui.widget.DateField
import mobi.cwiklinski.bloodline.ui.widget.DonationTypeItem
import mobi.cwiklinski.bloodline.ui.widget.FormProgress
import mobi.cwiklinski.bloodline.ui.widget.MobileLayoutWithTitle
import mobi.cwiklinski.bloodline.ui.widget.OutlinedInput
import mobi.cwiklinski.bloodline.ui.widget.SelectView
import mobi.cwiklinski.bloodline.ui.widget.SubmitButton
import mobi.cwiklinski.bloodline.ui.widget.getIcon
import mobi.cwiklinski.bloodline.ui.widget.getName
import mobi.cwiklinski.bloodline.ui.widget.toLocalDate
import org.jetbrains.compose.resources.stringResource

@Parcelize
class NewDonationScreen(
    override val key: ScreenKey = Clock.System.now().toEpochMilliseconds().toString()
) : AppDonationScreen() {

    @IgnoredOnParcel
    override val supportDialogs = false

    @Composable
    override fun defaultView() {
        val navigator = LocalNavigator.currentOrThrow
        val focusManager = LocalFocusManager.current
        val screenModel = navigator.koinNavigatorScreenModel<DonationScreenModel>()
        val state by screenModel.state.collectAsStateWithLifecycle(DonationState.Idle)
        val profile by screenModel.profile.collectAsStateWithLifecycle(Profile(""))
        val centerSearch by screenModel.query.collectAsStateWithLifecycle("")
        val centerList by screenModel.filteredCenters.collectAsStateWithLifecycle(emptyList())
        if (state == DonationState.Saved) {
            screenModel.clearError()
            screenModel.postSideEffect(
                SideEffects.InformationDialog(
                    title = stringResource(Res.string.donationNewInformationTitle),
                    message = stringResource(Res.string.donationNewInformationMessage),
                )
            )
            navigator.pop()
        }
        val calendarState = rememberDatePickerState(
            initialSelectedDateMillis = Clock.System.now().toEpochMilliseconds(),
            initialDisplayMode = DisplayMode.Picker,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return !utcTimeMillis.toLocalDate().isAfter(today())
                }

                override fun isSelectableYear(year: Int): Boolean {
                    return year <= today().year
                }
            }
        )
        var donationType by remember { mutableStateOf(DonationType.FULL_BLOOD) }
        var centerLoaded by remember { mutableStateOf(false) }
        var centerLabel by remember { mutableStateOf("") }
        var centerSelected by remember { mutableStateOf<Center?>(null) }
        var amountValue by remember { mutableStateOf(0) }
        var pressure by remember { mutableStateOf("") }
        var hemoglobinValue by remember { mutableStateOf("") }
        var disqualificationValue by remember { mutableStateOf(0) }
        if (profile.centerId.isNotEmpty() && !centerLoaded) {
            centerList.firstOrNull { it.id == profile.centerId }?.let {
                screenModel.query.value = it.toSelection()
                centerLabel = it.toSelection()
                centerSelected = it
                centerLoaded = true
            }
        }
        MobileLayoutWithTitle(
            title = stringResource(Res.string.donationNewTitle),
            navigationIcon = {
                IconButton(onClick = { navigator.pop() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(Res.string.goBack)
                    )
                }
            }
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NewDonationForm(
                    formEnabled = state != DonationState.Saving,
                    donationType = donationType,
                    onDonationTypeChanged = { newType ->
                        donationType = newType
                    },
                    centerList = centerList,
                    centerSearch = centerSearch,
                    onCenterSearchChanged = { updatedSymbol ->
                        screenModel.clearError()
                        screenModel.query.value = updatedSymbol
                    },
                    onCenterSearchCleared = {
                        screenModel.query.value = ""
                    },
                    onCenterSearchChosen = {
                        centerSelected = it
                        centerLabel = it.toSelection()
                        screenModel.query.value = it.toSelection()
                        focusManager.clearFocus()
                    },
                    centerErrorMessage = if (state is DonationState.Error && (state as DonationState.Error).error == DonationError.CENTER_ERROR) getError(
                        (state as DonationState.Error).error
                    ) else null,
                    amount = amountValue.toString(),
                    onAmountChanged = {
                        screenModel.clearError()
                        try {
                            val amount = it.toInt()
                            amountValue = amount
                        } catch (e: Exception) {
                            Logger.d("Error parsing amount for: $it")
                        }
                    },
                    isAmountError = state is DonationState.Error && (state as DonationState.Error).error == DonationError.AMOUNT_ERROR,
                    amountErrorMessage = getError(DonationError.AMOUNT_ERROR),
                    calendarState = calendarState,
                    isDateError = state is DonationState.Error && (state as DonationState.Error).error == DonationError.DATE_IN_FUTURE_ERROR,
                    dateErrorMessage = getError(DonationError.DATE_IN_FUTURE_ERROR),
                    hemoglobin = hemoglobinValue.toString(),
                    onHemoglobinChanged = {
                        screenModel.clearError()
                        hemoglobinValue = it
                    },
                    isHemoglobinError = state is DonationState.Error && (state as DonationState.Error).error == DonationError.HEMOGLOBIN_ERROR,
                    hemoglobinErrorMessage = getError(DonationError.HEMOGLOBIN_ERROR),
                    pressure = pressure,
                    onPressureChanged = {
                        screenModel.clearError()
                        pressure = it
                    },
                    isPressureError = state is DonationState.Error && (state as DonationState.Error).error == DonationError.PRESSURE_ERROR,
                    pressureErrorMessage = getError(DonationError.PRESSURE_ERROR),
                    disqualification = disqualificationValue,
                    onDisqualificationChanged = { isChecked ->
                        disqualificationValue = if (isChecked) 1 else 0
                    }
                )
                if (state != DonationState.Saving) {
                    SubmitButton(
                        onClick = {
                            var systolic = 0
                            var diastolic = 0
                            var hemoglobin = 0f
                            try {
                                if (pressure.isValidPressure()) {
                                    val pressures = pressure.split("/")
                                    systolic = pressures.first().toInt()
                                    diastolic = pressures.last().toInt()
                                }
                            } catch (e: NumberFormatException) {
                                Logger.e("Parsing pressure", e)
                            }
                            try {
                                hemoglobin = hemoglobinValue.toFloat()
                            } catch (e: NumberFormatException) {
                                Logger.e("Parsing hemoglobin", e)
                            }
                            screenModel.addDonation(
                                amount = amountValue,
                                date = (calendarState.selectedDateMillis ?: 0).toLocalDate(),
                                center = centerSelected,
                                type = donationType.type,
                                hemoglobin = hemoglobin,
                                systolic = systolic,
                                diastolic = diastolic,
                                disqualification = disqualificationValue == 1
                            )
                        },
                        text = stringResource(Res.string.donationNewSubmit)
                    )
                } else {
                    FormProgress()
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun NewDonationForm(
    formEnabled: Boolean = true,
    donationType: DonationType = DonationType.FULL_BLOOD,
    onDonationTypeChanged: (DonationType) -> Unit = {},
    centerList: List<Center> = emptyList(),
    centerSearch: String = "",
    onCenterSearchChanged: (String) -> Unit = {},
    onCenterSearchCleared: () -> Unit = {},
    onCenterSearchChosen: (Center) -> Unit = {},
    centerErrorMessage: String? = null,
    amount: String = "",
    onAmountChanged: (String) -> Unit = {},
    isAmountError: Boolean = false,
    amountErrorMessage: String = "",
    calendarState: DatePickerState = rememberDatePickerState(),
    isDateError: Boolean = false,
    dateErrorMessage: String = "",
    hemoglobin: String = "",
    onHemoglobinChanged: (String) -> Unit = {},
    isHemoglobinError: Boolean = false,
    hemoglobinErrorMessage: String = "",
    pressure: String = "",
    onPressureChanged: (String) -> Unit = {},
    isPressureError: Boolean = false,
    pressureErrorMessage: String = "",
    disqualification: Int = 0,
    onDisqualificationChanged: (Boolean) -> Unit = {}
) {
    TrackScreen(Constants.ANALYTICS_SCREEN_NEW_DONATION)
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Bottom,
    ) {
        SelectView(
            modifier = Modifier.fillMaxWidth(),
            itemList = DonationType.entries.toList(),
            text = donationType.getName(),
            label = stringResource(Res.string.donationNewTypeLabel),
            icon = donationType.getIcon(),
            onSelectionChanged = onDonationTypeChanged,
            enabled = formEnabled,
        ) {
            DonationTypeItem(it)
        }
        AutoCompleteTextView(
            modifier = Modifier.fillMaxWidth(),
            query = centerSearch,
            enabled = formEnabled,
            queryLabel = stringResource(Res.string.donationNewCenterLabel),
            onQueryChanged = onCenterSearchChanged,
            predictions = centerList,
            onClearClick = onCenterSearchCleared,
            onItemClick = onCenterSearchChosen,
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            errorMessage = centerErrorMessage
        ) { center, index ->
            CenterSelectItem(
                center = center,
                previous = if (index > 0) centerList[index - 1] else null
            )
        }
        OutlinedInput(
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            text = amount,
            label = stringResource(Res.string.donationNewAmountLabel),
            error = isAmountError,
            errorMessage = amountErrorMessage,
            onValueChanged = onAmountChanged,
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        DateField(
            modifier = Modifier.fillMaxWidth(),
            calendarState,
            error = isDateError,
            errorMessage = dateErrorMessage,
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedInput(
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            text = hemoglobin,
            label = stringResource(Res.string.donationNewHemoglobin),
            error = isHemoglobinError,
            errorMessage = hemoglobinErrorMessage,
            onValueChanged = onHemoglobinChanged,
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedInput(
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            text = pressure,
            label = stringResource(Res.string.donationNewPressure),
            error = isPressureError,
            errorMessage = pressureErrorMessage,
            onValueChanged = onPressureChanged,
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Checkbox(
                checked = disqualification == 1,
                onCheckedChange = onDisqualificationChanged,
                colors = CheckboxDefaults.colors(
                    checkedColor = AppThemeColors.rose1,
                    uncheckedColor = AppThemeColors.rose3
                )
            )
            Text(
                stringResource(Res.string.donationNewDisqualificationLabel),
                style = contentText(),
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}