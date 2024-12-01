package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import mobi.cwiklinski.bloodline.domain.DonationType
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.donationNewTypeLabel
import mobi.cwiklinski.bloodline.ui.theme.itemSubTitle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DonationTypeItem(type: DonationType) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val (typeIcon, title) = createRefs()
        Image(
            painterResource(type.getIcon()),
            stringResource(Res.string.donationNewTypeLabel),
            modifier = Modifier.size(24.dp).constrainAs(typeIcon) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
            }
        )
        Text(
            type.getName(),
            style = itemSubTitle(),
            textAlign = TextAlign.Start,
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(typeIcon.end, margin = 10.dp)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
        )
    }
}