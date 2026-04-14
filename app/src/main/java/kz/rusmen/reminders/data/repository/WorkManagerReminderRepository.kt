package kz.rusmen.reminders.data.repository

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kz.rusmen.reminders.data.entity.Reminder
import kz.rusmen.reminders.worker.ReminderWorker
import java.util.concurrent.TimeUnit

class WorkManagerReminderRepository(context: Context) : ReminderWorkerRepository {
    private val workManager = WorkManager.getInstance(context)

    override fun getWorkInfo(id: Int): Flow<WorkInfo?> {
        return workManager.getWorkInfosForUniqueWorkFlow("reminder_$id")
            .map { list -> list.firstOrNull() }
    }

    override fun scheduleReminder(reminder: Reminder) {
        val uniqueName = "reminder_${reminder.id}"

        val currentTime = System.currentTimeMillis()
        val initialDelay = (reminder.nextRunAt - currentTime).coerceAtLeast(0)

        val workData = workDataOf(
            "ID" to reminder.id,
//            "TITLE" to reminder.title,
//            "MESSAGE" to reminder.message
        )

        val request = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setInputData(workData)
            .build()

        workManager.enqueueUniqueWork(
            uniqueName,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    override fun cancelReminder(reminderName: String) {
        workManager.cancelUniqueWork(reminderName)
    }
}
