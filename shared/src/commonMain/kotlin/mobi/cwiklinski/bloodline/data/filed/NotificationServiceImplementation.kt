package mobi.cwiklinski.bloodline.data.filed

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import mobi.cwiklinski.bloodline.data.api.NotificationService
import mobi.cwiklinski.bloodline.domain.model.Notification

class NotificationServiceImplementation : NotificationService {

    private val _memory = mutableListOf<Notification>()

    init {
        _memory.addAll(DummyData.NOTIFICATIONS)
    }

    override fun getNotifications(): Flow<List<Notification>> = flowOf(_memory)
}