package mobi.cwiklinski.bloodline.domain.usecase.auth

import mobi.cwiklinski.bloodline.auth.api.AuthenticationService

class LogOutUseCase(private val service: AuthenticationService) {

    suspend operator fun invoke() = service.logOut()
}
