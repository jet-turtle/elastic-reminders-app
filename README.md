# Elastic Reminders ⏰

A modern Android application for task management with smart, flexible reminders. Built with a focus on reliability and a clean, reactive UI.

## 📥 Download
[🚀 Download Latest APK](https://github.com/jet-turtle/elastic-reminders-app/releases/latest)

---

## ✨ Key Features
* **Custom Reminders**: Create tasks with detailed titles, descriptions, and precise timing.
* **Flexible Scheduling**: Set one-time or periodic reminders. (intervals in minutes, hours, or days).
* **High-Precision Periodic Tasks**: Custom scheduling logic prevents "time drift." Reminders stay synced to their original schedule regardless of system delays.
* **No Interval Limits**: Unlike standard Android implementations, reminders can be set for any duration, even less than 15 minutes.
* **Reliable Delivery**: Powered by **WorkManager** to ensure reminders trigger even after device reboots.
* **Offline-First**: All data is stored locally using **Room**, ensuring 100% availability.

## 🧠 Intelligent Scheduling
The app uses a "catch-up" algorithm for periodic reminders. Instead of scheduling the next task from the current execution time, it calculates the next occurrence based on the original anchor timestamp:

$$NextRun = LastScheduledTime + (n \times Interval)$$

This ensures that the reminder interval remains constant over weeks or months, avoiding the accumulation of small timing errors (drifting).

## 🛠 Tech Stack
* **Language**: Kotlin
* **UI**: Jetpack Compose (Modern Declarative UI)
* **Architecture**: MVVM (Model-View-ViewModel)
* **Database**: Room (Local persistence)
* **Background Tasks**: WorkManager (Recursive OneTimeWorkRequest for high-precision timing)
* **Concurrency**: Coroutines & Flow

## 🏗 Project Structure
The project is organized into clear functional layers:
* **Data**: Handles local storage (Room, DAO, Entities) and the Repository implementation.
* **UI**: Contains Compose screen, ViewModel, and UI state management.
* **Worker**: Manages background execution and notification delivery via WorkManager.

## 📸 Screenshots
<table>
  <tr>
    <th align="center">Light Theme</th>
    <th align="center">Dark Theme</th>
  </tr>
  <tr>
    <td align="center">
      <img src="screenshots/r1.jpg" width="300" alt="Light Theme">
    </td>
    <td align="center">
      <img src="screenshots/r2.jpg" width="300" alt="Dark Theme">
    </td>
  </tr>
</table>

---

## 🚀 Getting Started
1. Download the APK from the [Releases](https://github.com/jet-turtle/elastic-reminders-app/releases) section.
2. Install it on your Android device (ensure "Install from Unknown Sources" is enabled).
3. Set your first elastic reminder!
