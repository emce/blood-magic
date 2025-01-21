package mobi.cwiklinski.bloodline.common.manager

actual class BackgroundJobManager {
    actual fun enqueueUniqueWork(
        taskId: String,
        constraints: WorkConstraints,
        task: suspend () -> Unit
    ) {
    }

    actual fun enqueuePeriodicWork(
        taskId: String,
        interval: Long,
        constraints: WorkConstraints,
        initialDelay: Long,
        task: suspend () -> Unit
    ) {

    }

    actual fun enqueueParallelTasks(
        taskId: String,
        tasks: List<suspend () -> Unit>,
        onComplete: () -> Unit
    ) {

    }

    actual fun cancelTask(taskId: String) {

    }

    actual fun cancelAllTasks() {

    }

}

actual class WorkConstraints {
    actual fun meetsRequirements() = false

    actual companion object {
        actual fun getDefault() = WorkConstraints()
    }
}