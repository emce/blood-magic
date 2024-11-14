@file:Suppress("unused", "FunctionName")
package mobi.cwiklinski.bloodline

import androidx.compose.ui.window.ComposeUIViewController
import org.michaelbel.movies.IosMainContent
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    return ComposeUIViewController {
        NativeMainContent()
    }
}