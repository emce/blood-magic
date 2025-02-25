package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Iron
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import mobi.cwiklinski.bloodline.domain.DonationType
import mobi.cwiklinski.bloodline.domain.model.Donation
import mobi.cwiklinski.bloodline.isMobile
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.donationFullBlood
import mobi.cwiklinski.bloodline.resources.donationNewHemoglobin
import mobi.cwiklinski.bloodline.resources.donationNewPressureLabel
import mobi.cwiklinski.bloodline.resources.donationPacked
import mobi.cwiklinski.bloodline.resources.donationPlasma
import mobi.cwiklinski.bloodline.resources.donationPlatelets
import mobi.cwiklinski.bloodline.resources.donationsDelete
import mobi.cwiklinski.bloodline.resources.donationsEdit
import mobi.cwiklinski.bloodline.resources.icon_donation_alert
import mobi.cwiklinski.bloodline.resources.icon_full_blood
import mobi.cwiklinski.bloodline.resources.icon_packed
import mobi.cwiklinski.bloodline.resources.icon_plasma
import mobi.cwiklinski.bloodline.resources.icon_platelets
import mobi.cwiklinski.bloodline.resources.liter
import mobi.cwiklinski.bloodline.resources.milliliter
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.itemSubTitle
import mobi.cwiklinski.bloodline.ui.theme.itemTitle
import mobi.cwiklinski.bloodline.ui.theme.itemTrailing
import mobi.cwiklinski.bloodline.ui.util.getShareText
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DonationItem(
    donation: Donation,
    onEdit: (Donation) -> Unit,
    onDelete: (Donation) -> Unit,
    onShare: (text: String) -> Unit,
    showAction: Boolean = true,
    modifier: Modifier = Modifier
) {
    val defaultPadding = 10.dp
    var typeName = stringResource(Res.string.donationFullBlood)
    var icon = Res.drawable.icon_full_blood
    when (donation.type) {
        DonationType.FULL_BLOOD -> {
            typeName = stringResource(Res.string.donationFullBlood)
            icon = Res.drawable.icon_full_blood
        }

        DonationType.PLASMA -> {
            typeName = stringResource(Res.string.donationPlasma)
            icon = Res.drawable.icon_plasma
        }

        DonationType.PLATELETS -> {
            typeName = stringResource(Res.string.donationPlatelets)
            icon = Res.drawable.icon_platelets
        }

        DonationType.PACKED_CELLS -> {
            typeName = stringResource(Res.string.donationPacked)
            icon = Res.drawable.icon_packed
        }
    }
    if (donation.disqualification) {
        icon = Res.drawable.icon_donation_alert
    }
    Box(
        modifier = modifier
            .padding(vertical = 10.dp, horizontal = 20.dp)
    ) {
        Card(
            shape = RoundedCornerShape(4.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (donation.disqualification) AppThemeColors.greyish else AppThemeColors.white
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(defaultPadding)
            ) {
                val (typeIcon, title, subtitle, amount, share, center, additional, actions) = createRefs()
                // Type icon
                Image(
                    painterResource(icon),
                    donation.amount.capacity(
                        stringResource(Res.string.milliliter),
                        stringResource(Res.string.liter)
                    ),
                    modifier = Modifier.size(40.dp).constrainAs(typeIcon) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                )
                // Share icon
                val shareText = getShareText(donation)
                Box(
                    modifier = Modifier.size(if (isMobile() && showAction) 40.dp else 0.dp).clickable {
                        onShare.invoke(shareText)
                    }.constrainAs(share) {
                        end.linkTo(parent.end)
                        centerVerticallyTo(typeIcon)
                    },
                    contentAlignment = Alignment.Center
                ) {
                    if (isMobile() && showAction) {
                        Image(
                            Icons.Filled.Share,
                            stringResource(Res.string.donationsEdit),
                            colorFilter = ColorFilter.tint(AppThemeColors.rose2),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                // Amount
                Text(
                    donation.amount.capacity(
                        stringResource(Res.string.milliliter),
                        stringResource(Res.string.liter)
                    ),
                    style = itemTrailing(),
                    modifier = Modifier
                        .padding(horizontal = defaultPadding)
                        .constrainAs(amount) {
                            end.linkTo(share.start)
                            centerVerticallyTo(typeIcon)
                            width = Dimension.preferredWrapContent
                        }
                )
                // Title
                Text(
                    typeName,
                    style = itemTitle().copy(
                        color = if (donation.disqualification)
                            AppThemeColors.black70 else AppThemeColors.black
                    ),
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(horizontal = defaultPadding)
                        .constrainAs(title) {
                            top.linkTo(typeIcon.top)
                            start.linkTo(typeIcon.end)
                            end.linkTo(amount.start)
                            width = Dimension.fillToConstraints
                        }
                )
                // Subtitle
                Text(
                    donation.date.toString(),
                    style = itemSubTitle().copy(
                        fontSize = 12.sp
                    ),
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(horizontal = defaultPadding)
                        .constrainAs(subtitle) {
                            bottom.linkTo(typeIcon.bottom)
                            start.linkTo(typeIcon.end)
                        }
                )
                // Center
                if (showAction) {
                    Text(
                        "${donation.center.name}, ${donation.center.getFullAddress()}",
                        style = itemSubTitle(),
                        textAlign = TextAlign.Start,
                        minLines = if (isMobile()) 1 else 2,
                        modifier = Modifier
                            .padding(horizontal = defaultPadding)
                            .constrainAs(center) {
                                top.linkTo(typeIcon.bottom, 10.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                width = Dimension.fillToConstraints
                            }
                    )
                    // Additional info
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = defaultPadding)
                            .constrainAs(additional) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                top.linkTo(center.bottom, 10.dp)
                                bottom.linkTo(actions.top)
                                width = Dimension.fillToConstraints
                            }
                    ) {
                        if (donation.hemoglobin > 0f) {
                            Image(
                                Icons.Filled.Iron,
                                contentDescription = stringResource(Res.string.donationNewHemoglobin),
                                modifier = Modifier.size(16.dp),
                                colorFilter = ColorFilter.tint(AppThemeColors.grey)
                            )
                            Text(
                                "${donation.hemoglobin} g/dl",
                                style = itemSubTitle(),
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp).weight(1f))
                        if (donation.systolic > 0 && donation.diastolic > 0) {
                            Image(
                                Icons.Filled.Compress,
                                contentDescription = stringResource(Res.string.donationNewPressureLabel),
                                modifier = Modifier.size(16.dp),
                                colorFilter = ColorFilter.tint(AppThemeColors.grey)
                            )
                            Text(
                                "${donation.systolic}/${donation.diastolic} mmHg",
                                style = itemSubTitle(),
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                }
                // Actions
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .padding(top = if (showAction) defaultPadding else 0.dp)
                        .constrainAs(actions) {
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        }
                ) {
                    if (showAction) {
                        JustTextButton(
                            text = stringResource(Res.string.donationsEdit),
                            onClicked = {
                                onEdit.invoke(donation)
                            },
                            textColor = AppThemeColors.black,
                            textDecoration = TextDecoration.None,
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.EditNote,
                                    modifier = Modifier.size(16.dp),
                                    contentDescription = stringResource(Res.string.donationsEdit)
                                )
                            }
                        )
                        Spacer(Modifier.width(20.dp))
                        JustTextButton(
                            text = stringResource(Res.string.donationsDelete),
                            onClicked = {
                                onDelete.invoke(donation)
                            },
                            textDecoration = TextDecoration.None,
                            textColor = AppThemeColors.rose1,
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.DeleteOutline,
                                    modifier = Modifier.size(16.dp),
                                    contentDescription = stringResource(Res.string.donationsDelete),
                                    tint = AppThemeColors.rose1
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}