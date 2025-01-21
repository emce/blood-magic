package mobi.cwiklinski.bloodline.common.manager

import co.touchlab.kermit.Logger
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.memScoped
import kotlinx.coroutines.*
import platform.BackgroundTasks.*
import platform.Foundation.*
import kotlinx.cinterop.*

actual class BackgroundJobManager {
    private val taskOperationMap = mutableMapOf<String, NSOperationQueue>()

    @OptIn(DelicateCoroutinesApi::class)
    actual fun enqueueUniqueWork(
        taskId: String,
        constraints: WorkConstraints,
        task: suspend () -> Unit
    ) {
        BGTaskScheduler.sharedScheduler.registerForTaskWithIdentifier(
            taskId, usingQueue = null
        ) { bgTask ->
            GlobalScope.launch {
                if (constraints.meetsRequirements()) {
                    task()
                }
                bgTask?.setTaskCompletedWithSuccess(true)
            }
        }
    }

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

    actual fun enqueueParallelTasks(
        taskId: String,
        tasks: List<suspend () -> Unit>,
        onComplete: () -> Unit
    ) {
        val operationQueue = NSOperationQueue()
        val coroutineScope = CoroutineScope(Dispatchers.Default)
        tasks.forEach { task ->
            operationQueue.addOperationWithBlock {
                coroutineScope.launch {
                    task()
                }
            }
        }
        operationQueue.addOperationWithBlock {
            onComplete()
        }
        taskOperationMap[taskId] = operationQueue
    }

    actual fun cancelTask(taskId: String) {
        val queue = taskOperationMap[taskId]
        if (queue != null) {
            queue.cancelAllOperations()
            taskOperationMap.remove(taskId)
        }
    }

    actual fun cancelAllTasks() {
        taskOperationMap.forEach { (_, operationQueue) ->
            operationQueue.cancelAllOperations()
        }
        taskOperationMap.clear()
    }

}

actual class WorkConstraints constructor(
    val requiresNetwork: Boolean,
    val requiresCharging: Boolean
) {
    actual fun meetsRequirements(): Boolean {
        val isConnected = true
        val isCharging = true
        return (!requiresNetwork || isConnected) && (!requiresCharging || isCharging)
    }

    actual companion object {
        actual fun getDefault() = WorkConstraints(requiresNetwork = true, requiresCharging = false)
    }
}