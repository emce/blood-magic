package mobi.cwiklinski.bloodline.ui.model

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import mobi.cwiklinski.bloodline.auth.api.AuthenticationService
import mobi.cwiklinski.bloodline.auth.api.AuthenticationState
import mobi.cwiklinski.bloodline.common.launchUI

class SplashScreenModel(
    private val authService: AuthenticationService
) : AppModel<AuthenticationState>(AuthenticationState.Idle) {

    init {
        bootstrap()
        screenModelScope.launch {
            delay(SPLASH_DELAY)
            mutableState.value = authService.authenticationState.first()
        }
    }

    companion object {
        const val SPLASH_DELAY = 2000L
    }

}