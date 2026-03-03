package kz.rusmen.reminders.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminder")
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val message: String,
    val duration: Long,
    val timeUnit: String, // MINUTES, HOURS, etc.
    val isPeriodic: Boolean
)
