package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import co.touchlab.kermit.Logger
import mobi.cwiklinski.bloodline.Constants
import mobi.cwiklinski.bloodline.analytics.api.TrackScreen
import mobi.cwiklinski.bloodline.common.event.SideEffects
import mobi.cwiklinski.bloodline.common.isAfter
import mobi.cwiklinski.bloodline.common.isValidPressure
import mobi.cwiklinski.bloodline.common.toMillis
import mobi.cwiklinski.bloodline.common.today
import mobi.cwiklinski.bloodline.data.IgnoredOnParcel
import mobi.cwiklinski.bloodline.data.Parcelize
import mobi.cwiklinski.bloodline.domain.DonationType
import mobi.cwiklinski.bloodline.domain.model.Center
import mobi.cwiklinski.bloodline.domain.model.Donation
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.donationEditInformationMessage
import mobi.cwiklinski.bloodline.resources.donationEditSubmit
import mobi.cwiklinski.bloodline.resources.donationEditTitle
import mobi.cwiklinski.bloodline.resources.donationNewInformationTitle
import mobi.cwiklinski.bloodline.resources.goBack
import mobi.cwiklinski.bloodline.ui.model.DonationError
import mobi.cwiklinski.bloodline.ui.model.DonationScreenModel
import mobi.cwiklinski.bloodline.ui.model.DonationState
import mobi.cwiklinski.bloodline.ui.util.koinNavigatorScreenModel
import mobi.cwiklinski.bloodline.ui.widget.FormProgress
import mobi.cwiklinski.bloodline.ui.widget.MobileLayoutWithTitle
import mobi.cwiklinski.bloodline.ui.widget.SubmitButton
import mobi.cwiklinski.bloodline.ui.widget.toLocalDate
import org.jetbrains.compose.resources.stringResource

@Parcelize
class EditDonationScreen(
    val donation: Donation,
    override val key: ScreenKey = donation.id
) : AppDonationScreen() {

    @IgnoredOnParcel
    override val supportDialogs = false

    @Composable
    override fun defaultView() {
        val navigator = LocalNavigator.currentOrThrow
        val focusManager = LocalFocusManager.current
        val screenModel = navigator.koinNavigatorScreenModel<DonationScreenModel>()
        val state by screenModel.state.collectAsStateWithLifecycle(DonationState.Idle)
        val centerSearch by screenModel.query.collectAsStateWithLifecycle(donation.center.toSelection())
        val centerList by screenModel.filteredCenters.collectAsStateWithLifecycle(emptyList())
        TrackScreen(Constants.ANALYTICS_SCREEN_EDIT_DONATION)
        if (state == DonationState.Saved) {
            screenModel.clearError()
            screenModel.postSideEffect(
                SideEffects.InformationDialog(
                    title = stringResource(Res.string.donationNewInformationTitle),
                    message = stringResource(Res.string.donationEditInformationMessage),
                )
            )
            navigator.pop()
        }
        val calendarState = rememberDatePickerState(
            initialSelectedDateMillis = donation.date.toMillis(),
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
        var donationType by remember { mutableStateOf(donation.type) }
        var centerLabel by remember {
            mutableStateOf(
                donation.center.name
            )
        }
        var centerSelected by remember { mutableStateOf(donation.center) }
        var amountValue by remember { mutableStateOf(donation.amount) }
        var pressure by remember { mutableStateOf(if (donation.systolic > 0 && donation.diastolic > 0) "${donation.systolic}/${donation.diastolic}" else "") }
        var hemoglobinValue by remember { mutableStateOf(if (donation.hemoglobin > 0f) donation.hemoglobin.toString() else "") }
        var disqualificationValue by remember { mutableStateOf(if (donation.disqualification) 1 else 0) }
        centerSelected = donation.center
        centerLabel = donation.center.toSelection()
        screenModel.query.value = donation.center.toSelection()
        focusManager.clearFocus()
        MobileLayoutWithTitle(
            title = stringResource(Res.string.donationEditTitle),
            navigationIcon = {
                IconButton(onClick = { navigator.pop() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(Res.string.goBack)
                    )
                }
            }
        ) {
            EditDonationView(
                state = state,
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
                },
                updateDonation = {
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
                    screenModel.updateDonation(
                        id = donation.id,
                        amount = amountValue,
                        date = calendarState.selectedDateMillis.toLocalDate(),
                        center = centerSelected,
                        type = donationType.type,
                        hemoglobin = hemoglobin,
                        systolic = systolic,
                        diastolic = diastolic,
                        disqualification = disqualificationValue == 1
                    )
                }
            )
        }
    }
}

@Composable
fun EditDonationView(
    state: DonationState = DonationState.Idle,
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
    onDisqualificationChanged: (Boolean) -> Unit = {},
    updateDonation: () -> Unit = {}
) {
    val systemBarsPadding = WindowInsets.systemBars.asPaddingValues()
    val padding = systemBarsPadding.calculateTopPadding() * 2
    Column(
        modifier = Modifier.fillMaxWidth().wrapContentHeight()
            .padding(start = padding, end = padding)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NewDonationForm(
            formEnabled = formEnabled,
            donationType = donationType,
            onDonationTypeChanged = onDonationTypeChanged,
            centerList = centerList,
            centerSearch = centerSearch,
            onCenterSearchChanged = onCenterSearchChanged,
            onCenterSearchCleared = onCenterSearchCleared,
            onCenterSearchChosen = onCenterSearchChosen,
            centerErrorMessage = centerErrorMessage,
            amount = amount,
            onAmountChanged = onAmountChanged,
            isAmountError = isAmountError,
            amountErrorMessage = amountErrorMessage,
            calendarState = calendarState,
            isDateError = isDateError,
            dateErrorMessage = dateErrorMessage,
            hemoglobin = hemoglobin,
            onHemoglobinChanged = onHemoglobinChanged,
            isHemoglobinError = isHemoglobinError,
            hemoglobinErrorMessage = hemoglobinErrorMessage,
            pressure = pressure,
            onPressureChanged = onPressureChanged,
            isPressureError = isPressureError,
            pressureErrorMessage = pressureErrorMessage,
            disqualification = disqualification,
            onDisqualificationChanged = onDisqualificationChanged
        )
        if (state != DonationState.Saving) {
            SubmitButton(
                onClick = updateDonation,
                text = stringResource(Res.string.donationEditSubmit),
            )
        } else {
            FormProgress()
        }
    }
    Spacer(modifier = Modifier.height(40.dp))
}