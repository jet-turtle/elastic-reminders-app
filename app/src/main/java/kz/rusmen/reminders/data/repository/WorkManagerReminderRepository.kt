package kz.rusmen.reminders.data.repository

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
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
        val duration = reminder.duration
        //val timeUnit = TimeUnit.valueOf(reminder.timeUnit)
        val timeUnit = try {
            TimeUnit.valueOf(reminder.timeUnit.uppercase())
        } catch (e: Exception) {
            TimeUnit.MINUTES
        }
        val uniqueName = "reminder_${reminder.id}"

        val workData = workDataOf(
            "TITLE" to reminder.title,
            "MESSAGE" to reminder.message
        )

        if (reminder.isPeriodic) {
            val periodicRequest = PeriodicWorkRequestBuilder<ReminderWorker>(duration, timeUnit)
                .setInputData(workData)
                .build()

            workManager.enqueueUniquePeriodicWork(
                uniqueName,
                ExistingPeriodicWorkPolicy.UPDATE,
                periodicRequest
            )
        } else {
            val oneTimeRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
                .setInitialDelay(duration, timeUnit)
                .setInputData(workData)
                .build()

            workManager.enqueueUniqueWork(
                uniqueName,
                ExistingWorkPolicy.REPLACE,
                oneTimeRequest
            )
        }
    }

    override fun cancelReminder(reminderName: String) {
        workManager.cancelUniqueWork(reminderName)
    }
}
