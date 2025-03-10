package mobi.cwiklinski.bloodline.ui.theme

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.mikepenz.markdown.m3.markdownTypography
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.quicksand_bold
import mobi.cwiklinski.bloodline.resources.quicksand_light
import mobi.cwiklinski.bloodline.resources.quicksand_regular
import mobi.cwiklinski.bloodline.resources.quicksand_semibold
import org.jetbrains.compose.resources.Font

@Composable
fun getTypography(): Typography {
    return Typography(
        titleLarge = TextStyle(
            fontFamily = quickSandFamily(),
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        ),
        titleMedium = TextStyle(
            fontFamily = quickSandFamily(),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        ),
        titleSmall = TextStyle(
            fontFamily = quickSandFamily(),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
        ),
        headlineLarge = TextStyle(
            fontFamily = quickSandFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = 24.sp,
            lineHeight = 28.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = quickSandFamily(),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            lineHeight = 24.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = quickSandFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            lineHeight = 21.sp
        ),
        displayLarge = TextStyle(
            fontFamily = quickSandFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 36.sp,
            lineHeight = 42.sp
        ),
        displayMedium = TextStyle(
            fontFamily = quickSandFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = 32.sp,
            lineHeight = 37.sp
        ),
        displaySmall = TextStyle(
            fontFamily = quickSandFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = 28.sp,
            lineHeight = 32.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = quickSandFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = quickSandFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        ),
        bodySmall = TextStyle(
            fontFamily = quickSandFamily(),
            fontSize = 12.sp
        ),
        labelLarge = TextStyle(
            fontFamily = quickSandFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = 10.sp
        ),
        labelMedium = TextStyle(
            fontFamily = quickSandFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = 9.sp
        ),
        labelSmall = TextStyle(
            fontFamily = quickSandFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = 8.sp
        ),
    )
}

@Composable
fun hugeTitle() = TextStyle(
    fontFamily = quickSandFamily(),
    fontSize = 40.sp,
    color = AppThemeColors.violet4,
    fontWeight = FontWeight.Bold,
    lineHeight = 46.sp,
)

@Composable
fun toolbarTitle() = TextStyle(
    fontFamily = quickSandFamily(),
    fontSize = 20.sp,
    color = AppThemeColors.black,
    fontWeight = FontWeight.Bold,
    lineHeight = 28.sp,
)

@Composable
fun toolbarSubTitle() = TextStyle(
    fontFamily = quickSandFamily(),
    fontSize = 16.sp,
    color = AppThemeColors.black70,
    fontWeight = FontWeight.Medium,
    lineHeight = 20.sp,
)

@Composable
fun contentTitle() = TextStyle(
    fontFamily = quickSandFamily(),
    fontSize = 17.sp,
    color = AppThemeColors.black,
    fontWeight = FontWeight.Bold,
    lineHeight = 24.sp,
)

@Composable
fun cardTitle() = TextStyle(
    fontFamily = quickSandFamily(),
    fontSize = 28.sp,
    color = AppThemeColors.black,
    fontWeight = FontWeight.Bold,
    lineHeight = 36.sp,
)

@Composable
fun contentText() = TextStyle(
    fontFamily = quickSandFamily(),
    fontSize = 16.sp,
    color = AppThemeColors.black70,
    fontWeight = FontWeight.Medium,
    lineHeight = 20.sp,
)

@Composable
fun contentAction() = TextStyle(
    fontFamily = quickSandFamily(),
    fontSize = 13.sp,
    color = AppThemeColors.black,
    fontWeight = FontWeight.SemiBold,
    lineHeight = 18.sp
)

@Composable
fun inputPlaceHolder() = LocalTextStyle.current.copy(
    fontFamily = quickSandFamily(),
    fontSize = 13.sp,
    color = AppThemeColors.grey3,
    fontWeight = FontWeight.SemiBold,
    lineHeight = 18.sp
)

@Composable
fun itemTitle() = TextStyle(
    fontFamily = quickSandFamily(),
    fontSize = 13.sp,
    color = AppThemeColors.black,
    fontWeight = FontWeight.SemiBold,
    lineHeight = 18.sp,
    lineBreak = LineBreak.Paragraph
)

@Composable
fun itemSubTitle() = TextStyle(
    fontFamily = quickSandFamily(),
    fontSize = 11.sp,
    color = AppThemeColors.black70,
    fontWeight = FontWeight.Medium,
    lineHeight = 16.sp,
    lineBreak = LineBreak.Paragraph
)

@Composable
fun itemTrailing() = TextStyle(
    fontFamily = quickSandFamily(),
    fontSize = 13.sp,
    color = AppThemeColors.black,
    fontWeight = FontWeight.SemiBold,
    lineHeight = 18.sp
)

@Composable
fun itemDelete() = TextStyle(
    fontFamily = quickSandFamily(),
    fontSize = 13.sp,
    color = AppThemeColors.alertRed,
    fontWeight = FontWeight.Bold,
    lineHeight = 18.sp
)

@Composable
fun submitButton() = TextStyle(
    fontFamily = quickSandFamily(),
    fontSize = 12.sp,
    color = AppThemeColors.white,
    fontWeight = FontWeight.Bold,
    lineHeight = 20.sp
)

@Composable
fun alertTitle() = TextStyle(
    fontFamily = quickSandFamily(),
    fontSize = 22.sp,
    color = AppThemeColors.black,
    fontWeight = FontWeight.Normal,
    lineHeight = 30.sp
)

@Composable
fun notificationTitle() = TextStyle(
    fontFamily = quickSandFamily(),
    fontSize = 16.sp,
    color = AppThemeColors.black,
    fontWeight = FontWeight.SemiBold,
    lineHeight = 20.sp
)

@Composable
fun notificationInfo() = TextStyle(
    fontFamily = quickSandFamily(),
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
fun markdownText() = TextStyle(
    fontFamily = quickSandFamily(),
    fontSize = 14.sp,
    color = AppThemeColors.black70,
    fontWeight = FontWeight.Medium,
    lineHeight = 18.sp,
)

@Composable
fun richTextTypography() = markdownTypography(
    text = markdownText(),
    paragraph = markdownText(),
    ordered = markdownText(),
    bullet = markdownText(),
    list = markdownText(),
    link = markdownText().copy(
        fontWeight = FontWeight.SemiBold,
        textDecoration = TextDecoration.Underline
    )
)

@Composable
fun quickSandFamily() = FontFamily(
    Font(Res.font.quicksand_regular, FontWeight.Normal),
    Font(Res.font.quicksand_light, FontWeight.Light),
    Font(Res.font.quicksand_regular, FontWeight.Normal, FontStyle.Italic),
    Font(Res.font.quicksand_bold, FontWeight.Bold),
    Font(Res.font.quicksand_semibold, FontWeight.SemiBold)
)