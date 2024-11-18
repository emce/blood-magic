package mobi.cwiklinski.bloodline

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import mobi.cwiklinski.bloodline.activityprovider.implementation.di.activityProviderModule
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun createAppModule(): List<Module> = buildList {
    add(platformModule)
    add(activityProviderModule)
    addAll(appModule)
}

val platformModule = module {
    factory<CoroutineDispatcher> { Dispatchers.Main }
}