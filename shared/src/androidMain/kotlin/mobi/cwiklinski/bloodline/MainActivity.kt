package mobi.cwiklinski.bloodline

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.work.WorkManager
import co.touchlab.kermit.Logger
import com.mmk.kmpnotifier.extensions.onCreateOrOnNewIntent
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import com.mmk.kmpnotifier.permission.permissionUtil
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import mobi.cwiklinski.bloodline.common.manager.BackgroundJobManager
import mobi.cwiklinski.bloodline.di.Dependencies
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.error.KoinApplicationAlreadyStartedException
import org.koin.dsl.module
import org.publicvalue.multiplatform.oidc.appsupport.AndroidCodeAuthFlowFactory
import org.publicvalue.multiplatform.oidc.appsupport.CodeAuthFlowFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NotifierManager.onCreateOrOnNewIntent(intent)
        val codeAuthFlowFactory = AndroidCodeAuthFlowFactory(useWebView = false).also { it.registerActivity(this@MainActivity) }
        try {
            Dependencies.initKoin(
                platformModule = platformModule,
                koinApplication = {
                    androidContext(this@MainActivity)
                    androidLogger()
                    workManagerFactory()
                },
                customModules = listOf(
                    module {
                        single<WorkManager> { WorkManager.getInstance(this@MainActivity) }
                        factory<CodeAuthFlowFactory> { codeAuthFlowFactory }
                    }
                )
            )
        } catch (e: KoinApplicationAlreadyStartedException) {
            Logger.e("Android Activity", e)
        }
        NotifierManager.initialize(
            configuration = NotificationPlatformConfiguration.Android(
                notificationIconResId = R.drawable.ic_notification,
                showPushNotification = true,
            )
        )
        val permissionUtil by permissionUtil()
        permissionUtil.askNotificationPermission()
        setContent {
            MagicApp()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        NotifierManager.onCreateOrOnNewIntent(intent)
    }

}

val platformModule = module {
    factory<CoroutineDispatcher> { Dispatchers.Main }
    factory<CoroutineScope> { CoroutineScope(Dispatchers.Main) }
    single<BackgroundJobManager> { BackgroundJobManager(get()) }
}