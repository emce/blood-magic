package mobi.cwiklinski.bloodline.auth.firebase

import mobi.cwiklinski.bloodline.auth.api.AuthResult
import mobi.cwiklinski.bloodline.auth.api.AuthenticationService
import mobi.cwiklinski.bloodline.auth.api.AuthenticationState
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseException
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mobi.cwiklinski.bloodline.auth.api.AuthError
import org.koin.core.component.KoinComponent

class AuthenticationServiceImpl(private val coroutineScope: CoroutineScope) : AuthenticationService, KoinComponent {

    private val firebaseAuth: FirebaseAuth = Firebase.auth

    private val _authenticationState = MutableStateFlow<AuthenticationState>(value = AuthenticationState.Idle)
    override val authenticationState: Flow<AuthenticationState> = _authenticationState

    init {
        coroutineScope.launch {
            firebaseAuth.authStateChanged.collectLatest {
                _authenticationState.emit(
                    if (it != null) AuthenticationState.Logged else AuthenticationState.NotLogged
                )
            }
        }
        _authenticationState.value = if (firebaseAuth.currentUser == null)
            AuthenticationState.NotLogged
        else
            AuthenticationState.Logged
    }

    override fun loginWithEmailAndPassword(email: String, password: String) = send(
        authFunction = { firebaseAuth.signInWithEmailAndPassword(email, password).user != null },
        sideEffect = { _authenticationState.value = AuthenticationState.Logged }
    )

    override fun registerWithEmailAndPassWord(email: String, password: String): Flow<AuthResult> = send(
        authFunction = { firebaseAuth.createUserWithEmailAndPassword(email, password).user != null },
        sideEffect = { _authenticationState.value = AuthenticationState.Logged }
    )

    override fun logOut() = flow {
        try {
            firebaseAuth.signOut()
            _authenticationState.value = AuthenticationState.NotLogged
            emit(true)
        } catch (e: FirebaseException) {
            emit(false)
        }
    }

    override fun resetPassword(email: String): Flow<AuthResult> = send(
        authFunction = {
            firebaseAuth.sendPasswordResetEmail(email)
            true
       },
        sideEffect = { _authenticationState.value = AuthenticationState.Logged }
    )
}

fun send(
    authFunction: suspend () -> Boolean,
    sideEffect: () -> Unit = { }
): Flow<AuthResult> = callbackFlow {
    try {
        withContext(Dispatchers.Default) { authFunction() }
        trySend(AuthResult.Success()).also { sideEffect() }
    } catch (exception: FirebaseException) {
        val error = when (exception.message) {
            "ERROR_EMAIL_ALREADY_IN_USE",
            "account-exists-with-different-credential",
            "email-already-in-use" -> {
                AuthError.EMAIL_ALREADY_IN_USE
            }
            "ERROR_WRONG_PASSWORD",
            "wrong-password" -> {
                AuthError.INCORRECT_PASSWORD
            }
            "ERROR_USER_NOT_FOUND",
            "user-not-found" -> {
                AuthError.USER_NOT_FOUND
            }
            "ERROR_USER_DISABLED",
            "user-disabled" -> {
                AuthError.USER_DISABLED
            }
            "ERROR_INVALID_EMAIL",
            "invalid-email" -> {
                AuthError.INCORRECT_EMAIL
            }
            else -> {
                AuthError.ERROR
            }
        }
        trySend(AuthResult.Failure(error = error))
    }
    awaitClose { }
}
