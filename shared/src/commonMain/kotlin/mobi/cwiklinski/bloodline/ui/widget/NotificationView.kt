package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
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
import mobi.cwiklinski.bloodline.resources.notificationsLocationDescription
import mobi.cwiklinski.bloodline.resources.seeAll
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors.notificationRichTextColors
import mobi.cwiklinski.bloodline.ui.theme.notificationInfo
import mobi.cwiklinski.bloodline.ui.theme.notificationTitle
import mobi.cwiklinski.bloodline.ui.theme.richTextTypography
import mobi.cwiklinski.bloodline.ui.util.getType
import mobi.cwiklinski.bloodline.ui.util.toLocalizedString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun NotificationView(
    notification: Notification,
    isMarking: (String) -> Boolean = { false },
    onMarkRead: (notification: Notification) -> Unit = {},
) {
    Column(
        modifier = Modifier
        .background(notification.getType().color)
        .padding(20.dp)
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painterResource(notification.getType().icon),
                contentDescription = stringResource(notification.getType().description),
                modifier = Modifier.size(40.dp)
            )
            Text(
                notification.title,
                style = notificationTitle(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f).padding(horizontal = 10.dp)
            )
            if (!notification.read) {
                BoxWithConstraints(
                    modifier = Modifier.size(40.dp)
                ) {
                    if (isMarking(notification.id)) {
                        FormProgress(modifier = Modifier.size(40.dp))
                    } else {
                        IconButton(
                            onClick = {
                                onMarkRead.invoke(notification)
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .padding(8.dp)
                        ) {
                            Icon(
                                Icons.Filled.Done,
                                contentDescription = stringResource(Res.string.seeAll),
                            )
                        }
                    }
                }
            }
        }
        Markdown(
            notification.message.replace("<br>", "\n"),
            colors = notificationRichTextColors(),
            typography = richTextTypography(),
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
        )
        Row(
            modifier = Modifier.padding(start = 40.dp).fillMaxWidth(),
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
}