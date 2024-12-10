package mobi.cwiklinski.bloodline.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors.appColors

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = appColors,
        shapes = appShapes,
        typography = getTypography(),
        content = content
    )
}

val appShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

object AppThemeColors {
    val white = Color(0xFFFCFCFC)
    val black = Color(0xFF09101D)
    val black70 = Color(0xFF50555E)
    val rose4 = Color(0xFFFFF0ED)
    val rose3 = Color(0xFFFFB5A7)
    val rose2 = Color(0xFFDC9991)
    val rose1 = Color(0xFFD27D7D)
    val violet1 = Color(0xFFE1C7D3)
    val violet2 = Color(0xFFB794A5)
    val violet3 = Color(0xFF6A284F)
    val violet4 = Color(0xFF350022)
    val grey1 = Color(0xFFECEEF1)
    val grey2 = Color(0xFFDADEE3)
    val grey3 = Color(0xFF9F9F9F)
    val yellow1 = Color(0xFFFFF3D8)
    val yellow2 = Color(0xFFF9C784)
    val yellow3 = Color(0xFFF9DCC4)
    val red1 = Color(0xFFCE4257)
    val red2 = Color(0xFF720026)
    val red3 = Color(0xFF4F000B)

    val alertRed = Color(0xFFF80F30)
    val alertYellow = Color(0xFFFFF06A)
    val alertGreen = Color(0xFF10C761)
    val alertViolet2 = Color(0xFFEFD8EE)
    val alertViolet1 = Color(0xFFB03DAC)
    val alertBackground = Color(0xFFFEF6F4)

    val cardBackground = Color(0xFFFFF7F2)

    val iconOrange = Color(0xFFFF9B54)
    val iconOrangeBackground = Color(0xFFFFEBDD)
    val iconYellow = Color(0xFFF9C784)
    val iconYellowBackground = Color(0xFFFEF4E6)
    val iconPink = Color(0xFFCE4274)
    val iconPinkBackground = Color(0xFFF5D9E3)
    val iconRed = Color(0xFFF87171)
    val iconRedBackground = Color(0xFFFEE3E3)
    val iconBlue1 = Color(0xFFA2C3CB)
    val iconBlue1Background = Color(0xFFECF3F5)
    val iconGreen1 = Color(0xFF98DB09)
    val iconGreen1Background = Color(0xFFEAF8CE)
    val iconGreen2 = Color(0xFF27C29D)
    val iconGreen2Background = Color(0xFFD4F3EB)
    val iconBlue2 = Color(0xFF62A5CA)
    val iconBlue2Background = Color(0xFFE0EDF4)
    val background = Color(0xFFEFEFEF) //Color(0xFFFCFCFC)

    val grey = Color(0xFF767676)
    val greyish = Color(0xFFDBDBDB)
    val dark = Color(0xFF1A1A1A)


    val startingGradient = Brush.verticalGradient(
        listOf(violet1, yellow3)
    )
    val mainGradient = Brush.verticalGradient(
        0.00f to Color(0xFFB08FA2),
        0.26f to Color(0xFFE3B9B9),
        1.00f to white
    )
    val notificationIconGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFDC9991), Color(0xFFFFB5A7))
    )

    val authGradient = Brush.linearGradient(
        0.13f to white,
        0.56f to Color(0xFFFCFCFC),
        0.74f to Color(0xFFFFF3EE),
        1.00f to Color(0xFFE1C7D3)
    )
    val homeGradient = Brush.verticalGradient(
        0.0f to Color(0xFFB08FA2),
        0.26f to Color(0xFFE3B9B9)
    )

    val navigationGradient = Brush.verticalGradient(
        0.0f to Color(0xFFB794A5),
        1f to Color(0xFFE1C7D3),
        startY = 0.0f,
        endY = Float.POSITIVE_INFINITY
    )

    @Composable
    fun textButtonColors() = ButtonDefaults.textButtonColors(
        containerColor = Color.Transparent,
        contentColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        disabledContentColor = Color.Transparent
    )

    @Composable
    fun authFieldColors() = OutlinedTextFieldDefaults.colors(
        focusedTextColor = black,
        focusedBorderColor = black70,
        focusedLabelColor = black70,
        focusedContainerColor = white,
        unfocusedBorderColor = grey3,
        unfocusedLabelColor = grey3,
        unfocusedContainerColor = white,
        unfocusedTextColor = grey3,
        disabledBorderColor = grey2,
        disabledTextColor = grey2,
        errorBorderColor = red1,
        errorLabelColor = red1,
    )

    @Composable
    fun submitButtonColors() = ButtonDefaults.buttonColors(
        containerColor = rose1,
        contentColor = white,
        disabledContainerColor = rose1.copy(alpha = 0.7f),
        disabledContentColor = white
    )

    @Composable
    fun secondarySubmitButtonColors() = ButtonDefaults.buttonColors(
        containerColor = rose4,
        contentColor = violet4,
        disabledContainerColor = rose4.copy(alpha = 0.7f),
        disabledContentColor = violet4
    )

    @Composable
    fun attentionButtonColors() = ButtonDefaults.buttonColors(
        containerColor = red2,
        contentColor = white,
        disabledContainerColor = red2.copy(alpha = 0.7f),
        disabledContentColor = white
    )

    fun switchColors() = SwitchColors(
        checkedThumbColor = rose1,
        checkedTrackColor = white,
        checkedBorderColor = rose1,
        checkedIconColor = white,
        uncheckedThumbColor = grey3,
        uncheckedTrackColor = white,
        uncheckedBorderColor = grey3,
        uncheckedIconColor = white,
        disabledCheckedThumbColor = grey1,
        disabledCheckedTrackColor = white,
        disabledCheckedBorderColor = grey1,
        disabledCheckedIconColor = white,
        disabledUncheckedThumbColor = grey1,
        disabledUncheckedTrackColor = white,
        disabledUncheckedBorderColor = grey1,
        disabledUncheckedIconColor = white
    )

    @Composable
    fun checkboxColors() = CheckboxDefaults.colors(
        checkedColor = rose1,
        uncheckedColor = grey3.copy(alpha = 0.6f),
        checkmarkColor = white,
        disabledColor = grey1.copy(alpha = ContentAlpha.disabled),
        disabledIndeterminateColor = rose1.copy(alpha = ContentAlpha.disabled)
    )

    @Composable
    fun topBarColors() = TopAppBarDefaults.topAppBarColors(
        containerColor = Color.Transparent,
        scrolledContainerColor = Color.Transparent,
        titleContentColor = black,
        navigationIconContentColor = black
    )

    @Composable
    fun calendarPickerColors() = DatePickerDefaults.colors(
        containerColor = white,
        titleContentColor = black70,
        subheadContentColor = white,
        navigationContentColor = grey,
        yearContentColor = black70,
        disabledYearContentColor = grey3,
        currentYearContentColor = black,
        selectedYearContentColor = rose1,
        disabledSelectedYearContentColor = grey3,
        selectedYearContainerColor = rose3,
        disabledSelectedYearContainerColor = rose4,
        dayContentColor = black70,
        disabledDayContentColor = grey3,
        selectedDayContentColor = rose1,
        disabledSelectedDayContentColor = greyish,
        selectedDayContainerColor = rose3,
        disabledSelectedDayContainerColor = rose4,
        todayContentColor = white,
        todayDateBorderColor = black,
        dayInSelectionRangeContentColor = white,
        dayInSelectionRangeContainerColor = white,
        dividerColor = white,
    )

    internal val appColors = lightColorScheme(
        primary = black,
        error = alertRed,
        errorContainer = alertBackground,
        onError = alertRed,
        onErrorContainer = alertBackground,
        background = background,
        onBackground = background,
    )
}