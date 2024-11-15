package mobi.cwiklinski.bloodline.screen.splash

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import mobi.cwiklinski.bloodline.auth.api.AuthenticationState
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.appName
import mobi.cwiklinski.bloodline.resources.splash_logo
import mobi.cwiklinski.bloodline.screen.AppScreen
import mobi.cwiklinski.bloodline.screen.home.HomeScreen
import mobi.cwiklinski.bloodline.screen.login.LoginScreen
import mobi.cwiklinski.bloodline.ui.component.FormProgress
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class SplashScreen : AppScreen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<SplashScreenModel>()
        if (screenModel.loggedState.value == AuthenticationState.Logged) {
            navigator.replace(HomeScreen())
        } else if (screenModel.loggedState.value == AuthenticationState.NotLogged) {
            navigator.replace(LoginScreen())
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
                    stringResource(Res.string.appName),
                    style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
                )
                Spacer(Modifier.height(20.dp))
                FormProgress(filter = ColorFilter.tint(AppThemeColors.red2))
            }
        }
    }
}