package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import mobi.cwiklinski.bloodline.getDonationGridSize
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.close
import mobi.cwiklinski.bloodline.resources.donationsDelete
import mobi.cwiklinski.bloodline.resources.donationsDeleteMessage
import mobi.cwiklinski.bloodline.resources.donationsDeleteTitle
import mobi.cwiklinski.bloodline.resources.donationsTitle
import mobi.cwiklinski.bloodline.resources.homeSectionHistoryAddDonationEmptyText
import mobi.cwiklinski.bloodline.resources.homeSectionHistoryEmptyText
import mobi.cwiklinski.bloodline.ui.event.HandleSideEffect
import mobi.cwiklinski.bloodline.ui.event.SideEffects
import mobi.cwiklinski.bloodline.ui.model.CenterScreenModel
import mobi.cwiklinski.bloodline.ui.model.DonationScreenModel
import mobi.cwiklinski.bloodline.ui.model.DonationState
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.contentText
import mobi.cwiklinski.bloodline.ui.theme.contentTitle
import mobi.cwiklinski.bloodline.ui.theme.toolbarSubTitle
import mobi.cwiklinski.bloodline.ui.util.NavigationItem
import mobi.cwiklinski.bloodline.ui.widget.DesktopNavigationTitleScaffold
import mobi.cwiklinski.bloodline.ui.widget.DonationItem
import mobi.cwiklinski.bloodline.ui.widget.InformationDialog
import mobi.cwiklinski.bloodline.ui.widget.MobileLandscapeNavigationTitleLayout
import mobi.cwiklinski.bloodline.ui.widget.MobilePortraitNavigationTitleLayout
import mobi.cwiklinski.bloodline.ui.widget.SecondaryButton
import mobi.cwiklinski.bloodline.ui.widget.SubmitButton
import org.jetbrains.compose.resources.stringResource

class DonationsScreen : AppScreen() {

    @Composable
    override fun Content() {
        super.Content()
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val screenModel = navigator.koinNavigatorScreenModel<CenterScreenModel>()
        HandleSideEffect(screenModel.sideEffect) { effect ->
            if (effect is SideEffects.InformationDialog) {
                bottomSheetNavigator.show(InformationScreen(title = effect.title, message = effect.message))
            }
        }
    }

    @Composable
    override fun defaultView() {
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        MobilePortraitNavigationTitleLayout(
            title = stringResource(Res.string.donationsTitle),
            selected = NavigationItem.LIST,
            navigationAction = { navigationItem ->
                when (navigationItem) {
                    NavigationItem.LIST -> {
                        navigator.push(DonationsScreen())
                    }
                    NavigationItem.CENTER -> {
                        navigator.push(CentersScreen())
                    }
                    NavigationItem.PROFILE -> {
                        navigator.push(ProfileScreen())
                    }
                    else -> {
                        navigator.push(HomeScreen())
                    }
                }
            },
            floatingAction = {
                bottomSheetNavigator.show(NewDonationScreen())
            }
        ) { paddingValues ->
            DonationsView(paddingValues)
        }
    }

    @Composable
    override fun tabletView() {
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        MobileLandscapeNavigationTitleLayout(
            title = stringResource(Res.string.donationsTitle),
            selected = NavigationItem.LIST,
            navigationAction = { navigationItem ->
                when (navigationItem) {
                    NavigationItem.LIST -> {
                        navigator.push(DonationsScreen())
                    }
                    NavigationItem.CENTER -> {
                        navigator.push(CentersScreen())
                    }
                    NavigationItem.PROFILE -> {
                        navigator.push(ProfileScreen())
                    }
                    else -> {
                        navigator.push(HomeScreen())
                    }
                }
            },
            floatingAction = {
                bottomSheetNavigator.show(NewDonationScreen())
            },
            desiredContent = {
                DonationsView(PaddingValues(0.dp))
            }
        )
    }

    @Composable
    override fun desktopView() {
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        DesktopNavigationTitleScaffold(
            title = stringResource(Res.string.donationsTitle),
            selected = NavigationItem.LIST,
            navigationAction = { navigationItem ->
                when (navigationItem) {
                    NavigationItem.LIST -> {
                        navigator.push(DonationsScreen())
                    }
                    NavigationItem.CENTER -> {
                        navigator.push(CentersScreen())
                    }
                    NavigationItem.PROFILE -> {
                        navigator.push(ProfileScreen())
                    }
                    else -> {
                        navigator.push(HomeScreen())
                    }
                }
            },
            floatingAction = {
                bottomSheetNavigator.show(NewDonationScreen())
            },
            desiredContent = {
                DonationsView(PaddingValues(0.dp))
            }
        )
    }

    @Composable
    fun DonationsView(paddingValues: PaddingValues) {
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val screenModel = navigator.koinNavigatorScreenModel<DonationScreenModel>()
        val donations by screenModel.donations.collectAsStateWithLifecycle(emptyList())
        val state by screenModel.state.collectAsStateWithLifecycle(DonationState.Idle)
        var informationTitle by remember { mutableStateOf("") }
        var informationMessage by remember { mutableStateOf("") }
        var informationShow by remember { mutableStateOf(false) }
        InformationDialog(
            title = informationTitle,
            message = informationMessage,
            show = informationShow) {
            informationShow = false
        }
        if (state is DonationState.ToDelete) {
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
                            screenModel.deleteDonation((state as DonationState.ToDelete).donation)
                        },
                        text = stringResource(Res.string.donationsDelete)
                    )
                },
                dismissButton = {
                    SecondaryButton(
                        onClick = {
                            screenModel.clearError()
                        },
                        text = stringResource(Res.string.close)
                    )
                }
            )
        }
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
                .background(
                    SolidColor(AppThemeColors.background),
                    RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
        ) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxWidth()
                    .background(AppThemeColors.background)
                    .padding(vertical = 5.dp),
                columns = if (donations.isNotEmpty()) getDonationGridSize() else GridCells.Fixed(1),
                verticalArrangement = Arrangement.Top
            ) {
                if (donations.isNotEmpty()) {
                    items(donations) { donation ->
                        DonationItem(
                            donation,
                            { navigator.push(EditDonationScreen(donation)) },
                            { screenModel.markToDelete(donation) },
                            { }
                        )
                    }
                } else {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth().height(400.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                stringResource(Res.string.homeSectionHistoryEmptyText),
                                style = contentTitle(),
                                modifier = Modifier.padding(10.dp)
                            )
                            Text(
                                stringResource(Res.string.homeSectionHistoryAddDonationEmptyText),
                                style = toolbarSubTitle().copy(
                                    textDecoration = TextDecoration.Underline
                                ),
                                modifier = Modifier.padding(10.dp).clickable {
                                    bottomSheetNavigator.show(NewDonationScreen())
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}