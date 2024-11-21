package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.getTypography

@Composable
fun OutlinedInput(
    modifier: Modifier = Modifier.fillMaxWidth(),
    text: String,
    enabled: Boolean = true,
    onValueChanged: (String) -> Unit,
    label: String = "",
    error: Boolean = false,
    errorMessage: String = "",
    readOnly: Boolean = false,
    trailingIcon: @Composable () -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    placeholder: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    OutlinedTextField(
        value = text,
        onValueChange = onValueChanged,
        label = { Text(label) },
        modifier = modifier,
        enabled = enabled,
        colors = AppThemeColors.authFieldColors(),
        isError = error,
        singleLine = true,
        readOnly = readOnly,
        trailingIcon = trailingIcon,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        placeholder = placeholder,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource
    )
    if (error) {
        Text(
            text = errorMessage,
            color = AppThemeColors.red1,
            style = getTypography().bodyMedium.copy(textAlign = TextAlign.Start),
            modifier = Modifier.fillMaxWidth().padding(top = 6.dp)
        )
    }
}