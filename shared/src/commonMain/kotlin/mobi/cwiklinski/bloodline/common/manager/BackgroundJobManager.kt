package mobi.cwiklinski.bloodline.common.manager

expect class BackgroundJobManager {

    fun enqueuePeriodicWork(
        taskId: String,
        interval: Long, // ms
        constraints: WorkConstraints,
        initialDelay: Long, // ms
        task: suspend () -> Unit
    )

    fun cancelTask(taskId: String)
}

expect class WorkConstraints {
    fun meetsRequirements(): Boolean

    companion object {
        fun getDefault(): WorkConstraints
    }
}