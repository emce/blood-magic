package mobi.cwiklinski.bloodline.ui.model

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import mobi.cwiklinski.bloodline.auth.api.AuthenticationService
import mobi.cwiklinski.bloodline.auth.api.AuthenticationState
import mobi.cwiklinski.bloodline.storage.api.StorageService
import mobi.cwiklinski.bloodline.common.manager.CallbackManager

class SplashScreenModel(
    callbackManager: CallbackManager,
    private val authService: AuthenticationService,
    private val storageService: StorageService,
) : AppModel<AuthenticationState>(AuthenticationState.Idle, callbackManager) {

    init {
        bootstrap()
    }

    fun start() {
        screenModelScope.launch {
            authService.authenticationState
                .debounce(SPLASH_DELAY)
                .collectLatest {
                    if (storageService.getProfile() != null) {
                        mutableState.value = it
                    } else {
                        mutableState.value = AuthenticationState.NotLogged
                    }
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