package mobi.cwiklinski.bloodline.auth.api

import kotlinx.coroutines.flow.Flow

sealed class AuthenticationState {
    object Idle : AuthenticationState()
    object Logged : AuthenticationState()
    object NotLogged : AuthenticationState()
}

interface AuthenticationService {

    val authenticationState: Flow<AuthenticationState>

    fun loginWithEmailAndPassword(email: String, password: String): Flow<AuthResult>

    fun registerWithEmailAndPassWord(email: String, password: String): Flow<AuthResult>

    fun logOut(): Flow<Boolean>

    fun resetPassword(email: String): Flow<AuthResult>
}