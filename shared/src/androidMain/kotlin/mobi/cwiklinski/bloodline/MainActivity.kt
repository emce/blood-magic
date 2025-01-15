package mobi.cwiklinski.bloodline

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.lazyModules
import org.koin.dsl.module
import org.publicvalue.multiplatform.oidc.appsupport.AndroidCodeAuthFlowFactory
import org.publicvalue.multiplatform.oidc.appsupport.CodeAuthFlowFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val codeAuthFlowFactory = AndroidCodeAuthFlowFactory(useWebView = false).also { it.registerActivity(this@MainActivity) }
        startKoin {
            androidContext(this@MainActivity)
            androidLogger()
            val codeAuth = module {
                factory<CodeAuthFlowFactory> { codeAuthFlowFactory }
            }
            lazyModules(createAppLazyModule())
            val modules = createAppModule().toMutableList()
            modules.add(codeAuth)
            modules(modules)
        }
        setContent {
            MagicApp()
        }
    }

}

@Preview
@Composable
fun AppAndroidPreview() {
    MagicApp()
}