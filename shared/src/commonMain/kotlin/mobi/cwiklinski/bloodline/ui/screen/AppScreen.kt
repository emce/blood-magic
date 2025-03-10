package mobi.cwiklinski.bloodline.ui.screen

import StackedSnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.launch
import mobi.cwiklinski.bloodline.LocalSnackBar
import mobi.cwiklinski.bloodline.common.event.ScreenRoute
import mobi.cwiklinski.bloodline.common.event.SideEffect
import mobi.cwiklinski.bloodline.common.event.SideEffects
import mobi.cwiklinski.bloodline.common.manager.CallbackManager
import mobi.cwiklinski.bloodline.data.IgnoredOnParcel
import mobi.cwiklinski.bloodline.data.Parcelable
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.close
import mobi.cwiklinski.bloodline.ui.model.AppModel
import mobi.cwiklinski.bloodline.ui.rememberPlatformManager
import mobi.cwiklinski.bloodline.ui.util.HandleSideEffect
import mobi.cwiklinski.bloodline.ui.util.RenderLayout
import mobi.cwiklinski.bloodline.ui.util.koinNavigatorScreenModel
import mobi.cwiklinski.bloodline.ui.util.shareText
import mobi.cwiklinski.bloodline.ui.widget.InformationDialog
import mobi.cwiklinski.bloodline.ui.widget.InformationDialogData
import org.jetbrains.compose.resources.getString
import org.koin.compose.koinInject
import org.koin.core.component.KoinComponent

abstract class AppScreen : Screen, KoinComponent, Parcelable {

    val showInformation = mutableStateOf<InformationDialogData?>(null)

    @IgnoredOnParcel
    protected open val supportDialogs = true

    @Composable
    override fun Content() {
        if (supportDialogs && showInformation.value != null) {
            showInformation.value?.let { (title, message) ->
                InformationDialog(title, message, {
                    showInformation.value = null
                })
            }
        }
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
    inline fun <S, reified T : AppModel<S>> handleSideEffects() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<T>()
        val platformManager = rememberPlatformManager()
        val state = LocalSnackBar.current
        HandleSideEffect(screenModel.sideEffect) {
            when (it) {
                is SideEffects.SnackBar -> {
                    val snackBar = it
                    screenModel.screenModelScope.launch {
                        state.showInfoSnackbar(title = snackBar.text)
                    }
                }
                is SideEffects.ErrorSnackBar -> {
                    val snackBar = it
                    screenModel.screenModelScope.launch {
                        state.showErrorSnackbar(
                            title = snackBar.text,
                            actionTitle = getString(Res.string.close),
                            duration = StackedSnackbarDuration.Indefinite
                        )
                    }
                }
                is SideEffects.InformationDialog -> {
                    showInformation.value = InformationDialogData(it.title, it.message)
                }
                is SideEffects.OpenBrowser -> {
                    platformManager.openBrowser(it.url, it.openSystemBrowser)
                }
                is SideEffects.DeleteAccountEffect -> {
                    navigator.replaceAll(DeleteScreen())
                }
                is SideEffects.ShareText -> {
                    shareText(platformManager, it.text)
                }
                is SideEffects.Redirect -> {
                    when (it.route) {
                        ScreenRoute.UnreadNotification -> {
                            navigator.push(NotificationsScreen())
                        }
                        ScreenRoute.Donations -> {
                            navigator.push(DonationsScreen())
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun sideEffect(effect: SideEffect) = koinInject<CallbackManager>().postSideEffect(effect)

}