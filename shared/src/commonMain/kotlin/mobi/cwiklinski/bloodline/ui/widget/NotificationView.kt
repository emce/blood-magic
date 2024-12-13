package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.m3.Markdown
import mobi.cwiklinski.bloodline.domain.model.Notification
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.ic_map_pin
import mobi.cwiklinski.bloodline.resources.ic_mark_read
import mobi.cwiklinski.bloodline.resources.notificationsLocationDescription
import mobi.cwiklinski.bloodline.resources.seeAll
import mobi.cwiklinski.bloodline.ui.model.NotificationState
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors.notificationRichTextColors
import mobi.cwiklinski.bloodline.ui.theme.notificationInfo
import mobi.cwiklinski.bloodline.ui.theme.notificationTitle
import mobi.cwiklinski.bloodline.ui.util.getType
import mobi.cwiklinski.bloodline.ui.util.toLocalizedString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun NotificationView(
    notification: Notification,
    state: NotificationState,
    onMarkRead: (notification: Notification) -> Unit,
) {
    Row(
        modifier = Modifier
            .background(notification.getType().color)
            .padding(20.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painterResource(notification.getType().icon),
            contentDescription = stringResource(notification.getType().description),
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Column(
            modifier = Modifier.weight(1.0f),
        ) {
            Text(
                notification.title,
                style = notificationTitle(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Markdown(
                notification.message.replace("<br>", "\n"),
                colors = notificationRichTextColors()
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    notification.date.toLocalizedString(),
                    style = notificationInfo()
                )
                Spacer(modifier = Modifier.width(14.dp))
                Image(
                    painterResource(Res.drawable.ic_map_pin),
                    contentDescription = stringResource(Res.string.notificationsLocationDescription)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    notification.location,
                    style = notificationInfo()
                )
            }
        }
        Spacer(modifier = Modifier.width(20.dp))
        if (!notification.read) {
            if (state is NotificationState.MarkingAsRead && state.notification.id == notification.id) {
                FormProgress(modifier = Modifier.size(40.dp))
            } else {
                IconButton(
                    onClick = {
                        onMarkRead.invoke(notification)
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .padding(8.dp)
                ) {
                    Icon(
                        painterResource(Res.drawable.ic_mark_read),
                        contentDescription = stringResource(Res.string.seeAll),
                    )
                }
            }
            Spacer(modifier = Modifier.width(20.dp))
        }
    }
}