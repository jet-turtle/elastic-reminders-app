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

package kz.rusmen.reminders.worker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kz.rusmen.reminders.CHANNEL_ID
import kz.rusmen.reminders.MainActivity
import kz.rusmen.reminders.R
import kz.rusmen.reminders.REQUEST_CODE
import kz.rusmen.reminders.VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
import kz.rusmen.reminders.VERBOSE_NOTIFICATION_CHANNEL_NAME

@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
fun makeReminderNotification(
    title: String?,
    message: String?,
    context: Context
) {
    val tag = "NotificationDebug"
    Log.d(tag, "Attempting to show notification: Title=$title, Message=$message")

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val channel = NotificationChannel(
            CHANNEL_ID,
            VERBOSE_NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        notificationManager?.createNotificationChannel(channel)
        Log.d(tag, "Notification Channel created/checked")
    }

    // 2. Проверяем разрешение (Android 13+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED) {
            Log.e(tag, "Permission NOT GRANTED. Notification aborted.")
            return
        }
    }

    val pendingIntent: PendingIntent = createPendingIntent(context)

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(title?: "No Title")
        .setContentText(message?: "No Message")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setCategory(NotificationCompat.CATEGORY_REMINDER)
        .setDefaults(NotificationCompat.DEFAULT_ALL) // Вибрация и звук по умолчанию
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    // Используем хеш-код заголовка или ID из параметров, чтобы уведомления не заменяли друг друга
    val notificationId = title?.hashCode() ?: 1
    try {
        notificationManager?.notify(notificationId, builder.build())
        Log.d(tag, "notificationManager.notify() called successfully with ID: $notificationId")
    } catch (e: Exception) {
        Log.e(tag, "Failed to send notification", e)
    }
}

fun createPendingIntent(appContext: Context): PendingIntent {
    val intent = Intent(appContext, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    // Flag to detect unsafe launches of intents for Android 12 and higher
    // to improve platform security
    var flags = PendingIntent.FLAG_UPDATE_CURRENT
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        flags = flags or PendingIntent.FLAG_IMMUTABLE
    }

    return PendingIntent.getActivity(
        appContext,
        REQUEST_CODE,
        intent,
        flags
    )
}
