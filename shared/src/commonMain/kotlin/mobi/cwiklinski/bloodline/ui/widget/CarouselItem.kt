package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.appName
import mobi.cwiklinski.bloodline.resources.donationsTitle
import mobi.cwiklinski.bloodline.resources.homeTitle
import mobi.cwiklinski.bloodline.resources.home_carousel_background
import mobi.cwiklinski.bloodline.resources.home_stars
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.cardTitle
import mobi.cwiklinski.bloodline.ui.theme.toolbarSubTitle
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CarouselItem(
    modifier: Modifier = Modifier.fillMaxWidth().wrapContentHeight(),
    icon: DrawableResource,
    title: String,
    subTitle: String,
    titleStyle: TextStyle = cardTitle().copy(
        fontSize = 22.sp,
        color = AppThemeColors.black,
    ),
    subTitleStyle: TextStyle = toolbarSubTitle(),
    titleLines: Int = 1,
    suBtitleLines: Int = 1
) {
    Card(
        modifier = modifier.padding(10.dp).width(300.dp)
            .fillMaxHeight(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppThemeColors.yellow1,
            disabledContainerColor = AppThemeColors.yellow1
        ),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Box {
            Image(
                painterResource(Res.drawable.home_carousel_background),
                stringResource(Res.string.homeTitle),
                modifier = Modifier.height(320.dp).padding(0.dp).align(Alignment.BottomStart)
            )
            Row(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 15.dp)
                    .align(alignment = Alignment.CenterStart)
            ) {
                Image(
                    painterResource(icon),
                    title,
                    modifier = Modifier.size(88.dp)
                )
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        title,
                        style = titleStyle,
                        maxLines = titleLines,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        subTitle,
                        style = subTitleStyle,
                        maxLines = suBtitleLines,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CarouselItemPreview() {
    CarouselItem(
        Modifier.fillMaxWidth(),
        Res.drawable.home_stars,
        stringResource(Res.string.appName),
        stringResource(Res.string.donationsTitle)
    )
}