package mobi.cwiklinski.bloodline.ui.util

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberNavigatorScreenModel
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.ScreenTransition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import mobi.cwiklinski.bloodline.Constants
import mobi.cwiklinski.bloodline.common.event.SideEffect
import mobi.cwiklinski.bloodline.common.removeDiacritics
import mobi.cwiklinski.bloodline.domain.model.Center
import mobi.cwiklinski.bloodline.domain.model.Notification
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.monthGenitives
import mobi.cwiklinski.bloodline.storage.api.StorageService
import mobi.cwiklinski.bloodline.ui.PlatformManager
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import org.jetbrains.compose.resources.stringArrayResource
import org.koin.compose.currentKoinScope
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.emptyParametersHolder
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope

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

fun Modifier.clickWithRipple(
    color: Color = AppThemeColors.black70,
    onClick: () -> Unit
): Modifier = composed {
    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = ripple(color = color),
        onClick = onClick
    )
}

fun List<Center>.filter(query: String) = filter {
    it.name.lowercase().removeDiacritics()
        .contains(query.lowercase().removeDiacritics()) or
            it.city.lowercase().removeDiacritics()
                .contains(query.lowercase().removeDiacritics()) or
            it.street.lowercase().removeDiacritics().contains(
                query.lowercase().removeDiacritics()
            )
}

fun Notification.getType() = NavigationType.fromType(this.type)

@Composable
fun LocalDate.toLocalizedString(): String {
    val months = stringArrayResource(Res.array.monthGenitives)
    return "${this.dayOfMonth} ${months[this.monthNumber - 1]} ${this.year}"
}

fun List<Notification>.fillWithRead(readList: List<String>): List<Notification> {
    val filledList = mutableListOf<Notification>()
    this.forEach { notification ->
        filledList.add(notification.copy(
            read = readList.contains(notification.id)
        ))
    }
    return filledList.toList()
}

suspend fun StorageService.getReadList(): List<String> {
    val current = this.getString(Constants.NOTIFICATIONS_READ, "")
    return if (current.isEmpty()) {
        emptyList()
    } else {
        Json.decodeFromString<List<String>>(current)
    }
}

@OptIn(InternalVoyagerApi::class)
fun Navigator.clearStack() {
    items.forEach {
        dispose(it)
    }
}

@Composable
inline fun <reified T : ScreenModel> Navigator.koinNavigatorScreenModel(
    qualifier: Qualifier? = null,
    scope: Scope = currentKoinScope(),
    noinline parameters: ParametersDefinition? = null
): T {
    val currentParameters by rememberUpdatedState(parameters)
    val tag = remember(qualifier, scope) { qualifier?.value }
    return rememberNavigatorScreenModel(tag = tag) {
        scope.get(qualifier) {
            currentParameters?.invoke() ?: emptyParametersHolder()
        }
    }
}

@Composable
fun HandleSideEffect(
    sideEffects: Flow<SideEffect>,
    handler: suspend CoroutineScope.(sideEffect: SideEffect) -> Unit = {}
) {
    LaunchedEffect(Unit) {
        sideEffects.collectLatest {
            handler.invoke(this, it)
        }
    }
}

suspend fun shareText(platformManager: PlatformManager, text: String) =
    platformManager.shareText(content = text)

@ExperimentalVoyagerApi
class SlideTransition : ScreenTransition {

    override fun enter(lastEvent: StackEvent): EnterTransition {
        return slideIn { size ->
            val x = if (lastEvent == StackEvent.Pop) -size.width else size.width
            IntOffset(x = x, y = 0)
        }
    }

    override fun exit(lastEvent: StackEvent): ExitTransition {
        return slideOut { size ->
            val x = if (lastEvent == StackEvent.Pop) size.width else -size.width
            IntOffset(x = x, y = 0)
        }
    }
}

@ExperimentalVoyagerApi
class SlideInVerticallyTransition(val index: Int = 0) : ScreenTransition {

    override fun enter(lastEvent: StackEvent): EnterTransition {
        return if (lastEvent == StackEvent.Pop) {
            fadeIn(initialAlpha = 0.9f)
        } else {
            slideInVertically { it }
        }
    }

    override fun exit(lastEvent: StackEvent): ExitTransition {
        return if (lastEvent == StackEvent.Pop) {
            slideOutVertically { it }
        } else {
            fadeOut(targetAlpha = 0.9f)
        }
    }

    override fun zIndex(lastEvent: StackEvent): Float {
        return if (lastEvent == StackEvent.Pop) (index).toFloat() else (index + 1).toFloat()
    }
}