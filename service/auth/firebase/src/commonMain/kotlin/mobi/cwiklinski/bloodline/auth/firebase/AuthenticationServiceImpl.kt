package mobi.cwiklinski.bloodline.auth.firebase

import mobi.cwiklinski.bloodline.auth.api.AuthResult
import mobi.cwiklinski.bloodline.auth.api.AuthenticationService
import mobi.cwiklinski.bloodline.auth.api.AuthenticationState
import mobi.cwiklinski.bloodline.auth.api.authenticate
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent

open class AuthenticationServiceImpl : AuthenticationService, KoinComponent {

    private val firebaseAuth: FirebaseAuth = Firebase.auth

    private val _authenticationState = MutableStateFlow<AuthenticationState>(value = AuthenticationState.Idle)
    override val authenticationState: Flow<AuthenticationState> = _authenticationState

    init {
        _authenticationState.value = if (firebaseAuth.currentUser == null)
            AuthenticationState.NotLogged
        else
            AuthenticationState.Logged
    }

    fun getFirebaseAuth(): FirebaseAuth = firebaseAuth

    fun setAuthenticationState(state: AuthenticationState) {
        _authenticationState.value = state
    }

    override fun loginWithEmailAndPassword(email: String, password: String): Flow<AuthResult> = authenticate(
        authFunction = { firebaseAuth.signInWithEmailAndPassword(email, password).user != null },
        sideEffect = { _authenticationState.value = AuthenticationState.Logged }
    )

    override fun registerWithEmailAndPassWord(email: String, password: String): Flow<AuthResult> = authenticate(
        authFunction = { firebaseAuth.createUserWithEmailAndPassword(email, password).user != null },
        sideEffect = { _authenticationState.value = AuthenticationState.Logged }
    )

    override suspend fun logOut() {
        firebaseAuth.signOut()
        _authenticationState.value = AuthenticationState.NotLogged
    }

    override suspend fun resetPassword(email: String): Unit =
        firebaseAuth.sendPasswordResetEmail(email)
}
