package mobi.cwiklinski.bloodline.domain.di

import mobi.cwiklinski.bloodline.auth.firebase.createAuthenticationModule
import mobi.cwiklinski.bloodline.data.firebase.createDataModule
import org.koin.core.module.Module
import org.koin.dsl.module

expect fun createDomainModules(): List<Module>

fun createCommonDomainModules() = buildList {
    addAll(createServiceModules())
    add(createUseCaseModules())
}

private fun createServiceModules() = buildList {
    add(createDataModule())
    add(createStorageModule())
}

private fun createUseCaseModules() = module {

}