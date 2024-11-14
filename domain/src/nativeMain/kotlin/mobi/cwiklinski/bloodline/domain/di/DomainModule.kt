package mobi.cwiklinski.bloodline.domain.di

import mobi.cwiklinski.bloodline.auth.api.NativeMainAuthenticationService
import mobi.cwiklinski.bloodline.auth.firebase.createAuthenticationModule
import mobi.cwiklinski.bloodline.domain.usecase.auth.IsLoggedInUseCase
import mobi.cwiklinski.bloodline.domain.usecase.auth.LogOutUseCase
import mobi.cwiklinski.bloodline.domain.usecase.auth.LoginWithEmailAndPasswordUseCase
import mobi.cwiklinski.bloodline.domain.usecase.auth.RegisterWithEmailAndPasswordUseCase
import mobi.cwiklinski.bloodline.domain.usecase.auth.ResetPasswordUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun createDomainModules(): List<Module> = buildList {
    addAll(createAuthenticationModule())
    add(createAuthUseCaseModule())
} + createCommonDomainModules()

private fun createAuthUseCaseModule() = module {
    factory { IsLoggedInUseCase(service = get() as NativeMainAuthenticationService) }
    factory { LoginWithEmailAndPasswordUseCase(service = get() as NativeMainAuthenticationService) }
    factory { LogOutUseCase(service = get() as NativeMainAuthenticationService) }
    factory { RegisterWithEmailAndPasswordUseCase(service = get() as NativeMainAuthenticationService) }
    factory { ResetPasswordUseCase(service = get() as NativeMainAuthenticationService) }
}