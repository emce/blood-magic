package mobi.cwiklinski.bloodline.auth.firebase

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize
import kotlinx.coroutines.CoroutineScope
import mobi.cwiklinski.bloodline.auth.api.AuthenticationInitializer
import mobi.cwiklinski.bloodline.config.FirebaseConfig
import mobi.cwiklinski.bloodline.storage.api.StorageService

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

actual class AuthenticationInitializerImpl actual constructor(
    storageService: StorageService,
    coroutineScope: CoroutineScope
) : AuthenticationInitializer {

    private var initialized = false

    override fun run() {
        if (!initialized) {
            Firebase.initialize(
                Unit, options = FirebaseOptions(
                    applicationId = FirebaseConfig.FIREBASE_APP_ID,
                    apiKey = FirebaseConfig.FIREBASE_IOS_API_KEY,
                    databaseUrl = FirebaseConfig.FIREBASE_DATABASE_URL,
                    storageBucket = FirebaseConfig.FIREBASE_STORAGE_BUCKET,
                    projectId = FirebaseConfig.FIREBASE_PROJECT_ID,
                    gcmSenderId = FirebaseConfig.FIREBASE_MESSAGING_SENDER_ID
                )
            )
            initialized = true
        }
    }

}