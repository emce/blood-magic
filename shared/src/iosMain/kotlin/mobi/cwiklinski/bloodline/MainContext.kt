package mobi.cwiklinski.bloodline

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize
import mobi.cwiklinski.bloodline.config.FirebaseConfig
import mobi.cwiklinski.bloodline.ui.screen.SplashScreen
import mobi.cwiklinski.bloodline.ui.theme.getTypography
import org.koin.compose.KoinApplication

@Composable
fun NativeMainContent() {
    Firebase.initialize(
        Unit, options = FirebaseOptions(
            applicationId = FirebaseConfig.FIREBASE_APP_ID,
            apiKey = FirebaseConfig.FIREBASE_IOS_API_KEY,
            databaseUrl = FirebaseConfig.FIREBASE_DATABASE_URL,
            storageBucket = FirebaseConfig.FIREBASE_STORAGE_BUCKET,
            projectId = FirebaseConfig.FIREBASE_PROJECT_ID,
            gcmSenderId = FirebaseConfig.FIREBASE_MESSAGING_SENDER_ID
        )
    )
    KoinApplication(
        application = {
            modules(createAppModule())
        }
    ) {
        MaterialTheme(
            typography = getTypography()
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