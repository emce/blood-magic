package mobi.cwiklinski.bloodline

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import java.util.Locale

class AndroidPlatform : Platform {
    override val name = PlatformName.Android
    override val lang = Locale.getDefault().language
}

actual fun getPlatform(): Platform = AndroidPlatform()

@SuppressLint("DiscouragedApi")
@Composable
actual fun font(name: String, res: String, weight: FontWeight, style: FontStyle): Font {
    val context = LocalContext.current
    val id = context.resources.getIdentifier(res, "font", context.packageName)
    if (id == 0) {
        error("Font resource with name $res not found in package ${context.packageName}")
    }
    return Font(id, weight, style)
}

actual fun platformModule(): Module = module {
    single<Settings> {
        SharedPreferencesSettings((androidContext() as MainActivity).getPreferences(Context.MODE_PRIVATE))
    }
    factory<CoroutineDispatcher> { Dispatchers.Main }
}