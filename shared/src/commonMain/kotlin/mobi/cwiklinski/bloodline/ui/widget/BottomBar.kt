package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.util.NavigationItem
import org.jetbrains.compose.resources.painterResource

@Composable
fun BottomBar(
    onClicked: (NavigationItem) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth().wrapContentHeight(),
    selected: NavigationItem = NavigationItem.HOME
) {
    val componentHeight = 78.dp
    BottomAppBar(
        backgroundColor = AppThemeColors.white,
        contentColor = AppThemeColors.white,
        contentPadding = PaddingValues(0.dp),
        cutoutShape = CircleShape,
        modifier = modifier.padding(0.dp)
            .height(componentHeight)
    ) {
        BottomNavigation(
            backgroundColor = Color.Transparent,
            contentColor = Color.Transparent,
            elevation = 0.dp,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0.0f to Color(0xFFB794A5),
                        1.0f to Color(0xFFE1C7D3),
                    )
                ),
        ) {
            NavigationItem.entries.forEach { item ->
                BottomNavigationItem(
                    selected = item == selected,
                    enabled = item != selected,
                    icon = {
                        Icon(
                            painterResource(item.icon),
                            item.name,
                            tint = if (item == selected) AppThemeColors.red3 else AppThemeColors.white,
                            modifier = Modifier.size(48.dp)
                        )
                    },
                    alwaysShowLabel = false,
                    onClick = {
                        onClicked.invoke(item)
                    })
                if (item == NavigationItem.LIST) {
                    Spacer(Modifier.weight(1.0f))
                }
            }
        }
    }
}