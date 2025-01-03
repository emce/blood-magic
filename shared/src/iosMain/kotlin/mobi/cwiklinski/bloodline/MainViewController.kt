@file:Suppress("unused", "FunctionName")
package mobi.cwiklinski.bloodline

import androidx.compose.ui.window.ComposeUIViewController
import org.koin.core.context.startKoin
import org.koin.core.lazyModules

fun MainViewController() = ComposeUIViewController {
    startKoin {
        lazyModules(createAppLazyModule())
        modules(createAppModule())
    }
    MagicApp()
}