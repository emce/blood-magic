package mobi.cwiklinski.bloodline.auth.firebase

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import co.touchlab.kermit.Logger
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import dev.gitlive.firebase.auth.FacebookAuthProvider
import dev.gitlive.firebase.auth.GoogleAuthProvider
import dev.gitlive.firebase.auth.OAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import mobi.cwiklinski.bloodline.auth.api.AppleConfiguration
import mobi.cwiklinski.bloodline.auth.api.AuthError
import mobi.cwiklinski.bloodline.auth.api.AuthResult
import mobi.cwiklinski.bloodline.auth.api.AuthenticationState
import mobi.cwiklinski.bloodline.auth.api.FacebookConfiguration
import mobi.cwiklinski.bloodline.auth.api.getOauthClient
import mobi.cwiklinski.bloodline.config.AppConfig
import org.publicvalue.multiplatform.oidc.appsupport.CodeAuthFlowFactory

class AndroidAuthenticationServiceImpl(
    coroutineScope: CoroutineScope,
    private val context: Context,
    private val authFlowFactory: CodeAuthFlowFactory
) :
    AuthenticationServiceImpl(coroutineScope) {

    override fun loginWithGoogle() = send(
        authFunction = {
            val credentialManager = CredentialManager
                .create(context)
            val googleIdOption = GetSignInWithGoogleOption
                .Builder(AppConfig.ANDROID_CLIENT_ID)
                .build()
            val request = GetCredentialRequest
                .Builder()
                .addCredentialOption(googleIdOption)
                .build()

            try {
                val result = credentialManager.getCredential(
                    context = context,
                    request = request
                )

                val credential = result.credential
                if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdTokenCredential = GoogleIdTokenCredential
                        .createFrom(credential.data)
                    val authCredential = GoogleAuthProvider
                        .credential(
                            googleIdTokenCredential.idToken,
                            null
                        )
                    firebaseAuth.signInWithCredential(authCredential)
                        .user != null
                } else {
                    throw GoogleAuthException
                }
            } catch (e: GoogleIdTokenParsingException) {
                throw GoogleAuthException
            } catch (e: NoCredentialException) {
                throw GoogleAuthException
            } catch (e: GetCredentialCancellationException) {
                throw GoogleAuthException
            } catch (e: GetCredentialException) {
                throw GoogleAuthException
            } catch (t: Throwable) {
                throw GoogleAuthException
            }
        },
        sideEffect = { _authenticationState.value = AuthenticationState.Logged }
    )


    override fun loginWithFacebook() = callbackFlow {
        try {
            val client = getOauthClient(FacebookConfiguration())
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
            val client = getOauthClient(AppleConfiguration())
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