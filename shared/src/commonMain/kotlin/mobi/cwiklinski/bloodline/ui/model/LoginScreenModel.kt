package mobi.cwiklinski.bloodline.ui.model

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import mobi.cwiklinski.bloodline.Constants
import mobi.cwiklinski.bloodline.auth.api.AuthError
import mobi.cwiklinski.bloodline.auth.api.AuthResult
import mobi.cwiklinski.bloodline.auth.api.AuthenticationService
import mobi.cwiklinski.bloodline.common.isValidEmail
import mobi.cwiklinski.bloodline.storage.api.StorageService
import mobi.cwiklinski.bloodline.ui.manager.CallbackManager

class LoginScreenModel(
    callbackManager: CallbackManager,
    private val authService: AuthenticationService,
    private val storageService: StorageService
) : AppModel<LoginState>(LoginState.Idle, callbackManager) {

    init {
        bootstrap()
    }

    fun onLoginSubmit(email: String, password: String) {
        if (email.isValidEmail()) {
            if (password.length > 3) {
                mutableState.value = LoginState.LoggingIn
                screenModelScope.launch {
                    authService.loginWithEmailAndPassword(email, password)
                        .collectLatest {
                            when (it) {
                                is AuthResult.Dismissed -> mutableState.value = LoginState.Idle
                                is AuthResult.Failure -> {
                                    val errors = mutableListOf<LoginError>()
                                    when (it.error) {
                                        AuthError.INCORRECT_EMAIL,
                                        AuthError.EMAIL_ALREADY_IN_USE -> {
                                            errors.add(LoginError.EMAIL_ERROR)
                                        }

                                        AuthError.INCORRECT_PASSWORD -> {
                                            errors.add(LoginError.PASSWORD_ERROR)
                                        }

                                        else -> {
                                            errors.add(LoginError.LOGIN_ERROR)
                                        }
                                    }
                                    mutableState.value =
                                        LoginState.Error(errors)
                                }

                                is AuthResult.Success -> {
                                    storageService.storeString(Constants.EMAIL_KEY, email)
                                    mutableState.value = LoginState.LoggedIn
                                }
                            }
                        }
                }
            } else {
                mutableState.value = LoginState.Error(listOf(LoginError.PASSWORD_ERROR))
            }
        } else {
            mutableState.value = LoginState.Error(listOf(LoginError.EMAIL_ERROR))
        }
    }

    fun resetState() {
        mutableState.value = LoginState.Idle
    }

}

sealed class LoginState {

    data object Idle : LoginState()

    data object LoggedIn : LoginState()

    data object LoggingIn : LoginState()

    data class Error(val errors: List<LoginError>) : LoginState()
}

enum class LoginError {
    EMAIL_ERROR,
    PASSWORD_ERROR,
    LOGIN_ERROR,
    PROFILE_ERROR
}