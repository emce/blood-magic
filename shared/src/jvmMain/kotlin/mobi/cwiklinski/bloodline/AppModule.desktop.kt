package mobi.cwiklinski.bloodline

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import mobi.cwiklinski.bloodline.auth.firebase.DesktopCodeAuthFlowFactory
import org.koin.core.module.LazyModule
import org.koin.core.module.Module
import org.koin.dsl.module
import org.publicvalue.multiplatform.oidc.appsupport.CodeAuthFlowFactory

actual fun createAppModule(): List<Module> = buildList {
    add(platformModule)
    add(module {
        single<CodeAuthFlowFactory> { DesktopCodeAuthFlowFactory() }
    })
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

val platformModule = module {
    factory<CoroutineDispatcher> { Dispatchers.IO }
    factory<CoroutineScope> { CoroutineScope(Dispatchers.IO) }
}