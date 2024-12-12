package mobi.cwiklinski.bloodline

import android.app.Application
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication: Application()/*, Configuration.Provider*/ {

    //private val workerFactory: KoinWorkerFactory by inject()

    /*override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setWorkerFactory(workerFactory).build()*/

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
        }
        if (BuildConfig.DEBUG) {
            Napier.base(DebugAntilog())
        }
        //val notificationService: AndroidNotificationService = get()
        //notificationService.initialize(R.drawable.ic_launcher_foreground)
        //val permissionUtil by permissionUtil()
        //permissionUtil.askNotificationPermission()
    }
}