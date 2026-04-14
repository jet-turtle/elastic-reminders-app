package kz.rusmen.reminders.worker

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.flow.first
import kz.rusmen.reminders.ReminderApplication
import kz.rusmen.reminders.data.repository.ReminderDbRepository
import kz.rusmen.reminders.data.repository.ReminderWorkerRepository
import java.util.concurrent.TimeUnit

class ReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    // Достаем зависимости из DefaultAppContainer
    // Приводим context.applicationContext к классу ReminderApplication
    private val container = (context.applicationContext as ReminderApplication).container

    private val reminderDbRepository = container.reminderDbRepository
    private val reminderWorkerRepository = container.reminderWorkerRepository

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun doWork(): Result {

        val id = inputData.getInt(idKey, -1)
        if (id == -1) return Result.failure()

        val reminderFlow = reminderDbRepository.getReminderByIdStream(id)
        val reminder = reminderFlow.first() ?: return Result.failure()

        makeReminderNotification(
            title = reminder.title ?: "",
            message = reminder.message ?: "",
            applicationContext
        )

        if (reminder.isPeriodic) {
            val currentTime = System.currentTimeMillis()
            val timeUnit = try {
                TimeUnit.valueOf(reminder.timeUnit.uppercase())
            } catch (e: Exception) {
                TimeUnit.MINUTES
            }
            val durationMs = timeUnit.toMillis(reminder.duration)
            var newNextRunAt = reminder.nextRunAt + durationMs

            // КЛЮЧЕВОЙ МОМЕНТ: считаем от старого идеального времени, а не от System.currentTimeMillis()
            while (currentTime >= newNextRunAt) {
                newNextRunAt += durationMs
            }

            // Обновляем время в БД
            val updatedReminder = reminder.copy(nextRunAt = newNextRunAt)
            reminderDbRepository.updateReminder(updatedReminder)

            // Перезапускаем воркер
            reminderWorkerRepository.scheduleReminder(updatedReminder)
        }

        return Result.success()
    }

    companion object {
        const val idKey = "ID"
//        const val messageKey = "MESSAGE"
//        const val titleKey = "TITLE"
    }
}
