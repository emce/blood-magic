package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import mobi.cwiklinski.bloodline.Constants
import mobi.cwiklinski.bloodline.analytics.api.TrackScreen
import mobi.cwiklinski.bloodline.common.event.Events
import mobi.cwiklinski.bloodline.common.isValidUrl
import mobi.cwiklinski.bloodline.data.Parcelize
import mobi.cwiklinski.bloodline.domain.model.Center
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.centerSearchLabel
import mobi.cwiklinski.bloodline.resources.centersTitle
import mobi.cwiklinski.bloodline.resources.ic_search
import mobi.cwiklinski.bloodline.resources.icon_poland
import mobi.cwiklinski.bloodline.resources.loading
import mobi.cwiklinski.bloodline.ui.model.CenterScreenModel
import mobi.cwiklinski.bloodline.ui.model.CenterState
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.itemSubTitle
import mobi.cwiklinski.bloodline.ui.util.NavigationItem
import mobi.cwiklinski.bloodline.ui.util.koinNavigatorScreenModel
import mobi.cwiklinski.bloodline.ui.widget.CenterItemView
import mobi.cwiklinski.bloodline.ui.widget.DesktopNavigationTitleScaffold
import mobi.cwiklinski.bloodline.ui.widget.FormProgress
import mobi.cwiklinski.bloodline.ui.widget.MobileLandscapeNavigationTitleLayout
import mobi.cwiklinski.bloodline.ui.widget.MobilePortraitNavigationTitleLayout
import mobi.cwiklinski.bloodline.ui.widget.SearchView
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Parcelize
class CentersScreen : AppScreen() {

    @Composable
    override fun defaultView() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<CenterScreenModel>()
        val query by screenModel.query.collectAsStateWithLifecycle("")
        var showSearch by remember { mutableStateOf(false) }
        MobilePortraitNavigationTitleLayout(
            title = stringResource(Res.string.centersTitle),
            actions = {
                if (showSearch) {
                    SearchView(
                        modifier = Modifier.padding(5.dp).fillMaxWidth(),
                        text = query,
                        onValueChanged = {
                            screenModel.query.value = it
                        },
                        onClose = {
                            showSearch = false
                            screenModel.query.value = ""
                        },
                        placeholder = stringResource(Res.string.centerSearchLabel)
                    )
                } else {
                    IconButton(
                        onClick = {
                            showSearch = true
                        }
                    ) {
                        Icon(
                            painterResource(Res.drawable.ic_search),
                            stringResource(Res.string.centerSearchLabel),
                            tint = AppThemeColors.black70
                        )
                    }
                }
            },
            selected = NavigationItem.CENTER,
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
                navigator.push(NewDonationScreen())
            }
        ) { paddingValues ->
            InternalCentersView(paddingValues)
        }
    }

    @Composable
    override fun tabletView() {
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val screenModel = navigator.koinNavigatorScreenModel<CenterScreenModel>()
        val query by screenModel.query.collectAsStateWithLifecycle("")
        var showSearch by remember { mutableStateOf(false) }
        MobileLandscapeNavigationTitleLayout(
            title = stringResource(Res.string.centersTitle),
            selected = NavigationItem.CENTER,
            actions = {
                if (showSearch) {
                    SearchView(
                        modifier = Modifier.padding(5.dp).fillMaxWidth(),
                        text = query,
                        onValueChanged = {
                            screenModel.query.value = it
                        },
                        onClose = {
                            showSearch = false
                            screenModel.query.value = ""
                        },
                        placeholder = stringResource(Res.string.centerSearchLabel)
                    )
                } else {
                    Icon(
                        painterResource(Res.drawable.ic_search),
                        stringResource(Res.string.centerSearchLabel),
                        tint = AppThemeColors.grey,
                        modifier = Modifier.size(36.dp).clickable {
                            showSearch = true
                        }
                    )
                }
            },
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
                navigator.push(NewDonationScreen())
            },
            infoClicked = {
                navigator.push(AboutScreen())
            },
            desiredContent = {
                InternalCentersView(PaddingValues(0.dp))
            }
        )
    }

    @Composable
    override fun desktopView() {
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val screenModel = navigator.koinNavigatorScreenModel<CenterScreenModel>()
        val query by screenModel.query.collectAsStateWithLifecycle("")
        var showSearch by remember { mutableStateOf(false) }
        handleSideEffects<CenterState, CenterScreenModel>()
        DesktopNavigationTitleScaffold(
            title = stringResource(Res.string.centersTitle),
            selected = NavigationItem.CENTER,
            actions = {
                if (showSearch) {
                    SearchView(
                        modifier = Modifier.padding(5.dp).fillMaxWidth(),
                        text = query,
                        onValueChanged = {
                            screenModel.query.value = it
                        },
                        onClose = {
                            showSearch = false
                            screenModel.query.value = ""
                        },
                        placeholder = stringResource(Res.string.centerSearchLabel)
                    )
                } else {
                    Icon(
                        painterResource(Res.drawable.ic_search),
                        stringResource(Res.string.centerSearchLabel),
                        tint = AppThemeColors.grey,
                        modifier = Modifier.size(36.dp).clickable {
                            showSearch = true
                        }
                    )
                }
            },
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
                navigator.push(NewDonationScreen())
            },
            infoClicked = {
                navigator.push(AboutScreen())
            },
            desiredContent = {
                InternalCentersView(PaddingValues(0.dp))
            }
        )
    }

    @Composable
    private fun InternalCentersView(paddingValues: PaddingValues) {
        val navigator = LocalNavigator.currentOrThrow
        val bottomNavigator = LocalBottomSheetNavigator.current
        val screenModel = navigator.koinNavigatorScreenModel<CenterScreenModel>()
        val centers by screenModel.filteredCenters.collectAsStateWithLifecycle(emptyList())
        handleSideEffects<CenterState, CenterScreenModel>()
        CentersView(
            paddingValues = paddingValues,
            centers = centers,
            openCenter = { center ->
                bottomNavigator.show(CenterScreen(center) { link ->
                    if (link.isValidUrl()) {
                        screenModel.postEvent(Events.OpenBrowser(url = link))
                    }
                })
            }
        )
    }
}

@Composable
fun CentersView(
    paddingValues: PaddingValues,
    centers: List<Center>,
    openCenter: (Center) -> Unit
) {
    TrackScreen(Constants.ANALYTICS_SCREEN_CENTERS)
    if (centers.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues)
                .background(
                    SolidColor(AppThemeColors.background),
                    RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                ),
            verticalArrangement = Arrangement.Top
        ) {
            itemsIndexed(centers) { index, center ->
                if (index == 0) {
                    RegionHeader(
                        center,
                        modifier = Modifier.animateItem()
                    )
                } else {
                    centers.getOrNull(index - 1)?.let { previous ->
                        if (center.voivodeship != previous.voivodeship) {
                            RegionHeader(
                                center,
                                modifier = Modifier.animateItem()
                            )
                        }
                    }
                }
                CenterItemView(
                    center,
                    modifier = Modifier.animateItem()
                        .fillMaxWidth().clickable {
                            openCenter(center)
                        })
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues)
                .background(
                    SolidColor(AppThemeColors.background),
                    RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FormProgress()
            Spacer(Modifier.height(10.dp))
            Text(stringResource(Res.string.loading))
        }
    }
}

@Composable
fun RegionHeader(
    center: Center,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(AppThemeColors.grey1)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painterResource(Res.drawable.icon_poland),
            contentDescription = center.voivodeship,
            modifier = Modifier.size(16.dp),
            colorFilter = ColorFilter.tint(AppThemeColors.black70)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            center.voivodeship.toUpperCase(Locale.current),
            style = itemSubTitle()
        )
    }
}