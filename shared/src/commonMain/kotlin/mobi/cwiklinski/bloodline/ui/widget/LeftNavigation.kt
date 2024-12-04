package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.ui.screen.AppScreen
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.util.BottomNavigationItem
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun LeftNavigation(
    onClicked: (BottomNavigationItem) -> Unit,
    modifier: Modifier = Modifier,
    selected: BottomNavigationItem = BottomNavigationItem.HOME,
    floatingActionButton: @Composable (() -> Unit)? = null,
) {
    NavigationRail(
        modifier = modifier
            .background(
                Brush.horizontalGradient(
                    0.0f to Color(0xFFE1C7D3),
                    1f to Color(0xFFB794A5),
                    startX = 0.0f,
                    endX =  AppScreen.HORIZONTAL_MENU_WIDTH.value,
                ),
                RoundedCornerShape(8.dp)
            ),
        elevation = 0.dp,
        contentColor = AppThemeColors.rose4,
        backgroundColor = Color.Transparent
    ) {
        BottomNavigationItem.entries.forEach { item ->
            NavigationRailItem(
                modifier = Modifier.padding(vertical = 5.dp),
                selected = item == selected,
                onClick = {
                    onClicked.invoke(item)
                },
                icon = {
                    Image(
                        painterResource(item.icon),
                        contentDescription = stringResource(item.title)
                    )
                },
                alwaysShowLabel = false,
                selectedContentColor = AppThemeColors.black,
                unselectedContentColor = AppThemeColors.white
            )
        }
        Spacer(Modifier.weight(1f))
        floatingActionButton?.invoke()
        Spacer(Modifier.height(20.dp))
    }
}