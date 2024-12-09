package mobi.cwiklinski.bloodline.ui.model

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import mobi.cwiklinski.bloodline.auth.api.AuthenticationService
import mobi.cwiklinski.bloodline.auth.api.AuthenticationState
import mobi.cwiklinski.bloodline.storage.api.StorageService
import kotlin.time.Duration.Companion.seconds

class LogoutScreenModel(
    private val authService: AuthenticationService,
    private val storageService: StorageService
) : AppModel<AuthenticationState>(AuthenticationState.Idle) {

    init {
        bootstrap()
        logout()
        screenModelScope.launch {
            authService.authenticationState
                .collectLatest {
                    mutableState.value = it
                }
        }
    }

    private fun logout() {
        screenModelScope.launch {
            delay(1.seconds.inWholeMilliseconds)
            authService.logOut().collectLatest {
                if (it) {
                    storageService.clearAll()
                }
            }
        }
    }

    fun resetState() {
        mutableState.value = AuthenticationState.Idle
    }

}