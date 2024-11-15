package mobi.cwiklinski.bloodline.screen.splash

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import mobi.cwiklinski.bloodline.auth.api.AuthenticationService
import mobi.cwiklinski.bloodline.auth.api.AuthenticationState

class SplashScreenModel(
    private val authService: AuthenticationService
) : ScreenModel {

    val loggedState = mutableStateOf<AuthenticationState>(AuthenticationState.Idle)

    init {
        screenModelScope.launch {
            delay(2000)
            loggedState.value = authService.authenticationState.first()
        }
    }

}