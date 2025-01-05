package mobi.cwiklinski.bloodline

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import java.awt.Toolkit

class DesktopPlatform: Platform {
    override val name: String = "Desktop Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = DesktopPlatform()

@Composable
actual fun getOrientation() = Orientation.Horizontal

@Composable
actual fun getScreenWidth(): Dp = with(LocalDensity.current) {
    Toolkit.getDefaultToolkit().screenSize.width.toDp()
}

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