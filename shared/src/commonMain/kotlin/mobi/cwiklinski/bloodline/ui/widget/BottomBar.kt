package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.util.BottomNavigationItem
import org.jetbrains.compose.resources.painterResource

@Composable
fun BottomBar(
    onClicked: (BottomNavigationItem) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth().wrapContentHeight(),
    selected: BottomNavigationItem = BottomNavigationItem.HOME
) {
    val density = LocalDensity.current
    var componentHeight by remember { mutableStateOf(0.dp) }
    BottomAppBar(
        backgroundColor = AppThemeColors.white,
        contentColor = AppThemeColors.white,
        contentPadding = PaddingValues(0.dp),
        cutoutShape = CircleShape,
        modifier = modifier.padding(0.dp)
            .onGloballyPositioned {
                componentHeight = with(density) {
                    it.size.height.toDp()
                }
            }
    ) {
        BottomNavigation(
            backgroundColor = Color.Transparent,
            contentColor = Color.Transparent,
            elevation = 0.dp,
            modifier = Modifier.background(
                Brush.verticalGradient(
                    0.0f to Color(0xFFB794A5),
                    0.2f to Color(0xFFB794A5),
                    1f to Color(0xFFE1C7D3),
                    startY = 0.0f,
                    endY =  componentHeight.value
                )
            ),
        ) {
            BottomNavigationItem.entries.forEach { item ->
                BottomNavigationItem(
                    selected = item == selected,
                    enabled = item != selected,
                    icon = {
                        Image(
                            painterResource(item.icon),
                            item.name,
                            modifier = Modifier.padding(4.dp),
                            colorFilter = ColorFilter.tint(if (item == selected) AppThemeColors.red3 else AppThemeColors.white)
                        )
                    },
                    alwaysShowLabel = false,
                    onClick = {
                        onClicked.invoke(item)
                    })
                if (item == BottomNavigationItem.LIST) {
                    Spacer(Modifier.weight(1.0f))
                }
            }
        }
    }
}