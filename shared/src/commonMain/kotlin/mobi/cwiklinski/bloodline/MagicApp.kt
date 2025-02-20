package mobi.cwiklinski.bloodline

import StackedSnackbarAnimation
import StackedSnakbarHostState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import cafe.adriel.voyager.transitions.ScreenTransition
import coil3.compose.setSingletonImageLoaderFactory
import kotlinx.coroutines.delay
import mobi.cwiklinski.bloodline.auth.api.AuthenticationInitializer
import mobi.cwiklinski.bloodline.common.Job
import mobi.cwiklinski.bloodline.common.manager.AppManager
import mobi.cwiklinski.bloodline.ui.screen.SplashScreen
import mobi.cwiklinski.bloodline.ui.theme.AppTheme
import mobi.cwiklinski.bloodline.ui.util.FadeTransition
import mobi.cwiklinski.bloodline.ui.widget.getAsyncImageLoader
import org.koin.compose.KoinContext
import org.koin.compose.koinInject
import rememberStackedSnackbarHostState


val LocalSnackBar = compositionLocalOf<StackedSnakbarHostState> { error("No SnackbarState provided") }

@OptIn(ExperimentalVoyagerApi::class)
@Composable
fun MagicApp() {
    val snackBarHostState = rememberStackedSnackbarHostState(
        maxStack = 3,
        animation = StackedSnackbarAnimation.Slide
    )
    CompositionLocalProvider(
        LocalSnackBar provides snackBarHostState
    ) {
        setSingletonImageLoaderFactory { context ->
            getAsyncImageLoader(context)
        }
        AppTheme {
            KoinContext {
                val authInit = koinInject<AuthenticationInitializer>()
                authInit.run()
                LaunchedEffect(true) {
                    delay(3000)
                    AppManager.onApplicationStart()
                    Job.runNotificationCheck()
                    Job.runPotentialDonationCheck()
                }
                BottomSheetNavigator(
                    modifier = Modifier.animateContentSize(),
                    sheetShape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                    skipHalfExpanded = true
                ) {
                    Navigator(
                        screen = SplashScreen(),
                        onBackPressed = {
                            false
                        }
                    ) { navigator ->
                        ScreenTransition(
                            navigator = navigator,
                            defaultTransition = FadeTransition(),
                            disposeScreenAfterTransitionEnd = true
                        )
                    }
                }
            }
        }
    }
}