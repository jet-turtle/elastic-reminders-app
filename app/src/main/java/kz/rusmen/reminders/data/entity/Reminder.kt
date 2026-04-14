package kz.rusmen.reminders.data.entity

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep // Эта аннотация от AndroidX говорит R8: "Не трогай этот класс и его поля"
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
    val createdAt: Long,
    @ColumnInfo(name = "next_run_at")
    val nextRunAt: Long
)
