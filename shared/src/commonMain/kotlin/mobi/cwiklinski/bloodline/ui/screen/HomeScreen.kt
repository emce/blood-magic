package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.aakira.napier.Napier
import mobi.cwiklinski.bloodline.Constants
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
import mobi.cwiklinski.bloodline.ui.event.HandleSideEffect
import mobi.cwiklinski.bloodline.ui.event.SideEffects
import mobi.cwiklinski.bloodline.ui.event.shareText
import mobi.cwiklinski.bloodline.ui.manager.rememberPlatformManager
import mobi.cwiklinski.bloodline.ui.model.HomeScreenModel
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.contentAction
import mobi.cwiklinski.bloodline.ui.theme.contentTitle
import mobi.cwiklinski.bloodline.ui.theme.toolbarSubTitle
import mobi.cwiklinski.bloodline.ui.theme.toolbarTitle
import mobi.cwiklinski.bloodline.ui.widget.CarouselItem
import mobi.cwiklinski.bloodline.ui.widget.DonationItem
import mobi.cwiklinski.bloodline.ui.widget.HomeCard
import mobi.cwiklinski.bloodline.ui.widget.NextDonationPrediction
import mobi.cwiklinski.bloodline.ui.widget.capacity
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class HomeScreen : AppScreen() {

    @Composable
    override fun verticalView() {
        Napier.d("Home Screen started")
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<HomeScreenModel>()
        val platformManager = rememberPlatformManager()
        val donations by screenModel.donations.collectAsStateWithLifecycle()
        val profile by screenModel.profile.collectAsStateWithLifecycle()
        HandleSideEffect(screenModel) {
            if (it is SideEffects.ShareText) {
                shareText(platformManager, it.text)
            }
        }
        val hero = if (profile.sex.isFemale()) stringResource(Res.string.homeHeroin) else stringResource(Res.string.homeHero)
        VerticalScaffold { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues)
            ) {
                Box(
                    modifier = Modifier.height(180.dp).fillMaxWidth()
                        .background(Color.Transparent)
                ) {
                    Image(
                        painterResource(Res.drawable.home_stars),
                        stringResource(Res.string.homeTitle),
                        modifier = Modifier.padding(20.dp).align(Alignment.Center)
                            .offset(x = 62.dp)
                    )
                    val name = profile.name.ifEmpty { hero }
                    Text(
                        stringResource(Res.string.homeTitle).replace("%s", name),
                        modifier = Modifier.align(Alignment.CenterStart).offset(y = 15.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        style = toolbarTitle()
                    )
                    Text(
                        stringResource(Res.string.homeSubTitle),
                        modifier = Modifier.align(Alignment.CenterStart).offset(y = 50.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
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
                            stringResource(Res.string.homeSectionNextDonationEmptyTitle),
                            stringResource(Res.string.homeSectionNextDonationEmptyText)
                        )
                        Box(
                            modifier = Modifier.fillMaxWidth().wrapContentHeight()
                        ) {
                            HomeCard(
                                stringResource(Res.string.homeSectionHistoryEmptyTitle),
                                stringResource(Res.string.homeSectionHistoryEmptyText),
                                stringResource(Res.string.homeSectionHistoryAddDonationEmptyText)
                            )
                            Image(
                                painterResource(Res.drawable.home_arrow),
                                stringResource(Res.string.homeTitle),
                                modifier = Modifier.align(Alignment.Center)
                                    .offset(x = 70.dp, y = 100.dp)
                            )
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

    @Composable
    override fun horizontalView() {
        verticalView()
    }
}