package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.toolbarTitle

@Composable
fun DesktopTitleBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable (() -> Unit)? = null
) {
    TopAppBar(
        modifier = modifier,
        title = {
            if (title != null) {
                Text(
                    title,
                    style = toolbarTitle()
                )
            }
        },
        navigationIcon = {
            navigationIcon?.invoke()
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = AppThemeColors.black,
            navigationIconContentColor = AppThemeColors.black
        ),
        actions = {
            actions?.invoke()
        }
    )
}


@Composable
fun MobileTitleBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable (() -> Unit)? = null,
    behaviour: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
) {
    TopAppBar(
        modifier = modifier,
        title = {
            if (title != null) {
                Text(
                    title,
                    style = toolbarTitle()
                )
            }
        },
        navigationIcon = {
            navigationIcon?.invoke()
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = AppThemeColors.black,
            navigationIconContentColor = AppThemeColors.black
        ),
        scrollBehavior = behaviour,
        actions = {
            actions?.invoke()
        }
    )
}