package mobi.cwiklinski.bloodline.ui.screen.splash

import org.koin.dsl.module

val splashModule = module {
    factory { SplashScreenModel(get()) }
}