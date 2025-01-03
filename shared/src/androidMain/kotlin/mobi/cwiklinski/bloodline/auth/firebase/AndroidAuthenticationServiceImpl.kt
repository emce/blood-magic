package mobi.cwiklinski.bloodline.auth.firebase

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import dev.gitlive.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import mobi.cwiklinski.bloodline.auth.api.AuthError
import mobi.cwiklinski.bloodline.auth.api.AuthResult
import mobi.cwiklinski.bloodline.auth.api.AuthenticationState
import mobi.cwiklinski.bloodline.config.FirebaseConfig

class AndroidAuthenticationServiceImpl(coroutineScope: CoroutineScope, private val context: Context) :
    AuthenticationServiceImpl(coroutineScope) {

        override fun loginWithGoogle() = send(
            authFunction = {
                val credentialManager = CredentialManager
                    .create(context)
                val googleIdOption = GetSignInWithGoogleOption
                    .Builder(FirebaseConfig.ANDROID_CLIENT_ID)
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
        trySend(AuthResult.Failure(AuthError.NOT_IMPLEMENTED))
        awaitClose { }
    }

    override fun loginWithApple() = callbackFlow {
        trySend(AuthResult.Failure(AuthError.NOT_IMPLEMENTED))
        awaitClose { }
    }
}