package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.secondaryButton
import mobi.cwiklinski.bloodline.ui.theme.submitButton

@Composable
fun SubmitButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier.padding(10.dp),
    text: String,
    enabled: Boolean = true,
    textAlign: TextAlign = TextAlign.Center,
    colors: ButtonColors = AppThemeColors.submitButtonColors()
) {
    Button(
        modifier = Modifier
            .shadow(
                elevation = 8.dp,
                spotColor = AppThemeColors.rose3,
                ambientColor = AppThemeColors.rose3
            ),
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        colors = colors
    ) {
        Text(
            text,
            modifier = modifier,
            textAlign = textAlign,
            style = submitButton()
        )
    }
}

@Composable
fun SecondaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier.padding(10.dp),
    text: String,
    enabled: Boolean = true,
    textAlign: TextAlign = TextAlign.Center,
    colors: ButtonColors = AppThemeColors.secondarySubmitButtonColors()
) {
    Button(
        modifier = Modifier
            .shadow(
                elevation = 8.dp,
                spotColor = AppThemeColors.rose4,
                ambientColor = AppThemeColors.rose4
            ),
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        colors = colors
    ) {
        Text(
            text,
            modifier = modifier,
            textAlign = textAlign,
            style = secondaryButton()
        )
    }
}