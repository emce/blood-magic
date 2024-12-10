package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.launch
import mobi.cwiklinski.bloodline.ui.event.HandleSideEffect
import mobi.cwiklinski.bloodline.ui.event.SideEffects
import mobi.cwiklinski.bloodline.ui.model.AppModel
import mobi.cwiklinski.bloodline.ui.util.RenderLayout
import org.koin.core.component.KoinComponent

abstract class AppScreen : Screen, KoinComponent {

    @Composable
    override fun Content() {
        RenderLayout(
            default = {
                defaultView()
            },
            desktop = {
                desktopView()
            },
            tablet = {
                tabletView()
            },
            phonePortrait = {
                portraitPhoneView()
            },
            phoneLandscape = {
                landscapePhoneView()
            }
        )
    }

    @Composable
    abstract fun defaultView()

    @Composable
    open fun portraitPhoneView() = defaultView()

    @Composable
    open fun landscapePhoneView() = tabletView()

    @Composable
    open fun tabletView() = defaultView()

    @Composable
    open fun desktopView() = tabletView()

    @Composable
    inline fun <S, reified T : AppModel<S>> handleSnackBars(state: SnackbarHostState) {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<T>()
        HandleSideEffect(screenModel.sideEffect) {
            if (it is SideEffects.Snackbar) {
                val snackBar = it
                screenModel.screenModelScope.launch {
                    state.showSnackbar(snackBar.text)
                }
            }
        }
    }

}