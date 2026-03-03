package kz.rusmen.reminders.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kz.rusmen.reminders.data.dao.ReminderDao
import kz.rusmen.reminders.data.entity.Reminder

@Database(
    entities = [
        Reminder::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ReminderDatabase : RoomDatabase() {

    abstract fun reminderDao(): ReminderDao

    companion object {
        @Volatile
        private var Instance: ReminderDatabase? = null

        fun getDatabase(context: Context): ReminderDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    ReminderDatabase::class.java,
                    "reminder_database"
                )
                    //.createFromAsset("reminder.db")
                    //.fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
