package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import mobi.cwiklinski.bloodline.Constants
import mobi.cwiklinski.bloodline.isMobile
import mobi.cwiklinski.bloodline.isTablet
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.homeCarouselAmountSubtitle
import mobi.cwiklinski.bloodline.resources.homeCarouselAmountTitle
import mobi.cwiklinski.bloodline.resources.homeCarouselBadge1Subtitle
import mobi.cwiklinski.bloodline.resources.homeCarouselBadge1Title
import mobi.cwiklinski.bloodline.resources.homeCarouselBadge2Subtitle
import mobi.cwiklinski.bloodline.resources.homeCarouselBadge2Title
import mobi.cwiklinski.bloodline.resources.homeCarouselBadge3Subtitle
import mobi.cwiklinski.bloodline.resources.homeCarouselBadge3Title
import mobi.cwiklinski.bloodline.resources.homeCarouselBadgeFinalSubtitle
import mobi.cwiklinski.bloodline.resources.homeCarouselBadgeFinalTitle
import mobi.cwiklinski.bloodline.resources.homeCarouselTotalAmountTitle
import mobi.cwiklinski.bloodline.resources.homeDonationHistory
import mobi.cwiklinski.bloodline.resources.homeHero
import mobi.cwiklinski.bloodline.resources.homeHeroin
import mobi.cwiklinski.bloodline.resources.homeSectionEmptyTitle
import mobi.cwiklinski.bloodline.resources.homeSectionHistoryAddDonationEmptyText
import mobi.cwiklinski.bloodline.resources.homeSectionHistoryEmptyText
import mobi.cwiklinski.bloodline.resources.homeSectionHistoryEmptyTitle
import mobi.cwiklinski.bloodline.resources.homeSectionNextDonationEmptyText
import mobi.cwiklinski.bloodline.resources.homeSectionNextDonationEmptyTitle
import mobi.cwiklinski.bloodline.resources.homeSectionTitle
import mobi.cwiklinski.bloodline.resources.homeSubTitle
import mobi.cwiklinski.bloodline.resources.homeTitle
import mobi.cwiklinski.bloodline.resources.home_arrow
import mobi.cwiklinski.bloodline.resources.home_carousel_cauldron
import mobi.cwiklinski.bloodline.resources.home_carousel_wand
import mobi.cwiklinski.bloodline.resources.home_stars
import mobi.cwiklinski.bloodline.resources.ic_zdzn
import mobi.cwiklinski.bloodline.resources.ic_zhdk_1
import mobi.cwiklinski.bloodline.resources.ic_zhdk_2
import mobi.cwiklinski.bloodline.resources.ic_zhdk_3
import mobi.cwiklinski.bloodline.resources.liter
import mobi.cwiklinski.bloodline.resources.milliliter
import mobi.cwiklinski.bloodline.resources.seeAll
import mobi.cwiklinski.bloodline.ui.event.Events
import mobi.cwiklinski.bloodline.ui.event.SideEffects
import mobi.cwiklinski.bloodline.ui.model.HomeScreenModel
import mobi.cwiklinski.bloodline.ui.model.HomeState
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.contentAction
import mobi.cwiklinski.bloodline.ui.theme.contentTitle
import mobi.cwiklinski.bloodline.ui.theme.itemTitle
import mobi.cwiklinski.bloodline.ui.theme.toolbarSubTitle
import mobi.cwiklinski.bloodline.ui.theme.toolbarTitle
import mobi.cwiklinski.bloodline.ui.util.NavigationItem
import mobi.cwiklinski.bloodline.ui.widget.CarouselItem
import mobi.cwiklinski.bloodline.ui.widget.DesktopNavigationScaffold
import mobi.cwiklinski.bloodline.ui.widget.DonationItem
import mobi.cwiklinski.bloodline.ui.widget.HomeCard
import mobi.cwiklinski.bloodline.ui.widget.MobileLandscapeNavigationLayout
import mobi.cwiklinski.bloodline.ui.widget.MobilePortraitNavigationLayout
import mobi.cwiklinski.bloodline.ui.widget.NextDonationPrediction
import mobi.cwiklinski.bloodline.ui.widget.NotificationsButton
import mobi.cwiklinski.bloodline.ui.widget.capacity
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class HomeScreen : AppScreen() {

    @Composable
    override fun defaultView() {
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        MobilePortraitNavigationLayout(
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
            HomeView(paddingValues)
        }
    }

    @Composable
    override fun tabletView() {
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        MobileLandscapeNavigationLayout(
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
                HomeView(PaddingValues(0.dp))
            }
        )
    }

    @Composable
    override fun desktopView() {
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        DesktopNavigationScaffold(
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
                HomeView(PaddingValues(0.dp))
            }
        )
    }

    @Composable
    fun HomeView(paddingValues: PaddingValues) {
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val screenModel = navigator.koinNavigatorScreenModel<HomeScreenModel>()
        val donations by screenModel.donations.collectAsStateWithLifecycle()
        val profile by screenModel.profile.collectAsStateWithLifecycle()
        val unreadNotification by screenModel.notifications.collectAsStateWithLifecycle(emptyList())
        val hero = if (profile.sex.isFemale()) stringResource(Res.string.homeHeroin) else stringResource(Res.string.homeHero)
        handleSideEffects<HomeState, HomeScreenModel>()
        Column(
            modifier = Modifier.padding(paddingValues).background(AppThemeColors.mainGradient)
        ) {
            ConstraintLayout(
                modifier = Modifier.height(180.dp).fillMaxWidth()
                    .background(Color.Transparent)
            ) {
                val (titleRef, subTitleRef, starsRef, bellRef) = createRefs()
                NotificationsButton(
                    modifier = Modifier
                        .constrainAs(bellRef) {
                            top.linkTo(parent.top, 40.dp)
                            end.linkTo(parent.end, 10.dp)
                        },
                    onClick = {
                        navigator.push(NotificationsScreen())
                    },
                    newNotifications = unreadNotification.any { it.read }
                )
                Image(
                    painterResource(Res.drawable.home_stars),
                    stringResource(Res.string.homeTitle),
                    modifier = Modifier.padding(20.dp).constrainAs(starsRef){
                        top.linkTo(bellRef.top)
                        end.linkTo(bellRef.start, 10.dp)
                        bottom.linkTo(parent.bottom, 30.dp)
                    }
                )
                val name = profile.name.ifEmpty { hero }
                Text(
                    stringResource(Res.string.homeTitle).replace("%s", name),
                    modifier = Modifier.constrainAs(titleRef) {
                        bottom.linkTo(subTitleRef.top, 20.dp)
                        start.linkTo(parent.start, 20.dp)
                    },
                    style = toolbarTitle()
                )
                Text(
                    stringResource(Res.string.homeSubTitle),
                    modifier = Modifier.constrainAs(subTitleRef) {
                        bottom.linkTo(parent.bottom, 20.dp)
                        start.linkTo(parent.start, 20.dp)
                    },
                    style = toolbarSubTitle()
                )
            }
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(
                        SolidColor(AppThemeColors.background),
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                Spacer(Modifier.height(24.dp))
                Text(
                    if (donations.isNotEmpty()) stringResource(Res.string.homeSectionTitle).replace(
                        "%s",
                        hero
                    ) else stringResource(Res.string.homeSectionEmptyTitle).replace("%s", hero),
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    style = contentTitle()
                )
                if (donations.isEmpty()) {
                    Row(
                        modifier = Modifier.height(140.dp).fillMaxWidth()
                            .padding(start = 20.dp)
                            .horizontalScroll(rememberScrollState())
                    ) {
                        CarouselItem(
                            icon = Res.drawable.home_carousel_wand,
                            title = stringResource(Res.string.homeCarouselAmountTitle).replace(
                                "%d", "0"
                            ),
                            subTitle = stringResource(Res.string.homeCarouselAmountSubtitle)
                        )
                        CarouselItem(
                            icon = Res.drawable.home_carousel_cauldron,
                            title = 0.capacity(
                                stringResource(Res.string.milliliter),
                                stringResource(Res.string.liter)
                            ),
                            subTitle = stringResource(Res.string.homeCarouselTotalAmountTitle)
                        )
                    }
                    HomeCard(
                        title = stringResource(Res.string.homeSectionNextDonationEmptyTitle),
                        subTitle = stringResource(Res.string.homeSectionNextDonationEmptyText),
                    )
                    ConstraintLayout(
                        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                    ) {
                        val (card, image) = createRefs()
                        HomeCard(
                            modifier = Modifier.constrainAs(card) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                            title = stringResource(Res.string.homeSectionHistoryEmptyTitle),
                            subTitle = stringResource(Res.string.homeSectionHistoryEmptyText),
                            subSubTitle = {
                                Text(
                                    stringResource(Res.string.homeSectionHistoryAddDonationEmptyText),
                                    style = itemTitle().copy(
                                        textDecoration = TextDecoration.Underline
                                    ),
                                    modifier = Modifier.padding(top = 10.dp).clickable {
                                        bottomSheetNavigator.show(NewDonationScreen())
                                    }
                                )
                            }
                        )
                        if (isMobile() && !isTablet()) {
                            Image(
                                painterResource(Res.drawable.home_arrow),
                                stringResource(Res.string.homeTitle),
                                modifier = Modifier.constrainAs(image) {
                                    centerHorizontallyTo(parent)
                                    bottom.linkTo(parent.bottom)
                                }
                                    .offset(x = 70.dp, y = 100.dp)
                            )
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier.height(140.dp).fillMaxWidth()
                            .padding(start = 20.dp)
                            .horizontalScroll(rememberScrollState())
                    ) {
                        val amount = donations.filter { !it.disqualification }.size
                        CarouselItem(
                            icon = Res.drawable.home_carousel_wand,
                            title = stringResource(Res.string.homeCarouselAmountTitle).replace(
                                "%d",
                                amount.toString()
                            ),
                            subTitle = stringResource(Res.string.homeCarouselAmountSubtitle)
                        )
                        var totalSum = profile.starting
                        totalSum += donations
                            .filter { !it.disqualification }.sumOf {
                                it.convertToFullBlood(
                                    profile.sex
                                )
                            }
                        CarouselItem(
                            icon = Res.drawable.home_carousel_cauldron,
                            title = totalSum.capacity(
                                stringResource(Res.string.milliliter),
                                stringResource(Res.string.liter)
                            ),
                            subTitle = stringResource(Res.string.homeCarouselTotalAmountTitle)
                        )
                        val part =
                            if (profile.sex.isFemale()) Constants.BADGE_FEMALE_FIRST else Constants.BADGE_MALE_FIRST
                        var badge = Res.drawable.ic_zhdk_3
                        var badgeTitle = stringResource(Res.string.homeCarouselBadge1Title)
                        var badgeSubTitle = stringResource(Res.string.homeCarouselBadge1Subtitle)
                        when {
                            totalSum > Constants.BADGE_FINAL -> {
                                badge = Res.drawable.ic_zdzn
                                badgeTitle = stringResource(Res.string.homeCarouselBadgeFinalTitle)
                                badgeSubTitle = stringResource(Res.string.homeCarouselBadgeFinalSubtitle)
                            }

                            amount > part * 3 && amount < Constants.BADGE_FINAL -> {
                                badge = Res.drawable.ic_zhdk_1
                                badgeTitle = stringResource(Res.string.homeCarouselBadge3Title)
                                badgeSubTitle = stringResource(Res.string.homeCarouselBadge3Subtitle)
                            }

                            amount > part * 2 && amount < part * 3 -> {
                                badge = Res.drawable.ic_zhdk_2
                                badgeTitle = stringResource(Res.string.homeCarouselBadge2Title)
                                badgeSubTitle = stringResource(Res.string.homeCarouselBadge2Subtitle)
                            }

                            amount > part && amount < part * 2 -> {
                                badge = Res.drawable.ic_zhdk_3
                                badgeTitle = stringResource(Res.string.homeCarouselBadge1Title)
                                badgeSubTitle = stringResource(Res.string.homeCarouselBadge1Subtitle)
                            }
                        }
                        CarouselItem(
                            icon = badge,
                            title = badgeTitle,
                            subTitle = badgeSubTitle
                        )
                    }
                    NextDonationPrediction(
                        donations
                            .filter { !it.disqualification }
                            .maxByOrNull { it.date.toEpochDays() }
                    )
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(20.dp),
                    ) {
                        Text(
                            stringResource(Res.string.homeDonationHistory),
                            style = contentTitle(),
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                        TextButton(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            onClick = {
                                navigator.push(DonationsScreen())
                            },
                            contentPadding = PaddingValues(0.dp),
                            colors = AppThemeColors.textButtonColors()
                        ) {
                            Text(
                                stringResource(Res.string.seeAll),
                                style = contentAction()
                            )
                        }
                    }
                    donations.take(5).forEach {
                        DonationItem(it, {}, {}, { text ->
                            screenModel.postEvent(Events.EventSideEffect(sideEffect = SideEffects.ShareText(text)))
                        }, false)
                    }
                }
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}