package mobi.cwiklinski.bloodline.ui.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.data.filed.DummyData
import mobi.cwiklinski.bloodline.ui.screen.AdminNotificationForm
import mobi.cwiklinski.bloodline.ui.screen.AdminNotificationsView


@Preview
@Composable
fun AdminNotificationsFormPreview() {
    Column(
        modifier = Modifier.background(Color.White).padding(10.dp)
    ) {
        AdminNotificationForm()
    }

}

@Preview
@Composable
fun AdminNotificationsPreview() {
    AdminNotificationsView(notifications = DummyData.NOTIFICATIONS)
}