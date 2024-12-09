package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.NavigationRailItem
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import mobi.cwiklinski.bloodline.getOrientation
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.donationNewTitle
import mobi.cwiklinski.bloodline.resources.nav_icon_drop
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.util.BottomNavigationItem
import mobi.cwiklinski.bloodline.ui.util.avatarShadow
import mobi.cwiklinski.bloodline.ui.widget.BottomBar
import mobi.cwiklinski.bloodline.ui.widget.HorizontalTitleAppBar
import mobi.cwiklinski.bloodline.ui.widget.LeftNavigation
import mobi.cwiklinski.bloodline.ui.widget.isHorizontal
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.core.component.KoinComponent

abstract class AppScreen : Screen, KoinComponent {

    @Composable
    override fun Content() {
        if (getOrientation().isHorizontal()) {
            horizontalView()
        } else {
            verticalView()
        }
    }

    @Composable
    abstract fun verticalView()

    @Composable
    abstract fun horizontalView()


    @Composable
    fun VerticalScaffold(
        modifier: Modifier = Modifier,
        topBar: @Composable () -> Unit = {},
        backgroundColor: Color = AppThemeColors.white,
        content: @Composable (PaddingValues) -> Unit
    ) {
        val bottomNavigator = LocalBottomSheetNavigator.current
        val navigator = LocalNavigator.currentOrThrow
        Scaffold(
            modifier = modifier.background(AppThemeColors.homeGradient),
            topBar = topBar,
            backgroundColor = backgroundColor,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        bottomNavigator.show(NewDonationScreen())
                    },
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
                    onClicked = { item ->
                        navigator.push(when (item) {
                            BottomNavigationItem.HOME -> HomeScreen()
                            BottomNavigationItem.LIST -> DonationsScreen()
                            BottomNavigationItem.CENTER -> CentersScreen()
                            BottomNavigationItem.PROFILE -> ProfileScreen()
                        })
                    },
                    selected = when (this) {
                        is DonationsScreen -> BottomNavigationItem.LIST
                        is CentersScreen -> BottomNavigationItem.CENTER
                        is ProfileScreen -> BottomNavigationItem.PROFILE
                        else -> BottomNavigationItem.HOME
                    }
                )
            },
            content = content
        )
    }

    @Composable
    fun HorizontalScaffold(
        modifier: Modifier = Modifier,
        title: String? = null,
        actions: @Composable (() -> Unit)? = null,
        desiredContent: @Composable (PaddingValues) -> Unit
    ) {
        val bottomNavigator = LocalBottomSheetNavigator.current
        val navigator = LocalNavigator.currentOrThrow
        Scaffold(
            modifier = modifier.padding(4.dp),
            backgroundColor = AppThemeColors.background,
        ) { paddingValues ->
            ConstraintLayout(
                modifier = Modifier.fillMaxSize().padding(paddingValues)
            ) {
                val (navigation, content, bar) = createRefs()
                LeftNavigation(
                    modifier = Modifier
                        .width(HORIZONTAL_MENU_WIDTH)
                        .constrainAs(navigation) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            height = Dimension.fillToConstraints
                        },
                    onClicked = { item ->
                        navigator.push(when (item) {
                            BottomNavigationItem.HOME -> HomeScreen()
                            BottomNavigationItem.LIST -> DonationsScreen()
                            BottomNavigationItem.CENTER -> CentersScreen()
                            BottomNavigationItem.PROFILE -> ProfileScreen()
                        })
                    },
                    selected = getSelected(),
                    floatingActionButton = {
                        NavigationRailItem(
                            selected = false,
                            onClick = {
                                bottomNavigator.show(NewDonationScreen())
                            },
                            icon = {
                                Image(
                                    painterResource(Res.drawable.nav_icon_drop),
                                    stringResource(Res.string.donationNewTitle),
                                    modifier = Modifier.avatarShadow(
                                        color = AppThemeColors.white,
                                        sizeAdjustment = 0.5f
                                    )
                                )
                            }
                        )
                    }
                )
                HorizontalTitleAppBar(
                    modifier.constrainAs(bar) {
                        start.linkTo(navigation.end, 2.dp)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                        if (title == null) {
                            height = Dimension.value(0.dp)
                        }
                    },
                    title = title,
                    actions = actions
                )
                Box(
                    modifier = Modifier
                        .background(AppThemeColors.white)
                        .constrainAs(content) {
                            start.linkTo(navigation.end, 2.dp)
                            top.linkTo(bar.bottom)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        }
                ) {
                    desiredContent.invoke(PaddingValues(0.dp))
                }
            }
        }
    }

    @Composable
    fun getSelected() = when (this) {
        is DonationsScreen -> BottomNavigationItem.LIST
        is CentersScreen -> BottomNavigationItem.CENTER
        is ProfileScreen -> BottomNavigationItem.PROFILE
        else -> BottomNavigationItem.HOME
    }

    companion object {
        val HORIZONTAL_MENU_WIDTH = 80.dp
    }

}