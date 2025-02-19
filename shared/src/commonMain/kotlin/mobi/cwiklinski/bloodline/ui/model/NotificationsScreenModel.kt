package mobi.cwiklinski.bloodline.ui.model

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import mobi.cwiklinski.bloodline.common.isAfter
import mobi.cwiklinski.bloodline.common.manager.CallbackManager
import mobi.cwiklinski.bloodline.data.api.NotificationService
import mobi.cwiklinski.bloodline.storage.api.StorageService
import mobi.cwiklinski.bloodline.ui.util.getLatestRead

class NotificationScreenModel(
    callbackManager: CallbackManager,
    val notificationService: NotificationService,
    val storageService: StorageService
) : AppModel<NotificationState>(NotificationState.Idle, callbackManager) {

    private val _latestRead = MutableStateFlow<LocalDate>(LocalDate(2000, 1, 1))

    val notifications = combine(
        notificationService.getNotifications(),
        _latestRead
    ) { notifications, read ->
        notifications.filter { it.date.isAfter(read) }
    }

    init {
        bootstrap()
        screenModelScope.launch {
            _latestRead.emit(storageService.getLatestRead())
        }
    }

    fun resetState() {
        mutableState.value = NotificationState.Idle
    }
}

enum class NotificationError {
    ERROR
}

sealed class NotificationState {
    data object Idle : NotificationState()
    data class Error(val error: NotificationError) : NotificationState()
}