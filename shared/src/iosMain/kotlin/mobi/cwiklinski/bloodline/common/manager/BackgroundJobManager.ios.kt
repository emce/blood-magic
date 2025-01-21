package mobi.cwiklinski.bloodline.common.manager

import co.touchlab.kermit.Logger
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.memScoped
import platform.BackgroundTasks.*
import platform.Foundation.*
import kotlinx.cinterop.*

actual class BackgroundJobManager {
    private val taskOperationMap = mutableMapOf<String, NSOperationQueue>()

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    actual fun enqueuePeriodicWork(
        taskId: String,
        interval: Long,
        constraints: WorkConstraints,
        initialDelay: Long,
        task: suspend () -> Unit
    ) {
        val request = BGProcessingTaskRequest(taskId)
        request.earliestBeginDate = NSDate().dateByAddingTimeInterval(interval + initialDelay / 1000.0)

        memScoped {
            val errorPtr = alloc<ObjCObjectVar<NSError?>>()
            val success = BGTaskScheduler.sharedScheduler.submitTaskRequest(request, errorPtr.ptr)
            if (!success) {
                val error = errorPtr.value
                Logger.d("Error scheduling task: ${error?.localizedDescription ?: "Unknown error"}")
            } else {
                Logger.d("Task scheduled successfully.")
            }
        }
    }

    actual fun cancelTask(taskId: String) {
        val queue = taskOperationMap[taskId]
        if (queue != null) {
            queue.cancelAllOperations()
            taskOperationMap.remove(taskId)
        }
    }

}

actual class WorkConstraints constructor(
    val requiresNetwork: Boolean,
    val requiresCharging: Boolean
) {
    @Suppress("unused")
    actual fun meetsRequirements(): Boolean {
        val isConnected = true
        val isCharging = true
        return (!requiresNetwork || isConnected) && (!requiresCharging || isCharging)
    }

    actual companion object {
        actual fun getDefault() = WorkConstraints(requiresNetwork = true, requiresCharging = false)
    }
}