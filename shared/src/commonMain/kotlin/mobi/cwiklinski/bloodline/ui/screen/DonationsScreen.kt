package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
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
        val donations by screenModel.donations.collectAsStateWithLifecycle(emptyList())

        Box(
            modifier = Modifier.background(AppThemeColors.homeGradient)
                .padding(top = 20.dp)
        ) {
            MobileScaffold(
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
            ) { paddingValue ->
                Box(
                    modifier = Modifier.padding(paddingValue)
                        .background(
                            SolidColor(AppThemeColors.background),
                            RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                        )
                ) {
                    LazyVerticalGrid(
                        modifier = Modifier.fillMaxWidth()
                            .padding(vertical = 5.dp),
                        columns = getDonationGridSize(),
                        verticalArrangement = Arrangement.Top
                    ) {
                        items(donations) { donation ->
                            DonationItem(
                                donation,
                                { navigator.push(EditDonationScreen(donation)) },
                                { screenModel.deleteDonation(donation) },
                                { }
                            )
                        }
                    }
                }
            }
        }
    }
}