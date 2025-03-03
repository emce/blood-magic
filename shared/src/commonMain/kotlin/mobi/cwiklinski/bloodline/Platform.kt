package mobi.cwiklinski.bloodline

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

interface Platform {

    val name: String

    val isDebugBinary: Boolean

}

expect fun getPlatform(): Platform

@Composable
expect fun isTablet(): Boolean

@Composable
expect fun isMobile(): Boolean

@Suppress("unused")
@Composable
fun isDesktop() = !isTablet() && !isMobile()

@Composable
expect fun getOrientation(): Orientation

@Composable
expect fun getScreenWidth(): Dp

@Composable
expect fun getScreenHeight(): Dp

@Composable
expect fun getDonationGridSize(): GridCells

@Composable
expect fun getWindowSizeClass(): WindowSizeClass