package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.itemTrailing

@Composable
fun JustTextButton(
    text: String,
    onClicked: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    textDecoration: TextDecoration = TextDecoration.Underline,
    textColor: Color = AppThemeColors.grey3,
    contentPadding: PaddingValues = PaddingValues(horizontal = 5.dp, vertical = 0.dp),
    textStyle: TextStyle = itemTrailing(),
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    TextButton(
        modifier = modifier,
        onClick = onClicked,
        enabled = enabled,
        contentPadding = contentPadding,
        colors = AppThemeColors.textButtonColors(),
    ) {
        leadingIcon?.invoke()
        Text(
            text,
            style = textStyle.copy(color = textColor, textDecoration = textDecoration),
            modifier = Modifier.padding(horizontal = 5.dp)
        )
    }
}