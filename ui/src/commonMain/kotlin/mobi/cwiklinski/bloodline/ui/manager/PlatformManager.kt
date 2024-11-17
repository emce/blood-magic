package mobi.cwiklinski.bloodline.ui.manager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

val LocalPlatformManager: ProvidableCompositionLocal<PlatformManager> =
    staticCompositionLocalOf { error("PlatformManager not initialized") }

@Composable
expect fun rememberPlatformManager(): PlatformManager

expect class PlatformManager {
    fun openBrowser(url: String, openSystemBrowser: Boolean)

    fun openToast(content: String): Boolean

    fun copyToClipboard(content: String, label: String? = null, isSensitive: Boolean = false): Boolean
    internal fun getClipboard(): String?
    fun clearClipboard()

    suspend fun shareText(content: String)
    suspend fun shareFile(path: String)
    fun enableLocationService()
    fun openBluetoothSettings()
}

fun PlatformManager.getClipboard(clearClipboard: Boolean = false): String? {
    return getClipboard().also {
        if (clearClipboard) {
            clearClipboard()
        }
    }
}