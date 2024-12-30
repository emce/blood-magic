package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.icerock.moko.parcelize.Parcelize
import io.github.aakira.napier.Napier
import mobi.cwiklinski.bloodline.common.isAfter
import mobi.cwiklinski.bloodline.common.toMillis
import mobi.cwiklinski.bloodline.common.today
import mobi.cwiklinski.bloodline.domain.DonationType
import mobi.cwiklinski.bloodline.domain.model.Donation
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.donationEditInformationMessage
import mobi.cwiklinski.bloodline.resources.donationEditSubmit
import mobi.cwiklinski.bloodline.resources.donationEditTitle
import mobi.cwiklinski.bloodline.resources.donationNewAmountLabel
import mobi.cwiklinski.bloodline.resources.donationNewCenterLabel
import mobi.cwiklinski.bloodline.resources.donationNewDateLabel
import mobi.cwiklinski.bloodline.resources.donationNewDisqualificationLabel
import mobi.cwiklinski.bloodline.resources.donationNewInformationTitle
import mobi.cwiklinski.bloodline.resources.donationNewTypeLabel
import mobi.cwiklinski.bloodline.ui.event.SideEffects
import mobi.cwiklinski.bloodline.ui.model.DonationError
import mobi.cwiklinski.bloodline.ui.model.DonationScreenModel
import mobi.cwiklinski.bloodline.ui.model.DonationState
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.contentText
import mobi.cwiklinski.bloodline.ui.theme.contentTitle
import mobi.cwiklinski.bloodline.ui.util.koinNavigatorScreenModel
import mobi.cwiklinski.bloodline.ui.widget.AutoCompleteTextView
import mobi.cwiklinski.bloodline.ui.widget.CenterSelectItem
import mobi.cwiklinski.bloodline.ui.widget.CloseButton
import mobi.cwiklinski.bloodline.ui.widget.DateField
import mobi.cwiklinski.bloodline.ui.widget.DonationTypeItem
import mobi.cwiklinski.bloodline.ui.widget.FormProgress
import mobi.cwiklinski.bloodline.ui.widget.OutlinedInput
import mobi.cwiklinski.bloodline.ui.widget.SelectView
import mobi.cwiklinski.bloodline.ui.widget.SubmitButton
import mobi.cwiklinski.bloodline.ui.widget.getIcon
import mobi.cwiklinski.bloodline.ui.widget.getName
import mobi.cwiklinski.bloodline.ui.widget.toLocalDate
import org.jetbrains.compose.resources.stringResource

@Parcelize
class EditDonationScreen(
    val donation: Donation,
    override val key: ScreenKey = donation.id
) : AppDonationScreen() {

    override val supportDialogs = false

    @Composable
    override fun defaultView() {
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val focusManager = LocalFocusManager.current
        val screenModel = navigator.koinNavigatorScreenModel<DonationScreenModel>()
        val state by screenModel.state.collectAsStateWithLifecycle(DonationState.Idle)
        val centerSearch by screenModel.query.collectAsStateWithLifecycle(donation.center.toSelection())
        val centerList by screenModel.filteredCenters.collectAsStateWithLifecycle(emptyList())
        if (state == DonationState.Saved) {
            screenModel.clearError()
            screenModel.postSideEffect(
                SideEffects.InformationDialog(
                    title = stringResource(Res.string.donationNewInformationTitle),
                    message = stringResource(Res.string.donationEditInformationMessage),
                )
            )
            bottomSheetNavigator.hide()
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
        var amountLabel by remember { mutableStateOf(donation.amount.toString()) }
        var amountValue by remember { mutableStateOf(donation.amount.toString()) }
        var disqualificationValue by remember { mutableStateOf(if (donation.disqualification) 1 else 0) }
        centerSelected = donation.center
        centerLabel = donation.center.toSelection()
        screenModel.query.value = donation.center.toSelection()
        focusManager.clearFocus()
        BottomSheetScaffold(
            sheetGesturesEnabled = false,
            sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            sheetBackgroundColor = AppThemeColors.white,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(Res.string.donationEditTitle),
                            style = contentTitle(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    actions = {
                        CloseButton {
                            bottomSheetNavigator.hide()
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = AppThemeColors.modalHeader,
                        titleContentColor = AppThemeColors.white,
                        actionIconContentColor = AppThemeColors.white,
                    )
                )
            },
            sheetPeekHeight = 100.dp,
            sheetContent = {
                Column(
                    modifier = Modifier.fillMaxWidth().height(100.dp).background(AppThemeColors.modalHeader).padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (state != DonationState.Saving) {
                        SubmitButton(
                            onClick = {
                                screenModel.updateDonation(
                                    id = donation.id,
                                    amount = amountValue.toInt(),
                                    date = calendarState.selectedDateMillis.toLocalDate(),
                                    center = centerSelected,
                                    type = donationType.type,
                                    disqualification = disqualificationValue == 1
                                )
                            },
                            text = stringResource(Res.string.donationEditSubmit),
                        )
                    } else {
                        FormProgress()
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
            },

            ) {
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
                    onSelectionChanged = {
                        donationType = it
                    },
                    enabled = state != DonationState.Saving,
                ) {
                    DonationTypeItem(it)
                }
                AutoCompleteTextView(
                    modifier = Modifier.fillMaxWidth(),
                    query = centerSearch,
                    enabled = state != DonationState.Saving,
                    queryLabel = stringResource(Res.string.donationNewCenterLabel),
                    onQueryChanged = { updatedSymbol ->
                        screenModel.clearError()
                        screenModel.query.value = updatedSymbol
                    },
                    predictions = centerList,
                    onClearClick = {
                        screenModel.query.value = ""
                    },
                    onItemClick = {
                        centerSelected = it
                        centerLabel = it.toSelection()
                        screenModel.query.value = it.toSelection()
                        focusManager.clearFocus()
                    },
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                    errorMessage = if (state is DonationState.Error && (state as DonationState.Error).error == DonationError.CENTER_ERROR) getError(
                        (state as DonationState.Error).error
                    ) else null
                ) { center, index ->
                    CenterSelectItem(
                        center = center,
                        previous = if (index > 0) centerList[index - 1] else null
                    )
                }
                OutlinedInput(
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    text = amountLabel,
                    label = stringResource(Res.string.donationNewAmountLabel),
                    error = state is DonationState.Error && (state as DonationState.Error).error == DonationError.AMOUNT_ERROR,
                    errorMessage = getError(DonationError.AMOUNT_ERROR),
                    onValueChanged = {
                        screenModel.clearError()
                        try {
                            val amount = it.toInt()
                            amountLabel = amount.toString()
                            amountValue = amount.toString()
                        } catch (e: Exception) {
                            Napier.d("Error parsing amount for: $it")
                        }
                    },
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    )
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    stringResource(Res.string.donationNewDateLabel),
                    style = contentText()
                )
                DateField(
                    modifier = Modifier.fillMaxWidth(),
                    calendarState,
                    error = state is DonationState.Error && (state as DonationState.Error).error == DonationError.DATE_IN_FUTURE_ERROR,
                    errorMessage = getError(DonationError.DATE_IN_FUTURE_ERROR),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Checkbox(
                        checked = disqualificationValue == 1,
                        onCheckedChange = { isChecked ->
                            disqualificationValue = if (isChecked) 1 else 0
                        },
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
                Spacer(modifier = Modifier.height(105.dp))
            }
        }
    }
}