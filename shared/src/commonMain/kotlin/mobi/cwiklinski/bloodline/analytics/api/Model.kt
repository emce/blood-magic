package mobi.cwiklinski.bloodline.analytics.api

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import mobi.cwiklinski.bloodline.Constants.ANALYTICS_KEY_SECTION
import mobi.cwiklinski.bloodline.Constants.ANALYTICS_KEY_SUB_SECTION
import mobi.cwiklinski.bloodline.Constants.ANALYTICS_KEY_SUB_SUB_SECTION
import mobi.cwiklinski.bloodline.Constants.ANALYTICS_NO_VALUE

@Serializable
sealed class Type(val type: String) {
    @Serializable
    data object State : Type("state")

    @Serializable
    data object Action : Type("action")
}

@Serializable
sealed class Event(
    val analyticsType: Type
) {
    abstract val contextData: ContextData
    abstract val breadCrumbs: BreadCrumbs

    @Serializable
    data class View(
        val screenName: String,
        override val contextData: ContextData = ContextData(screenName),
        override val breadCrumbs: BreadCrumbs = BreadCrumbs()
    ) : Event(analyticsType = Type.State) {
        init {
            if (screenName.isEmpty()) {
                throw IllegalArgumentException("Page name cannot be empty")
            }
        }
    }

    @Serializable
    data class Action(
        val actionName: String,
        override val contextData: ContextData = ContextData(),
        override val breadCrumbs: BreadCrumbs = BreadCrumbs()
    ) : Event(analyticsType = Type.Action) {
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
    fun setBreadcrumbs(breadCrumbs: BreadCrumbs) {
        if (breadCrumbs.areNotEmpty()) {
            with(breadCrumbs) {
                if (section.isNotEmpty()) {
                    contextMap[ANALYTICS_KEY_SECTION] = section
                }
                if (subSection.isNotEmpty()) {
                    contextMap[ANALYTICS_KEY_SUB_SECTION] = subSection
                }
                if (subSubSection.isNotEmpty()) {
                    contextMap[ANALYTICS_KEY_SUB_SUB_SECTION] = subSubSection
                }
            }
        }
    }

    fun getMap(): Map<String, Any?> = contextMap.toMap()
}

@Serializable
data class BreadCrumbs(
    val section: String = ANALYTICS_NO_VALUE,
    val subSection: String = ANALYTICS_NO_VALUE,
    val subSubSection: String = ANALYTICS_NO_VALUE
) {
    fun areEmpty() = section.isEmpty() && subSection.isEmpty() && subSubSection.isEmpty()
    fun areNotEmpty() =
        section.isNotEmpty() || subSection.isNotEmpty() || subSubSection.isNotEmpty()
}