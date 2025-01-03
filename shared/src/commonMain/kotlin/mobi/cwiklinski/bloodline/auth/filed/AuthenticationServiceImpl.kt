package mobi.cwiklinski.bloodline.auth.filed

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import mobi.cwiklinski.bloodline.auth.api.AuthError
import mobi.cwiklinski.bloodline.auth.api.AuthResult
import mobi.cwiklinski.bloodline.auth.api.AuthenticationService
import mobi.cwiklinski.bloodline.auth.api.AuthenticationState
import mobi.cwiklinski.bloodline.storage.api.StorageService

class AuthenticationServiceImpl(private val storageService: StorageService) :
    AuthenticationService {

    private val _authenticationState =
        MutableStateFlow<AuthenticationState>(value = AuthenticationState.Idle)
    override val authenticationState: Flow<AuthenticationState> = _authenticationState

    private val _dataSessionKey = "__session"
    private val _dataUsersKey = "__users"

    init {
        runBlocking {
            saveUsers()
            _authenticationState.value = if (storageService.getBoolean(_dataSessionKey, false))
                AuthenticationState.Logged else AuthenticationState.NotLogged
        }

    }

    override fun loginWithEmailAndPassword(email: String, password: String): Flow<AuthResult> =
        flow {
            val users = Json.decodeFromString<Map<String, String>>(
                storageService.getString(
                    _dataUsersKey,
                    AuthData.users.toString()
                )
            )
            if (users.containsKey(email)) {
                if (users[email] == password) {
                    storageService.storeBoolean(_dataSessionKey, true)
                    storageService.storeProfile(AuthData.generateProfile(
                        email = email
                    ))
                    _authenticationState.value = AuthenticationState.Logged
                        emit(AuthResult.Success())
                } else {
                    emit(AuthResult.Failure(AuthError.INCORRECT_PASSWORD))
                }
            } else {
                emit(AuthResult.Failure(AuthError.INCORRECT_EMAIL))
            }
        }

    override fun registerWithEmailAndPassWord(email: String, password: String) =
        flow {
            saveUsers(email to password)
            emit(loginWithEmailAndPassword(email, password).first())
        }

    override fun logOut(): Flow<Boolean> = flow {
        storageService.deleteBoolean(_dataSessionKey)
        storageService.deleteProfile()
        _authenticationState.value = AuthenticationState.NotLogged
        emit(!storageService.exists(_dataSessionKey))
    }

    override fun resetPassword(email: String) = flow<AuthResult> {
        AuthResult.Success(true)
    }

    private suspend fun saveUsers(newUser: Pair<String, String>? = null) {
        val users = mutableMapOf<String, String>()
        users.putAll(AuthData.users)
        newUser?.let {
            users.put(it.first, it.second)
        }
        storageService.storeString(
            _dataUsersKey,
            Json.encodeToString(
                MapSerializer(String.serializer(), String.serializer()),
                users
            )
        )
    }

    override fun removeAccount() = flow {
        emit(true)
    }

    override fun loginWithFacebook() = flow<AuthResult> {
        AuthResult.Success(true)
    }

    override fun loginWithGoogle() = flow<AuthResult> {
        AuthResult.Success(true)
    }

    override fun loginWithApple() = flow<AuthResult> {
        AuthResult.Success(true)
    }
}