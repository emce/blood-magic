package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import mobi.cwiklinski.bloodline.data.Parcelable
import mobi.cwiklinski.bloodline.data.Parcelize
import mobi.cwiklinski.bloodline.domain.model.Center
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.contentTitle
import mobi.cwiklinski.bloodline.ui.widget.CenterView
import mobi.cwiklinski.bloodline.ui.widget.CloseButton
import org.jetbrains.compose.ui.tooling.preview.Preview

@Parcelize
class CenterScreen(val center: Center, private val onSiteClick: ((link: String) -> Unit)? = null) : Screen,
    Parcelable {

    @Preview
    @Composable
    override fun Content() {
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        BottomSheetScaffold(
            sheetGesturesEnabled = false,
            sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            sheetBackgroundColor = AppThemeColors.white,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            center.name,
                            style = contentTitle(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    actions = {
                        CloseButton {
                            bottomSheetNavigator.hide()
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = AppThemeColors.modalHeader,
                        titleContentColor = AppThemeColors.white,
                        actionIconContentColor = AppThemeColors.white,
                    )
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