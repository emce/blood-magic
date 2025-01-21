package mobi.cwiklinski.bloodline.common.manager

actual class BackgroundJobManager {

    actual fun enqueuePeriodicWork(
        taskId: String,
        interval: Long,
        constraints: WorkConstraints,
        initialDelay: Long,
        task: suspend () -> Unit
    ) { }

    actual fun cancelTask(taskId: String) { }

}

actual class WorkConstraints {
    @Suppress("unused")
    actual fun meetsRequirements() = false

    actual companion object {
        actual fun getDefault() = WorkConstraints()
    }
}