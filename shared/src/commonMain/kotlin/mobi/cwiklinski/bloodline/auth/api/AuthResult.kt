package mobi.cwiklinski.bloodline.auth.api

sealed class AuthResult {

    data class Success(val data: Any? = null) : AuthResult()

    data class Failure(val error: AuthError) : AuthResult()

    data class Dismissed(val error: String? = null) : AuthResult()
}

enum class AuthError {
    INCORRECT_EMAIL,
    INCORRECT_PASSWORD,
    USER_NOT_FOUND,
    USER_DISABLED,
    EMAIL_ALREADY_IN_USE,
    NOT_IMPLEMENTED,
    GOOGLE_AUTH_ERROR,
    FACEBOOK_AUTH_ERROR,
    APPLE_AUTH_ERROR,
    ERROR
}
