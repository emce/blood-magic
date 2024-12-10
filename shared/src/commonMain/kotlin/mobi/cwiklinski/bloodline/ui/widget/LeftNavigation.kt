package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.NavigationRail
import androidx.compose.material.NavigationRailItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.util.NavigationItem
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun LeftNavigation(
    onClicked: (NavigationItem) -> Unit,
    modifier: Modifier = Modifier,
    selected: NavigationItem = NavigationItem.HOME,
    floatingActionButton: @Composable (ColumnScope.() -> Unit)? = null,
) {
    NavigationRail(
        modifier = modifier
            .background(
                Brush.horizontalGradient(
                    0.0f to Color(0xFFE1C7D3),
                    1.0f to Color(0xFFB794A5),
                ),
                RoundedCornerShape(8.dp)
            ),
        header = floatingActionButton,
        elevation = 0.dp,
        contentColor = AppThemeColors.rose4,
        backgroundColor = Color.Transparent
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        NavigationItem.entries.forEach { item ->
            NavigationRailItem(
                modifier = Modifier.padding(vertical = 5.dp),
                selected = item.icon == selected.icon,
                onClick = {
                    onClicked.invoke(item)
                },
                icon = {
                    Image(
                        painterResource(item.icon),
                        contentDescription = stringResource(item.title),
                        colorFilter = ColorFilter.tint(
                            if (item == selected) AppThemeColors.black else AppThemeColors.white
                        )
                    )
                },
                alwaysShowLabel = false,
                selectedContentColor = AppThemeColors.black,
                unselectedContentColor = AppThemeColors.white
            )
        }
    }
}