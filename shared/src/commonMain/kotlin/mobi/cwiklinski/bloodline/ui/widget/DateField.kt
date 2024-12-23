package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import mobi.cwiklinski.bloodline.common.toMillis
import mobi.cwiklinski.bloodline.common.today
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.close
import mobi.cwiklinski.bloodline.resources.confirm
import mobi.cwiklinski.bloodline.resources.donationNewDateLabel
import mobi.cwiklinski.bloodline.resources.icon_calendar
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors.calendarPickerColors
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors.datePickerColors
import mobi.cwiklinski.bloodline.ui.util.DateDefaults.DATE_LENGTH
import mobi.cwiklinski.bloodline.ui.util.DateDefaults.DATE_MASK
import mobi.cwiklinski.bloodline.ui.util.MaskVisualTransformation
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DateField(
    modifier: Modifier = Modifier,
    state: DatePickerState,
    error: Boolean = false,
    errorMessage: String = "",
    keyboardActions: KeyboardActions = KeyboardActions()
) {
    val starting = state.selectedDateMillis.toFormattedDate()
    var showDatePicker by remember { mutableStateOf(false) }
    val dateValue = remember { mutableStateOf(starting) }
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                SubmitButton(
                    onClick = {
                        dateValue.value = state.selectedDateMillis.toFormattedDate()
                        showDatePicker = false
                    },
                    text = stringResource(Res.string.confirm)
                )
            },
            dismissButton = {
                SecondaryButton(
                    onClick = {
                        showDatePicker = false
                    },
                    text = stringResource(Res.string.close)
                )
            },
            colors = datePickerColors()
        ) {
            DatePicker(
                state = state,
                title = null,
                showModeToggle = false,
                headline = null,
                colors = calendarPickerColors()
            )
        }
    }
    OutlinedInput(
        modifier = modifier,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        text = dateValue.value,
        label = stringResource(Res.string.donationNewDateLabel),
        error = error,
        errorMessage = errorMessage,
        onValueChanged = {
            if (it.length <= DATE_LENGTH) {
                dateValue.value = it
                if (it.length == DATE_LENGTH) {
                    state.selectedDateMillis = it.toMillis()
                }
            }
        },
        keyboardActions = keyboardActions,
        visualTransformation = MaskVisualTransformation(DATE_MASK),
        trailingIcon = {
            Icon(
                painterResource(Res.drawable.icon_calendar),
                contentDescription = stringResource(Res.string.donationNewDateLabel),
                tint = AppThemeColors.rose1,
                modifier = Modifier.clickable {
                    showDatePicker = true
                }
            )
        }
    )
}

fun Long?.toFormattedDate(): String {
    var date = today()
    if (this != null) {
        date = this.toLocalDate()
    }
    var dateString = ""
    dateString += if (date.dayOfMonth.toString().length == 1) {
        "0${date.dayOfMonth}"
    } else {
        date.dayOfMonth.toString()
    }
    dateString += if (date.monthNumber.toString().length == 1) {
        "0${date.monthNumber}"
    } else {
        date.monthNumber.toString()
    }
    dateString += date.year.toString()
    return dateString
}

@OptIn(FormatStringsInDatetimeFormats::class)
fun String.toMillis(): Long {
    if (this.length == 8) {
        var formatted = ""
        this.forEachIndexed { index, character ->
            formatted += character
            if (listOf(1, 3).contains(index)) {
                formatted += "/"
            }
        }
        return LocalDate.parse(formatted, LocalDate.Format { byUnicodePattern("dd/MM/yyyy") }).toMillis()
    } else {
        return 0L
    }
}