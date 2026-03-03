package kz.rusmen.reminders.data.repository

import androidx.work.WorkInfo
import kotlinx.coroutines.flow.Flow
import kz.rusmen.reminders.data.entity.Reminder

interface ReminderRepository {
    fun getWorkInfo(id: Int): Flow<WorkInfo?>
    fun scheduleReminder(reminder: Reminder)
    fun cancelReminder(reminderName: String)
}
