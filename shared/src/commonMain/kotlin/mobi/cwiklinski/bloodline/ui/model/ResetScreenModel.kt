package mobi.cwiklinski.bloodline.ui.model

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import mobi.cwiklinski.bloodline.auth.api.AuthResult
import mobi.cwiklinski.bloodline.auth.api.AuthenticationService
import mobi.cwiklinski.bloodline.common.isValidEmail
import kotlin.time.Duration.Companion.seconds

class ResetScreenModel(
    private val authService: AuthenticationService
) : AppModel<ResetState>(ResetState.Idle) {

    init {
        bootstrap()
    }

    fun onPasswordReset(email: String) {
        resetState()
        mutableState.value = ResetState.Sending
        if (email.isValidEmail()) {
            screenModelScope.launch {
                authService.resetPassword(email)
                    .debounce(2.seconds.inWholeMilliseconds)
                    .collectLatest {
                        when (it) {
                            is AuthResult.Dismissed -> mutableState.value =
                                ResetState.Idle
                            is AuthResult.Failure -> mutableState.value =
                                ResetState.Error(listOf(ResetError.ERROR))
                            is AuthResult.Success -> mutableState.value =
                                ResetState.Sent
                        }
                    }
            }
        } else {
            mutableState.value = ResetState.Error(listOf(ResetError.EMAIL_ERROR))
        }
    }

    fun resetState() {
        mutableState.value = ResetState.Idle
    }

}

sealed class ResetState {

    data object Idle : ResetState()

    data object Sending : ResetState()

    data object Sent : ResetState()

    data class Error(val errors: List<ResetError>) : ResetState()

}

enum class ResetError {
    EMAIL_ERROR,
    ERROR
}