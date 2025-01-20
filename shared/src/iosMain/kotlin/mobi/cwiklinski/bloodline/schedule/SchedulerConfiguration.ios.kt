package mobi.cwiklinski.bloodline.schedule

import com.tweener.alarmee.AlarmeeScheduler
import com.tweener.alarmee.AlarmeeSchedulerIos
import com.tweener.alarmee.configuration.AlarmeeIosPlatformConfiguration
import org.koin.dsl.module

actual fun createSchedulerModule() = module {
    single<AlarmeeScheduler> {
        AlarmeeSchedulerIos(configuration = AlarmeeIosPlatformConfiguration)
    }
}