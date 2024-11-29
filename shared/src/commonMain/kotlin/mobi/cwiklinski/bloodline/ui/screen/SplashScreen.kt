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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.aakira.napier.Napier
import mobi.cwiklinski.bloodline.auth.api.AuthenticationState
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.appName
import mobi.cwiklinski.bloodline.resources.splash_logo
import mobi.cwiklinski.bloodline.ui.model.SplashScreenModel
import mobi.cwiklinski.bloodline.ui.widget.FormProgress
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.hugeTitle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

class SplashScreen : AppScreen() {

    @Composable
    override fun Content() {
        Napier.d("Splash Screen started")
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<SplashScreenModel>()
        when (screenModel.state.collectAsStateWithLifecycle().value) {
            AuthenticationState.Logged -> {
                Napier.v("Redirecting to SetupScreen")
                navigator.replaceAll(SetupScreen())
            }
            AuthenticationState.NotLogged -> {
                Napier.v("Redirecting to LoginScreen")
                navigator.replaceAll(LoginScreen())
            }
            else -> Unit
        }
        MainWindow()
    }

    @Preview
    @Composable
    fun MainWindow() {
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
                    style = hugeTitle()
                )
                Spacer(Modifier.height(20.dp))
                FormProgress(filter = ColorFilter.tint(AppThemeColors.red2))
            }
        }
    }
}
