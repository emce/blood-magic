package mobi.cwiklinski.bloodline.ui.model

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import mobi.cwiklinski.bloodline.Constants
import mobi.cwiklinski.bloodline.auth.api.AuthError
import mobi.cwiklinski.bloodline.auth.api.AuthResult
import mobi.cwiklinski.bloodline.auth.api.AuthenticationService
import mobi.cwiklinski.bloodline.common.isValidEmail
import mobi.cwiklinski.bloodline.data.api.ProfileService
import mobi.cwiklinski.bloodline.domain.Sex
import mobi.cwiklinski.bloodline.storage.api.StorageService
import mobi.cwiklinski.bloodline.common.manager.CallbackManager
import mobi.cwiklinski.bloodline.ui.util.Avatar

class RegisterScreenModel(
    callbackManager: CallbackManager,
    private val authService: AuthenticationService,
    private val profileService: ProfileService,
    private val storageService: StorageService
) : AppModel<RegisterState>(RegisterState.Idle, callbackManager) {

    init {
        bootstrap()
    }

    fun onRegisterSubmit(email: String, password: String, repeat: String) {
        clearState()
        mutableState.value = RegisterState.Registering
        if (!email.isValidEmail()) {
            mutableState.value = RegisterState.Error(listOf(RegisterError.EMAIL_ERROR))
        } else {
            if (password.isNotEmpty() && password.length > 5) {
                if (repeat.isNotEmpty() && repeat.length > 5) {
                    if (repeat == password) {
                        screenModelScope.launch {
                            authService.registerWithEmailAndPassWord(email, password)
                                .collectLatest {
                                    when (it) {
                                        is AuthResult.Dismissed -> mutableState.value =
                                            RegisterState.Idle
                                        is AuthResult.Failure -> mutableState.value =
                                            RegisterState.Error(listOf(RegisterError.REGISTER_ERROR))
                                        is AuthResult.Success -> {
                                            profileService.updateProfileData(
                                                name = email.split("@").first(),
                                                email = email,
                                                avatar = Avatar.WIZARD.name,
                                                sex = Sex.MALE,
                                                notification = false,
                                                starting = 0,
                                                centerId = ""
                                            )
                                            storageService.storeString(Constants.EMAIL_KEY, email)
                                            mutableState.value = RegisterState.Registered
                                        }
                                    }
                                }
                        }
                    } else {
                        mutableState.value = RegisterState.Error(listOf(RegisterError.REPEAT_ERROR))
                    }
                } else {
                    mutableState.value = RegisterState.Error(listOf(RegisterError.REPEAT_ERROR))
                }
            } else {
                mutableState.value = RegisterState.Error(listOf(RegisterError.PASSWORD_ERROR))
            }
        }
    }

    fun registerWithGoogle() {
        mutableState.value = RegisterState.Registering
        screenModelScope.launch {
            authService.loginWithGoogle().collectLatest {
                when (it) {
                    is AuthResult.Dismissed -> mutableState.value = RegisterState.Idle
                    is AuthResult.Failure -> {
                        val errors = mutableListOf<RegisterError>()
                        when (it.error) {
                            AuthError.NOT_IMPLEMENTED -> {
                                errors.add(RegisterError.NOT_IMPLEMENTED)
                            }
                            else -> {
                                errors.add(RegisterError.REGISTER_ERROR)
                            }
                        }
                        mutableState.value =
                            RegisterState.Error(errors)
                    }
                    is AuthResult.Success -> mutableState.value = RegisterState.Registered
                }
            }
        }
    }

    fun registerWithFacebook() {
        mutableState.value = RegisterState.Registering
        screenModelScope.launch {
            authService.loginWithFacebook().collectLatest {
                when (it) {
                    is AuthResult.Dismissed -> mutableState.value = RegisterState.Idle
                    is AuthResult.Failure -> {
                        val errors = mutableListOf<RegisterError>()
                        when (it.error) {
                            AuthError.NOT_IMPLEMENTED -> {
                                errors.add(RegisterError.NOT_IMPLEMENTED)
                            }
                            else -> {
                                errors.add(RegisterError.REGISTER_ERROR)
                            }
                        }
                        mutableState.value =
                            RegisterState.Error(errors)
                    }
                    is AuthResult.Success -> mutableState.value = RegisterState.Registered
                }
            }
        }
    }

    fun registerWithApple() {
        mutableState.value = RegisterState.Registering
        screenModelScope.launch {
            authService.loginWithApple().collectLatest {
                when (it) {
                    is AuthResult.Dismissed -> mutableState.value = RegisterState.Idle
                    is AuthResult.Failure -> {
                        val errors = mutableListOf<RegisterError>()
                        when (it.error) {
                            AuthError.NOT_IMPLEMENTED -> {
                                errors.add(RegisterError.NOT_IMPLEMENTED)
                            }
                            else -> {
                                errors.add(RegisterError.REGISTER_ERROR)
                            }
                        }
                        mutableState.value =
                            RegisterState.Error(errors)
                    }
                    is AuthResult.Success -> mutableState.value = RegisterState.Registered
                }
            }
        }
    }

    fun clearState() {
        mutableState.value = RegisterState.Idle
    }

}

sealed class RegisterState {
    data object Idle : RegisterState()
    data object Registering : RegisterState()
    data object Registered : RegisterState()
    data class Error(val errors: List<RegisterError>) : RegisterState()
}

enum class RegisterError {
    EMAIL_ERROR,
    PASSWORD_ERROR,
    REPEAT_ERROR,
    PROFILE_ERROR,
    REGISTER_ERROR,
    NOT_IMPLEMENTED
}