package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.close
import mobi.cwiklinski.bloodline.resources.icon_close
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.util.clickWithRipple
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CloseButton(modifier: Modifier = Modifier, onClose: () -> Unit) {
    Icon(
        painterResource(Res.drawable.icon_close),
        stringResource(Res.string.close),
        tint = AppThemeColors.black,
        modifier = modifier
            .size(72.dp)
            .padding(16.dp)
            .clickWithRipple {
                onClose.invoke()
            }
    )
}