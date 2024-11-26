package mobi.cwiklinski.bloodline.ui.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.toArgb
import org.jetbrains.skia.FilterBlurMode
import org.jetbrains.skia.MaskFilter

internal actual fun blurredPaint(color: Color, blurRadius: Float): Paint {
    return Paint().also { paint ->
        paint.asFrameworkPaint().also { nativePaint ->
            nativePaint.isAntiAlias = true
            nativePaint.isDither = true
            nativePaint.color = color.toArgb()
            nativePaint.maskFilter = MaskFilter.makeBlur(FilterBlurMode.NORMAL, blurRadius)
        }
    }
}