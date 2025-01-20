package mobi.cwiklinski.bloodline

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import mobi.cwiklinski.bloodline.auth.firebase.DesktopCodeAuthFlowFactory
import mobi.cwiklinski.bloodline.di.Dependencies
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.appName
import mobi.cwiklinski.bloodline.resources.splash_logo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.dsl.module
import org.publicvalue.multiplatform.oidc.appsupport.CodeAuthFlowFactory

fun main() = application {
    NotifierManager.initialize(
        NotificationPlatformConfiguration.Desktop(
            showPushNotification = true,
            notificationIconPath = "composeResources/mobi.cwiklinski.bloodline.resources/drawable/ic_notification.png"
        )
    )
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
        Dependencies.initKoin(
            platformModule = platformModule,
            customModules = listOf(
                module {
                    single<CodeAuthFlowFactory> { DesktopCodeAuthFlowFactory() }
                }
            )
        )
        MagicApp()
    }
}

val platformModule = module {
    factory<CoroutineDispatcher> { Dispatchers.IO }
    factory<CoroutineScope> { CoroutineScope(Dispatchers.IO) }
}