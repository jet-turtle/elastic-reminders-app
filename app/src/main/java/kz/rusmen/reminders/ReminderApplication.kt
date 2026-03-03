package kz.rusmen.reminders

import android.app.Application
import kz.rusmen.reminders.data.AppContainer
import kz.rusmen.reminders.data.DefaultAppContainer

class ReminderApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
