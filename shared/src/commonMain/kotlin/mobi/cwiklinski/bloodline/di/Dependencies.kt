package mobi.cwiklinski.bloodline.di

import co.touchlab.kermit.Logger
import mobi.cwiklinski.bloodline.analytics.firebase.createAnalyticsModule
import mobi.cwiklinski.bloodline.auth.firebase.createAuthenticationModule
import mobi.cwiklinski.bloodline.common.manager.AppCallbackManager
import mobi.cwiklinski.bloodline.common.manager.CallbackManager
import mobi.cwiklinski.bloodline.data.firebase.createDataModule
import mobi.cwiklinski.bloodline.storage.datastore.storageModule
import mobi.cwiklinski.bloodline.ui.uiModule
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.logger.MESSAGE
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

object Dependencies {

    fun initKoin(
        platformModule: Module,
        customModules: List<Module> = emptyList(),
        koinApplication: KoinAppDeclaration = {}
    ) = startKoin {
        logger(logger = object : org.koin.core.logger.Logger() {
            override fun display(level: Level, msg: MESSAGE) = when (level) {
                Level.DEBUG -> Logger.d(messageString = msg)
                Level.INFO -> Logger.i(messageString = msg)
                Level.WARNING -> Logger.w(messageString = msg)
                Level.ERROR -> Logger.e(messageString = msg)
                Level.NONE -> Logger.d(messageString = msg)
            }
        })
        koinApplication()
        modules(
            platformModule,
            createAuthenticationModule(),
            *createDataModule().toTypedArray(),
            createAnalyticsModule(),
            *uiModule.toTypedArray(),
            module {
                single<CallbackManager> { AppCallbackManager(get()) }
            },
            storageModule,
            *customModules.toTypedArray()
        )
    }

}