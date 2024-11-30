package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import mobi.cwiklinski.bloodline.domain.model.Center

@Composable
fun CenterItem(center: Center) {
    Card {
        Text(center.getFullAddress())
    }
}