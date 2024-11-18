package mobi.cwiklinski.bloodline.auth.api

import kotlinx.coroutines.flow.Flow

interface  DesktopMainAuthenticationService : AuthenticationService {

    override val authenticationState: Flow<AuthenticationState>

    override fun loginWithEmailAndPassword(email: String, password: String): Flow<AuthResult>

    override fun registerWithEmailAndPassWord(email: String, password: String): Flow<AuthResult>

    override fun logOut(): Flow<Boolean>

    override fun resetPassword(email: String): Flow<Boolean>
}