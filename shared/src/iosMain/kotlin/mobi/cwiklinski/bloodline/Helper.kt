@file:Suppress("unused", "FunctionName")
package mobi.cwiklinski.bloodline

import org.koin.core.context.startKoin
import org.koin.core.lazyModules

fun initKoin(){
    startKoin {
        lazyModules(createAppLazyModule())
        modules(createAppModule())
    }

}