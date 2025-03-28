package mobi.cwiklinski.bloodline.auth.firebase

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseException
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mobi.cwiklinski.bloodline.auth.api.AuthError
import mobi.cwiklinski.bloodline.auth.api.AuthResult
import mobi.cwiklinski.bloodline.auth.api.AuthenticationService
import mobi.cwiklinski.bloodline.auth.api.AuthenticationState
import org.koin.core.component.KoinComponent

abstract class AuthenticationServiceImpl(coroutineScope: CoroutineScope) : AuthenticationService,
    KoinComponent, FlowCollector<FirebaseUser?> {

    protected val firebaseAuth: FirebaseAuth = Firebase.auth

    protected val _authenticationState =
        MutableStateFlow<AuthenticationState>(AuthenticationState.Idle)
    override val authenticationState: Flow<AuthenticationState> = _authenticationState

    init {
        coroutineScope.launch {
            firebaseAuth.authStateChanged.collect(this@AuthenticationServiceImpl)
        }
    }

    override fun loginWithEmailAndPassword(email: String, password: String) = send(
        authFunction = { firebaseAuth.signInWithEmailAndPassword(email, password).user != null },
        sideEffect = { _authenticationState.value = AuthenticationState.Logged }
    )

    override fun registerWithEmailAndPassWord(email: String, password: String): Flow<AuthResult> =
        send(
            authFunction = {
                val result = firebaseAuth.createUserWithEmailAndPassword(
                    email,
                    password
                )
                result.user != null
            },
            sideEffect = { _authenticationState.value = AuthenticationState.Logged }
        )

    override fun logOut(): Flow<Boolean> = callbackFlow {
        try {
            firebaseAuth.signOut()
            _authenticationState.value = AuthenticationState.NotLogged
            trySend(true)
        } catch (e: FirebaseException) {
            trySend(false)
        }
        awaitClose { }
    }

    override fun resetPassword(email: String): Flow<AuthResult> = callbackFlow {
        try {
            firebaseAuth.sendPasswordResetEmail(email)
            trySend(AuthResult.Success())
        } catch (e: FirebaseException) {
            trySend(AuthResult.Failure(AuthError.ERROR))
        }
        awaitClose { }
    }

    override suspend fun emit(value: FirebaseUser?) {
        _authenticationState.value =
            if (value != null) AuthenticationState.Logged else AuthenticationState.NotLogged
    }

    override fun removeAccount() = callbackFlow {
        try {
            firebaseAuth.currentUser?.delete()
            trySend(firebaseAuth.currentUser == null)
        } catch (e: FirebaseException) {
            trySend(false)
        }
    }
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
    } catch (e: GoogleAuthException) {
        AuthError.GOOGLE_AUTH_ERROR
    } catch (e: FacebookAuthException) {
        AuthError.FACEBOOK_AUTH_ERROR
    } catch (e: AppleAuthException) {
        AuthError.APPLE_AUTH_ERROR
    }
    awaitClose { }
}

object GoogleAuthException : Exception()
object FacebookAuthException : Exception()
object AppleAuthException : Exception()
