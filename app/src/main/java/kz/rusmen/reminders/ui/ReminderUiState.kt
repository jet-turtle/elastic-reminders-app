package kz.rusmen.reminders.ui

import kz.rusmen.reminders.data.entity.Reminder

data class ReminderUiState(
    val id: Int = 0,
    val title: String = "",
    val message: String = "",
    val duration: String = "",
    val timeType: TimeType = TimeType.MINUTES,
    val isPeriodic: Boolean = false,
    val status: String = ""
)

enum class TimeType(val title: String) {
    MINUTES(title = "minutes"),
    HOURS(title = "hours"),
    DAYS(title = "days")
}

fun Reminder.toReminderUiState(): ReminderUiState = ReminderUiState(
    id = id,
    title = title,
    message = message,
    duration = duration.toString(),
    timeType = TimeType.valueOf(timeUnit),
    isPeriodic = isPeriodic
)

fun ReminderUiState.toReminder(): Reminder = Reminder(
    id = id,
    title = title,
    message = message,
    duration = duration.toLongOrNull() ?: 0L,
    timeUnit = timeType.name,
    isPeriodic = isPeriodic
)
