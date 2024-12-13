package mobi.cwiklinski.bloodline.ui.theme

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.unit.sp
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.quicksand_bold
import mobi.cwiklinski.bloodline.resources.quicksand_light
import mobi.cwiklinski.bloodline.resources.quicksand_medium
import mobi.cwiklinski.bloodline.resources.quicksand_regular
import mobi.cwiklinski.bloodline.resources.quicksand_semibold
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

@Composable
fun hugeTitle() = TextStyle(
    fontFamily = getFontFamily(AppFontFamily.BOLD),
    fontSize = 40.sp,
    color = AppThemeColors.violet4,
    fontWeight = FontWeight.Bold,
    lineHeight = 46.sp,
)

@Composable
fun toolbarTitle() = TextStyle(
    fontFamily = getFontFamily(AppFontFamily.BOLD),
    fontSize = 20.sp,
    color = AppThemeColors.black,
    fontWeight = FontWeight.Bold,
    lineHeight = 28.sp,
)

@Composable
fun toolbarSubTitle() = TextStyle(
    fontFamily = getFontFamily(AppFontFamily.MEDIUM),
    fontSize = 16.sp,
    color = AppThemeColors.black70,
    fontWeight = FontWeight.Medium,
    lineHeight = 20.sp,
)

@Composable
fun contentTitle() = TextStyle(
    fontFamily = getFontFamily(AppFontFamily.BOLD),
    fontSize = 17.sp,
    color = AppThemeColors.black,
    fontWeight = FontWeight.Bold,
    lineHeight = 24.sp,
)

@Composable
fun cardTitle() = TextStyle(
    fontFamily = getFontFamily(AppFontFamily.BOLD),
    fontSize = 28.sp,
    color = AppThemeColors.black,
    fontWeight = FontWeight.Bold,
    lineHeight = 36.sp,
)

@Composable
fun contentText() = TextStyle(
    fontFamily = getFontFamily(AppFontFamily.MEDIUM),
    fontSize = 16.sp,
    color = AppThemeColors.black70,
    fontWeight = FontWeight.Medium,
    lineHeight = 20.sp,
)

@Composable
fun contentAction() = TextStyle(
    fontFamily = getFontFamily(AppFontFamily.SEMIBOLD),
    fontSize = 13.sp,
    color = AppThemeColors.black,
    fontWeight = FontWeight.SemiBold,
    lineHeight = 18.sp
)

@Composable
fun inputPlaceHolder() = LocalTextStyle.current.copy(
    fontFamily = getFontFamily(AppFontFamily.SEMIBOLD),
    fontSize = 13.sp,
    color = AppThemeColors.grey3,
    fontWeight = FontWeight.SemiBold,
    lineHeight = 18.sp
)

@Composable
fun itemTitle() = TextStyle(
    fontFamily = getFontFamily(AppFontFamily.SEMIBOLD),
    fontSize = 13.sp,
    color = AppThemeColors.black,
    fontWeight = FontWeight.SemiBold,
    lineHeight = 18.sp,
    lineBreak = LineBreak.Paragraph
)

@Composable
fun itemSubTitle() = TextStyle(
    fontFamily = getFontFamily(AppFontFamily.MEDIUM),
    fontSize = 11.sp,
    color = AppThemeColors.black70,
    fontWeight = FontWeight.Medium,
    lineHeight = 16.sp,
    lineBreak = LineBreak.Paragraph
)

@Composable
fun itemTrailing() = TextStyle(
    fontFamily = getFontFamily(AppFontFamily.SEMIBOLD),
    fontSize = 13.sp,
    color = AppThemeColors.black,
    fontWeight = FontWeight.SemiBold,
    lineHeight = 18.sp
)

@Composable
fun submitButton() = TextStyle(
    fontFamily = getFontFamily(AppFontFamily.BOLD),
    fontSize = 12.sp,
    color = AppThemeColors.white,
    fontWeight = FontWeight.Bold,
    lineHeight = 20.sp
)

@Composable
fun alertTitle() = TextStyle(
    fontFamily = getFontFamily(AppFontFamily.REGULAR),
    fontSize = 22.sp,
    color = AppThemeColors.black,
    fontWeight = FontWeight.Normal,
    lineHeight = 30.sp
)

@Composable
fun notificationTitle() = TextStyle(
    fontFamily = getFontFamily(AppFontFamily.SEMIBOLD),
    fontSize = 16.sp,
    color = AppThemeColors.black,
    fontWeight = FontWeight.SemiBold,
    lineHeight = 20.sp
)

@Composable
fun notificationInfo() = TextStyle(
    fontFamily = getFontFamily(AppFontFamily.REGULAR),
    fontSize = 12.sp,
    color = AppThemeColors.black70,
    fontWeight = FontWeight.Normal,
    lineHeight = 16.sp
)

@Composable
fun secondaryButton() = submitButton().copy(
    color = AppThemeColors.rose1,
)

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
    val quickSandSemiBold = FontFamily(
        Font(Res.font.quicksand_semibold, FontWeight.Bold, FontStyle.Normal)
    )
    val quickSandBold = FontFamily(
        Font(Res.font.quicksand_bold, FontWeight.Bold, FontStyle.Normal)
    )
    return when (family) {
        AppFontFamily.LIGHT -> quickSandLight
        AppFontFamily.MEDIUM -> quickSandMedium
        AppFontFamily.BOLD -> quickSandBold
        AppFontFamily.SEMIBOLD -> quickSandSemiBold
        else -> quickSandRegular
    }
}

enum class AppFontFamily {
    REGULAR,
    LIGHT,
    MEDIUM,
    SEMIBOLD,
    BOLD
}