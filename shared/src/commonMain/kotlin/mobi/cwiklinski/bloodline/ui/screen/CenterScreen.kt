package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import mobi.cwiklinski.bloodline.Constants
import mobi.cwiklinski.bloodline.analytics.api.ContextData
import mobi.cwiklinski.bloodline.analytics.api.Event
import mobi.cwiklinski.bloodline.analytics.api.TrackScreen
import mobi.cwiklinski.bloodline.data.Parcelable
import mobi.cwiklinski.bloodline.data.Parcelize
import mobi.cwiklinski.bloodline.domain.model.Center
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.donationNewCenterLabel
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.widget.CenterView
import mobi.cwiklinski.bloodline.ui.widget.CloseButton
import mobi.cwiklinski.bloodline.ui.widget.MobileTitleBar
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Parcelize
class CenterScreen(val center: Center, private val onSiteClick: ((link: String) -> Unit)? = null) : Screen,
    Parcelable {

    @Preview
    @Composable
    override fun Content() {
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        TrackScreen(
            Event.View(
                Constants.ANALYTICS_SCREEN_CENTER,
                ContextData(Constants.ANALYTICS_SCREEN_CENTER, mutableMapOf(
                    "name" to center.name,
                    "address" to center.getFullAddress()
                ))
            )
        )
        BottomSheetScaffold(
            sheetGesturesEnabled = false,
            sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            sheetBackgroundColor = AppThemeColors.white,
            topBar = {
                MobileTitleBar(
                    title = stringResource(Res.string.donationNewCenterLabel),
                    actions = {
                        CloseButton {
                            bottomSheetNavigator.hide()
                        }
                    }
                )
            },
            sheetPeekHeight = 0.dp,
            sheetContent = { },
        ) {
            CenterView(
                center = center,
                modifier = Modifier.fillMaxWidth(),
                onSiteClick = {
                    onSiteClick?.invoke(center.site)
                }
            )
        }
    }
}