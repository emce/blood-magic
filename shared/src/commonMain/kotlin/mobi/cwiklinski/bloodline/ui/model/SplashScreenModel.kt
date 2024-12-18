package mobi.cwiklinski.bloodline.ui.model

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import mobi.cwiklinski.bloodline.auth.api.AuthenticationService
import mobi.cwiklinski.bloodline.auth.api.AuthenticationState
import mobi.cwiklinski.bloodline.ui.manager.CallbackManager

class SplashScreenModel(
    callbackManager: CallbackManager,
    private val authService: AuthenticationService
) : AppModel<AuthenticationState>(AuthenticationState.Idle, callbackManager) {

    init {
        bootstrap()
    }

    fun start() {
        screenModelScope.launch {
            authService.authenticationState
                .debounce(SPLASH_DELAY)
                .collectLatest {
                    mutableState.value = it
                }
        }
    }

    fun resetState() {
        mutableState.value = AuthenticationState.Idle
    }

    companion object {
        const val SPLASH_DELAY = 2000L
    }

}