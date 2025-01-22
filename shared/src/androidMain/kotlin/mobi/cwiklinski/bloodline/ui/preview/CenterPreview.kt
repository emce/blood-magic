package mobi.cwiklinski.bloodline.ui.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import mobi.cwiklinski.bloodline.data.filed.DummyData
import mobi.cwiklinski.bloodline.ui.widget.CenterView

@Preview(locale = "pl")
@Composable
fun CenterPreview() {
    Column(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        CenterView(DummyData.CENTERS.random())
    }
}