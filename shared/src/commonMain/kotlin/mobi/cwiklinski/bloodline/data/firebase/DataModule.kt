package mobi.cwiklinski.bloodline.data.firebase

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.database.FirebaseDatabase
import dev.gitlive.firebase.database.database
import mobi.cwiklinski.bloodline.data.api.CenterService
import mobi.cwiklinski.bloodline.data.api.DonationService
import mobi.cwiklinski.bloodline.data.api.NotificationService
import mobi.cwiklinski.bloodline.data.api.ProfileService
import mobi.cwiklinski.bloodline.data.api.TokenService
import mobi.cwiklinski.bloodline.getPlatform
import org.koin.dsl.module

fun createDataModule() = buildList {
    add(module {
        single<FirebaseDatabase> {
            val db = Firebase.database
            db.setPersistenceEnabled(true)
            db.setLoggingEnabled(getPlatform().isDebugBinary)
            db
        }
        single<FirebaseAuth> {
            Firebase.auth
        }
        single<CenterService> { CenterServiceImplementation(get()) }
        single<DonationService> { DonationServiceImplementation(get(), get()) }
        single<ProfileService> { ProfileServiceImplementation(get(), get(), get()) }
        single<NotificationService> { NotificationServiceImplementation(get()) }
        single<TokenService> { TokenServiceImplementation(get(), get()) }
    })
}