package kz.rusmen.reminders.data.repository

import kotlinx.coroutines.flow.Flow
import kz.rusmen.reminders.data.dao.ReminderDao
import kz.rusmen.reminders.data.entity.Reminder

class OfflineReminderDbRepository(private val reminderDao: ReminderDao) : ReminderDbRepository {
    override fun getAllRemindersStream(): Flow<List<Reminder>> = reminderDao.getAllReminders()

    override suspend fun insertReminder(reminder: Reminder): Long = reminderDao.insertReminder(reminder)

    override suspend fun deleteReminder(reminder: Reminder) = reminderDao.deleteReminder(reminder)

    override suspend fun deleteReminderById(id: Int) = reminderDao.deleteReminderById(id)
}
