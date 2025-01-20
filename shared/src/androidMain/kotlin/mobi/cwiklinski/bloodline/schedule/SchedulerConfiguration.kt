package mobi.cwiklinski.bloodline.schedule

import android.app.NotificationManager
import androidx.compose.ui.graphics.Color
import com.tweener.alarmee.channel.AlarmeeNotificationChannel
import com.tweener.alarmee.configuration.AlarmeeAndroidPlatformConfiguration
import com.tweener.alarmee.configuration.AlarmeePlatformConfiguration
import mobi.cwiklinski.bloodline.R

val platformConfiguration: AlarmeePlatformConfiguration = AlarmeeAndroidPlatformConfiguration(
    notificationIconResId = R.drawable.ic_launcher_background,
    notificationIconColor = Color.Transparent,
    notificationChannels = listOf(
        AlarmeeNotificationChannel(
            id = "dailyNewsChannelId",
            name = "Daily news notifications",
            importance = NotificationManager.IMPORTANCE_HIGH,
            soundFilename = "notifications_sound",
        ),
        AlarmeeNotificationChannel(
            id = "breakingNewsChannelId",
            name = "Breaking news notifications",
            importance = NotificationManager.IMPORTANCE_LOW,
        ),
    )
)