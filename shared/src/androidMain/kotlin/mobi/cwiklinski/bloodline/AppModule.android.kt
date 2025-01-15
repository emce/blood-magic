package mobi.cwiklinski.bloodline

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.LazyModule
import org.koin.core.module.Module
import org.koin.dsl.module
import org.publicvalue.multiplatform.oidc.ExperimentalOpenIdConnect

actual fun createAppModule(): List<Module> = buildList {
    add(platformModule)
    appModule.forEach {
        if (it is Module) {
            add(it)
        }
    }
}

actual fun createAppLazyModule(): List<LazyModule> = buildList {
    appModule.forEach {
        if (it is LazyModule) {
            add(it)
        }
    }
}

@OptIn(ExperimentalOpenIdConnect::class)
val platformModule = module {
    factory<CoroutineDispatcher> { Dispatchers.Main }
    factory<CoroutineScope> { CoroutineScope(Dispatchers.Main) }
}