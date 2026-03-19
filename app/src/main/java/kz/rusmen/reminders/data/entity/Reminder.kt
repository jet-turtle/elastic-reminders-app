package kz.rusmen.reminders.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminder")
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val message: String,
    val duration: Long,
    @ColumnInfo(name = "time_unit")
    val timeUnit: String, // MINUTES, HOURS, etc.
    @ColumnInfo(name = "is_periodic")
    val isPeriodic: Boolean,
    @ColumnInfo(name = "created_at")
    val createdAt: Long
)
