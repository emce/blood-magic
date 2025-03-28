package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
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
import mobi.cwiklinski.bloodline.DeviceOrientation
import mobi.cwiklinski.bloodline.getDeviceOrientation
import mobi.cwiklinski.bloodline.isTablet
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.ic_drop
import mobi.cwiklinski.bloodline.resources.infoTitle
import mobi.cwiklinski.bloodline.resources.navAbout
import mobi.cwiklinski.bloodline.resources.navAdd
import mobi.cwiklinski.bloodline.resources.nav_icon_info
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
    informationAction: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .background(
                Brush.horizontalGradient(
                    0.0f to Color(0xFFE1C7D3),
                    1.0f to Color(0xFFB794A5),
                )
            )
            .padding(vertical = 10.dp)
    ) {
        if (isTablet() && getDeviceOrientation() == DeviceOrientation.LANDSCAPE_LEFT) {
            val systemBarsPadding = WindowInsets.systemBars.asPaddingValues()
            Spacer(modifier = Modifier.width(systemBarsPadding.calculateTopPadding()))
        }
        PermanentNavigationDrawer(
            modifier = modifier,
            drawerContent = {
                PermanentDrawerSheet(
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
                    NavigationDrawerItem(
                        selected = false,
                        onClick = floatingAction,
                        shape = RoundedCornerShape(8.dp),
                        icon = {
                            Image(
                                painter = painterResource(Res.drawable.ic_drop),
                                contentDescription = stringResource(Res.string.navAdd)
                            )
                        },
                        label = {
                            Text(text = stringResource(Res.string.navAdd))
                        },
                        modifier = Modifier.padding(horizontal = 12.dp),
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    NavigationDrawerItem(
                        selected = false,
                        onClick = {
                            informationAction?.invoke()
                        },
                        shape = RoundedCornerShape(8.dp),
                        icon = {
                            Image(
                                painter = painterResource(Res.drawable.nav_icon_info),
                                contentDescription = stringResource(Res.string.infoTitle)
                            )
                        },
                        label = {
                            Text(text = stringResource(Res.string.navAbout))
                        },
                        modifier = Modifier.padding(horizontal = 12.dp),
                    )
                }
            },
            content = content
        )

    }
}