package kz.rusmen.reminders.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kz.rusmen.reminders.ReminderApplication
import kz.rusmen.reminders.data.repository.ReminderDbRepository
import kz.rusmen.reminders.data.repository.ReminderWorkerRepository

class ReminderViewModel(
    private val reminderWorkerRepository: ReminderWorkerRepository,
    private val reminderDbRepository: ReminderDbRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReminderUiState())
    val uiState = _uiState.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalCoroutinesApi::class)
    val allReminders: StateFlow<List<ReminderUiState>> = reminderDbRepository.getAllRemindersStream()
        .flatMapLatest { reminders ->
            if (reminders.isEmpty()) return@flatMapLatest flowOf(emptyList())

            val flows = reminders.map { reminder ->
                reminderWorkerRepository.getWorkInfo(reminder.id).map { workInfo ->
                    reminder.toReminderUiState().copy(
                        status = workInfo?.state?.name ?: "UNKNOWN"
                    )
                }
            }

            combine(flows) { it.toList() }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun updateTextField(field: Field, newValue: String) {
        _uiState.update { currentState ->
            when (field) {
                Field.TITLE -> currentState.copy(title = newValue)
                Field.MESSAGE -> currentState.copy(message = newValue)
                Field.DURATION -> currentState.copy(duration = newValue)
            }
        }
    }

    fun updateTimeType(newValue: TimeType) {
        _uiState.update { currentState ->
            currentState.copy(timeType = newValue)
        }
    }

    fun updatePeriodic(newValue: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(isPeriodic = newValue)
        }
    }

    fun scheduleReminder(reminderUiState: ReminderUiState) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val reminder = reminderUiState.copy(createdAt = now).toReminder()
            val generatedId = reminderDbRepository.insertReminder(reminder)
            val finalReminder = reminder.copy(id = generatedId.toInt())

            reminderWorkerRepository.scheduleReminder(finalReminder)
            _uiState.update { ReminderUiState() }
        }
    }

    fun cancelReminder(reminderUiState: ReminderUiState) {
        viewModelScope.launch {
            reminderWorkerRepository.cancelReminder("reminder_${reminderUiState.id}")
            reminderDbRepository.deleteReminderById(reminderUiState.id)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ReminderApplication)
                val reminderWorkerRepository = application.container.reminderWorkerRepository
                val reminderDbRepository = application.container.reminderDbRepository

                ReminderViewModel(
                    reminderWorkerRepository = reminderWorkerRepository,
                    reminderDbRepository = reminderDbRepository
                )
            }
        }
    }
}
