package mobi.cwiklinski.bloodline.auth.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext

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
    ERROR
}
