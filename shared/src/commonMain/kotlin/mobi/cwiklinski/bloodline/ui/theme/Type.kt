package mobi.cwiklinski.bloodline.ui.theme

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Typography
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.mikepenz.markdown.m3.markdownTypography
import mobi.cwiklinski.bloodline.getWindowSizeClass
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
            fontSize = (22 * getFontFactor()).sp
        ),
        titleMedium = TextStyle(
            fontFamily = quickSandFamily(),
            fontWeight = FontWeight.Bold,
            fontSize = (20 * getFontFactor()).sp
        ),
        titleSmall = TextStyle(
            fontFamily = quickSandFamily(),
            fontWeight = FontWeight.Bold,
            fontSize = (18 * getFontFactor()).sp,
        ),
        headlineLarge = TextStyle(
            fontFamily = quickSandFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = (24 * getFontFactor()).sp,
            lineHeight = (28 * getFontFactor()).sp
        ),
        headlineMedium = TextStyle(
            fontFamily = quickSandFamily(),
            fontWeight = FontWeight.Bold,
            fontSize = (20 * getFontFactor()).sp,
            lineHeight = (24 * getFontFactor()).sp
        ),
        headlineSmall = TextStyle(
            fontFamily = quickSandFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = (18 * getFontFactor()).sp,
            lineHeight = (21 * getFontFactor()).sp
        ),
        displayLarge = TextStyle(
            fontFamily = quickSandFamily(),
            fontWeight = FontWeight.SemiBold,
            fontSize = (36 * getFontFactor()).sp,
            lineHeight = (42 * getFontFactor()).sp
        ),
        displayMedium = TextStyle(
            fontFamily = quickSandFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = (32 * getFontFactor()).sp,
            lineHeight = (37 * getFontFactor()).sp
        ),
        displaySmall = TextStyle(
            fontFamily = quickSandFamily(),
            fontWeight = FontWeight.Medium,
            fontSize = (28 * getFontFactor()).sp,
            lineHeight = (32 * getFontFactor()).sp
        ),
        bodyLarge = TextStyle(
            fontFamily = quickSandFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = (16 * getFontFactor()).sp
        ),
        bodyMedium = TextStyle(
            fontFamily = quickSandFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = (14 * getFontFactor()).sp
        ),
        bodySmall = TextStyle(
            fontFamily = quickSandFamily(),
            fontSize = (12 * getFontFactor()).sp
        ),
        labelLarge = TextStyle(
            fontFamily = quickSandFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = (10 * getFontFactor()).sp
        ),
        labelMedium = TextStyle(
            fontFamily = quickSandFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = (9 * getFontFactor()).sp
        ),
        labelSmall = TextStyle(
            fontFamily = quickSandFamily(),
            fontWeight = FontWeight.Normal,
            fontSize = (8 * getFontFactor()).sp
        ),
    )
}

@Composable
fun hugeTitle() = TextStyle(
    fontFamily = quickSandFamily(),
    fontSize = (40 * getFontFactor()).sp,
    color = AppThemeColors.violet4,
    fontWeight = FontWeight.Bold,
    lineHeight = (46 * getFontFactor()).sp,
)

@Composable
fun toolbarTitle() = TextStyle(
    fontFamily = quickSandFamily(),
    fontSize = (20 * getFontFactor()).sp,
    color = AppThemeColors.black,
    fontWeight = FontWeight.Bold,
    lineHeight = (28 * getFontFactor()).sp,
)

@Composable
fun toolbarSubTitle() = TextStyle(
    fontFamily = quickSandFamily(),
    fontSize = (16 * getFontFactor()).sp,
    color = AppThemeColors.black70,
    fontWeight = FontWeight.Medium,
    lineHeight = (20 * getFontFactor()).sp,
)

@Composable
fun contentTitle() = TextStyle(
    fontFamily = quickSandFamily(),
    fontSize = (17 * getFontFactor()).sp,
    color = AppThemeColors.black,
    fontWeight = FontWeight.Bold,
    lineHeight = (24 * getFontFactor()).sp,
)

@Composable
fun cardTitle() = TextStyle(
    fontFamily = quickSandFamily(),
    fontSize = (28 * getFontFactor()).sp,
    color = AppThemeColors.black,
    fontWeight = FontWeight.Bold,
    lineHeight = (36 * getFontFactor()).sp,
)

@Composable
fun contentText() = TextStyle(
    fontFamily = quickSandFamily(),
    fontSize = (16 * getFontFactor()).sp,
    color = AppThemeColors.black70,
    fontWeight = FontWeight.Medium,
    lineHeight = (20 * getFontFactor()).sp,
)

@Composable
fun contentAction() = TextStyle(
    fontFamily = quickSandFamily(),
    fontSize = (13 * getFontFactor()).sp,
    color = AppThemeColors.black,
    fontWeight = FontWeight.SemiBold,
    lineHeight = (18 * getFontFactor()).sp
)

@Composable
fun inputPlaceHolder() = LocalTextStyle.current.copy(
    fontFamily = quickSandFamily(),
    fontSize = (13 * getFontFactor()).sp,
    color = AppThemeColors.grey3,
    fontWeight = FontWeight.SemiBold,
    lineHeight = (18 * getFontFactor()).sp
)

@Composable
fun itemTitle() = TextStyle(
    fontFamily = quickSandFamily(),
    fontSize = (13 * getFontFactor()).sp,
    color = AppThemeColors.black,
    fontWeight = FontWeight.SemiBold,
    lineHeight = (18 * getFontFactor()).sp,
    lineBreak = LineBreak.Paragraph
)

@Composable
fun itemSubTitle() = TextStyle(
    fontFamily = quickSandFamily(),
    fontSize = (11 * getFontFactor()).sp,
    color = AppThemeColors.black70,
    fontWeight = FontWeight.Medium,
    lineHeight = (16 * getFontFactor()).sp,
    lineBreak = LineBreak.Paragraph
)

@Composable
fun itemTrailing() = TextStyle(
    fontFamily = quickSandFamily(),
    fontSize = (13 * getFontFactor()).sp,
    color = AppThemeColors.black,
    fontWeight = FontWeight.SemiBold,
    lineHeight = (18 * getFontFactor()).sp
)

@Composable
fun itemDelete() = TextStyle(
    fontFamily = quickSandFamily(),
    fontSize = (13 * getFontFactor()).sp,
    color = AppThemeColors.alertRed,
    fontWeight = FontWeight.Bold,
    lineHeight = (18 * getFontFactor()).sp
)

@Composable
fun submitButton() = TextStyle(
    fontFamily = quickSandFamily(),
    fontSize = (12 * getFontFactor()).sp,
    color = AppThemeColors.white,
    fontWeight = FontWeight.Bold,
    lineHeight = (20 * getFontFactor()).sp
)

@Composable
fun alertTitle() = TextStyle(
    fontFamily = quickSandFamily(),
    fontSize = (22 * getFontFactor()).sp,
    color = AppThemeColors.black,
    fontWeight = FontWeight.Normal,
    lineHeight = (30 * getFontFactor()).sp
)

@Composable
fun notificationTitle() = TextStyle(
    fontFamily = quickSandFamily(),
    fontSize = (16 * getFontFactor()).sp,
    color = AppThemeColors.black,
    fontWeight = FontWeight.SemiBold,
    lineHeight = (20 * getFontFactor()).sp
)

@Composable
fun notificationInfo() = TextStyle(
    fontFamily = quickSandFamily(),
    fontSize = (12 * getFontFactor()).sp,
    color = AppThemeColors.black70,
    fontWeight = FontWeight.Normal,
    lineHeight = (16 * getFontFactor()).sp
)

@Composable
fun secondaryButton() = submitButton().copy(
    color = AppThemeColors.rose1,
)

@Composable
fun markdownText() = TextStyle(
    fontFamily = quickSandFamily(),
    fontSize = (14 * getFontFactor()).sp,
    color = AppThemeColors.black70,
    fontWeight = FontWeight.Medium,
    lineHeight = (18 * getFontFactor()).sp,
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
fun getFontFactor(windowSizeClass: WindowSizeClass = getWindowSizeClass()) = when (windowSizeClass.widthSizeClass) {
    WindowWidthSizeClass.Medium -> 1.4f
    WindowWidthSizeClass.Expanded -> 1.2f
    else -> 1.0f
}

@Composable
fun quickSandFamily() = FontFamily(
    Font(Res.font.quicksand_regular, FontWeight.Normal),
    Font(Res.font.quicksand_light, FontWeight.Light),
    Font(Res.font.quicksand_regular, FontWeight.Normal, FontStyle.Italic),
    Font(Res.font.quicksand_bold, FontWeight.Bold),
    Font(Res.font.quicksand_semibold, FontWeight.SemiBold)
)