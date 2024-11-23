package mobi.cwiklinski.bloodline.data.filed

import mobi.cwiklinski.bloodline.data.api.CenterService
import mobi.cwiklinski.bloodline.data.api.DonationService
import mobi.cwiklinski.bloodline.data.api.ProfileService
import org.koin.dsl.module

fun createDataModule() = module {
    single<CenterService> { CenterServiceImplementation() }
    single<DonationService> { DonationServiceImplementation() }
    single<ProfileService> { ProfileServiceImplementation(get(), get()) }
}