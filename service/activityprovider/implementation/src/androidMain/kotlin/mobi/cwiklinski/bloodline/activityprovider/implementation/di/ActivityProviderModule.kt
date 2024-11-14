package mobi.cwiklinski.bloodline.activityprovider.implementation.di

import mobi.cwiklinski.bloodline.activityprovider.api.ActivityProvider
import mobi.cwiklinski.bloodline.activityprovider.api.ActivitySetter
import mobi.cwiklinski.bloodline.activityprovider.implementation.ActivityProviderImp
import org.koin.core.module.Module
import org.koin.dsl.module

val activityProviderModule: Module = module {
    single<ActivityProvider> { get<ActivityProviderImp>() }
    single<ActivitySetter> { get<ActivityProviderImp>() }
    single { ActivityProviderImp() }
}
