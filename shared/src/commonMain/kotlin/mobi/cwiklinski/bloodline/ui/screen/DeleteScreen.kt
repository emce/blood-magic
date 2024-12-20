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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.appName
import mobi.cwiklinski.bloodline.resources.profileDeleteDonationsMessage
import mobi.cwiklinski.bloodline.resources.profileDeleteProfileMessage
import mobi.cwiklinski.bloodline.resources.settingsLogoutTitle
import mobi.cwiklinski.bloodline.resources.splash_logo
import mobi.cwiklinski.bloodline.ui.model.ExitScreenModel
import mobi.cwiklinski.bloodline.ui.model.ExitState
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.hugeTitle
import mobi.cwiklinski.bloodline.ui.widget.FormProgress
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class DeleteScreen : AppScreen() {

    override val supportDialogs = false

    @Composable
    override fun defaultView() = portraitPhoneView()

    @Composable
    override fun portraitPhoneView() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<ExitScreenModel>()
        val state by screenModel.state.collectAsStateWithLifecycle()
        when (state) {
            ExitState.Deleted -> {
                navigator.replaceAll(LoginScreen())
            }
            ExitState.Error -> {

                navigator.pop()
            }
            else -> {}
        }
        if (state == ExitState.LoggedOut) {
            navigator.replaceAll(LoginScreen())
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
                    stringResource(Res.string.appName)
                )
                Spacer(Modifier.height(20.dp))
                Text(
                    stringResource(
                        when (state) {
                            ExitState.DonationsDeleted -> Res.string.profileDeleteDonationsMessage
                            ExitState.ProfileDeleted -> Res.string.profileDeleteProfileMessage
                            else -> Res.string.settingsLogoutTitle
                        }
                    ),
                    style = hugeTitle()
                )
                Spacer(Modifier.height(20.dp))
                FormProgress(filter = ColorFilter.tint(AppThemeColors.red2))
            }
        }
    }

    @Composable
    override fun tabletView() = portraitPhoneView()
}