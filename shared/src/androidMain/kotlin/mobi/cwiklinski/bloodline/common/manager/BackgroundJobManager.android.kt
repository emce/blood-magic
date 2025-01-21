package mobi.cwiklinski.bloodline.common.manager

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import java.util.concurrent.TimeUnit

actual class BackgroundJobManager(private val workManager: WorkManager) {

    actual fun enqueueUniqueWork(
        taskId: String,
        constraints: WorkConstraints,
        task: suspend () -> Unit
    ) {
        val workRequest = OneTimeWorkRequestBuilder<TaskWorker>()
            .setConstraints(toWorkConstraints(constraints))
            .build()

        workManager.enqueueUniqueWork(taskId, ExistingWorkPolicy.REPLACE, workRequest)
    }

    actual fun enqueuePeriodicWork(
        taskId: String,
        interval: Long,
        constraints: WorkConstraints,
        initialDelay: Long,
        task: suspend () -> Unit
    ) {
        val periodicWorkRequest = PeriodicWorkRequestBuilder<TaskWorker>(
            interval, TimeUnit.MILLISECONDS
        )
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setConstraints(toWorkConstraints(constraints))
            .build()

        workManager.enqueueUniquePeriodicWork(taskId, ExistingPeriodicWorkPolicy.UPDATE, periodicWorkRequest)
    }

    actual fun enqueueParallelTasks(
        taskId: String,
        tasks: List<suspend () -> Unit>,
        onComplete: () -> Unit
    ) {
        val workRequests = List(tasks.size) { index ->
            val taskData = workDataOf("TASK_ID" to "$taskId-task-$index")

            OneTimeWorkRequestBuilder<TaskWorker>()
                .setInputData(taskData)
                .build()
        }

        val continuation = workManager.beginWith(workRequests)

        continuation.enqueue()
        continuation.then(
            OneTimeWorkRequestBuilder<OnCompleteWorker>()
                .build()
        ).enqueue()
    }

    actual fun cancelTask(taskId: String) {
        workManager.cancelUniqueWork(taskId)
    }

    actual fun cancelAllTasks() {
        workManager.cancelAllWork()
    }

    private fun toWorkConstraints(constraints: WorkConstraints): Constraints {
        val builder = Constraints.Builder()
        if (constraints.requiresNetwork) {
            builder.setRequiredNetworkType(NetworkType.CONNECTED)
        }
        if (constraints.requiresCharging) {
            builder.setRequiresCharging(true)
        }
        return builder.build()
    }

}

class TaskWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val taskId = inputData.getString("TASK_ID") ?: return Result.failure()
        try {
            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }
}

class OnCompleteWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        println("All tasks completed")
        return Result.success()
    }
}

actual class WorkConstraints(
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