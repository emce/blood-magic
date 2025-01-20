package mobi.cwiklinski.bloodline.schedule

import com.tweener.alarmee.AlarmeeScheduler
import com.tweener.alarmee.AlarmeeSchedulerJvm
import com.tweener.alarmee.configuration.AlarmeeJvmPlatformConfiguration
import org.koin.dsl.module

actual fun createSchedulerModule() = module {
    single<AlarmeeScheduler> {
        AlarmeeSchedulerJvm(configuration = AlarmeeJvmPlatformConfiguration)
    }
}