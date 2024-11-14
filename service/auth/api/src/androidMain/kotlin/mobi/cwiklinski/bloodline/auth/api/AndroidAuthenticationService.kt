package mobi.cwiklinski.bloodline.auth.api

import android.content.Intent
import kotlinx.coroutines.flow.Flow

interface AndroidAuthenticationService : AuthenticationService {

    override val authenticationState: Flow<AuthenticationState>

    override fun loginWithEmailAndPassword(email: String, password: String): Flow<AuthResult>

    override fun registerWithEmailAndPassWord(email: String, password: String): Flow<AuthResult>

    override suspend fun logOut()

    override suspend fun resetPassword(email: String)
}