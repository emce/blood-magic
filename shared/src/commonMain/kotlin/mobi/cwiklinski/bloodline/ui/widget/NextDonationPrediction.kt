package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import mobi.cwiklinski.bloodline.Constants
import mobi.cwiklinski.bloodline.domain.model.Donation
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.day
import mobi.cwiklinski.bloodline.resources.days
import mobi.cwiklinski.bloodline.resources.homeNextDonationPendingTitle
import mobi.cwiklinski.bloodline.resources.homeNextDonationReadySubtitle
import mobi.cwiklinski.bloodline.resources.homeNextDonationReadyTitle
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.cardTitle
import mobi.cwiklinski.bloodline.ui.theme.contentText
import mobi.cwiklinski.bloodline.ui.util.NextDonationTime
import mobi.cwiklinski.bloodline.ui.util.coloredShadow
import org.jetbrains.compose.resources.stringResource

@Composable
fun NextDonationPrediction(lastDonation: Donation?) {

    val now = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date
    var progress = 0f
    var progressTitle = ""
    var progressSubtitle = stringResource(Res.string.homeNextDonationReadyTitle)
    val readySubtitle = stringResource(Res.string.homeNextDonationReadySubtitle)
    val readyTitle = stringResource(Res.string.homeNextDonationReadyTitle)
    val notReadySubtitle = stringResource(Res.string.homeNextDonationPendingTitle)
    val day = stringResource(Res.string.day)
    val days = stringResource(Res.string.days)
    NextDonationTime(lastDonation, now).fillData { nextDonationDate ->
        if (nextDonationDate.fullBlood < 1) {
            progress = 1f
            progressTitle = readyTitle
            progressSubtitle = readySubtitle
        } else {
            val nextDonationTime =
                now.plus(DatePeriod(0, 0, nextDonationDate.fullBlood))
            progress =
                1 - (nextDonationDate.fullBlood / Constants.PERIOD_FULL_BLOOD_FULL_BLOOD.toFloat())
            progressTitle = nextDonationTime.toString()
            progressSubtitle =
                notReadySubtitle.replace(
                    "%s",
                    if (nextDonationDate.fullBlood == 1) {
                        "${nextDonationDate.fullBlood} $day}"
                    } else {
                        "${nextDonationDate.fullBlood} $days}"
                    }
                )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 20.dp, vertical = 12.dp),
    ) {
        Column(
            modifier = Modifier
                .coloredShadow(
                    color = AppThemeColors.black70.copy(alpha = 0.3f),
                    cornerRadiusDp = 8.dp,
                    offsetX = 3.dp,
                    offsetY = 3.dp,
                    blurRadius = 20f
                )
                .background(SolidColor(AppThemeColors.white), shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                progressTitle,
                style = cardTitle(),
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(20.dp))
            Text(
                progressSubtitle,
                style = contentText(),
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(20.dp))
            LinearProgressIndicator(
                progress = {
                    progress
                },
                modifier = Modifier.fillMaxWidth()
                    .height(24.dp)
                    .coloredShadow(
                        color = AppThemeColors.grey3,
                        cornerRadiusDp = 12.dp,
                        offsetX = 3.dp,
                        offsetY = 3.dp
                    ),
                color = AppThemeColors.red2,
                trackColor = AppThemeColors.iconRedBackground,
                strokeCap = StrokeCap.Round
            )
        }
    }
}