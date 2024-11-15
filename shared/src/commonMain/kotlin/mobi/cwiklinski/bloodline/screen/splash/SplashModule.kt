package mobi.cwiklinski.bloodline.screen.splash

import org.koin.dsl.module

val splashModule = module {
    factory { SplashScreenModel(get()) }
}