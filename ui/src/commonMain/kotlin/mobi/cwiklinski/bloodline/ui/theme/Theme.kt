package mobi.cwiklinski.bloodline.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AppThemeColors.appColors,
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
    val background = Color(0xFFFCFCFC)

    val grey = Color(0xFF767676)
    val greyish = Color(0xFFDBDBDB)
    val dark = Color(0xFF1A1A1A)


    val startingGradient = Brush.verticalGradient(
        listOf(violet1, yellow3),
        startY = 0.00f,
        endY = Float.POSITIVE_INFINITY
    )
    val mainGradient = Brush.verticalGradient(
        0.00f to Color(0xFFB08FA2),
        0.26f to Color(0xFFE3B9B9),
        1.00f to white,
        startY = 0.00f,
        endY = 1.00f
    )
    val notificationIconGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFDC9991), Color(0xFFFFB5A7)),
        startY = 0.00f,
        endY = 1.00f
    )

    val authGradient = Brush.linearGradient(
        0.13f to white,
        0.56f to Color(0xFFFCFCFC),
        0.74f to Color(0xFFFFF3EE),
        1.00f to Color(0xFFE1C7D3),
        start = Offset(0.0f, Float.POSITIVE_INFINITY),
        end = Offset(Float.POSITIVE_INFINITY, 0.0f)
    )
    val homeGradient = Brush.verticalGradient(
        0.0f to Color(0xFFB08FA2),
        0.26f to Color(0xFFE3B9B9),
        startY = 0.0f,
        endY = Float.POSITIVE_INFINITY
    )

    @Composable
    fun textButtonColors() = ButtonDefaults.buttonColors(
        containerColor = Color.Transparent,
        contentColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        disabledContentColor = Color.Transparent
    )

    @Composable
    fun authFieldColors() = OutlinedTextFieldDefaults.colors(
        focusedTextColor = AppThemeColors.black,
        focusedBorderColor = AppThemeColors.black70,
        focusedLabelColor = AppThemeColors.black70,
        unfocusedBorderColor = AppThemeColors.grey3,
        unfocusedLabelColor = AppThemeColors.grey3,
        disabledBorderColor = AppThemeColors.grey1,
        disabledTextColor = AppThemeColors.grey1,
        errorBorderColor = AppThemeColors.red1,
        errorLabelColor = AppThemeColors.red1,
    )

    @Composable
    fun submitButtonColors() = ButtonDefaults.buttonColors(
        containerColor = AppThemeColors.rose1,
        contentColor = AppThemeColors.white,
        disabledContainerColor = AppThemeColors.rose1.copy(alpha = 0.7f),
        disabledContentColor = AppThemeColors.white
    )

    @Composable
    fun secondarySubmitButtonColors() = ButtonDefaults.buttonColors(
        containerColor = AppThemeColors.rose4,
        contentColor = AppThemeColors.violet4,
        disabledContainerColor = AppThemeColors.rose4.copy(alpha = 0.7f),
        disabledContentColor = AppThemeColors.violet4
    )

    @Composable
    fun attentionButtonColors() = ButtonDefaults.buttonColors(
        containerColor = AppThemeColors.red2,
        contentColor = AppThemeColors.white,
        disabledContainerColor = AppThemeColors.red2.copy(alpha = 0.7f),
        disabledContentColor = AppThemeColors.white
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