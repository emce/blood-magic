package mobi.cwiklinski.bloodline.auth.firebase

import android.content.Context
import com.google.firebase.FirebasePlatform
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mobi.cwiklinski.bloodline.auth.api.AuthenticationInitializer
import mobi.cwiklinski.bloodline.config.FirebaseConfig
import mobi.cwiklinski.bloodline.storage.api.StorageService

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

actual class AuthenticationInitializerImpl actual constructor(
    val storageService: StorageService,
    val coroutineScope: CoroutineScope
) : AuthenticationInitializer {

    private var initialized = false

    override fun run() {
        if (!initialized) {
            try {
                initPersistence()
                Firebase.initialize(
                    Context(), options = FirebaseOptions(
                        applicationId = FirebaseConfig.FIREBASE_APP_ID,
                        apiKey = FirebaseConfig.FIREBASE_ANDROID_API_KEY,
                        databaseUrl = FirebaseConfig.FIREBASE_DATABASE_URL,
                        storageBucket = FirebaseConfig.FIREBASE_STORAGE_BUCKET,
                        projectId = FirebaseConfig.FIREBASE_PROJECT_ID,
                        gcmSenderId = FirebaseConfig.FIREBASE_MESSAGING_SENDER_ID
                    )
                )
                initialized = true
            } catch (e: IllegalStateException) {
                Napier.e("Firebase initialization", e)
            }
        }
    }

    private fun initDebug() {
        FirebasePlatform.initializeFirebasePlatform(object : FirebasePlatform(){
            val storage = mutableMapOf<String,String>()
            override fun clear(key: String) {
                storage.remove(key)
            }

            override fun log(msg: String) {
                println(msg)
            }

            override fun retrieve(key: String): String? {
                return storage[key]
            }

            override fun store(key: String, value: String) {
                storage[key] = value
            }
        })
    }

    private fun initPersistence() {
        FirebasePlatform.initializeFirebasePlatform(object : FirebasePlatform() {

            override fun clear(key: String) {
                coroutineScope.launch {
                    storageService.clearAll()
                }
            }

            override fun log(msg: String) {
                println(msg)
            }

            override fun retrieve(key: String) = runBlocking {
                storageService.getString(key, "")
            }

            override fun store(key: String, value: String) {
                coroutineScope.launch {
                    storageService.storeString(key, value)
                }
            }
        })
    }
}