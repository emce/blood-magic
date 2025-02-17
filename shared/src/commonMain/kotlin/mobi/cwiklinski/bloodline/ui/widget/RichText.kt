package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import com.mikepenz.markdown.compose.LocalMarkdownTypography
import com.mikepenz.markdown.compose.components.MarkdownComponent
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.utils.buildMarkdownAnnotatedString
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors.dialogRichTextColors
import mobi.cwiklinski.bloodline.ui.theme.getTypography
import mobi.cwiklinski.bloodline.ui.theme.richTextTypography

@Composable
fun RichText(
    text: String,
    modifier: Modifier = Modifier,
    centered: Boolean = false
) {
    val finalText = text.replace("<br>", "\n\n")
    var components = markdownComponents()
    if (centered) {
        val customParagraphComponent: MarkdownComponent = {
            val styledText = buildAnnotatedString {
                pushStyle(LocalMarkdownTypography.current.text.toSpanStyle())
                buildMarkdownAnnotatedString(finalText, it.node)
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
        finalText,
        modifier = modifier,
        colors = dialogRichTextColors(),
        typography = richTextTypography(),
        components = components
    )
}