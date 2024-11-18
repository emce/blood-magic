package mobi.cwiklinski.bloodline

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun createAppModule(): List<Module> = buildList {
    addAll(appModule)
}

private val platformModule = module {
    factory<CoroutineDispatcher> { Dispatchers.Main }
}