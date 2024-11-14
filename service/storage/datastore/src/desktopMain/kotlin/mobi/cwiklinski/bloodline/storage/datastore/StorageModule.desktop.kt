package mobi.cwiklinski.bloodline.storage.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import mobi.cwiklinski.bloodline.storage.api.StorageService
import okio.Path.Companion.toOkioPath
import org.koin.core.module.Module
import org.koin.dsl.module
import java.io.File

actual val commonModule: Module = module {
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.createWithPath {
            File(
                System.getProperty("java.io.tmpdir"),
                StorageService.FILE_NAME,
            ).toOkioPath()
        }
    }
    single<StorageService> { StorageServiceImpl(get()) }
}