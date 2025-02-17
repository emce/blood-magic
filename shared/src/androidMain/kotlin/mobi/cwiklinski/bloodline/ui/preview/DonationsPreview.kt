package mobi.cwiklinski.bloodline.ui.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.data.filed.DummyData
import mobi.cwiklinski.bloodline.ui.screen.DonationsView
import mobi.cwiklinski.bloodline.ui.widget.DonationDeleteDialog
import mobi.cwiklinski.bloodline.ui.widget.DonationItem

@Preview
@Composable
fun DonationItemPreview() {
    DonationItem(DummyData.DONATIONS.random(), {}, {}, {}, true)
}

@Preview
@Composable
fun DonationsPreview() {
    DonationsView(
        paddingValues = PaddingValues(0.dp),
        donations = DummyData.DONATIONS.filter{ it.hemoglobin > 0 && it.systolic > 0 }.toList(),
        onEdit = {},
        onDelete = {},
        onShare = {},
        onDonationAdd = {}
    )
}

@Preview
@Composable
fun DonationsEmptyPreview() {
    DonationsView(
        paddingValues = PaddingValues(0.dp),
        donations = emptyList(),
        onEdit = {},
        onDelete = {},
        onShare = {},
        onDonationAdd = {}
    )
}

@Preview(locale = "pl")
@Composable
fun DonationDeleteDialogPreview() {
    Column {
        DonationDeleteDialog(
            DummyData.DONATIONS.random(),
            {}, {}
        )
    }
}