package mobi.cwiklinski.bloodline.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.icerock.moko.parcelize.Parcelize
import io.github.aakira.napier.Napier
import kotlinx.datetime.Clock
import mobi.cwiklinski.bloodline.auth.api.AuthenticationState
import mobi.cwiklinski.bloodline.resources.Res
import mobi.cwiklinski.bloodline.resources.appName
import mobi.cwiklinski.bloodline.resources.splash_logo
import mobi.cwiklinski.bloodline.ui.model.SplashScreenModel
import mobi.cwiklinski.bloodline.ui.theme.AppThemeColors
import mobi.cwiklinski.bloodline.ui.theme.hugeTitle
import mobi.cwiklinski.bloodline.ui.util.koinNavigatorScreenModel
import mobi.cwiklinski.bloodline.ui.widget.FormProgress
import mobi.cwiklinski.bloodline.ui.widget.MobileLayout
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Parcelize
class SplashScreen(override val key: ScreenKey = Clock.System.now().toString()) : AppScreen() {

    @Composable
    override fun Content() {
        super.Content()
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<SplashScreenModel>()
        val state by screenModel.state.collectAsStateWithLifecycle(AuthenticationState.Idle)
        when (state) {
            AuthenticationState.Logged -> {
                Napier.v("Redirecting to SetupScreen")
                screenModel.resetState()
                navigator.replaceAll(SetupScreen())
            }
            AuthenticationState.NotLogged -> {
                Napier.v("Redirecting to Login Screen")
                screenModel.resetState()
                navigator.replaceAll(LoginScreen())
            }
            else -> Unit
        }
        LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
            screenModel.start()
        }
    }

    @Composable
    override fun defaultView() {
        MobileLayout(
            modifier = Modifier.fillMaxSize(),
            desiredContent = { paddingValues ->
                SplashView(paddingValues)
            }
        )
    }

    @Composable
    override fun tabletView() = defaultView()

    @Composable
    fun SplashView(paddingValues: PaddingValues) {
        ConstraintLayout(
            modifier = Modifier.padding(paddingValues).fillMaxSize().background(
                AppThemeColors.startingGradient
            )
        ) {
            val (image, title, progress) = createRefs()
            Image(
                painterResource(Res.drawable.splash_logo),
                stringResource(Res.string.appName),
                modifier = Modifier.constrainAs(image) {
                    centerTo(parent)
                }
            )
            Text(
                stringResource(Res.string.appName),
                style = hugeTitle(),
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(image.bottom, 20.dp)
                    centerHorizontallyTo(parent)
                }
            )
            FormProgress(
                modifier = Modifier.constrainAs(progress) {
                    top.linkTo(title.bottom, 20.dp)
                    centerHorizontallyTo(parent)
                },
                filter = ColorFilter.tint(AppThemeColors.red2)
            )
        }
    }
}
