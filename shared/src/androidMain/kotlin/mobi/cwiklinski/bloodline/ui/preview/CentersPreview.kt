package mobi.cwiklinski.bloodline.ui.preview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.data.filed.DummyData
import mobi.cwiklinski.bloodline.domain.sortByRegion
import mobi.cwiklinski.bloodline.ui.screen.CentersView
import mobi.cwiklinski.bloodline.ui.widget.CenterItemView

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