package mobi.cwiklinski.bloodline

import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.ui.widget.isHorizontal

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val isDebugBinary = BuildConfig.DEBUG
}

actual fun getPlatform(): Platform = AndroidPlatform()

@Composable
actual fun getOrientation(): Orientation = when (LocalConfiguration.current.orientation) {
    Configuration.ORIENTATION_PORTRAIT -> Orientation.Vertical
    Configuration.ORIENTATION_LANDSCAPE -> Orientation.Horizontal
    else -> Orientation.Vertical
}

@Composable
actual fun getScreenWidth(): Dp = LocalConfiguration.current.screenWidthDp.dp

@Suppress("unused")
@Composable
actual fun getScreenHeight(): Dp = LocalConfiguration.current.screenHeightDp.dp

@Composable
actual fun getDonationGridSize(): GridCells =
    GridCells.Fixed(if (getOrientation().isHorizontal()) 2 else 1)

@Composable
actual fun isTablet(): Boolean {
    val configuration = LocalConfiguration.current
    return if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        configuration.screenWidthDp > 840
    } else {
        configuration.screenWidthDp > 600
    }
}

@Composable
actual fun isMobile() = true

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
actual fun getWindowSizeClass(): WindowSizeClass = calculateWindowSizeClass(LocalActivity.current as Activity)