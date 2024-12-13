package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.centersTitle
import mobi.cwiklinski.bloodline.resources.clear
import mobi.cwiklinski.bloodline.resources.ic_search
import mobi.cwiklinski.bloodline.resources.icon_close
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.inputPlaceHolder
import mobi.cwiklinski.bloodline.ui.util.clickWithRipple
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SearchView(
    modifier: Modifier = Modifier.fillMaxWidth(),
    text: String,
    placeholder: String,
    onValueChanged: (String) -> Unit,
    onClose: () -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    OutlinedTextField(
        value = text,
        onValueChange = onValueChanged,
        modifier = modifier,
        enabled = true,
        readOnly = false,
        textStyle = inputPlaceHolder(),
        placeholder = @Composable {
            Text(
                placeholder,
                style = inputPlaceHolder()
            )
        },
        leadingIcon = @Composable {
            Image(
                painterResource(Res.drawable.ic_search),
                contentDescription = stringResource(Res.string.centersTitle),
                colorFilter = ColorFilter.tint(AppThemeColors.grey),
                modifier = Modifier.size(24.dp)
            )
        },
        trailingIcon = @Composable {
            Image(
                painterResource(Res.drawable.icon_close),
                stringResource(Res.string.clear),
                colorFilter = ColorFilter.tint(AppThemeColors.grey),
                modifier = Modifier
                    .size(40.dp)
                    .padding(5.dp)
                    .clickWithRipple {
                        onClose.invoke()
                    }
            )
        },
        isError = false,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
        maxLines = 1,
        minLines = 1,
        interactionSource = interactionSource,
        shape = OutlinedTextFieldDefaults.shape,
        colors = AppThemeColors.authFieldColors()
    )
}