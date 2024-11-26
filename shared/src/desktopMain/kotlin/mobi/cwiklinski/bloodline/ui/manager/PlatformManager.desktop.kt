package mobi.cwiklinski.bloodline.ui.manager

import androidx.compose.runtime.Composable
import java.awt.Desktop
import java.net.URI

@Composable
actual fun rememberPlatformManager(): PlatformManager = PlatformManager()

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class PlatformManager {
    actual fun openToast(content: String): Boolean {
        return false
    }

    actual fun openBrowser(url: String, openSystemBrowser: Boolean) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop()
                .isSupported(Desktop.Action.BROWSE)
        ) {
            Desktop.getDesktop().browse(URI(url))
        }
    }

    actual fun copyToClipboard(content: String, label: String?, isSensitive: Boolean): Boolean {
        return false
    }

    internal actual fun getClipboard(): String? {
        return null
    }

    actual fun clearClipboard() {
    }

    actual suspend fun shareText(content: String) {
    }

    actual suspend fun shareFile(path: String) {
    }

    actual fun enableLocationService() {
    }
}