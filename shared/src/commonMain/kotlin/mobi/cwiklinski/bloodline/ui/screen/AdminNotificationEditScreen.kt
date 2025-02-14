package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import mobi.cwiklinski.bloodline.common.toMillis
import mobi.cwiklinski.bloodline.data.IgnoredOnParcel
import mobi.cwiklinski.bloodline.data.Parcelize
import mobi.cwiklinski.bloodline.domain.model.Notification
import mobi.cwiklinski.bloodline.ui.model.NotificationError
import mobi.cwiklinski.bloodline.ui.model.NotificationScreenModel
import mobi.cwiklinski.bloodline.ui.model.NotificationState
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.util.koinNavigatorScreenModel
import mobi.cwiklinski.bloodline.ui.widget.CloseButton
import mobi.cwiklinski.bloodline.ui.widget.FormProgress
import mobi.cwiklinski.bloodline.ui.widget.MobileTitleBar
import mobi.cwiklinski.bloodline.ui.widget.SubmitButton
import mobi.cwiklinski.bloodline.ui.widget.toLocalDate

@Parcelize
class AdminNotificationEditScreen(private val notification: Notification) : AppScreen() {

    @IgnoredOnParcel
    override val supportDialogs = false

    @Composable
    override fun defaultView() {
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val screenModel = navigator.koinNavigatorScreenModel<NotificationScreenModel>()
        val state by screenModel.state.collectAsStateWithLifecycle(NotificationState.Idle)
        val calendarState = rememberDatePickerState()
        calendarState.selectedDateMillis = notification.date.toMillis()
        var title by remember { mutableStateOf(notification.title) }
        val message = rememberRichTextState()
        message.setMarkdown(notification.message)
        var location by remember { mutableStateOf(notification.location) }
        var type by remember { mutableStateOf(notification.type) }
        BottomSheetScaffold(
            sheetGesturesEnabled = false,
            sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            sheetBackgroundColor = AppThemeColors.white,
            topBar = {
                MobileTitleBar(
                    title = "Edycja notyfikacji",
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