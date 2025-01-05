package mobi.cwiklinski.bloodline.ui.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import mobi.cwiklinski.bloodline.ui.screen.NewDonationScreen

@Preview
@Composable
fun NewDonationPreview() {
    val screen = NewDonationScreen()
    screen.defaultView()
}