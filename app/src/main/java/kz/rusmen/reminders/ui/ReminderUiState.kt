package kz.rusmen.reminders.ui

import android.os.Build
import androidx.annotation.RequiresApi
import kz.rusmen.reminders.data.entity.Reminder
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

data class ReminderUiState(
    val id: Int = 0,
    val title: String = "",
    val message: String = "",
    val duration: String = "",
    val timeType: TimeType = TimeType.MINUTES,
    val isPeriodic: Boolean = false,
    val status: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val formattedDate: String = ""
)

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
    formattedDate = createdAt.toFormattedDateTime()
)

fun ReminderUiState.toReminder(): Reminder = Reminder(
    id = id,
    title = title,
    message = message,
    duration = duration.toLongOrNull() ?: 0L,
    timeUnit = timeType.name,
    isPeriodic = isPeriodic,
    createdAt = createdAt
)

@RequiresApi(Build.VERSION_CODES.O)
fun Long.toFormattedDateTime(): String {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm", Locale.getDefault())
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .format(formatter)
}
