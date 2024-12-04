package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import mobi.cwiklinski.bloodline.domain.model.Center
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.centerRegion
import mobi.cwiklinski.bloodline.resources.icon_poland
import mobi.cwiklinski.bloodline.resources.placeholder_center
import mobi.cwiklinski.bloodline.resources.placeholder_map
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.itemSubTitle
import mobi.cwiklinski.bloodline.ui.theme.itemTitle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CenterView(center: Center, modifier: Modifier = Modifier) {
    ConstraintLayout(
        modifier = modifier.padding(20.dp)
            .background(AppThemeColors.background),
    ) {
        val density = LocalDensity.current
        var centerImageWidth by remember { mutableStateOf(0.dp) }
        val (image, region, address, location, map) = createRefs()
        val margin = 10.dp
        RemoteImage(
            modifier = Modifier
                .onGloballyPositioned {
                    centerImageWidth = with(density) {
                        it.size.width.toDp()
                    }
                }
                .height((centerImageWidth / 3) * 2)
                .constrainAs(image) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            url = "https://bloodline.cwiklin.ski/storage/images/center/${center.id}.jpg",
            description = center.name,
            error = Res.drawable.placeholder_center,
            placeHolder = Res.drawable.placeholder_center,
        )
        RemoteImage(
            modifier = Modifier
                .width(200.dp)
                .constrainAs(map) {
                    top.linkTo(image.bottom, margin = 20.dp)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
            url = "https://bloodline.cwiklin.ski/storage/images/center/${center.id}.jpg",
            description = center.getFullAddress(),
            error = Res.drawable.placeholder_map,
            placeHolder = Res.drawable.placeholder_map,
        )
        Row(modifier = Modifier
                .padding(vertical = margin)
                .constrainAs(region) {
                    start.linkTo(parent.start, margin)
                    top.linkTo(image.bottom)
                    end.linkTo(map.start, margin)
                    width = Dimension.fillToConstraints
                },
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
                stringResource(Res.string.centerRegion).replace("%s", center.voivodeship)
                    .uppercase(),
                style = itemSubTitle(),
                modifier = Modifier.weight(1.0f)
            )
        }
        Text(
            center.getFullAddress(),
            style = itemTitle(),
            modifier = Modifier
                .constrainAs(address) {
                    start.linkTo(parent.start, margin)
                    top.linkTo(region.bottom)
                    end.linkTo(map.start, margin)
                    width = Dimension.fillToConstraints
                }
        )
    }
}