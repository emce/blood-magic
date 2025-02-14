package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.Text
import androidx.compose.material3.DatePickerState
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
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import mobi.cwiklinski.bloodline.data.IgnoredOnParcel
import mobi.cwiklinski.bloodline.data.Parcelize
import mobi.cwiklinski.bloodline.domain.NotificationType
import mobi.cwiklinski.bloodline.ui.model.NotificationError
import mobi.cwiklinski.bloodline.ui.model.NotificationScreenModel
import mobi.cwiklinski.bloodline.ui.model.NotificationState
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.itemSubTitle
import mobi.cwiklinski.bloodline.ui.util.koinNavigatorScreenModel
import mobi.cwiklinski.bloodline.ui.widget.CloseButton
import mobi.cwiklinski.bloodline.ui.widget.DateField
import mobi.cwiklinski.bloodline.ui.widget.FormProgress
import mobi.cwiklinski.bloodline.ui.widget.MobileTitleBar
import mobi.cwiklinski.bloodline.ui.widget.OutlinedInput
import mobi.cwiklinski.bloodline.ui.widget.SelectView
import mobi.cwiklinski.bloodline.ui.widget.SubmitButton
import mobi.cwiklinski.bloodline.ui.widget.camelCase
import mobi.cwiklinski.bloodline.ui.widget.toLocalDate
import org.jetbrains.compose.ui.tooling.preview.Preview

@Parcelize
class AdminNotificationNewScreen : AppScreen() {

    @IgnoredOnParcel
    override val supportDialogs = false

    @Composable
    override fun defaultView() {
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val screenModel = navigator.koinNavigatorScreenModel<NotificationScreenModel>()
        val state by screenModel.state.collectAsStateWithLifecycle(NotificationState.Idle)
        val calendarState = rememberDatePickerState()
        var title by remember { mutableStateOf("") }
        val message = rememberRichTextState()
        var location by remember { mutableStateOf("") }
        var type by remember { mutableStateOf(NotificationType.STANDARD) }
        BottomSheetScaffold(
            sheetGesturesEnabled = false,
            sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            sheetBackgroundColor = AppThemeColors.white,
            topBar = {
                MobileTitleBar(
                    title = "Nowa notyfikacja",
                    actions = {
                        CloseButton {
                            bottomSheetNavigator.hide()
                        }
                    }
                )
            },
            sheetPeekHeight = 100.dp,
            sheetElevation = 16.dp,
            sheetContent = {
                Column(
                    modifier = Modifier.fillMaxWidth().height(100.dp).background(AppThemeColors.modalHeader).padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (state != NotificationState.Saving) {
                        SubmitButton(
                            onClick = {
                                screenModel.addNotification(
                                    date = (calendarState.selectedDateMillis ?: 0).toLocalDate(),
                                    title = title,
                                    message = message.toMarkdown(),
                                    type = type,
                                    location = location
                                )
                            },
                            text = "Dodaj nowtyfikację",
                        )
                    } else {
                        FormProgress()
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
            },
        ) {
            AdminNotificationForm(
                formEnabled = state != NotificationState.Saving,
                title = title,
                titleError = state is NotificationState.Error && (state as NotificationState.Error).error == NotificationError.TITLE_ERROR,
                titleErrorMessage = "Nieprawidłowy tytuł",
                onTitleChanged = { newTitle ->
                    title = newTitle
                },
                messageState = message,
                messageError = state is NotificationState.Error && (state as NotificationState.Error).error == NotificationError.MESSAGE_ERROR,
                calendarState = calendarState,
                isDateError = state is NotificationState.Error && (state as NotificationState.Error).error == NotificationError.DATE_IN_FUTURE_ERROR,
                dateErrorMessage = "Nieprawidłowa data",
                type = type,
                onTypeChanged = { newType ->
                    type = newType
                },
                location = location,
                locationError = state is NotificationState.Error && (state as NotificationState.Error).error == NotificationError.NO_LOCATION_ERROR,
                locationErrorMessage = "Nieprawidłowy zasięg",
                onLocationChanged = { newLocation ->
                    location = newLocation
                },
            )
        }
    }
}


@Preview
@Composable
fun AdminNotificationForm(
    paddingValues: PaddingValues = PaddingValues(0.dp),
    formEnabled: Boolean = true,
    title: String = "",
    titleError: Boolean = false,
    titleErrorMessage: String = "",
    onTitleChanged: (String) -> Unit = {},
    messageState: RichTextState = rememberRichTextState(),
    messageError: Boolean = false,
    calendarState: DatePickerState = rememberDatePickerState(),
    isDateError: Boolean = false,
    dateErrorMessage: String = "",
    type: NotificationType = NotificationType.STANDARD,
    onTypeChanged: (NotificationType) -> Unit = {},
    location: String = "",
    locationError: Boolean = false,
    locationErrorMessage: String = "",
    onLocationChanged: (String) -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(paddingValues)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Bottom,
    ) {
        OutlinedInput(
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            text = title,
            label = "Tytuł notyfikacji",
            error = titleError,
            errorMessage = titleErrorMessage,
            enabled = formEnabled,
            onValueChanged = onTitleChanged,
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        RichTextEditor(
            state = messageState,
            modifier = Modifier
                .fillMaxWidth(),
            label = {
                Text(
                    "Treść notyfikacji",
                    style = itemSubTitle()
                )
            },
            enabled = formEnabled,
            isError = messageError,
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
        SelectView(
            modifier = Modifier.fillMaxWidth(),
            itemList = NotificationType.entries.toList(),
            text = type.name.camelCase(),
            label = "Typ notyfikacji",
            onSelectionChanged = onTypeChanged,
            enabled = formEnabled,
        ) {
            Text(type.name.camelCase())
        }
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedInput(
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            text = location,
            label = "Zasięg notyfikacji",
            error = locationError,
            errorMessage = locationErrorMessage,
            enabled = formEnabled,
            onValueChanged = onLocationChanged,
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}