package kz.rusmen.reminders.data.dao

import androidx.annotation.Keep
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kz.rusmen.reminders.data.entity.Reminder

@Keep // Эта аннотация от AndroidX говорит R8: "Не трогай этот класс и его поля"
@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminder ORDER BY id DESC")
    fun getAllReminders(): Flow<List<Reminder>>

    @Query("SELECT * FROM reminder WHERE id = :id")
    fun getReminderById(id: Int): Flow<Reminder?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertReminder(reminder: Reminder): Long

    @Update
    suspend fun updateReminder(reminder: Reminder)

    @Delete
    suspend fun deleteReminder(reminder: Reminder)

    @Query("DELETE FROM reminder " +
            "WHERE id = :id")
    suspend fun deleteReminderById(id: Int)
}
