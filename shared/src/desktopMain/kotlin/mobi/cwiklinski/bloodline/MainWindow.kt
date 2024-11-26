package mobi.cwiklinski.bloodline

import android.content.Context
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import com.google.firebase.FirebasePlatform
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize
import mobi.cwiklinski.bloodline.config.FirebaseConfig
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.appName
import mobi.cwiklinski.bloodline.resources.splash_logo
import mobi.cwiklinski.bloodline.ui.screen.SplashScreen
import mobi.cwiklinski.bloodline.ui.theme.AppTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import java.awt.Dimension

fun main() = application {
    FirebasePlatform.initializeFirebasePlatform(object : FirebasePlatform() {
        val storage = mutableMapOf<String, String>()
        override fun store(key: String, value: String) = storage.set(key, value)
        override fun retrieve(key: String) = storage[key]
        override fun clear(key: String) { storage.remove(key) }
        override fun log(msg: String) = println(msg)
    })
    Firebase.initialize(
        Context(), options = FirebaseOptions(
            applicationId = FirebaseConfig.FIREBASE_APP_ID,
            apiKey = FirebaseConfig.FIREBASE_IOS_API_KEY,
            databaseUrl = FirebaseConfig.FIREBASE_DATABASE_URL,
            storageBucket = FirebaseConfig.FIREBASE_STORAGE_BUCKET,
            projectId = FirebaseConfig.FIREBASE_PROJECT_ID,
            gcmSenderId = FirebaseConfig.FIREBASE_MESSAGING_SENDER_ID
        )
    )
    Window(
        onCloseRequest = ::exitApplication,
        state = WindowState(
            position = WindowPosition.Aligned(Alignment.Center),
            size = DpSize(600.dp, 500.dp)
        ),
        title = stringResource(Res.string.appName),
        icon = painterResource(Res.drawable.splash_logo),
        focusable = true,
        onKeyEvent = { false }
    ) {
        window.minimumSize = Dimension(600, 500)
        App()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun App() {
    KoinApplication(
        application = {
            modules(createAppModule())
        }
    ) {
        AppTheme {
            BottomSheetNavigator(
                modifier = Modifier.animateContentSize(),
                sheetShape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                skipHalfExpanded = true
            ) {
                Navigator(
                    screen = SplashScreen(),
                    onBackPressed = {
                        false
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun AppDesktopPreview() {
    App()
}