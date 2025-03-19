package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.domain.model.Center
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.icon_poland
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.inputPlaceHolder
import mobi.cwiklinski.bloodline.ui.theme.itemSubTitle
import org.jetbrains.compose.resources.painterResource

@Composable
fun CenterSelectItem(modifier: Modifier = Modifier, center: Center, previous: Center?) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        if (previous == null || center.voivodeship != previous.voivodeship) {
            Row(
                modifier = Modifier
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
        Text(
            center.toSelection(),
            style = inputPlaceHolder(),
            modifier = modifier.padding(vertical = 10.dp, horizontal = 5.dp).fillMaxWidth()
        )
    }
}