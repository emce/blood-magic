package mobi.cwiklinski.bloodline.ui.manager

import androidx.compose.runtime.Composable
import platform.Foundation.NSURL
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIPasteboard

@Composable
actual fun rememberPlatformManager(): PlatformManager {
    return PlatformManager(UIApplication.sharedApplication())
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class PlatformManager(val application: UIApplication) {
    actual fun openToast(content: String): Boolean {
        return false
    }

    actual fun openBrowser(url: String, openSystemBrowser: Boolean) {
        NSURL(string = url).takeIf { application.canOpenURL(it) }?.also {
            application.openURL(it)
        }
    }

    actual fun copyToClipboard(content: String, label: String?, isSensitive: Boolean): Boolean {
        UIPasteboard.generalPasteboard().string = content
        return false
    }

    internal actual fun getClipboard(): String? {
        return UIPasteboard.generalPasteboard().string
    }

    actual fun clearClipboard() {
        UIPasteboard.generalPasteboard().string = null
    }

    actual suspend fun shareText(content: String) {
        val currentViewController = UIApplication.sharedApplication().keyWindow?.rootViewController
        val activityViewController = UIActivityViewController(listOf(content), null)
        currentViewController?.presentViewController(
            viewControllerToPresent = activityViewController,
            animated = true,
            completion = null
        )
    }

    actual suspend fun shareFile(path: String) {

    }

    actual fun enableLocationService() {
    }
}