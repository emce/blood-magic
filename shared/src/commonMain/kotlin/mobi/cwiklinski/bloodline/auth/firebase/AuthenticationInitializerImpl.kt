package mobi.cwiklinski.bloodline.auth.firebase

import kotlinx.coroutines.CoroutineScope
import mobi.cwiklinski.bloodline.auth.api.AuthenticationInitializer
import mobi.cwiklinski.bloodline.storage.api.StorageService

expect class AuthenticationInitializerImpl(storageService: StorageService, coroutineScope: CoroutineScope) :
    AuthenticationInitializer