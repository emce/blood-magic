package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import mobi.cwiklinski.bloodline.getDonationGridSize
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.donationsTitle
import mobi.cwiklinski.bloodline.ui.model.DonationScreenModel
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.toolbarTitle
import mobi.cwiklinski.bloodline.ui.widget.DonationItem
import org.jetbrains.compose.resources.stringResource

class DonationsScreen : AppScreen() {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<DonationScreenModel>()
        val donations by screenModel.donations.collectAsStateWithLifecycle(emptyList())
        Box(
            modifier = Modifier.background(AppThemeColors.homeGradient)
                .padding(top = 20.dp)
        ) {
            Scaffold(
                contentColor = AppThemeColors.background,
                topBar = {
                    TopAppBar(
                        modifier = Modifier.background(AppThemeColors.homeGradient),
                        title = {
                            Text(
                                stringResource(Res.string.donationsTitle),
                                style = toolbarTitle().copy(color = AppThemeColors.red2),
                                modifier = Modifier.fillMaxWidth().align(Alignment.CenterStart).padding(20.dp)
                                    .background(Color.Transparent)
                            )
                        },
                        colors = AppThemeColors.topBarColors()
                    )
                },
                floatingActionButtonPosition = FabPosition.Center,
                /**
                 * @TODO
                 */
                //isFloatingActionButtonDocked = true,
                floatingActionButton = { getFAB() },
                bottomBar = { getBottomBar() }
            ) { paddingValue ->
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxWidth()
                        .padding(paddingValue)
                        .background(
                            SolidColor(AppThemeColors.background),
                            RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                        ),
                    columns = getDonationGridSize(),
                ) {
                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                    items(donations) { donation ->
                        DonationItem(
                            donation,
                            { navigator.push(EditDonationScreen(donation)) },
                            { screenModel.deleteDonation(donation) },
                            {

                            }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
            }
        }
    }
}