package mobi.cwiklinski.bloodline.data.api

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import mobi.cwiklinski.bloodline.common.Either
import mobi.cwiklinski.bloodline.domain.NotificationType
import mobi.cwiklinski.bloodline.domain.model.Notification

interface NotificationService {
    fun getNotifications(): Flow<List<Notification>>

    fun getNotification(id: String): Flow<Notification>

    fun addNotification(
        date: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
        location: String = "Polska",
        title: String = "",
        message: String = "",
        type: NotificationType = NotificationType.STANDARD
    ): Flow<Either<Notification, Throwable>>

    fun updateNotification(
        id: String,
        date: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
        location: String = "Polska",
        title: String = "",
        message: String = "",
        type: NotificationType = NotificationType.STANDARD
    ): Flow<Either<Notification, Throwable>>

    fun deleteNotification(id: String): Flow<Either<Boolean, Throwable>>
}