package kz.rusmen.reminders.worker

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class ReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun doWork(): Result {

        val message = inputData.getString(messageKey)
        val title = inputData.getString(titleKey)

        makeReminderNotification(
            title = title ?: "",
            message = message ?: "",
            applicationContext
        )

        return Result.success()
    }

    companion object {
        const val messageKey = "MESSAGE"
        const val titleKey = "TITLE"
    }
}
