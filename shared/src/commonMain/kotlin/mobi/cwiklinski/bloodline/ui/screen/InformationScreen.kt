package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import kotlinx.datetime.Clock
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.close
import mobi.cwiklinski.bloodline.resources.icon_close
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.alertMessage
import mobi.cwiklinski.bloodline.ui.theme.alertTitle
import mobi.cwiklinski.bloodline.ui.widget.ConfirmAnimation
import mobi.cwiklinski.bloodline.ui.widget.StarsAnimation
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class InformationScreen(
    override val key: ScreenKey = Clock.System.now().toEpochMilliseconds().toString(),
    private val title: String,
    private val message: String
) : Screen {

    @Composable
    override fun Content() {
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    AppThemeColors.white,
                    RoundedCornerShape(24.dp)
                ).padding(
                    top = 24.dp,
                    bottom = 48.dp,
                    start = 24.dp,
                    end = 24.dp
                )
        ) {
            val (titleRef, messageRef, confirmRef, starsRef, closeRef) = createRefs()
            Image(
                painterResource(Res.drawable.icon_close),
                contentDescription = stringResource(Res.string.close),
                modifier = Modifier.constrainAs(closeRef) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }.clickable {
                    bottomSheetNavigator.hide()
                }
            )
            StarsAnimation(
                modifier = Modifier.constrainAs(starsRef) {
                    top.linkTo(parent.top, 20.dp)
                    centerHorizontallyTo(parent)
                }
            )
            ConfirmAnimation(
                modifier = Modifier.constrainAs(confirmRef) {
                    bottom.linkTo(starsRef.bottom, (-40).dp)
                    centerHorizontallyTo(parent)
                }
            )
            Text(
                title,
                style = alertTitle(),
                modifier = Modifier.constrainAs(titleRef) {
                    top.linkTo(starsRef.bottom, 60.dp)
                    centerHorizontallyTo(parent)
                }
            )
            Text(
                message,
                style = alertMessage(),
                modifier = Modifier.constrainAs(messageRef) {
                    top.linkTo(titleRef.bottom, 20.dp)
                    bottom.linkTo(parent.bottom, 20.dp)
                    centerHorizontallyTo(parent)
                }
            )
        }
    }
}