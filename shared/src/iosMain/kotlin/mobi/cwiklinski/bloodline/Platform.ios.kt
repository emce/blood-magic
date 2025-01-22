package mobi.cwiklinski.bloodline

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import mobi.cwiklinski.bloodline.ui.widget.isHorizontal
import platform.UIKit.UIApplication
import platform.UIKit.UIDevice
import platform.UIKit.UIInterfaceOrientationLandscapeLeft
import platform.UIKit.UIInterfaceOrientationLandscapeRight
import platform.UIKit.UIInterfaceOrientationPortrait
import platform.UIKit.UIInterfaceOrientationPortraitUpsideDown
import platform.UIKit.UIUserInterfaceIdiomPad
import platform.UIKit.UIWindow
import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val isDebugBinary = kotlin.native.Platform.isDebugBinary
}

actual fun getPlatform(): Platform = IOSPlatform()

@Composable
actual fun getOrientation(): Orientation =
    (UIApplication.sharedApplication.windows.first() as? UIWindow)?.windowScene?.interfaceOrientation?.let {
        when (it) {
            UIInterfaceOrientationLandscapeLeft, UIInterfaceOrientationLandscapeRight -> Orientation.Horizontal
            UIInterfaceOrientationPortrait, UIInterfaceOrientationPortraitUpsideDown -> Orientation.Vertical
            else -> Orientation.Vertical
        }
    } ?: Orientation.Vertical

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun getScreenWidth(): Dp = with(LocalDensity.current) {
    LocalWindowInfo.current.containerSize.width.toDp()
}

@Suppress("unused")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun getScreenHeight(): Dp = with(LocalDensity.current) {
    LocalWindowInfo.current.containerSize.height.toDp()
}

@Composable
actual fun getDonationGridSize(): GridCells =
    GridCells.Fixed(if (getOrientation().isHorizontal()) 2 else 1)

@Composable
actual fun isTablet() = UIDevice.currentDevice.userInterfaceIdiom == UIUserInterfaceIdiomPad

@Composable
actual fun isMobile() = true

