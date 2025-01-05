package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import mobi.cwiklinski.bloodline.LocalSnackBar
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.util.NavigationItem

@Composable
fun DesktopWithTitleScaffold(
    modifier: Modifier = Modifier,
    backgroundColor: Color = AppThemeColors.white,
    title: String,
    actions: @Composable (() -> Unit)? = null,
    desiredContent: @Composable (PaddingValues) -> Unit
) {
    DesktopScaffold(
        modifier = modifier,
        backgroundColor = backgroundColor,
        topBar = {
            DesktopTitleBar(
                title = title,
                actions = actions
            )
        },
        desiredContent = desiredContent
    )
}

@Composable
fun DesktopNavigationTitleScaffold(
    modifier: Modifier = Modifier,
    backgroundColor: Color = AppThemeColors.white,
    title: String,
    actions: @Composable (() -> Unit)? = null,
    selected: NavigationItem = NavigationItem.HOME,
    navigationAction: (NavigationItem) -> Unit,
    floatingAction: () -> Unit = {},
    infoClicked: (() -> Unit)? = null,
    desiredContent: @Composable () -> Unit
) {
    DesktopNavigationScaffold(
        modifier = modifier,
        backgroundColor = backgroundColor,
        topBar = {
            DesktopTitleBar(
                title = title,
                actions = actions
            )
        },
        selected = selected,
        navigationAction = navigationAction,
        floatingAction = floatingAction,
        informationAction = infoClicked,
        desiredContent = desiredContent
    )
}

@Composable
fun DesktopNavigationScaffold(
    modifier: Modifier = Modifier,
    backgroundColor: Color = AppThemeColors.white,
    topBar: @Composable () -> Unit = {},
    selected: NavigationItem = NavigationItem.HOME,
    navigationAction: (NavigationItem) -> Unit,
    floatingAction: () -> Unit = {},
    informationAction: (() -> Unit)? = null,
    desiredContent: @Composable () -> Unit
) {
    DesktopScaffold(
        modifier = modifier,
        backgroundColor = backgroundColor,
    ) { paddingValues ->
        DesktopNavigation(
            modifier = Modifier.padding(paddingValues),
            onClicked = navigationAction,
            selected = selected,
            floatingAction = floatingAction,
            informationAction = informationAction
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
                    .background(
                        AppThemeColors.homeGradient
                    )
            ) {
                topBar.invoke()
                desiredContent.invoke()
            }
        }
    }
}

@Composable
fun DesktopScaffold(
    modifier: Modifier = Modifier,
    backgroundColor: Color = AppThemeColors.white,
    topBar: @Composable () -> Unit = {},
    desiredContent: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        backgroundColor = backgroundColor,
        topBar = topBar,
        snackbarHost = {
            SnackbarHost(LocalSnackBar.current)
        },
        content = desiredContent
    )
}