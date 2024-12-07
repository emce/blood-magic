package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.domain.model.Center
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.placeholder_center
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.itemSubTitle
import mobi.cwiklinski.bloodline.ui.theme.itemTitle

@Composable
fun CenterItemView(center: Center, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(10.dp)
            .background(AppThemeColors.background)
    ) {
        Card(
            shape = RoundedCornerShape(4.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = AppThemeColors.white
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = modifier.padding(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                RemoteImage(
                    modifier = Modifier.size(width = 120.dp, height = 90.dp),
                    url = "https://bloodline.cwiklin.ski/storage/images/center/${center.id}.jpg",
                    description = center.name,
                    error = Res.drawable.placeholder_center,
                    placeHolder = Res.drawable.placeholder_center,
                )
                Column(
                    modifier = Modifier.weight(1.0f).padding(start = 6.dp)
                ) {
                    Text(
                        center.name,
                        style = itemTitle()
                    )
                    Text(
                        center.getFullAddress(),
                        style = itemSubTitle(),
                    )
                }
            }
        }
    }
}