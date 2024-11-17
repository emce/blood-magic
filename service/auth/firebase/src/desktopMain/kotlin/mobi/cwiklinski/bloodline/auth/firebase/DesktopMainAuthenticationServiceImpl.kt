package mobi.cwiklinski.bloodline.auth.firebase

import com.google.firebase.FirebasePlatform
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize
import mobi.cwiklinski.bloodline.auth.api.DesktopMainAuthenticationService
import mobi.cwiklinski.bloodline.config.FirebaseConfig

class DesktopMainAuthenticationServiceImpl : AuthenticationServiceImpl(), DesktopMainAuthenticationService {

    init {
        FirebasePlatform.initializeFirebasePlatform(object : FirebasePlatform() {
            val storage = mutableMapOf<String, String>()
            override fun store(key: String, value: String) = storage.set(key, value)
            override fun retrieve(key: String) = storage[key]
            override fun clear(key: String) { storage.remove(key) }
            override fun log(msg: String) = println(msg)
        })
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
    }
}