package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import mobi.cwiklinski.bloodline.data.IgnoredOnParcel
import mobi.cwiklinski.bloodline.data.Parcelize
import mobi.cwiklinski.bloodline.domain.model.Notification
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.icon_delete
import mobi.cwiklinski.bloodline.resources.icon_edit
import mobi.cwiklinski.bloodline.ui.model.NotificationScreenModel
import mobi.cwiklinski.bloodline.ui.model.NotificationState
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.contentText
import mobi.cwiklinski.bloodline.ui.theme.itemSubTitle
import mobi.cwiklinski.bloodline.ui.util.koinNavigatorScreenModel
import mobi.cwiklinski.bloodline.ui.widget.FormProgress
import mobi.cwiklinski.bloodline.ui.widget.NotificationDeleteDialog
import mobi.cwiklinski.bloodline.ui.widget.SecondaryButton
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Parcelize
class AdminNotificationsScreen : AppScreen() {

    @IgnoredOnParcel
    override val supportDialogs = false

    @Composable
    override fun defaultView() {
        val navigator = LocalNavigator.currentOrThrow
        val bottomNavigator = LocalBottomSheetNavigator.current
        val screenModel = navigator.koinNavigatorScreenModel<NotificationScreenModel>()
        val state by screenModel.state.collectAsStateWithLifecycle(NotificationState.Idle)
        val notifications by screenModel.notifications.collectAsStateWithLifecycle(emptyList())
        if (state is NotificationState.ToDelete) {
            NotificationDeleteDialog(
                notification = (state as NotificationState.ToDelete).notification,
                onClose = {
                    screenModel.resetState()
                },
                onDelete = { notification ->
                    screenModel.deleteNotification(notification)
                }
            )
        }
        if (state is NotificationState.Saving) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                FormProgress()
            }
        } else {
            AdminNotificationsView(
                notifications = notifications,
                onPreview = { notification ->
                    bottomNavigator.push(AdminNotificationPreviewScreen(notification))
                },
                onEdit = { notification ->
                    bottomNavigator.push(AdminNotificationEditScreen(notification))
                },
                onDelete = { notification ->
                    screenModel.markToDeletion(notification)
                }
            )
        }
    }
}

@Preview
@Composable
fun AdminNotificationsView(
    paddingValues: PaddingValues = PaddingValues(0.dp),
    notifications: List<Notification>,
    onPreview: (Notification) -> Unit = {},
    onEdit: (Notification) -> Unit = {},
    onDelete: (Notification) -> Unit = {},
    onNewNotification: () -> Unit = {}
) {
    LazyColumn(
        contentPadding = paddingValues,
        modifier = Modifier.fillMaxSize()
            .background(AppThemeColors.white),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Box(
                modifier = Modifier
                    .padding(horizontal = 5.dp, vertical = 10.dp)
                    .fillMaxWidth()
            ) {
                SecondaryButton(
                    onClick = onNewNotification,
                    text = "Nowa notyfikacja",
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
        items(notifications) { notification ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    notification.date.toString(),
                    style = itemSubTitle(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(75.dp)
                )
                Text(
                    notification.title,
                    style = contentText(),
                    modifier = Modifier
                        .weight(1.0f)
                        .clickable {
                            onPreview.invoke(notification)
                        }
                )
                Icon(
                    painterResource(Res.drawable.icon_delete),
                    contentDescription = "usunięcie notifyfikacji",
                    tint = AppThemeColors.accentRed1,
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .clickable {
                            onDelete.invoke(notification)
                        }
                )
                Icon(
                    painterResource(Res.drawable.icon_edit),
                    contentDescription = "edycja notifyfikacji",
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .clickable {
                            onEdit.invoke(notification)
                        }
                )
            }
        }
    }
}