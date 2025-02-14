package mobi.cwiklinski.bloodline.data.firebase

import dev.gitlive.firebase.FirebaseException
import dev.gitlive.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import mobi.cwiklinski.bloodline.common.Either
import mobi.cwiklinski.bloodline.data.api.NotificationService
import mobi.cwiklinski.bloodline.data.firebase.model.FirebaseNotification
import mobi.cwiklinski.bloodline.domain.NotificationType
import mobi.cwiklinski.bloodline.domain.model.Notification

class NotificationServiceImplementation(db: FirebaseDatabase) : NotificationService {

    private val mainRef = db.reference("notification")

    override fun getNotifications() = mainRef
        .valueEvents
        .map { events ->
            events.value<Map<String, FirebaseNotification>>().values.map { it.toNotification() }
                .toList().sortedByDescending { it.date }
        }

    override fun getNotification(id: String) = mainRef
        .child(id)
        .valueEvents
        .map { events ->
            events.value<Map<String, FirebaseNotification>>().values.map { it.toNotification() }.first()
        }

    override fun addNotification(
        date: LocalDate,
        location: String,
        title: String,
        message: String,
        type: NotificationType
    ) = flow<Either<Notification, Throwable>> {
        try {
            val newRef = mainRef.push()
            val id = newRef.key
            if (id != null) {
                try {
                    newRef.setValue(
                        FirebaseNotification(
                            id,
                            type.type,
                            date.year,
                            date.monthNumber,
                            date.dayOfMonth,
                            title,
                            message,
                            location
                        )
                    )
                } finally {
                    emit(
                        Either.Left(
                            Notification(
                                id,
                                type,
                                date,
                                title,
                                message,
                                location
                            )
                        )
                    )
                }
            } else {
                emit(Either.Right(Exception()))
            }
        } catch (e: FirebaseException) {
            emit(Either.Right(e))
        }
    }

    override fun updateNotification(
        id: String,
        date: LocalDate,
        location: String,
        title: String,
        message: String,
        type: NotificationType
    ) = flow<Either<Notification, Throwable>> {
        try {
            val updatedNotification = FirebaseNotification(
                id,
                type.type,
                date.year,
                date.monthNumber,
                date.dayOfMonth,
                title,
                message,
                location
            )
            mainRef
                .child(id)
                .setValue(updatedNotification)
            emit(Either.Left(updatedNotification.toNotification()))
        } catch (e: FirebaseException) {
            emit(Either.Right(e))
        }
    }

    override fun deleteNotification(id: String) = flow {
        try {
            mainRef
                .child(id)
                .removeValue()
            emit(Either.Left(true))
        } catch (e: FirebaseException) {
            emit(Either.Right(e))
        }
    }
}