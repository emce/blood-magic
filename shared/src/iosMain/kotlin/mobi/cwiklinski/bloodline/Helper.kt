@file:Suppress("unused", "FunctionName")
package mobi.cwiklinski.bloodline

import org.koin.core.context.startKoin
import org.koin.core.lazyModules
import org.koin.dsl.module
import org.publicvalue.multiplatform.oidc.appsupport.CodeAuthFlowFactory
import org.publicvalue.multiplatform.oidc.appsupport.IosCodeAuthFlowFactory

fun initKoin(){
    startKoin {
        module {
            single<CodeAuthFlowFactory> { IosCodeAuthFlowFactory() }
        }
        lazyModules(createAppLazyModule())
        modules(createAppModule())
    }

}