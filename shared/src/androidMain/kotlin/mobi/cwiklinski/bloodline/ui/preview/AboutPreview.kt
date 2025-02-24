package mobi.cwiklinski.bloodline.ui.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import mobi.cwiklinski.bloodline.ui.screen.AboutHorizontalView
import mobi.cwiklinski.bloodline.ui.screen.AboutVerticalView
import mobi.cwiklinski.bloodline.ui.theme.AppTheme

@Preview
@Composable
fun AboutPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
            AboutVerticalView()
        }
    }
}


@Preview(device = "spec:parent=pixel_tablet")
@Composable
fun HorizontalAboutPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
            AboutHorizontalView()
        }
    }
}
