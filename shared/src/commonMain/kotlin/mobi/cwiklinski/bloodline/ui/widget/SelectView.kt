package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import io.github.aakira.napier.Napier
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.icon_chevron_down
import mobi.cwiklinski.bloodline.resources.icon_chevron_up
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.util.bottomDivider
import org.jetbrains.compose.resources.painterResource

@Composable
fun <T> SelectView(
    modifier: Modifier,
    text: String = "",
    label: String,
    onSelectionChanged: (T) -> Unit = {},
    itemList: List<T>,
    enabled: Boolean = true,
    itemContent: @Composable (T) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    var expanded by remember { mutableStateOf(emptyList<T>()) }
    val borderColor = AppThemeColors.grey3
    val borderSize = 1.dp
    val interactionSource = remember {
        object : MutableInteractionSource {
            override val interactions = MutableSharedFlow<Interaction>(
                extraBufferCapacity = 16,
                onBufferOverflow = BufferOverflow.DROP_OLDEST,
            )

            override suspend fun emit(interaction: Interaction) {
                if (interaction is PressInteraction.Release) {
                    Napier.d("SelectView clicked")
                    focusManager.clearFocus()
                    expanded = if (expanded.isEmpty()) itemList else emptyList()
                }

                interactions.emit(interaction)
            }

            override fun tryEmit(interaction: Interaction): Boolean {
                return interactions.tryEmit(interaction)
            }
        }
    }
    OutlinedInput(
        modifier = modifier
            .fillMaxWidth(),
        text = text,
        enabled = enabled,
        readOnly = true,
        onValueChanged = {},
        label = label,
        trailingIcon = {
            Image(
                painterResource(if (expanded.isNotEmpty()) Res.drawable.icon_chevron_up else Res.drawable.icon_chevron_down),
                label
            )
        },
        interactionSource = interactionSource
    )
    LazyColumn(
        state = rememberLazyListState(),
        modifier = modifier.heightIn(max = 72.dp * itemList.size, min = 0.dp)
            .padding(vertical = 8.dp)
            .background(AppThemeColors.white)
            .border(borderSize, borderColor, RoundedCornerShape(8.dp)),
    ) {
        items(expanded.size) { position ->
            val rowModifier = Modifier
                .padding(vertical = 16.dp, horizontal = 24.dp)
            Box(
                modifier = Modifier
                    .bottomDivider(borderSize, borderColor, 10.dp)
                    .clickable {
                        onSelectionChanged(expanded[position])
                        expanded = emptyList()
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = rowModifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    itemContent(expanded[position])
                }
            }
        }
    }
}