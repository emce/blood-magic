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
import mobi.cwiklinski.bloodline.auth.api.AuthenticationState
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.appName
import mobi.cwiklinski.bloodline.resources.settingsLogoutTitle
import mobi.cwiklinski.bloodline.resources.splash_logo
import mobi.cwiklinski.bloodline.ui.model.LogoutScreenModel
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.hugeTitle
import mobi.cwiklinski.bloodline.ui.widget.FormProgress
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class LogoutScreen : AppScreen() {

    @Composable
    override fun defaultView() = portraitView()

    @Composable
    override fun portraitView() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<LogoutScreenModel>()
        val state by screenModel.state.collectAsStateWithLifecycle()
        if (state == AuthenticationState.NotLogged) {
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
                    stringResource(Res.string.settingsLogoutTitle),
                    style = hugeTitle()
                )
                Spacer(Modifier.height(20.dp))
                FormProgress(filter = ColorFilter.tint(AppThemeColors.red2))
            }
        }
    }

    @Composable
    override fun landscapeView() = portraitView()
}