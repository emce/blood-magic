package mobi.cwiklinski.bloodline.ui

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PersistableBundle
import android.provider.Settings
import android.widget.Toast
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.idShare
import org.jetbrains.compose.resources.stringResource
import java.io.File

@Composable
actual fun rememberPlatformManager(): PlatformManager {
    val context = LocalContext.current
    val shareId: String = stringResource(Res.string.idShare)

    return remember {
        PlatformManager(context, shareId)
    }
}

actual class PlatformManager(
    private val context: Context,
    private val shareId: String
) {

    actual fun openToast(content: String): Boolean {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show()
        return true
    }

    actual fun openBrowser(url: String, openSystemBrowser: Boolean) {
        try {
            if (openSystemBrowser) {
                context.startActivity(Intent(Intent.ACTION_VIEW).also {
                    it.setData(url.toUri())
                })
            } else {
                val builder = CustomTabsIntent.Builder()
                builder.setShowTitle(true)
                builder.setUrlBarHidingEnabled(false)
                builder.setDefaultColorSchemeParams(
                    CustomTabColorSchemeParams.Builder().build()
                )
                val customTabsIntent = builder.build()
                customTabsIntent.launchUrl(context, url.toUri())

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    actual fun copyToClipboard(content: String, label: String?, isSensitive: Boolean): Boolean {
        (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(
            ClipData.newPlainText(label ?: "Green", content).apply {
                if (isSensitive) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        description.extras = PersistableBundle().apply {
                            putBoolean(ClipDescription.EXTRA_IS_SENSITIVE, true)
                        }
                    }
                }
            }
        )

        return Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2
    }

    internal actual fun getClipboard(): String? {
        return (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).let {
            it.primaryClip?.getItemAt(0)?.text?.toString()
        }
    }

    @SuppressLint("NewApi")
    actual fun clearClipboard() {
        (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                it.clearPrimaryClip()
            }
        }
    }

    actual suspend fun shareText(content: String) {
        val builder = ShareCompat.IntentBuilder(context)
            .setType("text/plain")
            .setText(content)

        context.startActivity(
            Intent.createChooser(
                builder.intent,
                shareId
            )
        )
    }

    actual suspend fun shareFile(path: String) {
        val fileUri = FileProvider.getUriForFile(
            context,
            context.packageName.toString() + ".provider",
            File(path)
        )

        val builder = ShareCompat.IntentBuilder(context)
            .setType("text/plain")
            .setStream(fileUri)

        context.startActivity(
            Intent.createChooser(
                builder.intent,
                shareId
            )
        )
    }

    actual fun enableLocationService() {
        context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }
}