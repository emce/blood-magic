package mobi.cwiklinski.bloodline.ui.model

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import mobi.cwiklinski.bloodline.auth.api.AuthenticationService
import mobi.cwiklinski.bloodline.auth.api.AuthenticationState
import mobi.cwiklinski.bloodline.common.launchUI

@OptIn(FlowPreview::class)
class SplashScreenModel(
    private val authService: AuthenticationService
) : AppModel<AuthenticationState>(AuthenticationState.Idle) {

    init {
        bootstrap()
        screenModelScope.launch {
            authService.authenticationState
                .debounce(SPLASH_DELAY)
                .collectLatest {
                    mutableState.value = it
                }
        }
    }

    companion object {
        const val SPLASH_DELAY = 2000L
    }

}