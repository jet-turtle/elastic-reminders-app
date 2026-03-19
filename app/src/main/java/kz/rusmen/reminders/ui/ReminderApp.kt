package kz.rusmen.reminders.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kz.rusmen.reminders.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemindersTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier
) {
    val alpha = 1f - scrollBehavior.state.collapsedFraction

    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.graphicsLayer {
                    this.alpha = alpha
                }
            ) {
                Text(
                    text = stringResource(R.string.elastic),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = stringResource(R.string.app_name),
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.offset(y = (-6).dp),
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        },
        modifier = modifier.shadow(
            elevation = if (scrollBehavior.state.contentOffset < 0f && alpha > 0.1f) 8.dp else 0.dp
        ),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderApp(
    reminderViewModel: ReminderViewModel = viewModel(factory = ReminderViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val uiState by reminderViewModel.uiState.collectAsStateWithLifecycle()
    val allReminders by reminderViewModel.allReminders.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { RemindersTopAppBar(scrollBehavior = scrollBehavior) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            item {
                ReminderCard(
                    uiState = uiState,
                    onTitleInputChange = { newValue ->
                        reminderViewModel.updateTextField(Field.TITLE, newValue)
                    },
                    onMessageInputChange = { newValue ->
                        reminderViewModel.updateTextField(Field.MESSAGE, newValue)
                    },
                    onDurationInputChange = { newValue ->
                        reminderViewModel.updateTextField(Field.DURATION, newValue)
                    },
                    onTimeTypeChange = { newValue ->
                        reminderViewModel.updateTimeType(newValue)
                    },
                    onPeriodicChange = { newValue ->
                        reminderViewModel.updatePeriodic(newValue)
                    },
                    onScheduleReminder = { reminderViewModel.scheduleReminder(uiState) }
                )
            }
            item {
                Text(
                    "Active reminders:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }

            items(allReminders) { reminder ->
                ActiveReminderItem(
                    reminder = reminder,
                    onCancel = { reminderViewModel.cancelReminder(reminder) }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun ActiveReminderItemPreview() {
    ActiveReminderItem(
        reminder = ReminderUiState(
            title = "Тренировки",
            message = "Ура! Завтра - тренировка.",
            duration = "14",
            timeType = TimeType.DAYS,
            isPeriodic = true,
            status = "ENQUEUED",
            createdAt = 123456789
        ),
        onCancel = {}
    )
}

@Composable
fun ActiveReminderItem(
    reminder: ReminderUiState,
    onCancel: (ReminderUiState) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = reminder.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = reminder.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 14,
                    overflow = TextOverflow.Ellipsis
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = reminder.formattedDate,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                Text(
                    text = if (reminder.isPeriodic) {
                        "Periodic" + " - " + reminder.duration + " " + reminder.timeType.title
                    } else {
                        "Single" + " - " + reminder.duration + " " + reminder.timeType.title
                    },
                    style = MaterialTheme.typography.bodySmall
                )
                StatusBadge(status = reminder.status)
            }
            IconButton(onClick = { onCancel(reminder) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Cancel",
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun StatusBadge(status: String, modifier: Modifier = Modifier) {
    // Определяем основной цвет для каждого статуса
    val statusColor = when (status) {
        "ENQUEUED" -> Color(0xFF2196F3) // Насыщенный синий
        "RUNNING" -> Color(0xFF9C27B0)  // Пурпурный
        "SUCCEEDED" -> Color(0xFF4CAF50) // Зеленый
        "FAILED" -> Color(0xFFF44336)    // Красный
        else -> Color.Gray
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = statusColor.copy(alpha = 0.12f), // Бледный фон
        modifier = modifier.padding(top = 4.dp)
    ) {
        Text(
            text = status,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelMedium,
            color = statusColor, // Яркий текст на бледном фоне
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun ReminderCard(
    uiState: ReminderUiState,
    modifier: Modifier = Modifier,
    onTitleInputChange: (String) -> Unit,
    onMessageInputChange: (String) -> Unit,
    onDurationInputChange: (String) -> Unit,
    onTimeTypeChange: (TimeType) -> Unit,
    onPeriodicChange: (Boolean) -> Unit,
    onScheduleReminder: () -> Unit
) {
    Card(
        modifier = modifier
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.set_notification),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                textAlign = TextAlign.Center
            )
            OutlinedTextField(
                value = uiState.title,
                onValueChange = onTitleInputChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text(stringResource(R.string.title)) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
            )
            OutlinedTextField(
                value = uiState.message,
                onValueChange = onMessageInputChange,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3,
                label = { Text(stringResource(R.string.message)) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
            )
            Row(modifier = Modifier.height(160.dp)) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(140.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        value = uiState.duration,
                        onValueChange = onDurationInputChange,
                        singleLine = true,
                        label = { Text(stringResource(R.string.duration)) },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )
                    PeriodicRow(
                        item = false,
                        selectedItem = uiState.isPeriodic,
                        onClick = onPeriodicChange,
                        modifier = Modifier.selectable(
                            selected = !uiState.isPeriodic,
                            onClick = { onPeriodicChange(false) }
                        )
                    )
                    PeriodicRow(
                        item = true,
                        selectedItem = uiState.isPeriodic,
                        onClick = onPeriodicChange,
                        modifier = Modifier.selectable(
                            selected = uiState.isPeriodic,
                            onClick = { onPeriodicChange(true) }
                        )
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    TimeTypeRow(
                        item = TimeType.MINUTES,
                        selectedItem = uiState.timeType,
                        onClick = onTimeTypeChange,
                        modifier = Modifier.selectable(
                            selected = TimeType.MINUTES == uiState.timeType,
                            onClick = { onTimeTypeChange(TimeType.MINUTES) }
                        )
                    )
                    TimeTypeRow(
                        item = TimeType.HOURS,
                        selectedItem = uiState.timeType,
                        onClick = onTimeTypeChange,
                        modifier = Modifier.selectable(
                            selected = TimeType.HOURS == uiState.timeType,
                            onClick = { onTimeTypeChange(TimeType.HOURS) }
                        )
                    )
                    TimeTypeRow(
                        item = TimeType.DAYS,
                        selectedItem = uiState.timeType,
                        onClick = onTimeTypeChange,
                        modifier = Modifier.selectable(
                            selected = TimeType.DAYS == uiState.timeType,
                            onClick = { onTimeTypeChange(TimeType.DAYS) }
                        )
                    )
                }
            }
            Button(
                modifier = Modifier
                    .padding(8.dp)
                    .width(160.dp),
                onClick = onScheduleReminder,
                enabled = uiState.duration.isNotBlank() && uiState.title.isNotBlank() && uiState.message.isNotBlank()
            ) {
                Text(stringResource(R.string.start))
            }
        }
    }
}

@Composable
fun TimeTypeRow(
    item: TimeType,
    selectedItem: TimeType,
    onClick: (TimeType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selectedItem == item,
            onClick = { onClick(item) }
        )
        Text(
            text = stringResource(R.string.approx_sign) + item.title,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun PeriodicRow(
    item: Boolean,
    selectedItem: Boolean,
    onClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selectedItem == item,
            onClick = { onClick(item) }
        )
        Text(
            text = if (item) stringResource(R.string.periodic) else stringResource(R.string.single),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
