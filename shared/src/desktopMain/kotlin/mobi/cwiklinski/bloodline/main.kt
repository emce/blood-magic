package mobi.cwiklinski.bloodline

import androidx.compose.foundation.Image
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import cafe.adriel.voyager.navigator.Navigator
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.appName
import mobi.cwiklinski.bloodline.resources.splash_logo
import mobi.cwiklinski.bloodline.ui.screen.splash.SplashScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.KoinApplication
import java.awt.Dimension

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = WindowState(
            position = WindowPosition.Aligned(Alignment.Center),
            size = DpSize(600.dp, 500.dp)
        ),
        title = stringResource(Res.string.appName),
        icon = painterResource(Res.drawable.splash_logo),
        onKeyEvent = { false }
    ) {
        window.minimumSize = Dimension(600, 500)
        MaterialTheme {
            Image(
                painterResource(Res.drawable.splash_logo),
                stringResource(Res.string.appName)
            )
        }
    }
}

@Composable
private fun App() {
    KoinApplication(
        application = {
            modules(appModule)
        }
    ) {
        MaterialTheme(
            //typography = getTypography()
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

@Preview
@Composable
fun AppDesktopPreview() {
    App()
}