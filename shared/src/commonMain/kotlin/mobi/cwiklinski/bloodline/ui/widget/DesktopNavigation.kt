package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.material.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.ui.util.NavigationItem
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DesktopNavigation(
    onClicked: (NavigationItem) -> Unit,
    modifier: Modifier = Modifier,
    selected: NavigationItem = NavigationItem.HOME,
    content: @Composable () -> Unit
) {
    PermanentNavigationDrawer(
        modifier = modifier.consumeWindowInsets(WindowInsets.safeContent),
        drawerContent = {
            PermanentDrawerSheet {
                NavigationItem.entries.forEach { item ->
                    val label = stringResource(item.title)
                    NavigationDrawerItem(
                        selected = item == selected,
                        onClick = { onClicked.invoke(item) },
                        icon = {
                            Icon(
                                painter = painterResource(item.icon),
                                contentDescription = label
                            )
                        },
                        label = {
                            Text(text = label)
                        },
                        modifier = Modifier.padding(horizontal = 12.dp),
                        colors = NavigationDrawerItemDefaults.colors()
                    )
                }
            }
        },
        content = content
    )
}