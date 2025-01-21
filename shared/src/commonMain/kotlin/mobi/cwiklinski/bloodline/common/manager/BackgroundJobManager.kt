package mobi.cwiklinski.bloodline.common.manager

expect class BackgroundJobManager {
    fun enqueueUniqueWork(
        taskId: String,
        constraints: WorkConstraints,
        task: suspend () -> Unit
    )

    fun enqueuePeriodicWork(
        taskId: String,
        interval: Long, // ms
        constraints: WorkConstraints,
        initialDelay: Long, // ms
        task: suspend () -> Unit
    )

    fun enqueueParallelTasks(
        taskId: String,
        tasks: List<suspend () -> Unit>,
        onComplete: () -> Unit
    )

    fun cancelTask(taskId: String)

    fun cancelAllTasks()
}

expect class WorkConstraints {
    fun meetsRequirements(): Boolean

    companion object {
        fun getDefault(): WorkConstraints
    }
}