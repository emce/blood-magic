package mobi.cwiklinski.bloodline

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import java.awt.Toolkit

class DesktopPlatform: Platform {
    override val name: String = "Desktop Java ${System.getProperty("java.version")}"
    override val isDebugBinary = System.getProperty("mobi.cwiklinski.bloodline.debug") == "true" ||
            System.getenv("BLOODMAGIC_JVM_DEBUG") == "true"
}

actual fun getPlatform(): Platform = DesktopPlatform()

@Composable
actual fun getOrientation() = Orientation.Horizontal

@Composable
actual fun getScreenWidth(): Dp = with(LocalDensity.current) {
    Toolkit.getDefaultToolkit().screenSize.width.toDp()
}

@Suppress("unused")
@Composable
actual fun getScreenHeight(): Dp = with(LocalDensity.current) {
    Toolkit.getDefaultToolkit().screenSize.height.toDp()
}

@Composable
actual fun getDonationGridSize(): GridCells = GridCells.Fixed(2)

@Composable
actual fun isTablet() = false

@Composable
actual fun isMobile() = false

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
actual fun getWindowSizeClass(): WindowSizeClass = calculateWindowSizeClass()

@Composable
actual fun StatusBarColors(statusBarColor: Color, navBarColor: Color) {
    SideEffect { }
}

@Composable
actual fun getDeviceOrientation() = DeviceOrientation.LANDSCAPE_LEFT