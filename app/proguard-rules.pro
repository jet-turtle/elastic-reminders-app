# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Сохраняем все классы, помеченные аннотацией Entity
-keep @androidx.room.Entity class * { *; }

# Если вы используете специфические аннотации Room для полей
-keepclassmembers class * {
    @androidx.room.PrimaryKey *;
    @androidx.room.ColumnInfo *;
    @androidx.room.Ignore *;
}

# Сохраняем интерфейсы DAO
-keep @androidx.room.Dao interface * { *; }

-keep class * extends androidx.work.ListenableWorker { *; }
-keepclassmembers enum * { *; }
-keep class kz.rusmen.reminders.ui.TimeType { *; }