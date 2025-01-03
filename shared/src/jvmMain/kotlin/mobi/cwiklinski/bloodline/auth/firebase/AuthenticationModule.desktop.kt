package mobi.cwiklinski.bloodline.auth.firebase

import io.ktor.client.HttpClient
import mobi.cwiklinski.bloodline.auth.api.AuthenticationInitializer
import mobi.cwiklinski.bloodline.auth.api.AuthenticationService
import org.koin.dsl.lazyModule

actual fun createAuthenticationModule() = lazyModule {
    single<AuthenticationService> { DesktopAuthenticationServiceImpl(get(), get(), get(), get()) }
    single<AuthenticationInitializer> { AuthenticationInitializerImpl(get(), get()) }
    single<HttpClient> { HttpClient() }
    single<AuthServer> { AuthServer(get()) }
}