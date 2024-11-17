@file:Suppress("unused", "FunctionName")
package mobi.cwiklinski.bloodline

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    return ComposeUIViewController {
        NativeMainContent()
    }
}