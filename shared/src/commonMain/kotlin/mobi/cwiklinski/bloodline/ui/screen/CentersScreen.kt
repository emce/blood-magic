package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import mobi.cwiklinski.bloodline.domain.model.Center
import mobi.cwiklinski.bloodline.getDonationGridSize
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.centersTitle
import mobi.cwiklinski.bloodline.resources.homeTitle
import mobi.cwiklinski.bloodline.resources.home_stars
import mobi.cwiklinski.bloodline.resources.icon_poland
import mobi.cwiklinski.bloodline.resources.loading
import mobi.cwiklinski.bloodline.ui.model.CenterScreenModel
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.itemSubTitle
import mobi.cwiklinski.bloodline.ui.theme.toolbarTitle
import mobi.cwiklinski.bloodline.ui.widget.CenterItemView
import mobi.cwiklinski.bloodline.ui.widget.CenterSelectItem
import mobi.cwiklinski.bloodline.ui.widget.CenterView
import mobi.cwiklinski.bloodline.ui.widget.FormProgress
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class CentersScreen : AppScreen() {

    @Composable
    override fun verticalView() {
        val navigator = LocalNavigator.currentOrThrow
        val bottomNavigator = LocalBottomSheetNavigator.current
        val screenModel = navigator.koinNavigatorScreenModel<CenterScreenModel>()
        val centers by screenModel.centers.collectAsStateWithLifecycle(emptyList())
        Box(
            modifier = Modifier
                .background(AppThemeColors.homeGradient)
                .padding(top = 20.dp)
        ) {
            VerticalScaffold(
                backgroundColor = Color.Transparent,
                topBar = {
                    Box(
                        modifier = Modifier.height(100.dp)
                            .fillMaxWidth()
                            .background(Color.Transparent)
                    ) {
                        Image(
                            painterResource(Res.drawable.home_stars),
                            stringResource(Res.string.homeTitle),
                            modifier = Modifier.padding(20.dp).align(Alignment.CenterEnd)
                        )
                        Text(
                            stringResource(Res.string.centersTitle),
                            style = toolbarTitle(),
                            modifier = Modifier.align(Alignment.CenterStart).padding(20.dp)
                        )
                    }
                }
            ) { paddingValue ->
                if (centers.isNotEmpty()) {
                    LazyVerticalGrid(
                        modifier = Modifier.fillMaxWidth()
                            .padding(paddingValue)
                            .background(
                                SolidColor(AppThemeColors.background),
                                RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                            ),
                        columns = getDonationGridSize(),
                        verticalArrangement = Arrangement.Top
                    ) {
                        itemsIndexed(centers) { index, center ->
                            if (index > 0) {
                                centers.getOrNull(index - 1)?.let { previous ->
                                    if (center.voivodeship != previous.voivodeship) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(AppThemeColors.grey1)
                                                .padding(10.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Start
                                        ) {
                                            Image(
                                                painterResource(Res.drawable.icon_poland),
                                                contentDescription = center.voivodeship,
                                                modifier = Modifier.size(16.dp),
                                                colorFilter = ColorFilter.tint(AppThemeColors.black70)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                center.voivodeship.toUpperCase(Locale.current),
                                                style = itemSubTitle()
                                            )
                                        }
                                    }
                                }
                                CenterItemView(center, modifier = Modifier
                                    .fillMaxWidth().clickable {
                                    bottomNavigator.show(CenterScreen(center))
                                })
                            }
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize()
                            .padding(paddingValue)
                            .background(
                                SolidColor(AppThemeColors.background),
                                RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                            ),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        FormProgress()
                        Spacer(Modifier.height(10.dp))
                        Text(stringResource(Res.string.loading))
                    }
                }
            }
        }

    }

    @Composable
    override fun horizontalView() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<CenterScreenModel>()
        val centers by screenModel.centers.collectAsStateWithLifecycle(emptyList())
        var selectedCenter by remember { mutableStateOf(centers.firstOrNull() ?: Center()) }
        HorizontalScaffold(
            modifier = Modifier.padding(0.dp),
            title = selectedCenter.name,
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                val (menu, content) = createRefs()
                LazyColumn(
                    modifier = Modifier
                        .constrainAs(menu) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start, 10.dp)
                            bottom.linkTo(parent.bottom)
                        }
                        .fillMaxWidth(.3f)
                ) {
                    itemsIndexed(centers) { index, center ->
                        CenterSelectItem(
                            modifier = Modifier.clickable {
                                selectedCenter = center
                            },
                            center = center,
                            previous = if (index > 0) centers[index - 1] else null)
                    }
                }
                Box(
                    modifier = Modifier
                        .background(AppThemeColors.background)
                        .constrainAs(content) {
                            top.linkTo(parent.top)
                            start.linkTo(menu.end)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end)
                            height = Dimension.fillToConstraints
                        }
                        .fillMaxWidth(.7f)) {
                    CenterView(
                        center = selectedCenter
                    )
                }
            }
        }
    }
}