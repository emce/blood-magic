package mobi.cwiklinski.bloodline.schedule

import android.app.NotificationManager
import androidx.compose.ui.graphics.Color
import com.tweener.alarmee.AlarmeeScheduler
import com.tweener.alarmee.AlarmeeSchedulerAndroid
import com.tweener.alarmee.channel.AlarmeeNotificationChannel
import com.tweener.alarmee.configuration.AlarmeeAndroidPlatformConfiguration
import mobi.cwiklinski.bloodline.R
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual fun createSchedulerModule() = module {
    single<AlarmeeScheduler> {
        AlarmeeSchedulerAndroid(
            context = androidContext(),
            configuration = AlarmeeAndroidPlatformConfiguration(
                notificationIconResId = R.drawable.ic_notification,
                notificationIconColor = Color.Red,
                notificationChannels = listOf(
                    AlarmeeNotificationChannel(
                        id = "notificationsChannel",
                        name = androidContext().getText(R.string.channel_notifications).toString(),
                        importance = NotificationManager.IMPORTANCE_DEFAULT,
                    ),
                    AlarmeeNotificationChannel(
                        id = "donationsChannel",
                        name = androidContext().getText(R.string.channel_donations).toString(),
                        importance = NotificationManager.IMPORTANCE_DEFAULT,
                    ),
                )
            )
        )
    }
}