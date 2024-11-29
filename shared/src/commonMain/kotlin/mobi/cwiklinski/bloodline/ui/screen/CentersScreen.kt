package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import mobi.cwiklinski.bloodline.domain.model.Center
import mobi.cwiklinski.bloodline.getOrientation
import mobi.cwiklinski.bloodline.getScreenWidth
import mobi.cwiklinski.bloodline.ui.model.CenterScreenModel
import mobi.cwiklinski.bloodline.ui.widget.isHorizontal
import org.jetbrains.compose.ui.tooling.preview.Preview

class CentersScreen() : AppScreen() {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val itemWidth = if (getOrientation().isHorizontal()) getScreenWidth() / 2 else getScreenWidth()
        val screenModel = navigator.koinNavigatorScreenModel<CenterScreenModel>()
        val centerList by screenModel.centers.collectAsStateWithLifecycle(emptyList())
        CentersView(screenModel, centerList)
    }

    @Preview
    @Composable
    fun CentersView(screenModel: CenterScreenModel, centers: List<Center>) {

    }

}