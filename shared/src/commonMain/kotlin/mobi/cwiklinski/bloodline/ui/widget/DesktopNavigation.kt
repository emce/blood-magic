package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.ic_drop
import mobi.cwiklinski.bloodline.resources.navAdd
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.util.NavigationItem
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DesktopNavigation(
    onClicked: (NavigationItem) -> Unit,
    modifier: Modifier = Modifier,
    selected: NavigationItem = NavigationItem.HOME,
    floatingAction: () -> Unit,
    content: @Composable () -> Unit
) {
    PermanentNavigationDrawer(
        modifier = modifier.consumeWindowInsets(WindowInsets.safeContent),
        drawerContent = {
            PermanentDrawerSheet(
                modifier = Modifier
                    .background(
                        Brush.horizontalGradient(
                            0.0f to Color(0xFFE1C7D3),
                            1.0f to Color(0xFFB794A5),
                        )
                    )
                    .padding(vertical = 10.dp),
                drawerContainerColor = Color.Transparent,
                drawerContentColor = Color.Transparent,
            ) {
                NavigationItem.entries.filter { it != NavigationItem.OTHER }.forEach { item ->
                    val label = stringResource(item.title)
                    NavigationDrawerItem(
                        selected = item == selected,
                        onClick = { onClicked.invoke(item) },
                        shape = RoundedCornerShape(8.dp),
                        icon = {
                            Icon(
                                painter = painterResource(item.icon),
                                contentDescription = label,
                                tint = if (selected == item) AppThemeColors.white else AppThemeColors.black
                            )
                        },
                        label = {
                            Text(text = label)
                        },
                        modifier = Modifier.padding(horizontal = 12.dp),
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = AppThemeColors.red2,
                            selectedIconColor = AppThemeColors.white,
                            selectedTextColor = AppThemeColors.white,
                        )
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                val label = stringResource(Res.string.navAdd)
                NavigationDrawerItem(
                    selected = false,
                    onClick = floatingAction,
                    shape = RoundedCornerShape(8.dp),
                    icon = {
                        Image(
                            painter = painterResource(Res.drawable.ic_drop),
                            contentDescription = label
                        )
                    },
                    label = {
                        Text(text = label)
                    },
                    modifier = Modifier.padding(horizontal = 12.dp),
                )
            }
        },
        content = content
    )
}