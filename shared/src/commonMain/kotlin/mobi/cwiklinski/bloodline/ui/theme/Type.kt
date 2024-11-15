package mobi.cwiklinski.bloodline.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.quicksand_bold
import mobi.cwiklinski.bloodline.resources.quicksand_light
import mobi.cwiklinski.bloodline.resources.quicksand_medium
import mobi.cwiklinski.bloodline.resources.quicksand_regular
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font

@Composable
fun getTypography(): Typography {
    return Typography(
        titleLarge = TextStyle(
            fontFamily = getFontFamily(AppFontFamily.BOLD),
            fontWeight = FontWeight.Bold,
            fontSize = 52.sp,
        ),
        titleMedium = TextStyle(
            fontFamily = getFontFamily(AppFontFamily.BOLD),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        ),
        titleSmall = TextStyle(
            fontFamily = getFontFamily(AppFontFamily.BOLD),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
        ),
        headlineLarge = TextStyle(
            fontFamily = getFontFamily(AppFontFamily.MEDIUM),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
        ),
        headlineMedium = TextStyle(
            fontFamily = getFontFamily(AppFontFamily.BOLD),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = getFontFamily(AppFontFamily.MEDIUM),
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
        ),
        displayLarge = TextStyle(
            fontFamily = getFontFamily(AppFontFamily.MEDIUM),
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
        ),
        displayMedium = TextStyle(
            fontFamily = getFontFamily(AppFontFamily.MEDIUM),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
        ),
        displaySmall = TextStyle(
            fontFamily = getFontFamily(AppFontFamily.MEDIUM),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
        ),
        bodyLarge = TextStyle(
            fontFamily = getFontFamily(AppFontFamily.REGULAR),
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = getFontFamily(AppFontFamily.REGULAR),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        ),
        bodySmall = TextStyle(
            fontFamily = getFontFamily(AppFontFamily.REGULAR),
            fontSize = 12.sp
        ),
        labelLarge = TextStyle(
            fontFamily = getFontFamily(AppFontFamily.MEDIUM),
            fontWeight = FontWeight.Normal,
            fontSize = 10.sp
        ),
        labelMedium = TextStyle(
            fontFamily = getFontFamily(AppFontFamily.MEDIUM),
            fontWeight = FontWeight.Normal,
            fontSize = 9.sp
        ),
        labelSmall = TextStyle(
            fontFamily = getFontFamily(AppFontFamily.MEDIUM),
            fontWeight = FontWeight.Normal,
            fontSize = 8.sp
        ),
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun getFontFamily(family: AppFontFamily): FontFamily {
    val quickSandLight =
        FontFamily(Font(Res.font.quicksand_light, FontWeight.Light, FontStyle.Normal))

    val quickSandRegular =
        FontFamily(
            Font(Res.font.quicksand_regular, FontWeight.Normal, FontStyle.Normal)
        )
    val quickSandMedium = FontFamily(
        Font(Res.font.quicksand_medium, FontWeight.SemiBold, FontStyle.Normal)
    )
    val quickSandBold = FontFamily(
        Font(Res.font.quicksand_bold, FontWeight.Bold, FontStyle.Normal)
    )
    return when (family) {
        AppFontFamily.LIGHT -> quickSandLight
        AppFontFamily.MEDIUM -> quickSandMedium
        AppFontFamily.BOLD -> quickSandBold
        else -> quickSandRegular
    }
}

enum class AppFontFamily {
    REGULAR,
    LIGHT,
    MEDIUM,
    BOLD
}