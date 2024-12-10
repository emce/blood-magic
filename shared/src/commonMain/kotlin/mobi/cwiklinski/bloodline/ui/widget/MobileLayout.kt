package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.donationNewTitle
import mobi.cwiklinski.bloodline.resources.nav_icon_drop
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.util.NavigationItem
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun MobilePortraitNavigationTitleLayout(
    modifier: Modifier = Modifier,
    backgroundColor: Color = AppThemeColors.white,
    title: String,
    actions: @Composable (() -> Unit)? = null,
    floatingAction: () -> Unit = {},
    navigationAction: (NavigationItem) -> Unit,
    selected: NavigationItem = NavigationItem.HOME,
    snackBarState: SnackbarHostState = remember { SnackbarHostState() },
    desiredContent: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .background(AppThemeColors.homeGradient),
        topBar = {
            MobileTitleBar(
                title = title,
                actions = actions
            )
        },
        backgroundColor = backgroundColor,
        floatingActionButton = {
            FloatingActionButton(
                onClick = floatingAction,
                backgroundColor = Color.Transparent,
                contentColor = Color.Transparent
            ) {
                Image(
                    painterResource(Res.drawable.nav_icon_drop),
                    stringResource(Res.string.donationNewTitle)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        bottomBar = @Composable {
            BottomBar(
                onClicked = navigationAction,
                selected = selected
            )
        },
        snackbarHost = { SnackbarHost(snackBarState) },
        content = desiredContent
    )
}

@Composable
fun MobilePortraitNavigationLayout(
    modifier: Modifier = Modifier,
    backgroundColor: Color = AppThemeColors.white,
    topBar: @Composable () -> Unit = {},
    floatingAction: () -> Unit = {},
    navigationAction: (NavigationItem) -> Unit,
    selected: NavigationItem = NavigationItem.HOME,
    snackBarState: SnackbarHostState = remember { SnackbarHostState() },
    desiredContent: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .background(AppThemeColors.homeGradient),
        topBar = topBar,
        backgroundColor = backgroundColor,
        floatingActionButton = {
            FloatingActionButton(
                onClick = floatingAction,
                backgroundColor = Color.Transparent,
                contentColor = Color.Transparent
            ) {
                Image(
                    painterResource(Res.drawable.nav_icon_drop),
                    stringResource(Res.string.donationNewTitle)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        bottomBar = @Composable {
            BottomBar(
                onClicked = navigationAction,
                selected = selected
            )
        },
        snackbarHost = { SnackbarHost(snackBarState) },
        content = desiredContent
    )
}


@Composable
fun MobileLandscapeNavigationTitleLayout(
    modifier: Modifier = Modifier,
    backgroundColor: Color = AppThemeColors.white,
    title: String,
    actions: @Composable (() -> Unit)? = null,
    floatingAction: () -> Unit = {},
    navigationAction: (NavigationItem) -> Unit,
    selected: NavigationItem = NavigationItem.HOME,
    snackBarState: SnackbarHostState = remember { SnackbarHostState() },
    desiredContent: @Composable () -> Unit
) {
    MobileLandscapeNavigationLayout(
        modifier = modifier,
        backgroundColor = backgroundColor,
        topBar = {
            MobileTitleBar(
                title = title,
                actions = actions
            )
        },
        floatingAction = floatingAction,
        navigationAction = navigationAction,
        selected = selected,
        snackBarState = snackBarState,
        desiredContent = desiredContent
    )
}


@Composable
fun MobileLandscapeNavigationLayout(
    modifier: Modifier = Modifier,
    backgroundColor: Color = AppThemeColors.white,
    topBar: @Composable () -> Unit = {},
    floatingAction: () -> Unit = {},
    navigationAction: (NavigationItem) -> Unit,
    selected: NavigationItem = NavigationItem.HOME,
    snackBarState: SnackbarHostState = remember { SnackbarHostState() },
    desiredContent: @Composable () -> Unit
) {
    Scaffold(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .background(AppThemeColors.homeGradient),
        backgroundColor = backgroundColor,
        snackbarHost = { SnackbarHost(snackBarState) },
    ) { paddingValues ->
        Row(
            modifier = Modifier.padding(paddingValues),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            LeftNavigation(
                modifier = Modifier,
                onClicked = navigationAction,
                selected = selected,
                floatingActionButton = {
                    FloatingButton(floatingAction = floatingAction)
                }
            )
            Column(
                modifier = Modifier.weight(1.0f)
            ) {
                topBar.invoke()
                desiredContent.invoke()
            }
        }
    }
}

@Composable
fun MobileLayoutWithTitle(
    modifier: Modifier = Modifier,
    backgroundColor: Color = AppThemeColors.white,
    title: String,
    actions: @Composable (() -> Unit)? = null,
    snackBarState: SnackbarHostState = remember { SnackbarHostState() },
    desiredContent: @Composable (PaddingValues) -> Unit
) {
    MobileLayout(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.safeDrawing),
        topBar = {
            MobileTitleBar(
                title = title,
                actions = actions
            )
        },
        backgroundColor = backgroundColor,
        snackBarState = snackBarState,
        desiredContent = desiredContent
    )
}

@Composable
fun MobileLayout(
    modifier: Modifier = Modifier,
    backgroundColor: Color = AppThemeColors.white,
    topBar: @Composable () -> Unit = {},
    snackBarState: SnackbarHostState = remember { SnackbarHostState() },
    desiredContent: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.safeDrawing),
        topBar = topBar,
        backgroundColor = backgroundColor,
        snackbarHost = { SnackbarHost(snackBarState) },
        content = desiredContent
    )
}

@Composable
fun FloatingButton(floatingAction: () -> Unit = {}) {
    FloatingActionButton(
        onClick = floatingAction,
        backgroundColor = Color.Transparent,
        contentColor = Color.Transparent
    ) {
        Image(
            painterResource(Res.drawable.nav_icon_drop),
            stringResource(Res.string.donationNewTitle)
        )
    }
}