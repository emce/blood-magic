package mobi.cwiklinski.bloodline.auth.firebase

import co.touchlab.kermit.Logger
import dev.gitlive.firebase.auth.FacebookAuthProvider
import dev.gitlive.firebase.auth.GoogleAuthProvider
import dev.gitlive.firebase.auth.OAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import mobi.cwiklinski.bloodline.auth.api.AppleConfiguration
import mobi.cwiklinski.bloodline.auth.api.AuthError
import mobi.cwiklinski.bloodline.auth.api.AuthResult
import mobi.cwiklinski.bloodline.auth.api.FacebookConfiguration
import mobi.cwiklinski.bloodline.auth.api.GoogleConfiguration
import mobi.cwiklinski.bloodline.auth.api.getOauthClient
import org.publicvalue.multiplatform.oidc.appsupport.CodeAuthFlowFactory

class IosAuthenticationServiceImpl(
    coroutineScope: CoroutineScope,
    private val authFlowFactory: CodeAuthFlowFactory
) :
    AuthenticationServiceImpl(coroutineScope) {

    override fun loginWithGoogle() = callbackFlow {
        try {
            val client =
                getOauthClient(GoogleConfiguration())
            val newTokens = authFlowFactory.createAuthFlow(client).getAccessToken()
            val authCredential = GoogleAuthProvider
                .credential(
                    newTokens.id_token,
                    newTokens.access_token
                )
            if (firebaseAuth.signInWithCredential(authCredential)
                    .user != null
            ) {
                trySend(AuthResult.Success())
            } else {
                trySend(AuthResult.Failure(AuthError.GOOGLE_AUTH_ERROR))
            }
        } catch (e: Exception) {
            Logger.e("Login by Google", throwable = e)
            trySend(AuthResult.Failure(AuthError.GOOGLE_AUTH_ERROR))
        }
        awaitClose { }
    }

    override fun loginWithFacebook() = callbackFlow {
        try {
            val client =
                getOauthClient(FacebookConfiguration())
            val newTokens = authFlowFactory.createAuthFlow(client).getAccessToken()
            val authCredential = FacebookAuthProvider
                .credential(
                    newTokens.access_token
                )
            if (firebaseAuth.signInWithCredential(authCredential)
                    .user != null
            ) {
                trySend(AuthResult.Success())
            } else {
                trySend(AuthResult.Failure(AuthError.FACEBOOK_AUTH_ERROR))
            }
        } catch (e: Exception) {
            Logger.e("Login by Facebook", throwable = e)
            trySend(AuthResult.Failure(AuthError.FACEBOOK_AUTH_ERROR))
        }
        awaitClose { }
    }

    override fun loginWithApple() = callbackFlow {
        try {
            val client =
                getOauthClient(AppleConfiguration())
            val newTokens = authFlowFactory.createAuthFlow(client).getAccessToken()
            val authCredential = OAuthProvider.credential(
                providerId = "apple.com",
                newTokens.access_token,
                newTokens.id_token,
            )
            if (firebaseAuth.signInWithCredential(authCredential)
                    .user != null
            ) {
                trySend(AuthResult.Success())
            } else {
                trySend(AuthResult.Failure(AuthError.APPLE_AUTH_ERROR))
            }
        } catch (e: Exception) {
            Logger.e("Login by Apple", throwable = e)
            trySend(AuthResult.Failure(AuthError.APPLE_AUTH_ERROR))
        }
        awaitClose { }
    }
}