package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import kotlinx.serialization.Serializable
import mobi.cwiklinski.bloodline.data.Parcelable
import mobi.cwiklinski.bloodline.data.Parcelize
import mobi.cwiklinski.bloodline.domain.model.Donation
import mobi.cwiklinski.bloodline.domain.model.Notification
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.close
import mobi.cwiklinski.bloodline.resources.donationsDelete
import mobi.cwiklinski.bloodline.resources.donationsDeleteMessage
import mobi.cwiklinski.bloodline.resources.donationsDeleteTitle
import mobi.cwiklinski.bloodline.resources.goBack
import mobi.cwiklinski.bloodline.resources.icon_close
import mobi.cwiklinski.bloodline.resources.profileTitle
import mobi.cwiklinski.bloodline.resources.settingsLogoutAction
import mobi.cwiklinski.bloodline.resources.settingsLogoutMessage
import mobi.cwiklinski.bloodline.resources.settingsLogoutTitle
import mobi.cwiklinski.bloodline.resources.setupSave
import mobi.cwiklinski.bloodline.ui.model.ProfileState
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.alertTitle
import mobi.cwiklinski.bloodline.ui.theme.hugeTitle
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
            RichText(
                message,
                modifier = Modifier.constrainAs(messageRef) {
                    top.linkTo(titleRef.bottom, 20.dp)
                    bottom.linkTo(parent.bottom, 20.dp)
                    centerHorizontallyTo(parent)
                },
            )
        }
    }
}

@Serializable
@Parcelize
data class InformationDialogData(val title: String, val message: String): Parcelable

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
                Icons.Filled.DeleteForever,
                contentDescription = stringResource(Res.string.close),
                colorFilter = ColorFilter.tint(AppThemeColors.iconRed),
                modifier = Modifier
                    .size(64.dp)
                    .constrainAs(imageRef) {
                        top.linkTo(parent.top)
                        centerHorizontallyTo(parent)
                    }.clickable {
                        onClose.invoke()
                    }
            )
            Text(
                stringResource(Res.string.donationsDeleteTitle),
                style = alertTitle().copy(
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.constrainAs(titleRef) {
                    top.linkTo(imageRef.bottom, 20.dp)
                    centerHorizontallyTo(parent)
                }
            )
            RichText(
                stringResource(Res.string.donationsDeleteMessage),
                modifier = Modifier.constrainAs(messageRef) {
                    top.linkTo(titleRef.bottom, 20.dp)
                    centerHorizontallyTo(parent)
                },
                centered = true
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
fun LogoutDialog(
    state: ProfileState,
    onCancel: () -> Unit,
    onSubmit: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onCancel,
        modifier = Modifier
            .background(
                color = AppThemeColors.white,
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .padding(30.dp)
                .wrapContentSize(),
        ) {
            Text(
                stringResource(Res.string.settingsLogoutTitle),
                modifier = Modifier.fillMaxWidth(),
                style = hugeTitle().copy(textAlign = TextAlign.Center)
            )
            Spacer(Modifier.height(10.dp))
            RichText(
                stringResource(Res.string.settingsLogoutMessage),
                centered = true
            )
            Spacer(Modifier.height(30.dp))
            if (state == ProfileState.LoggingOut) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    FormProgress()
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SecondaryButton(
                        text = stringResource(Res.string.close),
                        onClick = onCancel
                    )
                    SubmitButton(
                        text = stringResource(Res.string.settingsLogoutAction),
                        onClick = onSubmit
                    )
                }
            }
        }
    }
}

@Composable
fun NotificationDeleteDialog(
    notification: Notification,
    onClose: () -> Unit,
    onDelete: (notification: Notification) -> Unit
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
                Icons.Filled.DeleteOutline,
                contentDescription = stringResource(Res.string.close),
                colorFilter = ColorFilter.tint(AppThemeColors.iconRed),
                modifier = Modifier
                    .size(64.dp)
                    .constrainAs(imageRef) {
                        top.linkTo(parent.top)
                        centerHorizontallyTo(parent)
                    }.clickable {
                        onClose.invoke()
                    }
            )
            Text(
                "Usunięcie notyfikacji",
                style = alertTitle().copy(
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.constrainAs(titleRef) {
                    top.linkTo(imageRef.bottom, 20.dp)
                    centerHorizontallyTo(parent)
                }
            )
            Text(
                "Notyfikacja zosytanie usunięta z bazy. Proszę o potwierdzenie:",
                textAlign = TextAlign.Center,
                modifier = Modifier.constrainAs(messageRef) {
                    top.linkTo(titleRef.bottom, 20.dp)
                    centerHorizontallyTo(parent)
                }
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
                        onDelete.invoke(notification)
                    },
                    text = "Usuń notyfikację"
                )
            }
        }
    }
}

@Composable
fun ProfileDialog(
    onClose: () -> Unit,
    onSubmit: () -> Unit,
    formEnabled: Boolean = true,
    content: @Composable () -> Unit = {}
) {
    BasicAlertDialog(
        onDismissRequest = { },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    AppThemeColors.white,
                    RoundedCornerShape(24.dp)
                ).padding(
                    top = 24.dp,
                    bottom = 48.dp,
                    start = 24.dp,
                    end = 24.dp
                )
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                stringResource(Res.string.profileTitle),
                style = alertTitle().copy(
                    textAlign = TextAlign.Center
                )
            )
            content.invoke()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (formEnabled) {
                    SecondaryButton(
                        onClick = {
                            onClose.invoke()
                        },
                        text = stringResource(Res.string.goBack)
                    )
                    SubmitButton(
                        onClick = onSubmit,
                        text = stringResource(Res.string.setupSave)
                    )
                } else {
                    FormProgress()
                }
            }
        }
    }
}