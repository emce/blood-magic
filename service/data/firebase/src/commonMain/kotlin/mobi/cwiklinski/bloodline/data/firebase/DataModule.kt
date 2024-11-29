package mobi.cwiklinski.bloodline.data.firebase

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.database.FirebaseDatabase
import dev.gitlive.firebase.database.database
import mobi.cwiklinski.bloodline.data.api.CenterService
import mobi.cwiklinski.bloodline.data.api.DonationService
import mobi.cwiklinski.bloodline.data.api.ProfileService
import org.koin.dsl.lazyModule
import org.koin.dsl.module

fun createDataModule() = buildList {
    add(module {
        single<FirebaseDatabase> {
            val db = Firebase.database
            db.setPersistenceEnabled(true)
            db.setLoggingEnabled(true)
            db
        }
        single<FirebaseAuth> {
            Firebase.auth
        }
    })
    add(lazyModule {
        single<CenterService> { CenterServiceImplementation(get()) }
        single<DonationService> { DonationServiceImplementation(get(), get()) }
        single<ProfileService> { ProfileServiceImplementation(get(), get()) }
    })
}