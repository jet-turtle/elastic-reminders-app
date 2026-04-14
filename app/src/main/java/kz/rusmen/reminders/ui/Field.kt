package kz.rusmen.reminders.ui

import androidx.annotation.Keep

@Keep // Эта аннотация от AndroidX говорит R8: "Не трогай этот класс и его поля"
enum class Field {
    TITLE,
    MESSAGE,
    DURATION
}
