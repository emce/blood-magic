package mobi.cwiklinski.bloodline.domain.di

import mobi.cwiklinski.bloodline.data.firebase.createDataModule
import mobi.cwiklinski.bloodline.storage.datastore.createStorageModule
import org.koin.core.module.Module
import org.koin.dsl.module

expect fun createDomainModules(): List<Module>

fun createCommonDomainModules(): List<Module> = buildList {
    addAll(createServiceModules())
    add(createUseCaseModules())
}

private fun createServiceModules(): List<Module> = buildList {
    add(createDataModule())
    add(createStorageModule())
}

private fun createUseCaseModules() = module {

}