package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.Constants
import mobi.cwiklinski.bloodline.domain.model.Center
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.centerAddress
import mobi.cwiklinski.bloodline.resources.centerContact
import mobi.cwiklinski.bloodline.resources.centerInfo
import mobi.cwiklinski.bloodline.resources.centerRegion
import mobi.cwiklinski.bloodline.resources.centersEmpty
import mobi.cwiklinski.bloodline.resources.ic_address
import mobi.cwiklinski.bloodline.resources.ic_browser
import mobi.cwiklinski.bloodline.resources.ic_phone
import mobi.cwiklinski.bloodline.resources.icon_poland
import mobi.cwiklinski.bloodline.resources.placeholder_center
import mobi.cwiklinski.bloodline.resources.placeholder_map
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.contentTitle
import mobi.cwiklinski.bloodline.ui.theme.itemSubTitle
import mobi.cwiklinski.bloodline.ui.theme.itemTitle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CenterView(center: Center, modifier: Modifier = Modifier, onSiteClick: ((link: String) -> Unit)? = null) {
    val density = LocalDensity.current
    var centerImageWidth by remember { mutableStateOf(0.dp) }
    val margin = 10.dp
    val iconSize = 16.dp
    LazyColumn(
        modifier = modifier.wrapContentHeight().padding(20.dp),
    ) {
        if (center.name.isEmpty()) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().height(400.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        stringResource(Res.string.centersEmpty),
                        style = contentTitle()
                    )
                }
            }
        } else {
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    RemoteImage(
                        modifier = Modifier
                            .weight(0.3f)
                            .padding(3.dp)
                            .background(
                                AppThemeColors.white,
                                RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                            )
                            .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
                            .onGloballyPositioned {
                                centerImageWidth = with(density) {
                                    it.size.width.toDp()
                                }
                            }
                            .fillMaxWidth()
                            .height((centerImageWidth / 2) * 3),
                        url = Constants.CENTER_URL.replace("%id", center.id),
                        description = center.name,
                        error = Res.drawable.placeholder_center,
                        placeHolder = Res.drawable.placeholder_center,
                        contentScale = ContentScale.FillHeight
                    )
                    RemoteImage(
                        modifier = Modifier
                            .weight(0.6f)
                            .padding(3.dp)
                            .background(
                                AppThemeColors.white,
                                RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
                            )
                            .clip(RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))
                            .height((centerImageWidth / 2) * 3),
                        url = Constants.MAP_URL.replace("%id", center.id),
                        description = center.getFullAddress(),
                        error = Res.drawable.placeholder_map,
                        placeHolder = Res.drawable.placeholder_map,
                        contentScale = ContentScale.FillWidth
                    )
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = margin),
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
            }
            item {
                HeaderText(stringResource(Res.string.centerAddress))
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        painterResource(Res.drawable.ic_address),
                        contentDescription = center.getFullAddress(),
                        modifier = Modifier.size(iconSize)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        center.getFullAddress(),
                        style = itemTitle(),
                        modifier = Modifier
                            .weight(1.0f)
                    )

                }
            }
            if (center.phone.isNotEmpty() || center.site.isNotEmpty()) {
                item {
                    HeaderText(stringResource(Res.string.centerContact))
                }
                if (center.site.isNotEmpty()) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Icon(
                                painterResource(Res.drawable.ic_browser),
                                contentDescription = center.getFullAddress(),
                                modifier = Modifier.size(iconSize)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                center.site,
                                style = itemTitle().copy(
                                    textDecoration = TextDecoration.Underline
                                ),
                                modifier = Modifier
                                    .clickable {
                                        onSiteClick?.invoke(center.site)
                                    }
                                    .weight(1.0f)
                            )

                        }
                    }
                }
                if (center.phone.isNotEmpty()) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Icon(
                                painterResource(Res.drawable.ic_phone),
                                contentDescription = center.getFullAddress(),
                                modifier = Modifier.size(iconSize)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                center.phone,
                                style = itemTitle(),
                                modifier = Modifier
                                    .weight(1.0f)
                            )
                        }
                    }
                }
            }
            if (center.info.isNotEmpty()) {
                item {
                    HeaderText(stringResource(Res.string.centerInfo))
                }
                item {
                    Text(
                        center.info,
                        style = itemTitle(),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}