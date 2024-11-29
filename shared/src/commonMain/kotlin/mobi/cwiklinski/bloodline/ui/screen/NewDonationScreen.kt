package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors

class NewDonationScreen : AppScreen() {

    @Composable
    override fun Content() {
        Box(
            modifier = Modifier.fillMaxSize()
                .background(AppThemeColors.black.copy(alpha = 0.1f))
                .padding(20.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(color = AppThemeColors.white, shape = RoundedCornerShape(40.dp))
            ) {

            }
        }
    }
}