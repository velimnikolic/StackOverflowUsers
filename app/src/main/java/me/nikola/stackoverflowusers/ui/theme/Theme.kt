package me.nikola.stackoverflowusers.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = StackOrange,
    onPrimary = Ink,
    surface = SurfaceLight,
    onSurface = Ink,
)

private val DarkColors = darkColorScheme(
    primary = StackOrange,
)

@Composable
fun StackOverflowUsersTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content,
    )
}
