package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import mobi.cwiklinski.bloodline.domain.model.Center
import mobi.cwiklinski.bloodline.ui.theme.inputPlaceHolder
import mobi.cwiklinski.bloodline.ui.theme.itemSubTitle

@Composable
fun CenterSelectItem(center: Center, previous: Center?) {
    Column() {
        if (center.voivodeship != previous?.voivodeship) {
            Text(
                center.voivodeship.toUpperCase(Locale.current),
                style = itemSubTitle()
            )
        }
        Text(
            center.toSelection(),
            style = inputPlaceHolder(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}