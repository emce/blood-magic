package mobi.cwiklinski.bloodline.storage.datastore

import org.koin.core.module.Module

fun createStorageModule() = commonModule

expect val commonModule: Module