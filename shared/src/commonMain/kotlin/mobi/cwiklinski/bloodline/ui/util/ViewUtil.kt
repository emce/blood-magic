package mobi.cwiklinski.bloodline.ui.util

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.common.removeDiacritics
import mobi.cwiklinski.bloodline.domain.model.Center
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors

fun Modifier.topBorder(strokeWidth: Dp, color: Color, cornerRadiusDp: Dp) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }
        val cornerRadiusPx = density.run { cornerRadiusDp.toPx() }

        Modifier.drawBehind {
            val width = size.width
            val height = size.height

            drawLine(
                color = color,
                start = Offset(x = 0f, y = height),
                end = Offset(x = 0f, y = cornerRadiusPx),
                strokeWidth = strokeWidthPx
            )

            drawArc(
                color = color,
                startAngle = 180f,
                sweepAngle = 90f,
                useCenter = false,
                topLeft = Offset.Zero,
                size = Size(cornerRadiusPx * 2, cornerRadiusPx * 2),
                style = Stroke(width = strokeWidthPx)
            )

            drawLine(
                color = color,
                start = Offset(x = cornerRadiusPx, y = 0f),
                end = Offset(x = width - cornerRadiusPx, y = 0f),
                strokeWidth = strokeWidthPx
            )

            drawArc(
                color = color,
                startAngle = 270f,
                sweepAngle = 90f,
                useCenter = false,
                topLeft = Offset(x = width - cornerRadiusPx * 2, y = 0f),
                size = Size(cornerRadiusPx * 2, cornerRadiusPx * 2),
                style = Stroke(width = strokeWidthPx)
            )

            drawLine(
                color = color,
                start = Offset(x = width, y = height),
                end = Offset(x = width, y = cornerRadiusPx),
                strokeWidth = strokeWidthPx
            )
        }
    }
)

fun Modifier.bottomBorder(strokeWidth: Dp, color: Color, cornerRadiusDp: Dp) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }
        val cornerRadiusPx = density.run { cornerRadiusDp.toPx() }

        Modifier.drawBehind {
            val width = size.width
            val height = size.height

            drawLine(
                color = color,
                start = Offset(x = 0f, y = 0f),
                end = Offset(x = 0f, y = height - cornerRadiusPx),
                strokeWidth = strokeWidthPx
            )

            drawArc(
                color = color,
                startAngle = 90f,
                sweepAngle = 90f,
                useCenter = false,
                topLeft = Offset(x = 0f, y = height - cornerRadiusPx * 2),
                size = Size(cornerRadiusPx * 2, cornerRadiusPx * 2),
                style = Stroke(width = strokeWidthPx)
            )

            drawLine(
                color = color,
                start = Offset(x = cornerRadiusPx, y = height),
                end = Offset(x = width - cornerRadiusPx, y = height),
                strokeWidth = strokeWidthPx
            )

            drawArc(
                color = color,
                startAngle = 0f,
                sweepAngle = 90f,
                useCenter = false,
                topLeft = Offset(x = width - cornerRadiusPx * 2, y = height - cornerRadiusPx * 2),
                size = Size(cornerRadiusPx * 2, cornerRadiusPx * 2),
                style = Stroke(width = strokeWidthPx)
            )

            drawLine(
                color = color,
                start = Offset(x = width, y = 0f),
                end = Offset(x = width, y = height - cornerRadiusPx),
                strokeWidth = strokeWidthPx
            )
        }
    }
)

fun Modifier.bottomDivider(strokeWidth: Dp, color: Color, paddingDp: Dp = 0.dp) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }
        val paddingPx = density.run { paddingDp.toPx() }

        Modifier.drawBehind {
            val width = size.width
            val height = size.height

            drawLine(
                color = color,
                start = Offset(x = paddingPx, y = height),
                end = Offset(x = width - paddingPx, y = height),
                strokeWidth = strokeWidthPx
            )
        }
    }
)

fun Modifier.sideBorder(strokeWidth: Dp, color: Color) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawBehind {
            val width = size.width
            val height = size.height

            drawLine(
                color = color,
                start = Offset(x = 0f, y = 0f),
                end = Offset(x = 0f, y = height),
                strokeWidth = strokeWidthPx
            )

            drawLine(
                color = color,
                start = Offset(x = width, y = 0f),
                end = Offset(x = width, y = height),
                strokeWidth = strokeWidthPx
            )
        }
    }
)

internal fun Modifier.coloredShadow(
    color: Color,
    cornerRadiusDp: Dp = 0.dp,
    offsetY: Dp,
    offsetX: Dp,
    blurRadius: Float = 10f
) = then(
    drawBehind {
        drawIntoCanvas { canvas ->
            val cornerRadiusPx = density.run { cornerRadiusDp.toPx() }
            val paint = blurredPaint(color, blurRadius)

            val leftPixel = density.run { offsetX.toPx() } + cornerRadiusPx / 2
            val topPixel = density.run { offsetY.toPx() } + cornerRadiusPx / 2
            val rightPixel = size.width
            val bottomPixel = size.height
            canvas.drawRoundRect(
                left = leftPixel,
                top = topPixel,
                right = rightPixel,
                bottom = bottomPixel,
                cornerRadiusPx,
                cornerRadiusPx,
                paint
            )
        }
    }
)

internal fun Modifier.avatarShadow(
    color: Color = AppThemeColors.white.copy(alpha = 0.6f),
    blurRadius: Float = 20f,
    sizeAdjustment: Float = 0.43f
) = then(
    drawBehind {
        drawIntoCanvas { canvas ->
            val paint = blurredPaint(color, blurRadius)

            val centerX = size.width / 2
            val centerY = size.height / 2
            val radius = size.width.coerceAtLeast(size.height) * sizeAdjustment

            canvas.drawCircle(Offset(centerX, centerY), radius, paint)
        }
    }
)

fun List<Center>.filter(query: String) = filter {
    it.name.lowercase().removeDiacritics()
        .contains(query.lowercase().removeDiacritics()) or
            it.city.lowercase().removeDiacritics()
                .contains(query.lowercase().removeDiacritics()) or
            it.street.lowercase().removeDiacritics().contains(
                query.lowercase().removeDiacritics()
            )
}