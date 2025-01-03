package mobi.cwiklinski.bloodline.auth.firebase

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.store.DataStoreFactory
import com.google.api.client.util.store.FileDataStoreFactory
import dev.gitlive.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import mobi.cwiklinski.bloodline.auth.api.AuthError
import mobi.cwiklinski.bloodline.auth.api.AuthResult
import mobi.cwiklinski.bloodline.common.Either
import mobi.cwiklinski.bloodline.common.event.Events
import mobi.cwiklinski.bloodline.common.manager.CallbackManager
import mobi.cwiklinski.bloodline.storage.api.StorageService
import java.io.File
import java.io.InputStreamReader

class DesktopAuthenticationServiceImpl(
    coroutineScope: CoroutineScope,
    private val storageService: StorageService,
    private val server: AuthServer,
    private val callbackManager: CallbackManager
) :
    AuthenticationServiceImpl(coroutineScope) {

        private val authCodeKey = ":authCode"
        private val clientSecretsFile = "./client_secrets.json"
        private val dataStoreDir = File("tokens")
        private val dataStoreFactory: DataStoreFactory = FileDataStoreFactory(dataStoreDir)
        private val scopes =
            mutableListOf(
                "https://www.googleapis.com/auth/userinfo.email",
                "https://www.googleapis.com/auth/userinfo.profile"
            )
        private val jsonFactory = GsonFactory.getDefaultInstance()


    override fun loginWithGoogle() = callbackFlow {
        val authCode = storageService.getString(authCodeKey, "")
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
        val clientSecrets =
            GoogleClientSecrets.load(
                jsonFactory,
                InputStreamReader(File(clientSecretsFile).inputStream())
            )

        // Build flow and trigger user authorization request
        val flow = GoogleAuthorizationCodeFlow.Builder(
            httpTransport, jsonFactory, clientSecrets, scopes
        )
            .setDataStoreFactory(dataStoreFactory)
            .setAccessType("offline")
            .build()


        if (authCode.isEmpty()) {
            val authorizationUrl: String =
                flow.newAuthorizationUrl()
                    .setRedirectUri("urn:ietf:wg:oauth:2.0:oob")
                    .setScopes(listOf("openid", "profile", "email"))
                    .build()
            callbackManager.postEvent(Events.OpenBrowser(java.net.URI(authorizationUrl).toString()))
        } else {
            val tokenResponse: GoogleTokenResponse =
                flow.newTokenRequest(authCode).setRedirectUri("urn:ietf:wg:oauth:2.0:oob").execute()
            val credential: Credential = flow.createAndStoreCredential(tokenResponse, "user")
            withContext(Dispatchers.Main) {
                when (val authResponse = server.authRequest(authCode = credential.accessToken)) {
                    is Either.Left -> {
                        storageService.storeString(authCodeKey, authResponse.value)
                        when (val profileResponse = server.profileGetRequest(authResponse.value)) {
                            is Either.Left -> {
                                val authCredential = GoogleAuthProvider
                                    .credential(
                                        profileResponse.value,
                                        null
                                    )
                                firebaseAuth.signInWithCredential(authCredential)
                                    .user != null
                            }
                            is Either.Right -> {
                                trySend(AuthResult.Failure(AuthError.GOOGLE_AUTH_ERROR))
                            }
                        }
                    }
                    is Either.Right -> {
                        trySend(AuthResult.Failure(AuthError.GOOGLE_AUTH_ERROR))
                    }
                }
            }
        }
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