package mobi.cwiklinski.bloodline

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import mobi.cwiklinski.bloodline.ui.widget.isHorizontal
import platform.CoreGraphics.CGRectMake
import platform.UIKit.UIApplication
import platform.UIKit.UIColor
import platform.UIKit.UIDevice
import platform.UIKit.UIDeviceOrientation
import platform.UIKit.UIInterfaceOrientationLandscapeLeft
import platform.UIKit.UIInterfaceOrientationLandscapeRight
import platform.UIKit.UIInterfaceOrientationPortrait
import platform.UIKit.UIInterfaceOrientationPortraitUpsideDown
import platform.UIKit.UINavigationBar
import platform.UIKit.UIScreen
import platform.UIKit.UIUserInterfaceIdiomPad
import platform.UIKit.UIView
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

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
actual fun getWindowSizeClass(): WindowSizeClass = calculateWindowSizeClass()

@OptIn(ExperimentalForeignApi::class)
@Composable
private fun statusBarView() = remember {
    val keyWindow: UIWindow? =
        UIApplication.sharedApplication.windows.firstOrNull { (it as? UIWindow)?.isKeyWindow() == true } as? UIWindow
    val safeAreaInsets = UIApplication.sharedApplication.keyWindow?.safeAreaInsets
    val width = UIScreen.mainScreen.bounds.useContents { this.size.width }
    var topInset = 0.0
    safeAreaInsets?.let {
        topInset = safeAreaInsets.useContents {
            this.top
        }
    }
    val tag = 3848245L
    val statusBarMine = UIView(frame = CGRectMake(.0, .0, width, topInset))
    keyWindow?.viewWithTag(tag) ?: run {
        statusBarMine.tag = tag
        statusBarMine.layer.zPosition = 999999.0
        keyWindow?.addSubview(statusBarMine)
        statusBarMine
    }
}

@Composable
actual fun StatusBarColors(statusBarColor: Color, navBarColor: Color) {
    val statusBar = statusBarView()
    SideEffect {
        statusBar.backgroundColor = statusBarColor.toUIColor()
        UINavigationBar.appearance().backgroundColor = navBarColor.toUIColor()
    }
}

private fun Color.toUIColor(): UIColor = UIColor(
    red = this.red.toDouble(),
    green = this.green.toDouble(),
    blue = this.blue.toDouble(),
    alpha = this.alpha.toDouble(),
)
@Composable
actual fun getDeviceOrientation() = when (UIDevice.currentDevice.orientation) {
    UIDeviceOrientation.UIDeviceOrientationLandscapeLeft -> DeviceOrientation.LANDSCAPE_LEFT
    UIDeviceOrientation.UIDeviceOrientationLandscapeRight -> DeviceOrientation.LANDSCAPE_RIGHT
    UIDeviceOrientation.UIDeviceOrientationPortraitUpsideDown -> DeviceOrientation.PORTRAIT_UPSIDE_DOWN
    else -> DeviceOrientation.PORTRAIT
}