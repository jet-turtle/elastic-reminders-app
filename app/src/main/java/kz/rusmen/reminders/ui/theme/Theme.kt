package kz.rusmen.reminders.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = DarkOrange,      // Оранжевый Elastic
    secondary = Green,
    onPrimary = Color.White,          // Текст на оранжевом
    surface = Color(0xFFFFFFFF),      // Чистый белый для AppBar
    surfaceVariant = Color(0xFFFFF8F6), // Ваш специфический беж для карточки ввода
    onSurface = Color(0xFF1C1B1F),    // Почти черный текст
    onSurfaceVariant = Color.DarkGray,    // Серый текст для дат
    background = LightBackground    // Светло-серый фон всего экрана
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkOrange,      // Тот же оранжевый (или чуть светлее)
    secondary = Green,
    onPrimary = Color.Black,          // На темной теме текст на оранжевом может быть черным
    surface = Color(0xFF1E1E1E),      // Темный AppBar
    surfaceVariant = Color(0xFF2C2C2C), // Темно-серый для карточки ввода
    onSurface = Color(0xFFE6E1E5),    // Светлый текст
    onSurfaceVariant = Color(0xFFCAC4D0), // Сероватый текст для дат
    background = Color(0xFF121212)    // Глубокий черный фон экрана
)

@Composable
fun RemindersTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}