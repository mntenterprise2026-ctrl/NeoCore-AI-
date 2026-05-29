package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// --- 1. Pujo Crimson & Off-White Color Schemes ---
private val PujoLightColors = lightColorScheme(
    primary = PujoCrimson,
    secondary = PujoMarigold,
    tertiary = EditorialInk,
    background = PujoCreamBackground,
    surface = PujoOffWhite,
    onPrimary = Color.White,
    onSecondary = EditorialInk,
    onBackground = EditorialInk,
    onSurface = EditorialInk,
    surfaceVariant = EditorialBeigeMuted,
    onSurfaceVariant = EditorialInk
)

private val PujoDarkColors = darkColorScheme(
    primary = PujoMarigold,
    secondary = PujoCrimson,
    tertiary = PujoOffWhiteDark,
    background = Color(0xFF1F080A),
    surface = Color(0xFF2A0F11),
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = PujoOffWhite,
    onSurface = PujoOffWhite
)

// --- 2. Royal Bengal Gold Color Schemes ---
private val BengalLightColors = lightColorScheme(
    primary = BengalGold,
    secondary = SunderbansJungleLight,
    tertiary = SunderbansJungle,
    background = Color(0xFFFFFDF8),
    surface = Color(0xFFFFF5E0),
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = Color(0xFF1B1B1B),
    onSurface = Color(0xFF1B1B1B)
)

private val BengalDarkColors = darkColorScheme(
    primary = BengalGold,
    secondary = SunderbansJungleLight,
    tertiary = SunderbansJungle,
    background = Color(0xFF0F0F0B),
    surface = Color(0xFF1B1B14),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color(0xFFFFF5E0),
    onSurface = Color(0xFFFFF5E0)
)

// --- 3. Terracotta Clay Color Schemes ---
private val TerracottaLightColors = lightColorScheme(
    primary = TerracottaOrange,
    secondary = ClayGround,
    tertiary = TerracottaClay,
    background = Color(0xFFFCF3EF),
    surface = Color(0xFFFBE4D8),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color(0xFF3E1E0F),
    onSurface = Color(0xFF3E1E0F)
)

private val TerracottaDarkColors = darkColorScheme(
    primary = ClayGround,
    secondary = TerracottaOrange,
    tertiary = TerracottaClay,
    background = Color(0xFF1D0F08),
    surface = Color(0xFF2A170F),
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = Color(0xFFFBE4D8),
    onSurface = Color(0xFFFBE4D8)
)

// --- 4. Ganges Blue Color Schemes ---
private val GangesLightColors = lightColorScheme(
    primary = GangesBlue,
    secondary = GangesBlueDark,
    tertiary = ShimmeringWater,
    background = Color(0xFFF5F9FD),
    surface = Color(0xFFE3F2FD),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF0D253F),
    onSurface = Color(0xFF0D253F)
)

private val GangesDarkColors = darkColorScheme(
    primary = GangesBlue,
    secondary = GangesBlueDark,
    tertiary = ShimmeringWater,
    background = Color(0xFF0B1724),
    surface = Color(0xFF122438),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFFE3F2FD),
    onSurface = Color(0xFFE3F2FD)
)

// --- 5. Baul Yellow Color Schemes ---
private val BaulLightColors = lightColorScheme(
    primary = BaulOchre,
    secondary = BaulSaffron,
    tertiary = BaulIndigo,
    background = Color(0xFFFCFAEC),
    surface = Color(0xFFF5EFC2),
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = Color(0xFF1E272C),
    onSurface = Color(0xFF1E272C)
)

private val BaulDarkColors = darkColorScheme(
    primary = BaulOchre,
    secondary = BaulSaffron,
    tertiary = BaulIndigo,
    background = Color(0xFF141A1D),
    surface = Color(0xFF222B30),
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = Color(0xFFF5EFC2),
    onSurface = Color(0xFFF5EFC2)
)

// --- Liquid Glass White Futuristic Color Scheme ---
private val FutureLiquidGlassDarkColors = darkColorScheme(
    primary = Color(0xFFA855F7), // Neon purple
    secondary = Color(0xFF06B6D4), // Soft Electric blue
    tertiary = Color(0xFFEC4899), // Neon Pink accent
    background = Color(0xFF06030F), // Ultra-deep cosmic space black
    surface = Color(0xFF0D081F), // Deep frosted glass purple/violet container
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFFF1F5F9),
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = Color(0xFF160E2A),
    onSurfaceVariant = Color(0xFFE2E8F0)
)

private val FutureLiquidGlassLightColors = lightColorScheme(
    primary = Color(0xFF7C3AED), // Premium royal purple
    secondary = Color(0xFF06B6D4), // Soft cyan blue
    tertiary = Color(0xFFEC4899), // Pink
    background = Color(0xFFFFFFFF), // Pure white
    surface = Color(0xFFFAF8FF), // Soft lavender tint
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF0F172A), // Dark slate
    onSurface = Color(0xFF0F172A), // Dark slate
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF64748B)
)

@Composable
fun SonarBanglaTheme(
    themeMode: String,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when (themeMode) {
        "ROYAL_BENGAL" -> if (darkTheme) BengalDarkColors else BengalLightColors
        "TERRACOTTA" -> if (darkTheme) TerracottaDarkColors else TerracottaLightColors
        "GANGES_BLUE" -> if (darkTheme) GangesDarkColors else GangesLightColors
        "BAUL_YELLOW" -> if (darkTheme) BaulDarkColors else BaulLightColors
        else -> if (darkTheme) FutureLiquidGlassDarkColors else FutureLiquidGlassLightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    // Legacy fallback so main components build without breakages
    SonarBanglaTheme(themeMode = "FUTURE_GLASS", darkTheme = darkTheme, content = content)
}
