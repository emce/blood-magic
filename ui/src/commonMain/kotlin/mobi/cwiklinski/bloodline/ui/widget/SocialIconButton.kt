package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun SocialIconButton(
    icon: DrawableResource,
    iconDescription: String = "icon",
    onClicked: () -> Unit,
    enabled: Boolean = true,
) {
    Button(
        modifier = Modifier
            .size(80.dp)
            .background(color = AppThemeColors.white, shape = RoundedCornerShape(size = 14.dp))
            .border(
                width = 2.dp,
                color = AppThemeColors.grey2,
                shape = RoundedCornerShape(size = 14.dp)
            ),
        onClick = onClicked,
        enabled = enabled,
        shape = RoundedCornerShape(size = 14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AppThemeColors.white,
            contentColor = AppThemeColors.white,
            disabledContainerColor = AppThemeColors.grey1,
            disabledContentColor = AppThemeColors.grey1
        )
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = iconDescription,
            modifier = Modifier.size(42.dp)
        )
    }
}