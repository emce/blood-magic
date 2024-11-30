package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.donationNewTitle
import mobi.cwiklinski.bloodline.resources.nav_icon_drop
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.util.BottomNavigationItem
import mobi.cwiklinski.bloodline.ui.widget.BottomBar
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.core.component.KoinComponent

abstract class AppScreen : Screen, KoinComponent {


    @Composable
    fun MobileScaffold(
        modifier: Modifier = Modifier,
        topBar: @Composable () -> Unit = {},
        backgroundColor: Color = Color.Transparent,
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

}