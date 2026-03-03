package kz.rusmen.reminders.data.repository

import kotlinx.coroutines.flow.Flow
import kz.rusmen.reminders.data.entity.Reminder

interface ReminderDbRepository {
    fun getAllRemindersStream(): Flow<List<Reminder>>

    suspend fun insertReminder(reminder: Reminder): Long

    suspend fun deleteReminder(reminder: Reminder)

    suspend fun deleteReminderById(id: Int)
}
