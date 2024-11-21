package mobi.cwiklinski.bloodline

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mmk.kmpnotifier.permission.permissionUtil
import mobi.cwiklinski.bloodline.activityprovider.api.ActivitySetter
import mobi.cwiklinski.bloodline.notification.api.AndroidNotificationService
import mobi.cwiklinski.bloodline.ui.screen.SplashScreen
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val activitySetter by inject<ActivitySetter>()
    private val notificationService by inject<AndroidNotificationService>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val permissionUtil by permissionUtil()
        permissionUtil.askNotificationPermission()
        notificationService.onCreateOrOnNewIntent(intent)
        setContent {
            SplashScreen()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        notificationService.onCreateOrOnNewIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        activitySetter.set(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        activitySetter.clear()
    }

}

@Preview
@Composable
fun AppAndroidPreview() {
    SplashScreen()
}