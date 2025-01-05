package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import com.mikepenz.markdown.compose.LocalMarkdownTypography
import com.mikepenz.markdown.compose.components.MarkdownComponent
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.markdownTypography
import com.mikepenz.markdown.utils.buildMarkdownAnnotatedString
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors.dialogRichTextColors
import mobi.cwiklinski.bloodline.ui.theme.getTypography

@Composable
fun RichText(
    text: String,
    modifier: Modifier = Modifier,
    centered: Boolean = false
) {
    var components = markdownComponents()
    if (centered) {
        val customParagraphComponent: MarkdownComponent = {
            val styledText = buildAnnotatedString {
                pushStyle(LocalMarkdownTypography.current.text.toSpanStyle())
                buildMarkdownAnnotatedString(text, it.node)
                pop()
            }
            Text(
                styledText,
                style = getTypography().bodyLarge,
                textAlign = TextAlign.Center
            )
        }
        components = markdownComponents(
            paragraph = customParagraphComponent
        )
    }
    Markdown(
        text,
        modifier = modifier,
        colors = dialogRichTextColors(),
        typography = markdownTypography(
            h1 = getTypography().displayLarge,
            h2 = getTypography().displayMedium,
            h3 = getTypography().displaySmall,
            h4 = getTypography().headlineLarge,
            h5 = getTypography().headlineMedium,
            h6 = getTypography().headlineSmall,
            text = getTypography().bodyLarge,
            code = getTypography().bodyMedium.copy(fontFamily = FontFamily.Monospace),
            inlineCode = getTypography().bodyLarge.copy(fontFamily = FontFamily.Monospace),
            quote = getTypography().bodyMedium.plus(SpanStyle(fontStyle = FontStyle.Italic)),
            paragraph = getTypography().bodyLarge,
            ordered = getTypography().bodyLarge,
            bullet = getTypography().bodyLarge,
            list = getTypography().bodyLarge,
            link = getTypography().bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                color = AppThemeColors.red3
            )
        ),
        components = components
    )
}