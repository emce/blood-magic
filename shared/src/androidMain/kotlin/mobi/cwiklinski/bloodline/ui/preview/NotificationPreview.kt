package mobi.cwiklinski.bloodline.ui.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import mobi.cwiklinski.bloodline.data.filed.DummyData
import mobi.cwiklinski.bloodline.ui.screen.NotificationsView
import mobi.cwiklinski.bloodline.ui.widget.NotificationView

@Preview
@Composable
fun NotificationsPreview() {
    Column(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        NotificationsView(notifications = DummyData.NOTIFICATIONS)
    }
}

@Preview
@Composable
fun NotificationPreview() {
    Column(
        modifier = Modifier.fillMaxWidth()
            .wrapContentHeight()
            .background(Color.White)
    ) {
        NotificationView(notification = DummyData.NOTIFICATIONS.random())
    }
}