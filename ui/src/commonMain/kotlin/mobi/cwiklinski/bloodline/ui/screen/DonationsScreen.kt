package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.flow.onEach
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.donationsTitle
import mobi.cwiklinski.bloodline.resources.homeTitle
import mobi.cwiklinski.bloodline.resources.home_stars
import mobi.cwiklinski.bloodline.ui.model.DonationScreenModel
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.toolbarTitle
import mobi.cwiklinski.bloodline.ui.widget.DonationItem
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class DonationsScreen : AppScreen() {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<DonationScreenModel>()
        Box(
            modifier = Modifier.background(AppThemeColors.homeGradient)
                .padding(top = 20.dp)
        ) {
            Scaffold(
                contentColor = Color.Transparent,
                topBar = {
                    Box(
                        modifier = Modifier.height(100.dp)
                            .fillMaxWidth()
                            .background(Color.Transparent)
                    ) {
                        Image(
                            painterResource(Res.drawable.home_stars),
                            stringResource(Res.string.homeTitle),
                            modifier = Modifier.padding(20.dp).align(Alignment.Center)
                                .offset(x = 62.dp)
                        )
                        Text(
                            stringResource(Res.string.donationsTitle),
                            style = toolbarTitle(),
                            modifier = Modifier.align(Alignment.CenterStart).padding(20.dp)
                        )
                    }
                },
                floatingActionButtonPosition = FabPosition.Center,
                /**
                 * @TODO
                 */
                //isFloatingActionButtonDocked = true,
                floatingActionButton = { getFAB() },
                bottomBar = { getBottomBar() }
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().background(
                        SolidColor(AppThemeColors.background),
                        RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                    )
                        .scrollable(rememberScrollState(), Orientation.Vertical),
                ) {
                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                    screenModel.donations.onEach { donations ->
                        items(donations) { donation ->
                            DonationItem(
                                donation,
                                { navigator.push(EditDonationScreen(donation)) },
                                { screenModel.deleteDonation(donation) }
                            )
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
            }
        }
    }
}