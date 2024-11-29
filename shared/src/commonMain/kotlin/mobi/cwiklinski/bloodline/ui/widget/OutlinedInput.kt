package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.inputPlaceHolder

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
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = inputPlaceHolder(),
        label = {
            Text(label)
        },
        placeholder = placeholder,
        trailingIcon = trailingIcon,
        isError = error,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
        maxLines = 1,
        minLines = 1,
        supportingText = {
            if (error) {
                Text(
                    errorMessage,
                    style = inputPlaceHolder().copy(
                        color = AppThemeColors.alertRed
                    )
                )
            }
        },
        interactionSource = interactionSource,
        shape = OutlinedTextFieldDefaults.shape,
        colors = AppThemeColors.authFieldColors()
    )
}

@Composable
fun OutlinedInputLabel(text: String) {
    Text(text)
}