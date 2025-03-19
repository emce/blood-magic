package mobi.cwiklinski.bloodline.ui.util

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberNavigatorScreenModel
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.datetime.LocalDate
import mobi.cwiklinski.bloodline.Constants
import mobi.cwiklinski.bloodline.common.event.SideEffect
import mobi.cwiklinski.bloodline.common.removeDiacritics
import mobi.cwiklinski.bloodline.common.today
import mobi.cwiklinski.bloodline.domain.model.Center
import mobi.cwiklinski.bloodline.domain.model.Donation
import mobi.cwiklinski.bloodline.domain.model.Notification
import mobi.cwiklinski.bloodline.getPlatform
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.android_link
import mobi.cwiklinski.bloodline.resources.donationsShare
import mobi.cwiklinski.bloodline.resources.ios_link
import mobi.cwiklinski.bloodline.resources.liter
import mobi.cwiklinski.bloodline.resources.milliliter
import mobi.cwiklinski.bloodline.resources.monthGenitives
import mobi.cwiklinski.bloodline.storage.api.StorageService
import mobi.cwiklinski.bloodline.ui.PlatformManager
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.widget.capacity
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.currentKoinScope
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.emptyParametersHolder
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope

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

fun Notification.getType() = NavigationType.fromType(this.type.type)

@Composable
fun LocalDate.toLocalizedString(): String {
    val months = stringArrayResource(Res.array.monthGenitives)
    return "${this.dayOfMonth} ${months[this.monthNumber - 1]} ${this.year}"
}

suspend fun StorageService.getLatestRead(): LocalDate {
    val current = this.getString(Constants.NOTIFICATIONS_LATEST, "2000-01-01")
    return try {
        if (current.isEmpty()) {
            LocalDate(2000, 1, 1)
        } else {
            LocalDate.parse(current)
        }
    } catch (e: IllegalArgumentException) {
        LocalDate(2000, 1, 1)
    }
}

suspend fun StorageService.setLatestRead() {
    this.storeString(Constants.NOTIFICATIONS_LATEST, today().toString())
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

@Composable
fun getShareText(donation: Donation): String {
    val link = stringResource(if (getPlatform().name.startsWith("Android")) Res.string.android_link else Res.string.ios_link)
    val milliliter = stringResource(Res.string.milliliter)
    val liter = stringResource(Res.string.liter)
    return stringResource(Res.string.donationsShare)
        .replace("%s", donation.amount.capacity(milliliter, liter))
        .replace("%p", donation.center.name)
        .replace("%l", link)
}

