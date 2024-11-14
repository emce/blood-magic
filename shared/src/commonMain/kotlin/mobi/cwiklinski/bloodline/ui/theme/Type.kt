package mobi.cwiklinski.bloodline.ui.theme

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import bloodmagic.shared.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font

@Composable
fun getTypography(): Typography {
    return Typography(
        h1 = TextStyle(
            fontFamily = getFontFamily(AppFontFamily.BOLD),
            fontWeight = FontWeight.Bold,
            fontSize = 52.sp,
        ),
        h2 = TextStyle(
            fontFamily = getFontFamily(AppFontFamily.BOLD),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        ),
        h3 = TextStyle(
            fontFamily = getFontFamily(AppFontFamily.BOLD),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
        ),
        h4 = TextStyle(
            fontFamily = getFontFamily(AppFontFamily.MEDIUM),
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
        ),
        h5 = TextStyle(
            fontFamily = getFontFamily(AppFontFamily.BOLD),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        ),
        h6 = TextStyle(
            fontFamily = getFontFamily(AppFontFamily.MEDIUM),
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
        ),
        subtitle1 = TextStyle(
            fontFamily = getFontFamily(AppFontFamily.MEDIUM),
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
        ),
        subtitle2 = TextStyle(
            fontFamily = getFontFamily(AppFontFamily.MEDIUM),
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
        ),
        body1 = TextStyle(
            //fontFamily = Res.font.quicksand_regular,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        ),
        body2 = TextStyle(fontFamily = getFontFamily(AppFontFamily.REGULAR), fontSize = 10.sp),
        button = TextStyle(
            fontFamily = getFontFamily(AppFontFamily.MEDIUM),
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp
        ),
        caption = TextStyle(
            fontFamily = getFontFamily(AppFontFamily.MEDIUM),
            fontWeight = FontWeight.Normal,
            fontSize = 10.sp
        ),
        overline = TextStyle(
            fontFamily = getFontFamily(AppFontFamily.LIGHT),
            fontWeight = FontWeight.Light,
            fontSize = 12.sp
        )
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