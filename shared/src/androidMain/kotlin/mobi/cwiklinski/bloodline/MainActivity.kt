package mobi.cwiklinski.bloodline

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import coil3.compose.setSingletonImageLoaderFactory
import com.mmk.kmpnotifier.permission.permissionUtil
import mobi.cwiklinski.bloodline.ui.screen.SplashScreen
import mobi.cwiklinski.bloodline.ui.theme.AppTheme
import mobi.cwiklinski.bloodline.ui.widget.getAsyncImageLoader

class MainActivity : ComponentActivity() {

    //private val notificationService by inject<AndroidNotificationService>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val permissionUtil by permissionUtil()
        permissionUtil.askNotificationPermission()
        //notificationService.onCreateOrOnNewIntent(intent)
        setContent {
            App()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        //notificationService.onCreateOrOnNewIntent(intent)
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun App() {
    setSingletonImageLoaderFactory { context ->
        getAsyncImageLoader(context)
    }
    AppTheme {
        BottomSheetNavigator(
            modifier = Modifier.animateContentSize(),
            sheetShape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            skipHalfExpanded = true
        ) {
            Navigator(
                screen = SplashScreen(),
                onBackPressed = {
                    true
                }
            )
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    MainApplication()
}