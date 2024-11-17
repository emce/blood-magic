package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.donationNewTitle
import mobi.cwiklinski.bloodline.resources.nav_icon_drop
import mobi.cwiklinski.bloodline.ui.event.Event
import mobi.cwiklinski.bloodline.ui.event.SideEffect
import mobi.cwiklinski.bloodline.ui.screen.donations.DonationsScreen
import mobi.cwiklinski.bloodline.ui.screen.home.HomeScreen
import mobi.cwiklinski.bloodline.ui.screen.new_donation.NewDonationScreen
import mobi.cwiklinski.bloodline.ui.screen.profile.ProfileScreen
import mobi.cwiklinski.bloodline.ui.widget.BottomBar
import mobi.cwiklinski.bloodline.ui.util.BottomNavigationItem
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.core.component.KoinComponent

abstract class AppScreen : Screen, KoinComponent {

    val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    val _sideEffect: Channel<SideEffect> = Channel()
    
    @Composable
    fun getBottomBar() {
        val navigator = LocalNavigator.currentOrThrow
        BottomBar(
            onClicked = { this.getNavigationContext(it, navigator) },
            selected = when (this) {
                is DonationsScreen -> BottomNavigationItem.LIST
                is ProfileScreen -> BottomNavigationItem.PROFILE
                else -> BottomNavigationItem.HOME
            }
        )
    }

    private fun getNavigationContext(
        item: BottomNavigationItem?,
        navigator: Navigator
    ) {
        when (item) {

            BottomNavigationItem.LIST ->
                if (this !is DonationsScreen) {
                    navigator.push(
                        DonationsScreen()
                    )
                }

            BottomNavigationItem.PROFILE ->
                if (this !is ProfileScreen) {
                    navigator.push(
                        ProfileScreen()
                    )
                }
            else -> {
                if (this !is HomeScreen) {
                    navigator.push(
                        HomeScreen()
                    )
                }
            }
        }
    }

    @Composable
    fun getFAB() {
        val navigator = LocalBottomSheetNavigator.current
        FloatingActionButton(
            onClick = {
                navigator.show(NewDonationScreen())
            },
            containerColor = Color.Transparent,
            contentColor = Color.Transparent
        ) {
            Image(
                painterResource(Res.drawable.nav_icon_drop),
                stringResource(Res.string.donationNewTitle)
            )
        }
    }
}