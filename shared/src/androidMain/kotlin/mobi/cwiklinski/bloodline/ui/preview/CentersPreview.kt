package mobi.cwiklinski.bloodline.ui.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.data.filed.DummyData
import mobi.cwiklinski.bloodline.domain.sortByRegion
import mobi.cwiklinski.bloodline.ui.screen.CentersView
import mobi.cwiklinski.bloodline.ui.util.getShareText
import mobi.cwiklinski.bloodline.ui.widget.CenterItemView
import mobi.cwiklinski.bloodline.ui.widget.CenterView

@Preview
@Composable
fun CenterItemPreview() {
    CenterItemView(DummyData.CENTERS.random())
}

@Preview
@Composable
fun CentersPreview() {
    CentersView(
        paddingValues = PaddingValues(0.dp),
        centers = DummyData.CENTERS.toList().sortByRegion()
    ) {}
}

@Preview
@Composable
fun CentersEmptyPreview() {
    CentersView(
        paddingValues = PaddingValues(0.dp),
        centers = emptyList()
    ) {}
}

@Preview(locale = "pl")
@Composable
fun CenterPreview() {
    Column(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        Text(getShareText(DummyData.DONATIONS.random()))
        CenterView(DummyData.CENTERS.random())
    }
}