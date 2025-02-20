package mobi.cwiklinski.bloodline.analytics.firebase

import mobi.cwiklinski.bloodline.analytics.api.Analytics
import org.koin.dsl.module

fun createAnalyticsModule() = module {
    single<Analytics> { AnalyticsFirebase() }
}