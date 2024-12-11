package mobi.cwiklinski.bloodline.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.close
import mobi.cwiklinski.bloodline.resources.icon_close
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.alertMessage
import mobi.cwiklinski.bloodline.ui.theme.alertTitle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun InformationDialog(
    title: String,
    message: String,
    show: Boolean,
    onClose: () -> Unit
) {
    if (show) {
        BasicAlertDialog(
            onDismissRequest = { },
        ) {
            ConstraintLayout(
                modifier = Modifier.background(
                    AppThemeColors.white,
                    RoundedCornerShape(24.dp)
                ).padding(
                    top = 24.dp,
                    bottom = 48.dp,
                    start = 24.dp,
                    end = 24.dp
                )
            ) {
                val (titleRef, messageRef, imageRef, closeRef) = createRefs()
                Image(
                    painterResource(Res.drawable.icon_close),
                    contentDescription = stringResource(Res.string.close),
                    modifier = Modifier.constrainAs(closeRef) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }.clickable {
                        onClose.invoke()
                    }
                )
                StarsAnimation(
                    modifier = Modifier.constrainAs(imageRef) {
                        top.linkTo(parent.top, 20.dp)
                        centerHorizontallyTo(parent)
                    }
                )
                Text(
                    title,
                    style = alertTitle(),
                    modifier = Modifier.constrainAs(titleRef) {
                        top.linkTo(imageRef.bottom)
                        centerHorizontallyTo(parent)
                    }
                )
                Text(
                    message,
                    style = alertMessage(),
                    modifier = Modifier.constrainAs(messageRef) {
                        top.linkTo(titleRef.bottom)
                        bottom.linkTo(parent.bottom)
                        centerHorizontallyTo(parent)
                    }
                )
            }
        }
    }
}

data class InformationDialogData(val title: String, val message: String)

class InformationDialogState() {

    var data by mutableStateOf<InformationDialogData?>(null)
        private set

    fun clear() {
        data = null
    }

}