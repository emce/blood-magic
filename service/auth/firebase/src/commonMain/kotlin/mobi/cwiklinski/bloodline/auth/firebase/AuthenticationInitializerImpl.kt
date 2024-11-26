package mobi.cwiklinski.bloodline.auth.firebase

import kotlinx.coroutines.CoroutineScope
import mobi.cwiklinski.bloodline.auth.api.AuthenticationInitializer
import mobi.cwiklinski.bloodline.storage.api.StorageService

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class AuthenticationInitializerImpl(storageService: StorageService, coroutineScope: CoroutineScope) :
    AuthenticationInitializer