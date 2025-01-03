package mobi.cwiklinski.bloodline.auth.firebase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import mobi.cwiklinski.bloodline.auth.api.AuthError
import mobi.cwiklinski.bloodline.auth.api.AuthResult

class IosAuthenticationServiceImpl(coroutineScope: CoroutineScope) :
    AuthenticationServiceImpl(coroutineScope) {

    override fun loginWithGoogle() = callbackFlow {
        trySend(AuthResult.Failure(AuthError.NOT_IMPLEMENTED))
        awaitClose { }
    }

    override fun loginWithFacebook() = callbackFlow {
        trySend(AuthResult.Failure(AuthError.NOT_IMPLEMENTED))
        awaitClose { }
    }

    override fun loginWithApple() = callbackFlow {
        trySend(AuthResult.Failure(AuthError.NOT_IMPLEMENTED))
        awaitClose { }
    }
}