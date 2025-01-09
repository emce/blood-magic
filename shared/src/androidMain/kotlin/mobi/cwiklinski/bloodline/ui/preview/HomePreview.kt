package mobi.cwiklinski.bloodline.ui.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mobi.cwiklinski.bloodline.data.filed.DummyData
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.homeCarouselAmountSubtitle
import mobi.cwiklinski.bloodline.resources.homeCarouselAmountTitle
import mobi.cwiklinski.bloodline.resources.homeCarouselBadge1Subtitle
import mobi.cwiklinski.bloodline.resources.homeCarouselBadge1Title
import mobi.cwiklinski.bloodline.resources.homeCarouselBadge3Subtitle
import mobi.cwiklinski.bloodline.resources.homeCarouselBadge3Title
import mobi.cwiklinski.bloodline.resources.homeCarouselBadgeFinalSubtitle
import mobi.cwiklinski.bloodline.resources.homeCarouselBadgeFinalTitle
import mobi.cwiklinski.bloodline.resources.homeCarouselTotalAmountTitle
import mobi.cwiklinski.bloodline.resources.home_carousel_cauldron
import mobi.cwiklinski.bloodline.resources.home_carousel_wand
import mobi.cwiklinski.bloodline.resources.ic_zdzn
import mobi.cwiklinski.bloodline.resources.ic_zhdk_1
import mobi.cwiklinski.bloodline.resources.ic_zhdk_3
import mobi.cwiklinski.bloodline.resources.liter
import mobi.cwiklinski.bloodline.resources.milliliter
import mobi.cwiklinski.bloodline.ui.screen.HomeView
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.cardTitle
import mobi.cwiklinski.bloodline.ui.theme.itemSubTitle
import mobi.cwiklinski.bloodline.ui.theme.toolbarSubTitle
import mobi.cwiklinski.bloodline.ui.widget.CarouselItem
import mobi.cwiklinski.bloodline.ui.widget.capacity
import org.jetbrains.compose.resources.stringResource

@Preview(locale = "pl", device = "id:Nexus 4")
@Composable
fun HomePreview() {
    HomeView(
        PaddingValues(0.dp),
        DummyData.DONATIONS.toList(),
        DummyData.generateProfile(),
        emptyList(),
        {},
        {},
        {},
        {}
    )
}

@Preview
@Composable
fun EmptyHomePreview() {
    HomeView(
        PaddingValues(0.dp),
        emptyList(),
        DummyData.generateProfile(),
        emptyList(),
        {},
        {},
        {},
        {}
    )
}

@Preview(locale = "pl", device = "id:Nexus 4")
@Composable
fun CarouselPreview() {
    val totalSum = 58000
    val amount = 48
    Column(
        modifier = Modifier.fillMaxSize().background(Color.White)
            .padding(start = 20.dp)
    ) {
        CarouselItem(
            icon = Res.drawable.home_carousel_cauldron,
            title = totalSum.capacity(
                stringResource(Res.string.milliliter),
                stringResource(Res.string.liter)
            ),
            subTitle = stringResource(Res.string.homeCarouselTotalAmountTitle),
            modifier = Modifier.height(140.dp)
        )
        CarouselItem(
            icon = Res.drawable.home_carousel_wand,
            title = stringResource(Res.string.homeCarouselAmountTitle).replace(
                "%d",
                amount.toString()
            ),
            subTitle = stringResource(Res.string.homeCarouselAmountSubtitle),
            modifier = Modifier.height(140.dp)
        )
        CarouselItem(
            icon = Res.drawable.ic_zdzn,
            title = stringResource(Res.string.homeCarouselBadgeFinalTitle),
            subTitle = stringResource(Res.string.homeCarouselBadgeFinalSubtitle),
            titleStyle = toolbarSubTitle(),
            subTitleStyle = cardTitle().copy(
                fontSize = 22.sp,
                color = AppThemeColors.black,
                lineHeight = 25.sp
            ),
            subTitleLines = 2,
            modifier = Modifier.height(140.dp)
        )
        CarouselItem(
            icon = Res.drawable.ic_zhdk_1,
            title = stringResource(Res.string.homeCarouselBadge3Title),
            subTitle = stringResource(Res.string.homeCarouselBadge3Subtitle),
            titleStyle = itemSubTitle(),
            subTitleStyle = cardTitle().copy(
                fontSize = 22.sp,
                color = AppThemeColors.black,
                lineHeight = 25.sp
            ),
            subTitleLines = 2,
            modifier = Modifier.height(140.dp)
        )
        CarouselItem(
            icon = Res.drawable.ic_zhdk_3,
            title = stringResource(Res.string.homeCarouselBadge1Title),
            subTitle = stringResource(Res.string.homeCarouselBadge1Subtitle),
            titleStyle = itemSubTitle(),
            subTitleStyle = cardTitle().copy(
                fontSize = 22.sp,
                color = AppThemeColors.black,
                lineHeight = 25.sp
            ),
            subTitleLines = 2,
            modifier = Modifier.height(140.dp)
        )
    }
}
