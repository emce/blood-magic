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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import mobi.cwiklinski.bloodline.LocalSnackBar
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
    navigationIcon: @Composable (() -> Unit)? = null,
    title: String,
    actions: @Composable (() -> Unit)? = null,
    floatingAction: () -> Unit = {},
    navigationAction: (NavigationItem) -> Unit,
    selected: NavigationItem = NavigationItem.HOME,
    desiredContent: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .background(AppThemeColors.homeGradient),
        topBar = {
            MobileTitleBar(
                navigationIcon = navigationIcon,
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
        snackbarHost = { SnackbarHost(LocalSnackBar.current) },
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
        snackbarHost = { SnackbarHost(LocalSnackBar.current) },
        content = desiredContent
    )
}


@Composable
fun MobileLandscapeNavigationTitleLayout(
    modifier: Modifier = Modifier,
    backgroundColor: Color = AppThemeColors.white,
    navigationIcon: @Composable (() -> Unit)? = null,
    title: String,
    actions: @Composable (() -> Unit)? = null,
    floatingAction: () -> Unit = {},
    navigationAction: (NavigationItem) -> Unit,
    selected: NavigationItem = NavigationItem.HOME,
    infoClicked: (() -> Unit)? = null,
    desiredContent: @Composable () -> Unit
) {
    MobileLandscapeNavigationLayout(
        modifier = modifier,
        backgroundColor = backgroundColor,
        topBar = {
            MobileTitleBar(
                navigationIcon = navigationIcon,
                title = title,
                actions = actions
            )
        },
        floatingAction = floatingAction,
        navigationAction = navigationAction,
        selected = selected,
        infoClicked = infoClicked,
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
    infoClicked: (() -> Unit)? = null,
    desiredContent: @Composable () -> Unit
) {
    Scaffold(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .background(AppThemeColors.homeGradient),
        backgroundColor = backgroundColor,
        snackbarHost = { SnackbarHost(LocalSnackBar.current) },
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
                },
                infoClicked = infoClicked
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
    navigationIcon: @Composable (() -> Unit)? = null,
    title: String,
    actions: @Composable (() -> Unit)? = null,
    desiredContent: @Composable (PaddingValues) -> Unit
) {
    MobileLayout(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.safeDrawing),
        topBar = {
            MobileTitleBar(
                navigationIcon = navigationIcon,
                title = title,
                actions = actions
            )
        },
        backgroundColor = backgroundColor,
        desiredContent = desiredContent
    )
}

@Composable
fun MobileLayout(
    modifier: Modifier = Modifier,
    backgroundColor: Color = AppThemeColors.white,
    topBar: @Composable () -> Unit = {},
    desiredContent: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.safeDrawing),
        topBar = topBar,
        backgroundColor = backgroundColor,
        snackbarHost = { SnackbarHost(LocalSnackBar.current) },
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