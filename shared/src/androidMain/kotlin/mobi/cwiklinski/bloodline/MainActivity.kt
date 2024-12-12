package mobi.cwiklinski.bloodline

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mmk.kmpnotifier.permission.permissionUtil

class MainActivity : ComponentActivity() {

    //private val notificationService by inject<AndroidNotificationService>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val permissionUtil by permissionUtil()
        permissionUtil.askNotificationPermission()
        //notificationService.onCreateOrOnNewIntent(intent)
        setContent {
            MagicApp()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        //notificationService.onCreateOrOnNewIntent(intent)
    }

}

@Preview
@Composable
fun AppAndroidPreview() {
    MagicApp()
}