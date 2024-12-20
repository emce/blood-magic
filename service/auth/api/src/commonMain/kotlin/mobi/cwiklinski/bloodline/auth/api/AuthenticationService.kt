package mobi.cwiklinski.bloodline.auth.api

import kotlinx.coroutines.flow.Flow

sealed class AuthenticationState {
    data object Idle : AuthenticationState()
    data object Logged : AuthenticationState()
    data object NotLogged : AuthenticationState()
}

interface AuthenticationService {

    val authenticationState: Flow<AuthenticationState>

    fun loginWithEmailAndPassword(email: String, password: String): Flow<AuthResult>

    fun registerWithEmailAndPassWord(email: String, password: String): Flow<AuthResult>

    fun logOut(): Flow<Boolean>

    fun resetPassword(email: String): Flow<AuthResult>

    fun removeAccount(): Flow<Boolean>
}