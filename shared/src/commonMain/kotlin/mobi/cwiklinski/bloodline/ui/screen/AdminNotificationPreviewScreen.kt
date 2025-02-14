package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import mobi.cwiklinski.bloodline.data.IgnoredOnParcel
import mobi.cwiklinski.bloodline.data.Parcelize
import mobi.cwiklinski.bloodline.domain.model.Notification
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.donationNewTitle
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.widget.CloseButton
import mobi.cwiklinski.bloodline.ui.widget.MobileTitleBar
import mobi.cwiklinski.bloodline.ui.widget.NotificationView
import org.jetbrains.compose.resources.stringResource

@Parcelize
class AdminNotificationPreviewScreen(private val notification: Notification) : AppScreen() {

    @IgnoredOnParcel
    override val supportDialogs = false

    @Composable
    override fun defaultView() {
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        BottomSheetScaffold(
            sheetGesturesEnabled = false,
            sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            sheetBackgroundColor = AppThemeColors.white,
            topBar = {
                MobileTitleBar(
                    title = stringResource(Res.string.donationNewTitle),
                    actions = {
                        CloseButton {
                            bottomSheetNavigator.hide()
                        }
                    }
                )
            },
            sheetPeekHeight = 100.dp,
            sheetElevation = 16.dp,
            sheetContent = { },
        ) {
            NotificationView(notification)
        }
    }
}