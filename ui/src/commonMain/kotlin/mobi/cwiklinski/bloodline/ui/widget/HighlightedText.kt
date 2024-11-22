package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors

data class LinkedText(
    val text: String,
    val link: String
)

@Composable
fun TextWithLinks(
    text: String,
    linkedTexts: List<LinkedText>,
    modifier: Modifier = Modifier,
    style: SpanStyle = SpanStyle(
        fontSize = 11.sp,
        fontWeight = FontWeight.Normal,
        color = AppThemeColors.grey3,
    )
) {
    val annotatedTextWithLink = buildAnnotatedString {
        withStyle(SpanStyle(fontSize = 30.sp)) { append(text) }
        linkedTexts.forEach { linkText ->
            val startIndex = text.indexOf(linkText.text)
            val endIndex = startIndex + linkText.text.length
            addStyle(
                style = style,
                start = startIndex,
                end = endIndex
            )
            addStringAnnotation(tag = "URL", annotation = linkText.link, start = startIndex, end = endIndex)
        }
    }
    Text(
        text = annotatedTextWithLink,
        modifier = modifier
    )
}