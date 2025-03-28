@file:Suppress("unused", "FunctionName")
package mobi.cwiklinski.bloodline

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import mobi.cwiklinski.bloodline.common.manager.BackgroundJobManager
import mobi.cwiklinski.bloodline.di.Dependencies
import org.koin.dsl.module
import org.publicvalue.multiplatform.oidc.appsupport.CodeAuthFlowFactory
import org.publicvalue.multiplatform.oidc.appsupport.IosCodeAuthFlowFactory

fun startKoin() {
    Dependencies.initKoin(
        platformModule = platformModule,
        customModules = listOf(
            module {
                single<CodeAuthFlowFactory> { IosCodeAuthFlowFactory() }
            }
        )
    )
}

val platformModule = module {
    factory<CoroutineDispatcher> { Dispatchers.Main }
    factory<CoroutineScope> { CoroutineScope(Dispatchers.Main) }
    single<BackgroundJobManager> { BackgroundJobManager() }
}