package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.clear
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.util.bottomDivider
import org.jetbrains.compose.resources.stringResource

@Composable
fun <T> AutoCompleteTextView(
    modifier: Modifier,
    query: String,
    queryLabel: String,
    onQueryChanged: (newQuery: String) -> Unit = {},
    predictions: List<T>,
    onClearClick: () -> Unit = {},
    onItemClick: (T) -> Unit = {},
    enabled: Boolean = true,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    itemContent: @Composable (T, Int) -> Unit
) {
    val borderColor = AppThemeColors.grey3
    val borderSize = 1.dp
    var hasFocus by remember { mutableStateOf(false) }
    OutlinedInput(
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                hasFocus = focusState.isFocused
            },
        text = query,
        enabled = enabled,
        readOnly = false,
        onValueChanged = onQueryChanged,
        label = queryLabel,
        trailingIcon = {
            if (hasFocus) {
                IconButton(onClick = {
                    onQueryChanged.invoke("")
                    onClearClick()
                }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(Res.string.clear)
                    )
                }
            }

        },
        keyboardActions = keyboardActions,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        ),
    )
    if (query.length > 3) {
        LazyColumn(
            state = rememberLazyListState(),
            modifier = modifier.heightIn(max = 72.dp * 3, min = 0.dp)
                .padding(vertical = 8.dp)
                .background(AppThemeColors.white)
                .border(borderSize, borderColor, RoundedCornerShape(8.dp)),
        ) {
            if (predictions.isNotEmpty()) {
                items(predictions.size) { position ->
                    val rowModifier = Modifier
                        .padding(vertical = 16.dp, horizontal = 24.dp)
                    Box(
                        modifier = Modifier
                            .bottomDivider(borderSize, borderColor, 10.dp)
                            .clickable {
                                onItemClick(predictions[position])
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = rowModifier,
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            itemContent(predictions[position], position)
                        }
                    }
                }
            }
        }
    }
}