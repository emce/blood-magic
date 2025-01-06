package mobi.cwiklinski.bloodline

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.appName
import mobi.cwiklinski.bloodline.resources.splash_logo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.core.context.startKoin
import org.koin.core.lazyModules

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = WindowState(
            position = WindowPosition.Aligned(Alignment.Center),
            size = DpSize(1024.dp, 768.dp)
        ),
        title = stringResource(Res.string.appName),
        icon = painterResource(Res.drawable.splash_logo),
        focusable = true,
        onKeyEvent = { false }
    ) {
        startKoin {
            modules(createAppModule())
            lazyModules(createAppLazyModule())
        }
        MagicApp()
    }
}