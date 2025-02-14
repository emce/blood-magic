package mobi.cwiklinski.bloodline.ui.model

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import mobi.cwiklinski.bloodline.Constants
import mobi.cwiklinski.bloodline.common.Either
import mobi.cwiklinski.bloodline.common.isAfter
import mobi.cwiklinski.bloodline.common.manager.CallbackManager
import mobi.cwiklinski.bloodline.common.today
import mobi.cwiklinski.bloodline.data.api.NotificationService
import mobi.cwiklinski.bloodline.domain.NotificationType
import mobi.cwiklinski.bloodline.domain.model.Notification
import mobi.cwiklinski.bloodline.storage.api.StorageService
import mobi.cwiklinski.bloodline.ui.util.fillWithRead
import mobi.cwiklinski.bloodline.ui.util.getReadList

class NotificationScreenModel(
    callbackManager: CallbackManager,
    val notificationService: NotificationService,
    val storageService: StorageService
) : AppModel<NotificationState>(NotificationState.Idle, callbackManager) {

    private val _notificationsRead = MutableStateFlow<List<String>>(emptyList())

    val notifications = combine(
        notificationService.getNotifications(),
        _notificationsRead, { notifications, readList ->
            notifications.fillWithRead(readList)
        }
    )

    init {
        bootstrap()
        screenModelScope.launch {
            _notificationsRead.emit(storageService.getReadList())
        }
    }

    fun addNotification(
        date: LocalDate,
        location: String,
        type: NotificationType,
        title: String,
        message: String
    ) {
        mutableState.value = NotificationState.Saving
        validateNotification(date, location, title, message) {
            screenModelScope.launch {
                notificationService.addNotification(
                    date,
                    location,
                    title,
                    message,
                    type
                )
                    .collectLatest {
                        when (it) {
                            is Either.Left -> {
                                mutableState.value = NotificationState.Saved
                            }

                            is Either.Right -> {
                                mutableState.value =
                                    NotificationState.Error(NotificationError.ERROR)
                            }
                        }
                    }
            }
        }
    }

    fun updateNotification(
        id: String,
        date: LocalDate,
        location: String,
        type: NotificationType,
        title: String,
        message: String
    ) {
        mutableState.value = NotificationState.Saving
        validateNotification(date, location, title, message) {
            screenModelScope.launch {
                notificationService.updateNotification(
                    id,
                    date,
                    location,
                    title,
                    message,
                    type
                )
                    .collectLatest {
                        when (it) {
                            is Either.Left -> {
                                mutableState.value = NotificationState.Saved
                            }

                            is Either.Right -> {
                                mutableState.value =
                                    NotificationState.Error(NotificationError.ERROR)
                            }
                        }
                    }
            }
        }
    }

    fun markToDeletion(notification: Notification) {
        mutableState.value = NotificationState.ToDelete(notification)
    }

    fun deleteNotification(notification: Notification) {
        mutableState.value = NotificationState.Saving
        screenModelScope.launch {
            notificationService.deleteNotification(notification.id)
                .collectLatest {
                    when (it) {
                        is Either.Left -> {
                            mutableState.value = NotificationState.Deleted
                        }

                        is Either.Right -> {
                            mutableState.value = NotificationState.Error(NotificationError.DELETION_ERROR)
                        }
                    }
                }
        }
    }

    fun markAsRead(notification: Notification) {
        mutableState.value = NotificationState.MarkingAsRead(notification)
        screenModelScope.launch {
            val alreadyRead = storageService.getReadList()
            if (alreadyRead.contains(notification.id)) {
                mutableState.value = NotificationState.AlreadyMarked(notification)
            } else {
                val newReadList = alreadyRead.toMutableList()
                newReadList.add(notification.id)
                storageService.storeString(Constants.NOTIFICATIONS_READ, Json.encodeToString(newReadList))
                val newList = storageService.getReadList()
                if (newList.contains(notification.id)) {
                    _notificationsRead.emit(newList)
                    mutableState.value = NotificationState.MarkedAsRead(notification)
                } else {
                    mutableState.value = NotificationState.MarkingAsRead(notification)
                }
            }
        }
    }

    fun markAllAsRead() {
        mutableState.value = NotificationState.MarkingAllAsRead
        screenModelScope.launch {
            notificationService.getNotifications().collectLatest { list ->
                storageService.storeString(Constants.NOTIFICATIONS_READ, Json.encodeToString(list.map { it.id }))
                val newList = storageService.getReadList()
                if (newList.size == list.size) {
                    _notificationsRead.emit(newList)
                    mutableState.value = NotificationState.MarkedAllAsRead
                } else {
                    mutableState.value = NotificationState.MarkingAllError
                }
            }
        }
    }

    fun setForMarkAllAsRead() {
        mutableState.value = NotificationState.SetMarkingAllAsRead
    }

    fun resetState() {
        mutableState.value = NotificationState.Idle
    }

    private fun validateNotification(
        date: LocalDate,
        location: String,
        title: String,
        message: String,
        follow: () -> Unit
    ) {
        if (!date.isAfter(today())) {
            if (location.isEmpty()) {
                if (title.isEmpty()) {
                    if (message.isEmpty()) {
                        follow.invoke()
                    } else {
                        mutableState.value = NotificationState.Error(NotificationError.MESSAGE_ERROR)
                    }
                } else {
                    mutableState.value = NotificationState.Error(NotificationError.TITLE_ERROR)
                }
            } else {
                mutableState.value = NotificationState.Error(NotificationError.NO_LOCATION_ERROR)
            }
        } else {
            mutableState.value = NotificationState.Error(NotificationError.DATE_IN_FUTURE_ERROR)
        }
    }
}

enum class NotificationError {
    DATE_IN_FUTURE_ERROR,
    NO_LOCATION_ERROR,
    TITLE_ERROR,
    MESSAGE_ERROR,
    DELETION_ERROR,
    ERROR
}

sealed class NotificationState {
    data object Idle : NotificationState()
    data class MarkingAsRead(val notification: Notification) : NotificationState()
    data class MarkedAsRead(val notification: Notification) : NotificationState()
    data class AlreadyMarked(val notification: Notification) : NotificationState()
    data object SetMarkingAllAsRead : NotificationState()
    data object MarkingAllAsRead : NotificationState()
    data object MarkedAllAsRead : NotificationState()
    data object MarkingAllError : NotificationState()
    data object Saving : NotificationState()
    data object Saved : NotificationState()
    data class ToDelete(val notification: Notification) : NotificationState()
    data object Deleted : NotificationState()
    data class Error(val error: NotificationError) : NotificationState()
}