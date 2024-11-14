package mobi.cwiklinski.bloodline.auth.firebase

import mobi.cwiklinski.bloodline.activityprovider.api.ActivityProvider
import mobi.cwiklinski.bloodline.auth.api.AndroidAuthenticationService
import org.koin.core.component.inject

class AndroidAuthenticationServiceImpl : AuthenticationServiceImpl(), AndroidAuthenticationService {

    private val activityProvider: ActivityProvider by inject()
}