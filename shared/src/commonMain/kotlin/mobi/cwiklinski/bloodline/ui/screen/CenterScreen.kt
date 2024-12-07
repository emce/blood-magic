package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import mobi.cwiklinski.bloodline.domain.model.Center
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.close
import mobi.cwiklinski.bloodline.resources.icon_close
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors.textButtonColors
import mobi.cwiklinski.bloodline.ui.theme.contentTitle
import mobi.cwiklinski.bloodline.ui.widget.CenterView
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class CenterScreen(val center: Center, private val onSiteClick: ((link: String) -> Unit)? = null) : Screen {

    @Composable
    override fun Content() {
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        BottomSheetScaffold(
            modifier = Modifier
                .padding(top = 10.dp),
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
                    modifier = Modifier,
                    actions = {
                        Button(
                            onClick = {
                                bottomSheetNavigator.hide()
                            },
                            colors = textButtonColors()
                        ) {
                            Image(
                                painterResource(Res.drawable.icon_close),
                                stringResource(Res.string.close)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = AppThemeColors.white,
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