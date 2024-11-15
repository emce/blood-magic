package mobi.cwiklinski.bloodline.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
    BottomAppBar(
        containerColor = AppThemeColors.rose2,
        contentPadding = PaddingValues(0.dp),
        modifier = modifier.padding(0.dp).background(AppThemeColors.mainGradient),
    ) {
        NavigationBar(
            modifier = Modifier.background(Color.Transparent),
            containerColor = Color.Transparent,
            contentColor = Color.Transparent
        ) {
            BottomNavigationItem.entries.forEach { item ->
                NavigationBarItem(
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
                        onClicked(item)
                    })
                if (item == BottomNavigationItem.LIST) {
                    Spacer(Modifier.weight(1.0f))
                }
            }
        }
    }
}