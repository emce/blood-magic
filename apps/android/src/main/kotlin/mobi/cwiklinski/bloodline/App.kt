package mobi.cwiklinski.bloodline

import android.app.Application
import androidx.work.Configuration
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.factory.KoinWorkerFactory
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class App: Application(), Configuration.Provider {

    private val workerFactory: KoinWorkerFactory by inject()

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setWorkerFactory(workerFactory).build()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            workManagerFactory()
            modules(appModule)
        }
    }
}