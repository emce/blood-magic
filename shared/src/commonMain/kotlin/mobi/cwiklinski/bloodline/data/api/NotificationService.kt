package mobi.cwiklinski.bloodline.data.api

import kotlinx.coroutines.flow.Flow
import mobi.cwiklinski.bloodline.domain.model.Notification

interface NotificationService {
    fun getNotifications(): Flow<List<Notification>>
}