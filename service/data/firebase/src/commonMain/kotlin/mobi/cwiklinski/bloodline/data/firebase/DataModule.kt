package mobi.cwiklinski.bloodline.data.firebase

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.FirebaseDatabase
import dev.gitlive.firebase.database.database
import mobi.cwiklinski.bloodline.data.api.CenterService
import mobi.cwiklinski.bloodline.data.api.DonationService
import mobi.cwiklinski.bloodline.data.api.ProfileService
import org.koin.dsl.module

fun createDataModule() = module {
    single<FirebaseDatabase> {
        val db = Firebase.database
        db.setPersistenceEnabled(true)
        db.setLoggingEnabled(true)
        db
    }
    single<CenterService> { CenterServiceImplementation(db = get()) }
    single<DonationService> { DonationServiceImplementation(db = get(), auth = get()) }
    single<ProfileService> { ProfileServiceImplementation(db = get(), auth = get()) }
}