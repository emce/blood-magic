package mobi.cwiklinski.bloodline.analytics.api

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import mobi.cwiklinski.bloodline.Constants.ANALYTICS_NO_VALUE

@Serializable
sealed class Event {
    abstract val contextData: ContextData

    @Serializable
    data class View(
        val screenName: String,
        override val contextData: ContextData = ContextData(screenName)
    ) : Event() {
        init {
            if (screenName.isEmpty()) {
                throw IllegalArgumentException("Page name cannot be empty")
            }
        }
    }

    @Serializable
    data class Action(
        val actionName: String,
        override val contextData: ContextData = ContextData()
    ) : Event() {
        init {
            if (actionName.isEmpty()) {
                throw IllegalArgumentException("Action name cannot be empty")
            }
        }
    }
}

@Serializable
class ContextData(
    private val screenName: String = ANALYTICS_NO_VALUE,
    private val contextMap: MutableMap<String, @Contextual Any?> = mutableMapOf()
) {

    fun getMap(): Map<String, Any?> = contextMap.toMap()
}