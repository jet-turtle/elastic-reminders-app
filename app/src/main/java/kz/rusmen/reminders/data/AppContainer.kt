/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kz.rusmen.reminders.data

import android.content.Context
import kz.rusmen.reminders.data.repository.OfflineReminderDbRepository
import kz.rusmen.reminders.data.repository.ReminderDbRepository
import kz.rusmen.reminders.data.repository.ReminderWorkerRepository
import kz.rusmen.reminders.data.repository.WorkManagerReminderRepository

interface AppContainer {
    val reminderWorkerRepository : ReminderWorkerRepository
    val reminderDbRepository : ReminderDbRepository
}

class DefaultAppContainer(context: Context) : AppContainer {
    override val reminderWorkerRepository = WorkManagerReminderRepository(context)

    override val reminderDbRepository: ReminderDbRepository by lazy {
        OfflineReminderDbRepository(ReminderDatabase.getDatabase(context).reminderDao())
    }
}
