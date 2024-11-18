package mobi.cwiklinski.bloodline

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import mobi.cwiklinski.bloodline.ui.screen.splash.SplashScreen
import mobi.cwiklinski.bloodline.ui.theme.getTypography
import org.koin.compose.KoinApplication

@Composable
fun NativeMainContent() {
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