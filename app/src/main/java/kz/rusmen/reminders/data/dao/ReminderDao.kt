package kz.rusmen.reminders.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kz.rusmen.reminders.data.entity.Reminder

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminder ORDER BY id DESC")
    fun getAllReminders(): Flow<List<Reminder>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertReminder(reminder: Reminder): Long

    @Delete
    suspend fun deleteReminder(reminder: Reminder)

    @Query("DELETE FROM reminder " +
            "WHERE id = :id")
    suspend fun deleteReminderById(id: Int)
}
