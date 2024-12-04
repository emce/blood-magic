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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import mobi.cwiklinski.bloodline.domain.model.Donation
import mobi.cwiklinski.bloodline.getDonationGridSize
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.close
import mobi.cwiklinski.bloodline.resources.donationsDelete
import mobi.cwiklinski.bloodline.resources.donationsDeleteMessage
import mobi.cwiklinski.bloodline.resources.donationsDeleteTitle
import mobi.cwiklinski.bloodline.resources.donationsTitle
import mobi.cwiklinski.bloodline.resources.homeTitle
import mobi.cwiklinski.bloodline.resources.home_stars
import mobi.cwiklinski.bloodline.ui.model.DonationScreenModel
import mobi.cwiklinski.bloodline.ui.model.DonationState
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.contentText
import mobi.cwiklinski.bloodline.ui.theme.contentTitle
import mobi.cwiklinski.bloodline.ui.theme.toolbarTitle
import mobi.cwiklinski.bloodline.ui.widget.DonationItem
import mobi.cwiklinski.bloodline.ui.widget.SecondaryButton
import mobi.cwiklinski.bloodline.ui.widget.SubmitButton
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class DonationsScreen : AppScreen() {

    @Composable
    override fun verticalView() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<DonationScreenModel>()
        val donations by screenModel.donations.collectAsStateWithLifecycle(emptyList())
        val state by screenModel.state.collectAsStateWithLifecycle(DonationState.Idle)
        var donationToDelete: Donation? by remember { mutableStateOf(null) }
        if (state == DonationState.Deleted) {
            donationToDelete = null
        }
        Box(
            modifier = Modifier.background(AppThemeColors.homeGradient)
                .padding(top = 20.dp)
        ) {
            if (donationToDelete != null) {
                AlertDialog(
                    onDismissRequest = { },
                    title = {
                        Text(
                            stringResource(Res.string.donationsDeleteTitle),
                            style = contentTitle()
                        )
                    },
                    text = {
                        Text(
                            stringResource(Res.string.donationsDeleteMessage),
                            style = contentText()
                        )
                   },
                    confirmButton = {
                        SubmitButton(
                            onClick = {
                                donationToDelete?.let {
                                    screenModel.deleteDonation(it)
                                }
                            },
                            text = stringResource(Res.string.donationsDelete)
                        )
                    },
                    dismissButton = {
                        SecondaryButton(
                            onClick = {
                                donationToDelete = null
                            },
                            text = stringResource(Res.string.close)
                        )
                    }
                )
            }
            VerticalScaffold(
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
                                { donationToDelete = donation },
                                { }
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    override fun horizontalView() {
        verticalView()
    }
}