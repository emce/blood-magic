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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import mobi.cwiklinski.bloodline.common.event.SideEffects
import mobi.cwiklinski.bloodline.data.Parcelize
import mobi.cwiklinski.bloodline.domain.model.Donation
import mobi.cwiklinski.bloodline.getDonationGridSize
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.donationsDeleteConfirmationMessage
import mobi.cwiklinski.bloodline.resources.donationsDeleteConfirmationTitle
import mobi.cwiklinski.bloodline.resources.donationsShare
import mobi.cwiklinski.bloodline.resources.donationsTitle
import mobi.cwiklinski.bloodline.resources.homeSectionHistoryAddDonationEmptyText
import mobi.cwiklinski.bloodline.resources.homeSectionHistoryEmptyText
import mobi.cwiklinski.bloodline.resources.liter
import mobi.cwiklinski.bloodline.resources.milliliter
import mobi.cwiklinski.bloodline.ui.model.DonationScreenModel
import mobi.cwiklinski.bloodline.ui.model.DonationState
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.contentTitle
import mobi.cwiklinski.bloodline.ui.theme.toolbarSubTitle
import mobi.cwiklinski.bloodline.ui.util.NavigationItem
import mobi.cwiklinski.bloodline.ui.util.koinNavigatorScreenModel
import mobi.cwiklinski.bloodline.ui.widget.DesktopNavigationTitleScaffold
import mobi.cwiklinski.bloodline.ui.widget.DonationDeleteDialog
import mobi.cwiklinski.bloodline.ui.widget.DonationItem
import mobi.cwiklinski.bloodline.ui.widget.MobileLandscapeNavigationTitleLayout
import mobi.cwiklinski.bloodline.ui.widget.MobilePortraitNavigationTitleLayout
import mobi.cwiklinski.bloodline.ui.widget.capacity
import org.jetbrains.compose.resources.stringResource

@Parcelize
class DonationsScreen : AppScreen() {

    @Composable
    override fun defaultView() {
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        handleSideEffects<DonationState, DonationScreenModel>()
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
            InternalDonationsView(paddingValues)
        }
    }

    @Composable
    override fun tabletView() {
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        handleSideEffects<DonationState, DonationScreenModel>()
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
            infoClicked = {
                navigator.push(AboutScreen())
            },
            desiredContent = {
                InternalDonationsView(PaddingValues(0.dp))
            }
        )
    }

    @Composable
    override fun desktopView() {
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        handleSideEffects<DonationState, DonationScreenModel>()
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
            infoClicked = {
                navigator.push(AboutScreen())
            },
            desiredContent = {
                InternalDonationsView(PaddingValues(0.dp))
            }
        )
    }

    @Composable
    fun InternalDonationsView(paddingValues: PaddingValues) {
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val screenModel = navigator.koinNavigatorScreenModel<DonationScreenModel>()
        val donations by screenModel.donations.collectAsStateWithLifecycle(emptyList())
        val state by screenModel.state.collectAsStateWithLifecycle(DonationState.Idle)
        val deletionTitle = stringResource(Res.string.donationsDeleteConfirmationTitle)
        val deletionMessage = stringResource(Res.string.donationsDeleteConfirmationMessage)
        if (state is DonationState.ToDelete) {
            DonationDeleteDialog(
                (state as DonationState.ToDelete).donation,
                { screenModel.clearError() }
            ) { donation ->
                screenModel.deleteDonation(donation)
                screenModel.postSideEffect(
                    SideEffects.InformationDialog(
                    title = deletionTitle,
                    message = deletionMessage
                ))
            }
        }
        DonationsView(
            paddingValues = paddingValues,
            donations = donations,
            onEdit = { donation ->
                bottomSheetNavigator.show(EditDonationScreen(donation))
            },
            onDelete = { donation ->
                screenModel.markToDelete(donation)
            },
            onShare = { text ->
                screenModel.postSideEffect(SideEffects.ShareText(text))
            },
            onDonationAdd = {
                bottomSheetNavigator.show(NewDonationScreen())
            }
        )
    }
}

@Composable
fun DonationsView(
    paddingValues: PaddingValues,
    donations: List<Donation>,
    onEdit: (Donation) -> Unit,
    onDelete: (Donation) -> Unit,
    onShare: (String) -> Unit,
    onDonationAdd: () -> Unit
) {
    val milliliter = stringResource(Res.string.milliliter)
    val liter = stringResource(Res.string.liter)
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
                    val shareText = stringResource(Res.string.donationsShare)
                        .replace("%s", donation.amount.capacity(milliliter, liter))
                        .replace("%p", donation.center.name)
                    DonationItem(
                        donation = donation,
                        onEdit = onEdit,
                        onDelete = onDelete,
                        onShare = {
                            onShare.invoke(shareText)
                        }
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
                            style = contentTitle().copy(
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier.fillMaxWidth().padding(10.dp)
                        )
                        Text(
                            stringResource(Res.string.homeSectionHistoryAddDonationEmptyText),
                            style = toolbarSubTitle().copy(
                                textDecoration = TextDecoration.Underline
                            ),
                            modifier = Modifier.padding(10.dp).clickable {
                                onDonationAdd.invoke()
                            }
                        )
                    }
                }
            }
        }
    }
}