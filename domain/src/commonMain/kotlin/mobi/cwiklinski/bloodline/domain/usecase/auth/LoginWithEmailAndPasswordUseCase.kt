package mobi.cwiklinski.bloodline.domain.usecase.auth

import mobi.cwiklinski.bloodline.auth.api.AuthResult
import mobi.cwiklinski.bloodline.auth.api.AuthenticationService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull

class LoginWithEmailAndPasswordUseCase(private val service: AuthenticationService) {

    operator fun invoke(email: String, password: String): Flow<AuthResult> =
        service.loginWithEmailAndPassword(email = email, password = password).filterNotNull()
}
