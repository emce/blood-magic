package mobi.cwiklinski.bloodline.storage.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import mobi.cwiklinski.bloodline.storage.api.StorageService
import org.koin.core.module.Module
import org.koin.dsl.module

actual val commonModule: Module = module {
    single<DataStore<Preferences>> {
        val context = get<Context>()
        PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile(name = StorageService.FILE_NAME)
        }
    }
    single<StorageService> { StorageServiceImpl(get()) }
}