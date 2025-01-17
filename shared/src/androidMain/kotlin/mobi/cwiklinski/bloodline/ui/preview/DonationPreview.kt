package mobi.cwiklinski.bloodline.ui.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import mobi.cwiklinski.bloodline.ui.screen.NewDonationForm
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors

@Preview
@Composable
fun NewDonationPreview() {
    Column(
        modifier = Modifier.background(AppThemeColors.white)
    ) {
        NewDonationForm(
            hemoglobin = "23.9"
        )
    }
}