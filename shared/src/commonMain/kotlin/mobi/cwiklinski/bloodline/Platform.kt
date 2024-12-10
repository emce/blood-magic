package mobi.cwiklinski.bloodline

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

interface Platform {
    fun isMobile() = !name.startsWith("Java")

    val name: String
}

expect fun getPlatform(): Platform

@Composable
expect fun isTablet(): Boolean

@Composable
expect fun getOrientation(): Orientation

@Composable
expect fun getScreenWidth(): Dp

@Composable
expect fun getScreenHeight(): Dp

@Composable
expect fun getDonationGridSize(): GridCells