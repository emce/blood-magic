package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import mobi.cwiklinski.bloodline.domain.DonationType
import mobi.cwiklinski.bloodline.domain.model.Donation
import mobi.cwiklinski.bloodline.getPlatform
import mobi.cwiklinski.bloodline.getScreenWidth
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.close
import mobi.cwiklinski.bloodline.resources.donationFullBlood
import mobi.cwiklinski.bloodline.resources.donationPacked
import mobi.cwiklinski.bloodline.resources.donationPlasma
import mobi.cwiklinski.bloodline.resources.donationPlatelets
import mobi.cwiklinski.bloodline.resources.donationsDelete
import mobi.cwiklinski.bloodline.resources.donationsDeleteMessage
import mobi.cwiklinski.bloodline.resources.donationsDeleteWarning
import mobi.cwiklinski.bloodline.resources.donationsEdit
import mobi.cwiklinski.bloodline.resources.donationsShare
import mobi.cwiklinski.bloodline.resources.icon_delete
import mobi.cwiklinski.bloodline.resources.icon_donation_alert
import mobi.cwiklinski.bloodline.resources.icon_edit
import mobi.cwiklinski.bloodline.resources.icon_full_blood
import mobi.cwiklinski.bloodline.resources.icon_packed
import mobi.cwiklinski.bloodline.resources.icon_plasma
import mobi.cwiklinski.bloodline.resources.icon_platelets
import mobi.cwiklinski.bloodline.resources.icon_share
import mobi.cwiklinski.bloodline.resources.liter
import mobi.cwiklinski.bloodline.resources.milliliter
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.cardTitle
import mobi.cwiklinski.bloodline.ui.theme.contentAction
import mobi.cwiklinski.bloodline.ui.theme.contentText
import mobi.cwiklinski.bloodline.ui.theme.contentTitle
import mobi.cwiklinski.bloodline.ui.theme.itemSubTitle
import mobi.cwiklinski.bloodline.ui.theme.itemTitle
import mobi.cwiklinski.bloodline.ui.theme.itemTrailing
import mobi.cwiklinski.bloodline.ui.theme.submitButton
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DonationItem(
    donation: Donation,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onShare: (text: String) -> Unit,
    showAction: Boolean = true
) {
    val width = getScreenWidth() / 2 - getScreenWidth() / 100
    val defaultPadding = 15.dp
    var deletionDialog by remember { mutableStateOf(false) }
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
        modifier = Modifier
            .width(width)
            .padding(10.dp)
            .background(AppThemeColors.background)
    ) {
        Card(
            shape = RoundedCornerShape(4.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = AppThemeColors.white
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(defaultPadding)
            ) {
                val (typeIcon, title, subtitle, amount, share, actions) = createRefs()
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
                val shareText = stringResource(Res.string.donationsShare)
                    .replace("%s", donation.type.getGenitive())
                Box(
                    modifier = Modifier.size(40.dp).clickable {
                        onShare.invoke(shareText)
                    }.constrainAs(share) {
                        end.linkTo(parent.end)
                        centerVerticallyTo(typeIcon)
                    },
                    contentAlignment = Alignment.Center
                ) {
                    if (getPlatform().isMobile()) {
                        Image(
                            painterResource(Res.drawable.icon_share),
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
                        }
                )
                // Title
                Text(
                    typeName,
                    style = itemTitle().copy(
                        color = if (donation.disqualification)
                            AppThemeColors.black70 else AppThemeColors.black,
                        textAlign = TextAlign.Start
                    ),
                    modifier = Modifier
                        .padding(horizontal = defaultPadding)
                        .constrainAs(title) {
                            top.linkTo(typeIcon.top)
                            start.linkTo(typeIcon.end)
                            end.linkTo(amount.start)
                        }
                )
                // Subtitle
                Text(
                    donation.date.toString(),
                    style = itemSubTitle().copy(
                        textAlign = TextAlign.Start
                    ),
                    modifier = Modifier
                        .padding(horizontal = defaultPadding)
                        .constrainAs(subtitle) {
                            bottom.linkTo(typeIcon.bottom)
                            start.linkTo(typeIcon.end)
                            end.linkTo(amount.start)
                        }
                )
                // Actions
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .padding(top = defaultPadding)
                        .wrapContentSize()
                        .constrainAs(actions) {
                            top.linkTo(typeIcon.bottom)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end)
                        }
                ) {
                    if (showAction) {
                        JustTextButton(
                            text = stringResource(Res.string.donationsEdit),
                            onClicked = onEdit,
                            textStyle = contentAction().copy(
                                color = AppThemeColors.black
                            ),
                            textDecoration = TextDecoration.None,
                            leadingIcon = {
                                Icon(
                                    painterResource(Res.drawable.icon_edit),
                                    modifier = Modifier.size(16.dp),
                                    contentDescription = stringResource(Res.string.donationsEdit)
                                )
                            }
                        )
                        Spacer(Modifier.width(20.dp))
                        JustTextButton(
                            text = stringResource(Res.string.donationsDelete),
                            onClicked = onDelete,
                            textDecoration = TextDecoration.None,
                            textStyle = contentAction().copy(
                                color = AppThemeColors.rose1
                            ),
                            leadingIcon = {
                                Icon(
                                    painterResource(Res.drawable.icon_delete),
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
        if (deletionDialog) {
            AlertDialog(
                title = {
                    Text(
                        stringResource(Res.string.donationsDelete),
                        style = contentTitle()
                    )
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            stringResource(Res.string.donationsDeleteMessage),
                            style = cardTitle(),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            stringResource(Res.string.donationsDeleteWarning),
                            style = contentText(),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                onDismissRequest = {
                    deletionDialog = !deletionDialog
                },
                dismissButton = {
                    JustTextButton(
                        text = stringResource(Res.string.close),
                        onClicked = {
                            deletionDialog = !deletionDialog
                        },
                        textDecoration = TextDecoration.None,
                        contentPadding = PaddingValues(horizontal = 10.dp)
                    )
                },
                confirmButton = {
                    JustTextButton(
                        text = stringResource(Res.string.donationsDelete),
                        onClicked = {
                            deletionDialog = !deletionDialog
                            onDelete.invoke()
                        },
                        textDecoration = TextDecoration.None,
                        textStyle = submitButton().copy(
                            color = AppThemeColors.alertRed
                        ),
                        contentPadding = PaddingValues(horizontal = 10.dp)
                    )
                },
                textContentColor = AppThemeColors.background,
                titleContentColor = AppThemeColors.black70
            )
        }
    }
}