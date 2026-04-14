package kz.rusmen.reminders.ui

import android.os.Build
import androidx.annotation.Keep
import androidx.annotation.RequiresApi
import kz.rusmen.reminders.data.entity.Reminder
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit

data class ReminderUiState(
    val id: Int = 0,
    val title: String = "",
    val message: String = "",
    val duration: String = "",
    val timeType: TimeType = TimeType.MINUTES,
    val isPeriodic: Boolean = false,
    val status: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val nextRunAt: Long = 0L,
    val createdAtFormattedDate: String = "",
    val nextRunAtFormattedDate: String = ""
)

@Keep
enum class TimeType(val title: String) {
    MINUTES(title = "minutes"),
    HOURS(title = "hours"),
    DAYS(title = "days")
}

@RequiresApi(Build.VERSION_CODES.O)
fun Reminder.toReminderUiState(): ReminderUiState = ReminderUiState(
    id = id,
    title = title,
    message = message,
    duration = duration.toString(),
    timeType = TimeType.entries.find { it.name == timeUnit } ?: TimeType.MINUTES,
    isPeriodic = isPeriodic,
    createdAt = createdAt,
    nextRunAt = nextRunAt,
    createdAtFormattedDate = createdAt.toFormattedDateTime(),
    nextRunAtFormattedDate = nextRunAt.toFormattedDateTime()
)

fun ReminderUiState.toReminder(): Reminder {
    // Превращаем строку duration и TimeType в честные миллисекунды
    val durationLong = duration.toLongOrNull() ?: 0L
    val durationInMs = timeType.toTimeUnit().toMillis(durationLong)

    return Reminder(
        id = id,
        title = title,
        message = message,
        duration = durationLong,
        timeUnit = timeType.name,
        isPeriodic = isPeriodic,
        createdAt = createdAt,
        // Если это новое напоминание, считаем от createdAt,
        // если обновляем старое — берем из стейта
        nextRunAt = if (nextRunAt == 0L) createdAt + durationInMs else nextRunAt
    )
}

// Полезный helper для Enum
fun TimeType.toTimeUnit(): TimeUnit = when (this) {
    TimeType.MINUTES -> TimeUnit.MINUTES
    TimeType.HOURS -> TimeUnit.HOURS
    TimeType.DAYS -> TimeUnit.DAYS
}

@RequiresApi(Build.VERSION_CODES.O)
fun Long.toFormattedDateTime(): String {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm", Locale.getDefault())
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .format(formatter)
}
