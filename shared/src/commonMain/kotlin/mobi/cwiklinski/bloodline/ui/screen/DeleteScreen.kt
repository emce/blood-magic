package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.lifecycle.LifecycleEffectOnce
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mobi.cwiklinski.bloodline.Constants
import mobi.cwiklinski.bloodline.analytics.api.TrackAction
import mobi.cwiklinski.bloodline.analytics.api.TrackScreen
import mobi.cwiklinski.bloodline.data.IgnoredOnParcel
import mobi.cwiklinski.bloodline.data.Parcelize
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.profileDeleteDonationsMessage
import mobi.cwiklinski.bloodline.resources.profileDeleteProfileMessage
import mobi.cwiklinski.bloodline.resources.profileDeleteTitle
import mobi.cwiklinski.bloodline.resources.splash_logo
import mobi.cwiklinski.bloodline.ui.model.ExitScreenModel
import mobi.cwiklinski.bloodline.ui.model.ExitState
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.hugeTitle
import mobi.cwiklinski.bloodline.ui.util.koinNavigatorScreenModel
import mobi.cwiklinski.bloodline.ui.widget.FormProgress
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Parcelize
class DeleteScreen : AppScreen() {

    @IgnoredOnParcel
    override val supportDialogs = false

    @OptIn(ExperimentalVoyagerApi::class)
    @Composable
    override fun defaultView() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<ExitScreenModel>()
        val state by screenModel.state.collectAsStateWithLifecycle(ExitState.Idle)
        TrackScreen(Constants.ANALYTICS_SCREEN_DELETE)
        when (state) {
            ExitState.Deleted -> {
                TrackAction(Constants.ANALYTICS_ACTION_ACCOUNT_DELETED)
                navigator.replaceAll(LoginScreen())
            }
            ExitState.Error -> {
                navigator.pop()
            }
            else -> {}
        }
        LifecycleEffectOnce {
            screenModel.screenModelScope.launch {
                delay(500)
                screenModel.delete()
            }
        }
        Scaffold {
            Column(
                Modifier.fillMaxSize().background(
                    AppThemeColors.startingGradient
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painterResource(Res.drawable.splash_logo),
                    stringResource(Res.string.profileDeleteTitle)
                )
                Spacer(Modifier.height(20.dp))
                Text(
                    stringResource(
                        when (state) {
                            ExitState.DonationsDeleted -> Res.string.profileDeleteDonationsMessage
                            ExitState.ProfileDeleted -> Res.string.profileDeleteProfileMessage
                            else -> Res.string.profileDeleteTitle
                        }
                    ),
                    style = hugeTitle()
                )
                Spacer(Modifier.height(20.dp))
                FormProgress()
            }
        }
    }
}