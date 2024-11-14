package mobi.cwiklinski.bloodline.domain.usecase.auth

import mobi.cwiklinski.bloodline.auth.api.AuthenticationService
import kotlinx.coroutines.flow.filterNotNull

class IsLoggedInUseCase(private val service: AuthenticationService) {

    operator fun invoke() = service.authenticationState.filterNotNull()
}