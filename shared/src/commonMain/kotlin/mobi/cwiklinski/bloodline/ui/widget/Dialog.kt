package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.mikepenz.markdown.m3.Markdown
import mobi.cwiklinski.bloodline.domain.model.Donation
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.close
import mobi.cwiklinski.bloodline.resources.donationsDelete
import mobi.cwiklinski.bloodline.resources.donationsDeleteMessage
import mobi.cwiklinski.bloodline.resources.donationsDeleteTitle
import mobi.cwiklinski.bloodline.resources.goBack
import mobi.cwiklinski.bloodline.resources.ic_mark_all_read
import mobi.cwiklinski.bloodline.resources.icon_close
import mobi.cwiklinski.bloodline.resources.icon_delete
import mobi.cwiklinski.bloodline.resources.notificationsMarkAllAsReadButton
import mobi.cwiklinski.bloodline.resources.notificationsMarkAllAsReadMessage
import mobi.cwiklinski.bloodline.resources.notificationsMarkAllAsReadTitle
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors.dialogRichTextColors
import mobi.cwiklinski.bloodline.ui.theme.alertTitle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun InformationDialog(
    title: String,
    message: String,
    onClose: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = { },
    ) {
        ConstraintLayout(
            modifier = Modifier.background(
                AppThemeColors.white,
                RoundedCornerShape(24.dp)
            ).padding(
                top = 24.dp,
                bottom = 48.dp,
                start = 24.dp,
                end = 24.dp
            )
        ) {
            val (titleRef, messageRef, starsRef, confirmRef, closeRef) = createRefs()
            Image(
                painterResource(Res.drawable.icon_close),
                contentDescription = stringResource(Res.string.close),
                modifier = Modifier.constrainAs(closeRef) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }.clickable {
                    onClose.invoke()
                }
            )
            StarsAnimation(
                modifier = Modifier.constrainAs(starsRef) {
                    top.linkTo(parent.top, 20.dp)
                    centerHorizontallyTo(parent)
                }
            )
            ConfirmAnimation(
                modifier = Modifier.constrainAs(confirmRef) {
                    bottom.linkTo(starsRef.bottom, (-40).dp)
                    centerHorizontallyTo(parent)
                }
            )
            Text(
                title,
                style = alertTitle(),
                modifier = Modifier.constrainAs(titleRef) {
                    top.linkTo(confirmRef.bottom, 20.dp)
                    centerHorizontallyTo(parent)
                }
            )
            Markdown(
                message,
                modifier = Modifier.constrainAs(messageRef) {
                    top.linkTo(titleRef.bottom, 20.dp)
                    bottom.linkTo(parent.bottom, 20.dp)
                    centerHorizontallyTo(parent)
                },
                colors = dialogRichTextColors()
            )
        }
    }
}

data class InformationDialogData(val title: String, val message: String)

@Composable
fun DonationDeleteDialog(
    donation: Donation,
    onClose: () -> Unit,
    onDelete: (donation: Donation) -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = { },
    ) {
        ConstraintLayout(
            modifier = Modifier
                .background(
                    AppThemeColors.white,
                    RoundedCornerShape(24.dp)
                ).padding(
                    top = 24.dp,
                    bottom = 48.dp,
                    start = 24.dp,
                    end = 24.dp
                )
        ) {
            val (titleRef, messageRef, imageRef, buttonsRef) = createRefs()
            Image(
                painterResource(Res.drawable.icon_delete),
                contentDescription = stringResource(Res.string.close),
                modifier = Modifier
                    .size(96.dp)
                    .constrainAs(imageRef) {
                        top.linkTo(parent.top)
                        centerHorizontallyTo(parent)
                    }.clickable {
                        onClose.invoke()
                    }
            )
            Text(
                stringResource(Res.string.donationsDeleteTitle),
                style = alertTitle(),
                modifier = Modifier.constrainAs(titleRef) {
                    top.linkTo(imageRef.bottom, 20.dp)
                    centerHorizontallyTo(parent)
                }
            )
            Markdown(
                stringResource(Res.string.donationsDeleteMessage),
                modifier = Modifier.constrainAs(messageRef) {
                    top.linkTo(titleRef.bottom, 20.dp)
                    centerHorizontallyTo(parent)
                },
                colors = dialogRichTextColors()
            )
            Row(
                modifier = Modifier.constrainAs(buttonsRef) {
                    top.linkTo(messageRef.bottom, 40.dp)
                }.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SecondaryButton(
                    onClick = {
                        onClose.invoke()
                    },
                    text = stringResource(Res.string.goBack)
                )
                SubmitButton(
                    onClick = {
                        onDelete.invoke(donation)
                    },
                    text = stringResource(Res.string.donationsDelete)
                )
            }
        }
    }
}



@Composable
fun MarkAllReadDialog(
    onClose: () -> Unit,
    onConfirm: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = { },
    ) {
        ConstraintLayout(
            modifier = Modifier.background(
                AppThemeColors.white,
                RoundedCornerShape(24.dp)
            ).padding(
                top = 24.dp,
                bottom = 48.dp,
                start = 24.dp,
                end = 24.dp
            )
        ) {
            val (titleRef, messageRef, imageRef, buttonsRef) = createRefs()
            Image(
                painterResource(Res.drawable.ic_mark_all_read),
                contentDescription = stringResource(Res.string.notificationsMarkAllAsReadTitle),
                modifier = Modifier
                    .size(96.dp)
                    .constrainAs(imageRef) {
                        top.linkTo(parent.top)
                        centerHorizontallyTo(parent)
                    }.clickable {
                        onClose.invoke()
                    }
            )
            Text(
                stringResource(Res.string.notificationsMarkAllAsReadTitle),
                style = alertTitle(),
                modifier = Modifier.constrainAs(titleRef) {
                    top.linkTo(imageRef.bottom, 20.dp)
                    centerHorizontallyTo(parent)
                }
            )
            Markdown(
                stringResource(Res.string.notificationsMarkAllAsReadMessage),
                modifier = Modifier.constrainAs(messageRef) {
                    top.linkTo(titleRef.bottom, 20.dp)
                    centerHorizontallyTo(parent)
                },
                colors = dialogRichTextColors()
            )
            Row(
                modifier = Modifier.constrainAs(buttonsRef) {
                    top.linkTo(messageRef.bottom, 40.dp)
                }.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SecondaryButton(
                    onClick = {
                        onClose.invoke()
                    },
                    text = stringResource(Res.string.goBack)
                )
                SubmitButton(
                    onClick = {
                        onConfirm.invoke()
                    },
                    text = stringResource(Res.string.notificationsMarkAllAsReadButton)
                )
            }
        }
    }
}