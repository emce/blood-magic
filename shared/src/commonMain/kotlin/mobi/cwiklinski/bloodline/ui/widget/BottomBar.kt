package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.ui.theme.AppPreview
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.util.BottomNavigationItem
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun BottomBar(
    onClicked: (BottomNavigationItem) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth().wrapContentHeight().background(AppThemeColors.homeGradient),
    selected: BottomNavigationItem = BottomNavigationItem.HOME
) {
    AppPreview {
        BottomAppBar(
            modifier = modifier.padding(0.dp),
            containerColor = Color.Transparent,
            contentPadding = PaddingValues(0.dp),
            tonalElevation = 0.dp,
        ) {
            NavigationBar(
                containerColor = Color.Transparent,
                contentColor = Color.Transparent
            ) {
                BottomNavigationItem.entries.forEach { item ->
                    NavigationBarItem(
                        selected = item == selected,
                        enabled = true,
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
                            onClicked(item)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AppThemeColors.red3,
                            unselectedIconColor = AppThemeColors.white
                        )
                    )
                    if (item == BottomNavigationItem.LIST) {
                        Spacer(Modifier.weight(1.0f))
                    }
                }
            }
        }
    }
}