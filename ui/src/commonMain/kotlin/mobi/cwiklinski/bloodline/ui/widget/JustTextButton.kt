package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.getTypography

@Composable
fun JustTextButton(
    text: String,
    onClicked: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    textDecoration: TextDecoration = TextDecoration.Underline,
    textColor: Color = AppThemeColors.grey3,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    textStyle: TextStyle = getTypography().bodyMedium.copy(
        color = textColor,
        textDecoration = textDecoration
    )
) {
    TextButton(
        modifier = modifier,
        onClick = onClicked,
        enabled = enabled,
        contentPadding = contentPadding,
        colors = AppThemeColors.textButtonColors()
    ) {
        Text(
            text,
            style = textStyle
        )
    }
}