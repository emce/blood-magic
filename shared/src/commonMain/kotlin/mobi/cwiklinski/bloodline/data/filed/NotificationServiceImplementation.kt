package mobi.cwiklinski.bloodline.data.filed

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.LocalDate
import mobi.cwiklinski.bloodline.common.Either
import mobi.cwiklinski.bloodline.data.api.NotificationService
import mobi.cwiklinski.bloodline.domain.model.Notification

class NotificationServiceImplementation : NotificationService {

    private val _memory = mutableListOf<Notification>()

    init {
        _memory.addAll(DummyData.NOTIFICATIONS)
    }

    override fun getNotifications(): Flow<List<Notification>> = flowOf(_memory)

    override fun getNotification(id: String): Flow<Notification> = flowOf(_memory.first { it.id == id })

    override fun addNotification(
        date: LocalDate,
        location: String,
        title: String,
        message: String,
        type: Int
    ) = flow<Either<Notification, Throwable>> {
        val id = DummyData.generateString()
        val notification = Notification(
            id = id,
            date = date,
            type = type,
            title = title,
            message = message,
            location = location
        )
        _memory.add(notification)
        emit(Either.Left(notification))
    }

    override fun updateNotification(
        id: String,
        date: LocalDate,
        location: String,
        title: String,
        message: String,
        type: Int
    ) = flow<Either<Notification, Throwable>> {
        val notification = _memory.firstOrNull { it.id == id }
        if (notification == null) {
            emit(Either.Right(RuntimeException()))
        } else {
            val updatedNotification = Notification(
                id = id,
                date = date,
                type = type,
                title = title,
                message = message,
                location = location
            )
            _memory.removeAll { it.id == id }
            _memory.add(updatedNotification)
            emit(Either.Left(updatedNotification))
        }
    }

    override fun deleteNotification(id: String) = flow<Either<Boolean, Throwable>> {
        _memory.removeAll { it.id == id }
        emit(Either.Left(true))
    }
}