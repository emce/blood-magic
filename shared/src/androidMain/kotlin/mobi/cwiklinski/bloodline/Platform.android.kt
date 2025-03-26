package mobi.cwiklinski.bloodline

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.DisplayMetrics
import android.view.Display
import android.view.Surface
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
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
    return configuration.smallestScreenWidthDp >= 600
}

@Composable
actual fun isMobile() = true

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
actual fun getWindowSizeClass(): WindowSizeClass = calculateWindowSizeClass(LocalActivity.current as Activity)

@Composable
actual fun StatusBarColors(
    statusBarColor: Color,
    navBarColor: Color
) {
    val activity = LocalActivity.current as ComponentActivity
    SideEffect {
        activity.enableEdgeToEdge(
            SystemBarStyle.light(statusBarColor.toArgb(), statusBarColor.toArgb()),
            SystemBarStyle.light(navBarColor.toArgb(), navBarColor.toArgb())
        )
    }
}

@SuppressLint("ObsoleteSdkInt", "Deprecation")
fun getDisplayCompat(context: Context): Display? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        context.display.let { display ->
            if (display.isValid) display else null
        }
    } else {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
        try {
            windowManager?.defaultDisplay?.also { display ->
                val metrics = DisplayMetrics()
                display.getRealMetrics(metrics)
                if (metrics.widthPixels > 0 && metrics.heightPixels > 0) {
                    return display
                }
            }
            null
        } catch (e: Exception) {
            null
        }
    }
}

@Composable
actual fun getDeviceOrientation(): DeviceOrientation {
    val activity = LocalActivity.current
    return if (activity != null) {
        when (getDisplayCompat(activity)?.rotation) {
            Surface.ROTATION_0 -> DeviceOrientation.PORTRAIT
            Surface.ROTATION_90 -> DeviceOrientation.LANDSCAPE_LEFT
            Surface.ROTATION_180 -> DeviceOrientation.PORTRAIT_UPSIDE_DOWN
            Surface.ROTATION_270 -> DeviceOrientation.LANDSCAPE_RIGHT
            else -> DeviceOrientation.PORTRAIT
        }
    } else {
        DeviceOrientation.PORTRAIT
    }
}