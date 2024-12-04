package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DatePicker
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
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.aakira.napier.Napier
import kotlinx.datetime.Clock
import mobi.cwiklinski.bloodline.common.isAfter
import mobi.cwiklinski.bloodline.common.today
import mobi.cwiklinski.bloodline.domain.DonationType
import mobi.cwiklinski.bloodline.domain.model.Profile
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.close
import mobi.cwiklinski.bloodline.resources.donationNewAmountError
import mobi.cwiklinski.bloodline.resources.donationNewAmountLabel
import mobi.cwiklinski.bloodline.resources.donationNewCenterError
import mobi.cwiklinski.bloodline.resources.donationNewCenterLabel
import mobi.cwiklinski.bloodline.resources.donationNewDateError
import mobi.cwiklinski.bloodline.resources.donationNewDateLabel
import mobi.cwiklinski.bloodline.resources.donationNewDisqualificationLabel
import mobi.cwiklinski.bloodline.resources.donationNewError
import mobi.cwiklinski.bloodline.resources.donationNewSubmit
import mobi.cwiklinski.bloodline.resources.donationNewTitle
import mobi.cwiklinski.bloodline.resources.donationNewTypeError
import mobi.cwiklinski.bloodline.resources.donationNewTypeLabel
import mobi.cwiklinski.bloodline.resources.icon_close
import mobi.cwiklinski.bloodline.ui.model.DonationError
import mobi.cwiklinski.bloodline.ui.model.DonationScreenModel
import mobi.cwiklinski.bloodline.ui.model.DonationState
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors.calendarPickerColors
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors.textButtonColors
import mobi.cwiklinski.bloodline.ui.theme.contentText
import mobi.cwiklinski.bloodline.ui.theme.contentTitle
import mobi.cwiklinski.bloodline.ui.widget.AutoCompleteTextView
import mobi.cwiklinski.bloodline.ui.widget.CenterSelectItem
import mobi.cwiklinski.bloodline.ui.widget.DonationTypeItem
import mobi.cwiklinski.bloodline.ui.widget.FormProgress
import mobi.cwiklinski.bloodline.ui.widget.OutlinedInput
import mobi.cwiklinski.bloodline.ui.widget.SelectView
import mobi.cwiklinski.bloodline.ui.widget.SubmitButton
import mobi.cwiklinski.bloodline.ui.widget.getIcon
import mobi.cwiklinski.bloodline.ui.widget.getName
import mobi.cwiklinski.bloodline.ui.widget.toLocalDate
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class NewDonationScreen(
    override val key: ScreenKey = Clock.System.now().toEpochMilliseconds().toString()
) : AppScreen() {

    @Composable
    override fun verticalView() {
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val focusManager = LocalFocusManager.current
        val screenModel = navigator.koinNavigatorScreenModel<DonationScreenModel>()
        val state by screenModel.state.collectAsStateWithLifecycle(DonationState.Idle)
        val profile by screenModel.profile.collectAsStateWithLifecycle(Profile(""))
        val centerSearch by screenModel.query.collectAsStateWithLifecycle("")
        val centerList by screenModel.filteredCenters.collectAsStateWithLifecycle(emptyList())
        if (state == DonationState.Saved) {
            bottomSheetNavigator.pop()
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
        var centerLabel by remember {
            mutableStateOf(
                centerList.firstOrNull { it.id == profile.centerId }?.name ?: ""
            )
        }
        var centerSelected by remember { mutableStateOf(centerList.firstOrNull { it.id == profile.centerId }) }
        var amountLabel by remember { mutableStateOf("") }
        var amountValue by remember { mutableStateOf(0) }
        var disqualificationValue by remember { mutableStateOf(0) }
        BottomSheetScaffold(
            modifier = Modifier
                .padding(top = 10.dp),
            sheetGesturesEnabled = false,
            sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            sheetBackgroundColor = AppThemeColors.white,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(Res.string.donationNewTitle),
                            style = contentTitle(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    modifier = Modifier,
                    actions = {
                        Button(
                            onClick = {
                                screenModel.query.value = ""
                                amountValue = 0
                                amountLabel = ""
                                donationType = DonationType.FULL_BLOOD
                                disqualificationValue = 0
                                centerSelected = null
                                bottomSheetNavigator.hide()
                            },
                            colors = textButtonColors()
                        ) {
                            Image(
                                painterResource(Res.drawable.icon_close),
                                stringResource(Res.string.close)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = AppThemeColors.white,
                        titleContentColor = AppThemeColors.white,
                        actionIconContentColor = AppThemeColors.white,
                    )
                )
            },
            sheetPeekHeight = 100.dp,
            sheetContent = {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (state != DonationState.Saving) {
                        SubmitButton(
                            onClick = {
                                screenModel.addDonation(
                                    amount = amountValue,
                                    date = (calendarState.selectedDateMillis ?: 0).toLocalDate(),
                                    center = centerSelected,
                                    type = donationType.type,
                                    disqualification = disqualificationValue == 1
                                )
                            },
                            text = stringResource(Res.string.donationNewSubmit),
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
                    )
                ) { center, index ->
                    CenterSelectItem(center = center, previous = if (index > 0) centerList[index - 1] else null)
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
                            amountValue = amount
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
                DatePicker(
                    state = calendarState,
                    title = null,
                    showModeToggle = false,
                    headline = null,
                    colors = calendarPickerColors(),
                    modifier = Modifier.padding(16.dp)
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

    @Composable
    override fun horizontalView() {
        verticalView()
    }

    @Composable
    fun getError(error: DonationError) = stringResource(
        when (error) {
            DonationError.DATE_IN_FUTURE_ERROR -> Res.string.donationNewDateError
            DonationError.AMOUNT_ERROR -> Res.string.donationNewAmountError
            DonationError.CENTER_ERROR -> Res.string.donationNewCenterError
            DonationError.TYPE_ERROR -> Res.string.donationNewTypeError
            else -> Res.string.donationNewError
        }
    )
}