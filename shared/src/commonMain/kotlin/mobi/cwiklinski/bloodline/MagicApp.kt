package mobi.cwiklinski.bloodline

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import cafe.adriel.voyager.transitions.ScreenTransition
import coil3.compose.setSingletonImageLoaderFactory
import mobi.cwiklinski.bloodline.auth.api.AuthenticationInitializer
import mobi.cwiklinski.bloodline.ui.screen.SplashScreen
import mobi.cwiklinski.bloodline.ui.theme.AppTheme
import mobi.cwiklinski.bloodline.ui.util.SlideInVerticallyTransition
import mobi.cwiklinski.bloodline.ui.util.SlideTransition
import mobi.cwiklinski.bloodline.ui.widget.getAsyncImageLoader
import org.koin.compose.koinInject

val LocalSnackBar = compositionLocalOf { SnackbarHostState() }

@OptIn(ExperimentalVoyagerApi::class)
@Composable
fun MagicApp() {
    val snackBarHostState = remember { SnackbarHostState() }
    CompositionLocalProvider(
        LocalSnackBar provides snackBarHostState,
    ) {
        setSingletonImageLoaderFactory { context ->
            getAsyncImageLoader(context)
        }
        val authInit = koinInject<AuthenticationInitializer>()
        authInit.run()
        AppTheme {
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
                        defaultTransition = if (isDesktop()) SlideInVerticallyTransition() else SlideTransition(),
                        disposeScreenAfterTransitionEnd = true
                    )
                }
            }
        }
    }
}