package mobi.cwiklinski.bloodline.auth.firebase

import co.touchlab.kermit.Logger
import dev.gitlive.firebase.auth.FacebookAuthProvider
import dev.gitlive.firebase.auth.GoogleAuthProvider
import dev.gitlive.firebase.auth.OAuthProvider
import io.ktor.http.Url
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import mobi.cwiklinski.bloodline.auth.api.AppleConfiguration
import mobi.cwiklinski.bloodline.auth.api.AuthError
import mobi.cwiklinski.bloodline.auth.api.AuthResult
import mobi.cwiklinski.bloodline.auth.api.FacebookConfiguration
import mobi.cwiklinski.bloodline.auth.api.GoogleConfiguration
import mobi.cwiklinski.bloodline.auth.api.getOauthClient
import org.publicvalue.multiplatform.oidc.OpenIdConnectClient
import org.publicvalue.multiplatform.oidc.appsupport.CodeAuthFlowFactory
import org.publicvalue.multiplatform.oidc.appsupport.openInBrowser
import org.publicvalue.multiplatform.oidc.appsupport.webserver.Webserver
import org.publicvalue.multiplatform.oidc.flows.AuthCodeResponse
import org.publicvalue.multiplatform.oidc.flows.CodeAuthFlow
import org.publicvalue.multiplatform.oidc.types.AuthCodeRequest

@Suppress("UNREACHABLE_CODE")
class DesktopAuthenticationServiceImpl(
    coroutineScope: CoroutineScope,
    private val authFlowFactory: CodeAuthFlowFactory
) : AuthenticationServiceImpl(coroutineScope) {

    private val redirectUrl = "https://bloodline.cwiklinski.mobi/auth"


    override fun loginWithGoogle() = callbackFlow {
        try {
            trySend(AuthResult.Failure(AuthError.NOT_IMPLEMENTED))
            throw NotImplementedError()
            val client = getOauthClient(GoogleConfiguration(redirectUri = redirectUrl))
            val newTokens = authFlowFactory.createAuthFlow(client).getAccessToken(
                configureAuthUrl = {
                    parameters.remove("client_secret")
                }
            )
            val authCredential = GoogleAuthProvider
                .credential(
                    newTokens.id_token,
                    newTokens.access_token
                )
            if (firebaseAuth.signInWithCredential(authCredential)
                    .user != null) {
                trySend(AuthResult.Success())
            } else {
                trySend(AuthResult.Failure(AuthError.GOOGLE_AUTH_ERROR))
            }
        } catch (e: NotImplementedError) {
            trySend(AuthResult.Failure(AuthError.NOT_IMPLEMENTED))
        } catch (e: Exception) {
            Logger.e("Login by Google", throwable = e)
            trySend(AuthResult.Failure(AuthError.GOOGLE_AUTH_ERROR))
        }
        awaitClose { }
    }

    override fun loginWithFacebook() = callbackFlow {
        try {
            throw NotImplementedError()
            val client = getOauthClient(FacebookConfiguration(redirectUri = redirectUrl))
            val newTokens = authFlowFactory.createAuthFlow(client).getAccessToken()
            val authCredential = FacebookAuthProvider
                .credential(
                    newTokens.access_token
                )
            if (firebaseAuth.signInWithCredential(authCredential)
                    .user != null) {
                trySend(AuthResult.Success())
            } else {
                trySend(AuthResult.Failure(AuthError.FACEBOOK_AUTH_ERROR))
            }
        } catch (e: NotImplementedError) {
            trySend(AuthResult.Failure(AuthError.NOT_IMPLEMENTED))
        } catch (e: Exception) {
            Logger.e("Login by Facebook", throwable = e)
            trySend(AuthResult.Failure(AuthError.FACEBOOK_AUTH_ERROR))
        }
        awaitClose { }
    }

    override fun loginWithApple() = callbackFlow {
        try {
            throw NotImplementedError()
            trySend(AuthResult.Failure(AuthError.NOT_IMPLEMENTED))
            val client = getOauthClient(AppleConfiguration(redirectUri = redirectUrl))
            val newTokens = authFlowFactory.createAuthFlow(client).getAccessToken()
            val authCredential = OAuthProvider.credential(
                    providerId = "apple.com",
                    newTokens.access_token,
                    newTokens.id_token,
                )
            if (firebaseAuth.signInWithCredential(authCredential)
                    .user != null) {
                trySend(AuthResult.Success())
            } else {
                trySend(AuthResult.Failure(AuthError.APPLE_AUTH_ERROR))
            }
        } catch (e: NotImplementedError) {
            trySend(AuthResult.Failure(AuthError.NOT_IMPLEMENTED))
        } catch (e: Exception) {
            Logger.e("Login by Apple", throwable = e)
            trySend(AuthResult.Failure(AuthError.APPLE_AUTH_ERROR))
        }
        awaitClose { }
    }
}

class DesktopCodeAuthFlowFactory(
    private val webserverProvider: () -> Webserver = { AuthServer() }
): CodeAuthFlowFactory {
    override fun createAuthFlow(client: OpenIdConnectClient): PlatformCodeAuthFlow {
        return PlatformCodeAuthFlow(client, webserverProvider = webserverProvider)
    }
}

class PlatformCodeAuthFlow(
    client: OpenIdConnectClient,
    private val webserverProvider: () -> Webserver = { AuthServer() },
    private val openUrl: (Url) -> Unit = { it.openInBrowser() }
) : CodeAuthFlow(client) {
    companion object {
        const val PORT = 8080
        const val REDIRECT_PATH = "/redirect"
    }

    override suspend fun getAuthorizationCode(request: AuthCodeRequest): AuthCodeResponse {

        val webserver = webserverProvider()

        val response =
            withContext(Dispatchers.IO) {
                async {
                    openUrl(request.url)
                    val response = webserver.startAndWaitForRedirect(PORT, REDIRECT_PATH)
                    webserver.stop()
                    response
                }.await()
            }

        return AuthCodeResponse.success(
            response
        )
    }
}