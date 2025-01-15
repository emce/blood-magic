package mobi.cwiklinski.bloodline.auth.firebase

import kotlinx.coroutines.CoroutineScope
import mobi.cwiklinski.bloodline.auth.api.AuthenticationInitializer
import mobi.cwiklinski.bloodline.storage.api.StorageService


actual class AuthenticationInitializerImpl actual constructor(
    storageService: StorageService,
    coroutineScope: CoroutineScope
) : AuthenticationInitializer {

    override fun run() { }

}