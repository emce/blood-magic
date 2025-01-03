package mobi.cwiklinski.bloodline.common

import mobi.cwiklinski.bloodline.common.manager.AppCallbackManager
import mobi.cwiklinski.bloodline.common.manager.CallbackManager
import org.koin.dsl.module

val commonModule = module {
    single<CallbackManager> { AppCallbackManager(get()) }
}