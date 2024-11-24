package mobi.cwiklinski.bloodline.ui.widget


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.domain.DonationType
import mobi.cwiklinski.bloodline.domain.model.Donation
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
import mobi.cwiklinski.bloodline.resources.icon_delete
import mobi.cwiklinski.bloodline.resources.icon_donation_alert
import mobi.cwiklinski.bloodline.resources.icon_edit
import mobi.cwiklinski.bloodline.resources.icon_full_blood
import mobi.cwiklinski.bloodline.resources.icon_packed
import mobi.cwiklinski.bloodline.resources.icon_plasma
import mobi.cwiklinski.bloodline.resources.icon_platelets
import mobi.cwiklinski.bloodline.resources.liter
import mobi.cwiklinski.bloodline.resources.milliliter
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.cardTitle
import mobi.cwiklinski.bloodline.ui.theme.contentText
import mobi.cwiklinski.bloodline.ui.theme.contentTitle
import mobi.cwiklinski.bloodline.ui.theme.itemTrailing
import mobi.cwiklinski.bloodline.ui.theme.itemSubTitle
import mobi.cwiklinski.bloodline.ui.theme.itemTitle
import mobi.cwiklinski.bloodline.ui.theme.submitButton
import mobi.cwiklinski.bloodline.ui.util.coloredShadow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DonationItem(
    donation: Donation,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    showAction: Boolean = true
) {
    val deletionDialog = remember { mutableStateOf(false) }
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
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        val dismissState = rememberSwipeToDismissBoxState()
        val deleteContentColor = AppThemeColors.red2
        val deleteContainerColor = AppThemeColors.iconRedBackground
        SwipeToDismissBox(
            modifier = Modifier.background(AppThemeColors.background),
            state = dismissState,
            backgroundContent = {
                Row(
                    Modifier
                        .fillMaxSize()
                        .background(
                            SolidColor(deleteContainerColor),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(end = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Spacer(Modifier.weight(1f))
                    Text(
                        stringResource(Res.string.donationsDelete),
                        style = submitButton().copy(color = deleteContentColor),
                        modifier = Modifier.padding(end = 20.dp)
                    )
                    Image(
                        painterResource(Res.drawable.icon_delete),
                        stringResource(Res.string.donationsDelete),
                        colorFilter = ColorFilter.tint(deleteContentColor),
                        modifier = Modifier
                    )
                }
            }
        ) {
            ListItem(
                modifier = Modifier
                    .background(
                        SolidColor(AppThemeColors.white),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .coloredShadow(
                        color = AppThemeColors.black70.copy(alpha = 0.1f),
                        cornerRadiusDp = 0.dp,
                        offsetX = 1.dp,
                        offsetY = 1.dp,
                        blurRadius = 50f
                    )
                    .padding(8.dp),
                leadingContent = {
                    Box(modifier = Modifier.size(40.dp)) {
                        Image(
                            painterResource(icon),
                            donation.amount.capacity(
                                stringResource(Res.string.milliliter),
                                stringResource(Res.string.liter)
                            ),
                            modifier = Modifier.size(40.dp)
                        )
                    }
                },
                headlineContent = {
                    Text(
                        typeName,
                        style = itemTitle().copy(
                            color = if (donation.disqualification)
                                AppThemeColors.black70 else AppThemeColors.black
                        )
                    )
                },
                supportingContent = {
                    Text(
                        donation.date.toString(),
                        style = itemSubTitle()
                    )
                },
                trailingContent = {
                    Row(
                        modifier = Modifier.background(
                            SolidColor(AppThemeColors.white),
                            shape = RoundedCornerShape(8.dp)
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            donation.amount.capacity(
                                stringResource(Res.string.milliliter),
                                stringResource(Res.string.liter)
                            ),
                            style = itemTrailing()
                        )
                        Box(
                            modifier = Modifier.size(40.dp).clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null,
                                onClick = onEdit
                            ),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            if (showAction) {
                                Image(
                                    painterResource(Res.drawable.icon_edit),
                                    stringResource(Res.string.donationsEdit),
                                    colorFilter = ColorFilter.tint(AppThemeColors.rose2),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                },
                colors = ListItemDefaults.colors(
                    containerColor = AppThemeColors.white,
                    headlineColor = AppThemeColors.black
                )
            )
        }
        if (deletionDialog.value) {
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
                    deletionDialog.value = !deletionDialog.value
                },
                dismissButton = {
                    JustTextButton(
                        text = stringResource(Res.string.close),
                        onClicked = {
                            deletionDialog.value = !deletionDialog.value
                        },
                        textDecoration = TextDecoration.None,
                        contentPadding = PaddingValues(horizontal = 10.dp)
                    )
                },
                confirmButton = {
                    JustTextButton(
                        text = stringResource(Res.string.donationsDelete),
                        onClicked = {
                            deletionDialog.value = !deletionDialog.value
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