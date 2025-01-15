package mobi.cwiklinski.bloodline.auth.api

import mobi.cwiklinski.bloodline.config.AppConfig
import org.publicvalue.multiplatform.oidc.OpenIdConnectClient

interface OauthConfiguration {
    val authorizationEndpoint: String
    val tokenEndpoint: String
    val clientId: String
    val clientSecret: String?
    val scope: String
    val redirectUri: String
    companion object {
        val defaultRedirect = "mobi.cwiklinski.bloodline://redirect"
    }
}

data class GoogleConfiguration(
    override val authorizationEndpoint: String = "https://accounts.google.com/o/oauth2/auth",
    override val tokenEndpoint: String = "https://oauth2.googleapis.com/token",
    override val clientId: String = AppConfig.GOOGLE_CLIENT_ID,
    override val clientSecret: String? = AppConfig.GOOGLE_CLIENT_SECRET,
    override val scope: String = "openid email profile",
    override val redirectUri: String = OauthConfiguration.defaultRedirect
) : OauthConfiguration


data class FacebookConfiguration(
    override val authorizationEndpoint: String = "https://www.facebook.com/v11.0/dialog/oauth",
    override val tokenEndpoint: String = "https://graph.facebook.com/v11.0/oauth/access_token",
    override val clientId: String = AppConfig.FACEBOOK_CLIENT_ID,
    override val clientSecret: String? = AppConfig.FACEBOOK_CLIENT_SECRET,
    override val scope: String = "public_profile email",
    override val redirectUri: String = OauthConfiguration.defaultRedirect
) : OauthConfiguration

data class AppleConfiguration(
    override val authorizationEndpoint: String = "https://appleid.apple.com/auth/authorize",
    override val tokenEndpoint: String = "https://appleid.apple.com/auth/token",
    override val clientId: String = AppConfig.APPLE_CLIENT_ID,
    override val clientSecret: String? = AppConfig.APPLE_CLIENT_SECRET,
    override val scope: String = "email profile",
    override val redirectUri: String = OauthConfiguration.defaultRedirect
) : OauthConfiguration

fun getOauthClient(config: OauthConfiguration) =
        OpenIdConnectClient {
            endpoints {
                authorizationEndpoint = config.authorizationEndpoint
                tokenEndpoint = config.tokenEndpoint
            }
            clientId = config.clientId
            clientSecret = config.clientSecret
            scope = config.scope
            redirectUri = config.redirectUri
        }
