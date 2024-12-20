package mobi.cwiklinski.bloodline

import androidx.compose.runtime.Composable
import org.koin.core.context.startKoin
import org.koin.core.lazyModules

@Composable
fun NativeMainContent() {
    startKoin {
        lazyModules(createAppLazyModule())
        modules(createAppModule())
    }
    MagicApp()
}