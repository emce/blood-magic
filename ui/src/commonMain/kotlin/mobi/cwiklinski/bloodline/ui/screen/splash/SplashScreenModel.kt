package mobi.cwiklinski.bloodline.ui.screen.splash

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import mobi.cwiklinski.bloodline.auth.api.AuthenticationService
import mobi.cwiklinski.bloodline.auth.api.AuthenticationState

class SplashScreenModel(
    private val authService: AuthenticationService
) : StateScreenModel<AuthenticationState>(AuthenticationState.Idle) {

    init {
        screenModelScope.launch {
            delay(2000)
            mutableState.value = authService.authenticationState.first()
        }
    }

}