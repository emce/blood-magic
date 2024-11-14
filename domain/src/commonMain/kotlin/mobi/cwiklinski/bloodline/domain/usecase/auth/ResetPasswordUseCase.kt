package mobi.cwiklinski.bloodline.domain.usecase.auth

import mobi.cwiklinski.bloodline.auth.api.AuthenticationService

class ResetPasswordUseCase(private val service: AuthenticationService) {

    suspend operator fun invoke(email: String) = service.resetPassword(email = email)
}
