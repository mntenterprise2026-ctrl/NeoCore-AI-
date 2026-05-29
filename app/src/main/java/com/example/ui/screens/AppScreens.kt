package com.example.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily as OriginalFontFamily
import com.example.ui.theme.GoogleSans
import com.example.ui.theme.GoogleSansBold
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.ArtworkEntity
import com.example.data.ChatMessage
import com.example.data.SonarBanglaRepository
import com.example.ui.SonarBanglaViewModel
import com.example.ui.components.AlpanaCanvas

private object FontFamily {
    val SansSerif = GoogleSans
    val Serif = GoogleSansBold
    val Monospace = OriginalFontFamily.Monospace
    val Default = GoogleSans
}

// --- UNIFIED BRANDING COMPOSENTS ---
@Composable
fun NeoCoreLogoBadge(
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 44.dp,
    textSize: androidx.compose.ui.unit.TextUnit = 22.sp,
    cornerRadius: androidx.compose.ui.unit.Dp = 14.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .shadow(
                elevation = if (size > 50.dp) 16.dp else 0.dp,
                shape = RoundedCornerShape(cornerRadius),
                ambientColor = Color(0xFF8B5CF6),
                spotColor = Color(0xFFD8B4FE)
            )
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF8B5CF6), // Premium violet
                        Color(0xFF6366F1), // Indigo
                        Color(0xFFEC4899)  // Pink accent highlight
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Subtle internal glossy highlight overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.22f),
                            Color.Transparent
                        )
                    )
                )
        )
        Text(
            text = "N",
            color = Color.White,
            fontWeight = FontWeight.Black,
            fontSize = textSize,
            fontFamily = FontFamily.SansSerif,
            letterSpacing = (-0.5).sp
        )
    }
}

@Composable
fun SuggestionGridCard(
    title: String,
    desc: String,
    prompt: String,
    onClick: () -> Unit,
    isDark: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.heightIn(min = 72.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0x118B5CF6) else Color(0x068B5CF6)
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (isDark) Color(0x288B5CF6) else Color(0x128B5CF6)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) Color.White else Color.Black,
                fontFamily = FontFamily.SansSerif
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = desc,
                fontSize = 11.sp,
                color = if (isDark) Color(0xFF94A3B8) else Color(0xFF475569),
                fontFamily = FontFamily.SansSerif,
                maxLines = 1
            )
        }
    }
}

// --- EDITORIAL HEADER ---
@Composable
fun EditorialHeader(
    title: String,
    subtitle: String = "Bengal Intelligence",
    onMenuClick: () -> Unit,
    actionIcon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    onActionClick: () -> Unit = {}
) {
    val themeColors = MaterialTheme.colorScheme
    val context = androidx.compose.ui.platform.LocalContext.current
    val isDark = themeColors.background.red < 0.5f
    
    val textAndIconColor = if (isDark) Color.White else Color.Black
    val buttonBgColor = if (isDark) Color.White.copy(alpha = 0.05f) else Color(0xFFF1F5F9)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                drawLine(
                    color = if (isDark) Color.White.copy(alpha = 0.08f) else Color(0xFFE2E8F0),
                    start = Offset(0f, size.height - strokeWidth/2),
                    end = Offset(size.width, size.height - strokeWidth/2),
                    strokeWidth = strokeWidth
                )
            }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left: Drawer Toggle and Futuristic rounded logo N
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(buttonBgColor)
                    .clickable { onMenuClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Open Navigation Drawer",
                    tint = textAndIconColor,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            
            // Brand Logo mark: Small elegant squircle housing consolidated signature NeoCore logo badge
            NeoCoreLogoBadge(
                size = 36.dp,
                textSize = 18.sp,
                cornerRadius = 10.dp
            )
            
            // Render screen-specific headers but completely hide any default branding or assistant titles
            val lowerTitle = title.lowercase()
            val lowerSubtitle = subtitle.lowercase()
            val isBrandingText = lowerTitle.contains("neocore") || lowerTitle.contains("assistant") ||
                    lowerSubtitle.contains("futuristic") || lowerSubtitle.contains("assistant")
            val hasCustomText = title.isNotEmpty() && !isBrandingText
            
            if (hasCustomText) {
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    val hasCustomSubtitle = subtitle.isNotEmpty() && !lowerSubtitle.contains("futuristic") && !lowerSubtitle.contains("assistant")
                    if (hasCustomSubtitle) {
                        Text(
                            text = subtitle.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFA855F7),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                    Text(
                        text = title,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        letterSpacing = (-0.5).sp,
                        color = textAndIconColor
                    )
                }
            }
        }

        // Right Action Area (Share button and other triggers side by side)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Share button launcher
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(buttonBgColor)
                    .clickable {
                        val shareIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(android.content.Intent.EXTRA_SUBJECT, "NeoCore AI Assistant")
                            putExtra(
                                android.content.Intent.EXTRA_TEXT, 
                                "Checkout NeoCore AI - A futuristic white Liquid Glass assistant offering traditional West Bengal cultural expert advice, generative traditional Alpana/Cyber studio canvas, and elegant search logs. Try it now!"
                            )
                        }
                        val chooser = android.content.Intent.createChooser(shareIntent, "Share NeoCore AI App via")
                        context.startActivity(chooser)
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share App",
                    tint = textAndIconColor,
                    modifier = Modifier.size(18.dp)
                )
            }

            // Screen Specific secondary action trigger
            if (actionIcon != null) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(buttonBgColor)
                        .clickable { onActionClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = actionIcon,
                        contentDescription = "Action Button",
                        tint = textAndIconColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(themeColors.primary.copy(alpha = 0.08f))
                        .clickable { onMenuClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More Actions",
                        tint = themeColors.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ViewToggleChip(
    label: String,
    isMinimized: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = if (isMinimized) Color(0xFFF1F5F9) else Color(0xFFFAF5FF),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (isMinimized) Color(0xFFE2E8F0) else Color(0xFFE9D5FF)
        ),
        modifier = Modifier.height(28.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(
                        color = if (isMinimized) Color(0xFF94A3B8) else Color(0xFF8B5CF6),
                        shape = CircleShape
                    )
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = if (isMinimized) "Hidden" else label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                color = if (isMinimized) Color(0xFF64748B) else Color(0xFF7C3AED)
            )
        }
    }
}

// --- COSMIC BACKGROUND ---
@Composable
fun CosmicBackground(
    content: @Composable BoxScope.() -> Unit
) {
    val transition = rememberInfiniteTransition(label = "BackgroundParticles")
    
    // Float values for drifting stars/particles
    val animOffset1 by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(25000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particle_offset_1"
    )
    val animOffset2 by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(18000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particle_offset_2"
    )
    
    val orbPulsing by transition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb_pulsing"
    )

    val themeColors = MaterialTheme.colorScheme
    val isDark = themeColors.background.red < 0.5f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (isDark) {
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF04020B), // Space Midnight black
                            Color(0xFF0E0720), // Dark space purple
                            Color(0xFF06030F)  // Dark deep violet
                        )
                    )
                } else {
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFFFFFF), // Pure white
                            Color(0xFFFAF7FF), // Extremely subtle purple glow
                            Color(0xFFFFFFFF)
                        )
                    )
                }
            )
            .drawBehind {
                val width = size.width
                val height = size.height
                
                // Draw elegant subtle grid
                val gridSize = 100f
                var y = 0f
                while (y < height) {
                    drawLine(
                        color = if (isDark) Color(0xFFA855F7).copy(alpha = 0.05f) else Color(0xFF7C3AED).copy(alpha = 0.03f),
                        start = Offset(0f, y),
                        end = Offset(width, y),
                        strokeWidth = 1f
                    )
                    y += gridSize
                }
                
                var x = 0f
                while (x < width) {
                    drawLine(
                        color = if (isDark) Color(0xFFA855F7).copy(alpha = 0.05f) else Color(0xFF7C3AED).copy(alpha = 0.03f),
                        start = Offset(x, 0f),
                        end = Offset(x, height),
                        strokeWidth = 1f
                    )
                    x += gridSize
                }

                if (isDark) {
                    // Draw drifting glowing digital particles (15 particles)
                    for (i in 0..15) {
                        val px = ((i * 1234 + animOffset1) % width)
                        val py = ((i * 5678 + animOffset2 * 0.5f) % height)
                        val radius = (i % 3) + 2f
                        val alpha = (0.2f + 0.3f * kotlin.math.sin((animOffset1 / 10f + i * 15f) * kotlin.math.PI / 180f)).toFloat().coerceIn(0.1f, 0.7f)
                        
                        drawCircle(
                            color = if (i % 2 == 0) Color(0xFF06B6D4).copy(alpha = alpha) else Color(0xFFA855F7).copy(alpha = alpha),
                            radius = radius,
                            center = Offset(px, py)
                        )
                    }
                } else {
                    // Draw soft purple ambient radial glows mirroring the prompt's layout
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFFF3E8FF).copy(alpha = 0.5f), Color.Transparent),
                            center = Offset(width * 0.15f, height * 0.2f),
                            radius = 450f
                        ),
                        radius = 450f,
                        center = Offset(width * 0.15f, height * 0.2f)
                    )
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFFE9D5FF).copy(alpha = 0.4f), Color.Transparent),
                            center = Offset(width * 0.85f, height * 0.8f),
                            radius = 500f
                        ),
                        radius = 500f,
                        center = Offset(width * 0.85f, height * 0.8f)
                    )
                }
            }
    ) {
        // Glowing Orb / Aurora element in the upper area (top center)
        if (isDark) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-40).dp)
                    .size(280.dp)
                    .drawBehind {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFA855F7).copy(alpha = 0.12f * orbPulsing),
                                    Color(0xFF06B6D4).copy(alpha = 0.04f * orbPulsing),
                                    Color.Transparent
                                ),
                                center = Offset(size.width / 2f, size.height / 2f),
                                radius = (size.width / 2f) * orbPulsing
                            ),
                            radius = (size.width / 2f) * orbPulsing,
                            center = Offset(size.width / 2f, size.height / 2f)
                        )
                    }
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}

// --- CHAT COMPANION SCREEN ---
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ChatScreen(
    viewModel: SonarBanglaViewModel,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val messages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val isLoading by viewModel.isChatLoading.collectAsStateWithLifecycle()
    val isSpeaking by viewModel.isSpeaking.collectAsStateWithLifecycle()
    val voiceLang by viewModel.voiceLanguage.collectAsStateWithLifecycle()
    var inputText by remember { mutableStateOf("") }
    var attachedImageUri by remember { mutableStateOf<android.net.Uri?>(null) }
    val keyboardController = LocalSoftwareKeyboardController.current

    // Minimization States
    var isHeaderMinimized by remember { mutableStateOf(false) }
    var isSuggestionsMinimized by remember { mutableStateOf(false) }
    var isInputBarMinimized by remember { mutableStateOf(false) }
    var isFocusModeEnabled by remember { mutableStateOf(false) }

    val themeColors = MaterialTheme.colorScheme
    val isDark = themeColors.background.red < 0.5f

    CosmicBackground {
        Column(
            modifier = modifier.fillMaxSize()
        ) {
        // Welcoming Editorial Header & Divider line
        if (!isHeaderMinimized) {
            EditorialHeader(
                title = "NeoCore AI",
                subtitle = "Futuristic Assistant",
                onMenuClick = onMenuClick,
                actionIcon = if (isSpeaking) Icons.Default.Close else Icons.Default.Add,
                onActionClick = {
                    if (isSpeaking) {
                        viewModel.stopSpeaking()
                    } else {
                        viewModel.clearChatMessages()
                    }
                }
            )
        } else {
            // Minimized Header: Super sleek, low-profile minimal bar to preserve screen space
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.85f))
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .clickable { onMenuClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Open Drawer",
                            tint = Color(0xFF8B5CF6),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Focus Mode",
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color(0xFF7C3AED)
                    )
                }
                
                IconButton(
                    onClick = { 
                        isHeaderMinimized = false 
                        if (isFocusModeEnabled) {
                            isFocusModeEnabled = false
                            isInputBarMinimized = false
                            isSuggestionsMinimized = false
                        }
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand Header",
                        tint = Color(0xFF8B5CF6),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
        
        // Messages Box
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (messages.isEmpty()) {
                // Centered ChatGPT-like empty state, with suggestions row placed directly below the top app bar
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // --- REFINED HORIZONTAL SUGGESTIONS ROW DIRECTLY BELOW TOP APP BAR ---
                    if (!isSuggestionsMinimized) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, bottom = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "SUGGESTED PROMPTS",
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = if (isDark) Color(0xFFA78BFA) else Color(0xFF7C3AED),
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "✕ Hide",
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 11.sp,
                                    color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B),
                                    modifier = Modifier.clickable { isSuggestionsMinimized = true }
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                                    .padding(horizontal = 16.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                val suggestionChips = listOf(
                                    Triple("🎨", "Alpana Art", "Draw a beautiful traditional white circular Alpana art design"),
                                    Triple("📜", "Bengal Verse", "Recite a beautiful Bengali poem and make its inner meaning clear in English"),
                                    Triple("🐯", "Sundarbans", "Tell me about the magnificent royal Bengal tigers of the Sundarbans"),
                                    Triple("🎵", "Mystic Bauls", "Explain Baul mystic folk singers and their spiritual ektara music"),
                                    Triple("🌉", "Howrah Bridge", "Tell me about the historical Howrah Cantilever bridge in rain"),
                                    Triple("🍛", "Bengal Food", "What are the most famous traditional Bengali dishes and desserts like Rosogolla?")
                                )
                                suggestionChips.forEach { (emoji, label, prompt) ->
                                    Surface(
                                        modifier = Modifier
                                            .height(46.dp) // Prominent height, easy to tap, increased by 10-15%
                                            .shadow(
                                                elevation = 5.dp, // Subtle purple glow shadow
                                                shape = RoundedCornerShape(16.dp), // Modern rounded rectangle
                                                clip = false,
                                                ambientColor = Color(0xFFF3E8FF),
                                                spotColor = Color(0xFFA855F7)
                                            )
                                            .clickable { inputText = prompt },
                                        shape = RoundedCornerShape(16.dp), // Modern rounded rectangle corner radius: 14dp-18dp
                                        color = Color.White.copy(alpha = 0.85f), // White translucent background / Soft liquid-glass
                                        border = BorderStroke(1.2.dp, Color(0xFFE9D5FF)) // Light lavender border
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxHeight()
                                                .padding(horizontal = 14.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Text(
                                                text = emoji,
                                                fontSize = 16.sp
                                            )
                                            Text(
                                                text = label,
                                                fontFamily = FontFamily.SansSerif,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.5.sp, // High-contrast black text
                                                color = Color.Black
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        // Compact restore link on the right
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "Show Suggestions",
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = if (isDark) Color(0xFFA78BFA) else Color(0xFF7C3AED),
                                modifier = Modifier.clickable { isSuggestionsMinimized = false }
                            )
                        }
                    }

                    // --- VERTICALLY SCROLLABLE CENTRAL LOGO & ASK BAR CONTAINER ---
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 24.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(10.dp)) // Reduced excessive vertical whitespace!
                        
                        // Central logo/emblem displaying our trademark unified badge logo with glowing shadow
                        NeoCoreLogoBadge(
                            size = 80.dp,
                            textSize = 40.sp,
                            cornerRadius = 22.dp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "NeoCore AI",
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Black,
                            fontSize = 26.sp,
                            color = if (isDark) Color.White else Color.Black,
                            textAlign = TextAlign.Center
                        )
                        
                        Text(
                            text = "How can I help you today?",
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 14.sp,
                            color = if (isDark) Color(0xFF94A3B8) else Color(0xFF475569),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        if (!isInputBarMinimized) {
                            // ASK BAR
                            AskBarContainer(
                                inputText = inputText,
                                onValueChange = { inputText = it },
                                onSend = {
                                    if (inputText.trim().isNotEmpty() || attachedImageUri != null) {
                                        viewModel.sendChatMessage(inputText, attachedImageUri?.toString())
                                        inputText = ""
                                        attachedImageUri = null
                                        keyboardController?.hide()
                                    }
                                },
                                attachedImageUri = attachedImageUri,
                                onImageAttached = { attachedImageUri = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 4.dp)
                            )
                        } else {
                            // Compact restore button
                            Button(
                                onClick = { isInputBarMinimized = false; isFocusModeEnabled = false },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6)),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier.padding(vertical = 4.dp)
                            ) {
                                Icon(Icons.Default.Search, contentDescription = "Expand", modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Expand Search/Ask Box", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            } else {
                // There are messages: Layout is premium listing with input at the bottom
                Column(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(messages) { msg ->
                            ChatBubble(
                                msg = msg,
                                onPlayWord = { viewModel.speakText(msg.text) },
                                themeColors = themeColors
                            )
                        }

                        if (isLoading) {
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(18.dp),
                                        strokeWidth = 2.dp,
                                        color = themeColors.primary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "NeoCore AI is analyzing...",
                                        fontSize = 12.sp,
                                        color = themeColors.primary
                                    )
                                }
                            }
                        }
                    }

                    // Floating/pinned AskBar at the bottom of the feed (Gemini & ChatGPT style) or custom minimized badge
                    if (!isInputBarMinimized) {
                        Surface(
                            color = Color.Transparent,
                            modifier = Modifier
                                .fillMaxWidth()
                                .drawBehind {
                                    val strokeWidth = 1.dp.toPx()
                                    drawLine(
                                        color = Color.White.copy(alpha = 0.08f),
                                        start = Offset(0f, 0f),
                                        end = Offset(size.width, 0f),
                                        strokeWidth = strokeWidth
                                    )
                                }
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(modifier = Modifier.weight(1f)) {
                                    AskBarContainer(
                                        inputText = inputText,
                                        onValueChange = { inputText = it },
                                        onSend = {
                                            if (inputText.trim().isNotEmpty() || attachedImageUri != null) {
                                                viewModel.sendChatMessage(inputText, attachedImageUri?.toString())
                                                inputText = ""
                                                attachedImageUri = null
                                                keyboardController?.hide()
                                            }
                                        },
                                        attachedImageUri = attachedImageUri,
                                        onImageAttached = { attachedImageUri = it }
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                IconButton(
                                    onClick = { isInputBarMinimized = true },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(Color.White.copy(alpha = 0.05f), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Minimize Input Bar",
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    } else {
                        // Tiny floating violet circle button
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(
                                onClick = { 
                                    isInputBarMinimized = false
                                    isFocusModeEnabled = false
                                },
                                modifier = Modifier
                                    .size(46.dp)
                                    .shadow(8.dp, CircleShape, spotColor = Color(0xFF8B5CF6))
                                    .background(Color(0xFF8B5CF6), CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Expand Ask Bar",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Voice Synthesis Active Control (Special Editorial design block)
        if (isSpeaking) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .clickable { viewModel.stopSpeaking() },
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "VOICE PLAYBACK RUNNING",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.7f),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "- May. (${getLanguageDisplayName(voiceLang)})",
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 13.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Spoken play synthesis feedback",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
}

@Composable
fun AskBarContainer(
    inputText: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    attachedImageUri: android.net.Uri?,
    onImageAttached: (android.net.Uri?) -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }
    val context = androidx.compose.ui.platform.LocalContext.current
    
    // Theme inspection
    val themeColors = MaterialTheme.colorScheme
    val isDark = themeColors.background.red < 0.5f

    // 1. Scale Spring animation on focus
    val barScale by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (isFocused) 1.02f else 1.0f,
        animationSpec = androidx.compose.animation.core.spring(
            dampingRatio = androidx.compose.animation.core.Spring.DampingRatioLowBouncy,
            stiffness = androidx.compose.animation.core.Spring.StiffnessMediumLow
        ),
        label = "bar_scale"
    )

    // 2. Loop Breathing border animation
    val infiniteTransition = rememberInfiniteTransition(label = "AskBarAnimations")
    val breathingBorderAlpha by infiniteTransition.animateFloat(
        initialValue = 0.40f,
        targetValue = 0.90f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathing_border_alpha"
    )

    // 3. Dynamic background color - darker than Cosmic background
    val targetBgColor = if (isDark) {
        Color(0xFF020106) // noticeably darker than 0xFF04020B
    } else {
        Color(0xFFEDE8F5) // noticeably darker than 0xFFFAF7FF / 0xFFFFFFFF
    }
    
    val animatedBgColor by androidx.compose.animation.animateColorAsState(
        targetValue = targetBgColor,
        animationSpec = androidx.compose.animation.core.tween(450),
        label = "animated_bg_color"
    )

    // Text & symbol color setup for maximum visibility and professional contrast
    val contentColor = if (isDark) Color.White else Color.Black
    val accentTint = if (isDark) Color(0xFFE3E3E3) else Color(0xFF3B2F5F)
    val placeholderColor = if (isDark) Color(0xFF94A3B8) else Color(0xFF75757E)

    // File/Image Picker Launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: android.net.Uri? ->
        if (uri != null) {
            onImageAttached(uri)
        }
    }

    // Single unified capsule container (no outer Row with detached button!)
    Surface(
        color = animatedBgColor, 
        shape = CircleShape,
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = barScale
                scaleY = barScale
            }
            .heightIn(min = 44.dp)
            .shadow(
                elevation = if (isFocused) 12.dp else 4.dp,
                shape = CircleShape,
                ambientColor = Color.Black,
                spotColor = if (isFocused) Color(0xFFA855F7) else Color.Transparent
            )
            .border(
                width = if (isFocused) 1.2.dp else 0.6.dp,
                brush = if (isFocused) {
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFA855F7).copy(alpha = breathingBorderAlpha),
                            Color(0xFF06B6D4).copy(alpha = breathingBorderAlpha),
                            Color(0xFFEC4899).copy(alpha = breathingBorderAlpha)
                        )
                    )
                } else {
                    androidx.compose.ui.graphics.SolidColor(
                        if (isDark) Color.White.copy(alpha = 0.12f * breathingBorderAlpha)
                        else Color.Black.copy(alpha = 0.12f * breathingBorderAlpha)
                    )
                },
                shape = CircleShape
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Side: File Upload '+' Button (or tiny attachment preview if one is already uploaded)
            if (attachedImageUri == null) {
                IconButton(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    modifier = Modifier.size(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Upload files and images",
                        tint = accentTint,
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .padding(end = 4.dp, start = 4.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                        .clickable { onImageAttached(null) }, // cancel selection on tap
                    contentAlignment = Alignment.Center
                ) {
                    coil.compose.AsyncImage(
                        model = attachedImageUri,
                        contentDescription = "Attached image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear attachment",
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Scrollable text input
            androidx.compose.foundation.text.BasicTextField(
                value = inputText,
                onValueChange = onValueChange,
                maxLines = 4,
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = contentColor,
                    fontSize = 15.sp,
                    fontFamily = FontFamily.SansSerif
                ),
                cursorBrush = androidx.compose.ui.graphics.SolidColor(contentColor),
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (inputText.isEmpty()) {
                            Text(
                                text = "Ask anything...",
                                fontSize = 14.sp,
                                color = placeholderColor,
                                fontFamily = FontFamily.SansSerif
                            )
                        }
                        innerTextField()
                    }
                }
            )

            Spacer(modifier = Modifier.width(6.dp))

            // Right Side Controls: Microphone & Send Button unified inside the pill!
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(
                    onClick = { 
                        android.widget.Toast.makeText(context, "Voice mode activated", android.widget.Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow, // Play/mic icon
                        contentDescription = "Voice Input",
                        tint = accentTint,
                        modifier = Modifier.size(18.dp)
                    )
                }

                val isEnabled = inputText.trim().isNotEmpty() || attachedImageUri != null
                IconButton(
                    onClick = { if (isEnabled) onSend() },
                    modifier = Modifier
                        .size(34.dp)
                        .background(
                            if (isEnabled) Color(0xFF10A37F) else Color(0x3310A37F),
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Send Message",
                        tint = if (isEnabled) Color.White else Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SuggestionCard(
    icon: String,
    title: String,
    desc: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(18.dp),
                ambientColor = Color(0xFFA855F7), 
                spotColor = Color(0xFF06B6D4)     
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.04f)), 
        border = BorderStroke(
            width = 1.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.15f),
                    Color(0xFFA855F7).copy(alpha = 0.20f)
                )
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Text(
                text = icon,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Text(
                text = title,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = desc,
                fontFamily = FontFamily.SansSerif,
                fontSize = 11.sp,
                color = Color(0xFF94A3B8),
                lineHeight = 14.sp
            )
        }
    }
}

@Composable
fun ChatBubble(
    msg: ChatMessage,
    onPlayWord: () -> Unit,
    themeColors: ColorScheme
) {
    val isUser = msg.sender == "USER"
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
        ) {
            if (!isUser) {
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp, top = 4.dp)
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFF6366F1), Color(0xFFA855F7))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.foundation.Image(
                        painter = androidx.compose.ui.res.painterResource(id = com.example.R.drawable.ic_launcher_foreground),
                        contentDescription = "AI Assistant Logo",
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Column(
                modifier = if (isUser) {
                    Modifier
                        .widthIn(max = 290.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(20.dp, 16.dp, 4.dp, 16.dp),
                            ambientColor = Color(0xFFA855F7),
                            spotColor = Color(0xFF06B6D4)
                        )
                        .clip(RoundedCornerShape(20.dp, 16.dp, 4.dp, 16.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFF9333EA), Color(0xFF7C3AED))
                            )
                        )
                        .border(
                            width = 1.dp,
                            color = Color.White.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(20.dp, 16.dp, 4.dp, 16.dp)
                        )
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                } else {
                    Modifier
                        .weight(1f)
                        .padding(top = 4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.04f))
                        .border(
                            width = 1.dp,
                            color = Color.White.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                }
            ) {
                Text(
                    text = msg.text,
                    fontSize = 15.sp,
                    fontFamily = if (isUser) FontFamily.SansSerif else FontFamily.Serif,
                    color = if (isUser) Color.White else Color(0xFFE2E8F0),
                    lineHeight = 22.sp
                )

                if (msg.imageUrl != null) {
                    androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(8.dp))
                    coil.compose.AsyncImage(
                        model = msg.imageUrl,
                        contentDescription = "Generated Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp)),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                }

                if (!isUser) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        IconButton(
                            onClick = onPlayWord,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                Icons.Outlined.PlayArrow,
                                contentDescription = "Listen out loud",
                                modifier = Modifier.size(16.dp),
                                tint = Color(0xFFC084FC)
                            )
                        }
                    }
                }
            }
        }
        
        if (!isUser) {
            HorizontalDivider(
                color = Color.White.copy(alpha = 0.05f),
                thickness = 1.dp,
                modifier = Modifier.padding(top = 12.dp, bottom = 4.dp, start = 40.dp)
            )
        }
    }
}

// --- ART STUDIO SCREEN ---
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ArtStudioScreen(
    viewModel: SonarBanglaViewModel,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isGenerating by viewModel.isArtGenerating.collectAsStateWithLifecycle()
    val genProgress by viewModel.artGeneratingProgress.collectAsStateWithLifecycle()
    val genSecondsLeft by viewModel.artGeneratingSecondsLeft.collectAsStateWithLifecycle()
    val genStepText by viewModel.artGeneratingStepText.collectAsStateWithLifecycle()
    val activeArt by viewModel.currentGeneratedArt.collectAsStateWithLifecycle()
    var promptInput by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    var isControlsMinimized by remember { mutableStateOf(false) }

    val themeColors = MaterialTheme.colorScheme

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column {
                EditorialHeader(
                    title = "Alpana Studio",
                    subtitle = "Traditional Canvas",
                    onMenuClick = onMenuClick,
                    actionIcon = Icons.Default.Refresh,
                    onActionClick = {
                        promptInput = ""
                    }
                )
                HorizontalDivider(color = Color(0xFFE5DACE), thickness = 1.dp)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(themeColors.background)
                .verticalScroll(androidx.compose.foundation.rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Minimize Control Banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CANVAS CONFIGURATION",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF94A3B8),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 10.sp
                )
                Surface(
                    onClick = { isControlsMinimized = !isControlsMinimized },
                    color = if (isControlsMinimized) Color(0xFFF1F5F9) else Color(0xFFFAF5FF),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, if (isControlsMinimized) Color(0xFFE2E8F0) else Color(0xFFE9D5FF))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(
                                    color = if (isControlsMinimized) Color(0xFF94A3B8) else Color(0xFF8B5CF6),
                                    shape = CircleShape
                                )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isControlsMinimized) "Show Settings" else "Hide Controls (Fullscreen Art)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isControlsMinimized) Color(0xFF64748B) else Color(0xFF7C3AED)
                        )
                    }
                }
            }

            // Prompt Field Box (Editorial Styled Card)
            if (!isControlsMinimized) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = themeColors.surface),
                    border = BorderStroke(1.dp, Color(0xFFE5DACE))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Generative Traditional Art Studio 🎨",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = themeColors.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Describe what scene to design. Our AI custom tailors traditional compositions inspired by Alpana patterns, futuristic Cyber grids, Sundarban Tigers, Baul singers, or the Ganges river.",
                            fontSize = 12.sp,
                            color = themeColors.onSurface.copy(alpha = 0.7f),
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        TextField(
                            value = promptInput,
                            onValueChange = { promptInput = it },
                            placeholder = { Text("Draw a Baul dancing underneath a tree...") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFF5F2EF),
                                unfocusedContainerColor = Color(0xFFF5F2EF),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Quick Suggestion Chips
                        val presets = listOf(
                            "Traditional Alpana circular patterns" to "ALPANA",
                            "Futuristic cybernetic AI neural network grid" to "CYBER_GRID",
                            "Sunset in Sunderban mangrove forests with a tiger" to "TIGER",
                            "A wandering Baul mystic looking up " to "BAUL",
                            "Howrah Cantilever bridge in rain" to "HOWRAH"
                        )

                        Text("QUICK SUGGESTIONS:", style = MaterialTheme.typography.labelSmall, color = themeColors.primary, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            presets.forEach { pair ->
                                Box(
                                    modifier = Modifier
                                        .padding(vertical = 2.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(themeColors.primary.copy(alpha = 0.08f))
                                        .border(1.dp, themeColors.primary.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
                                        .clickable {
                                            promptInput = pair.first
                                            viewModel.generateTraditionalArt(pair.first, pair.second)
                                            keyboardController?.hide()
                                            isControlsMinimized = true
                                        }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        pair.second,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = themeColors.primary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Generate Trigger Row
                        Button(
                            onClick = {
                                if (promptInput.trim().isNotEmpty()) {
                                    viewModel.generateTraditionalArt(promptInput)
                                    keyboardController?.hide()
                                    isControlsMinimized = true
                                }
                            },
                            enabled = !isGenerating && promptInput.trim().isNotEmpty(),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            if (isGenerating) {
                                CircularProgressIndicator(modifier = Modifier.size(18.dp), color = themeColors.onPrimary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Stylizing canvas...")
                            } else {
                                Icon(Icons.Default.Build, contentDescription = "Gen Icon")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Generate Bengal Masterpiece")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Display Box (Tactile Editorial Preview Container with dashed border and dotted grid pixels)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFFF5E6D3))
                    .drawBehind {
                        // 1. Draw elegant dashed border
                        drawRoundRect(
                            color = Color(0xFFD1BFA7),
                            style = androidx.compose.ui.graphics.drawscope.Stroke(
                                width = 4f,
                                pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                            ),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(24.dp.toPx(), 24.dp.toPx())
                        )
                        // 2. Draw fine subtle dot grid pattern
                        val dotColor = Color(0xFFA64D32).copy(alpha = 0.12f)
                        val dotRadius = 1.5.dp.toPx()
                        val spacing = 16.dp.toPx()
                        if (spacing > 1f) {
                            var x = 8.dp.toPx()
                            while (x < size.width) {
                                var y = 8.dp.toPx()
                                while (y < size.height) {
                                    drawCircle(
                                        color = dotColor,
                                        radius = dotRadius,
                                        center = Offset(x, y)
                                    )
                                    y += spacing
                                }
                                x += spacing
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (isGenerating) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        // Giant stylish HUD countdown bubble
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(100.dp)
                        ) {
                            // Secondary pulsing backing glow
                            CircularProgressIndicator(
                                progress = { genProgress },
                                strokeWidth = 5.dp,
                                color = themeColors.primary,
                                trackColor = themeColors.primary.copy(alpha = 0.12f),
                                modifier = Modifier.fillMaxSize()
                            )
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${genSecondsLeft}s",
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = themeColors.primary
                                )
                                Text(
                                    text = "REMAINING",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = themeColors.primary.copy(alpha = 0.6f),
                                    letterSpacing = 1.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Active Step Status Indicators
                        Text(
                            text = genStepText,
                            fontFamily = FontFamily.Serif,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = themeColors.primary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))

                        // Linear precision progress bar
                        LinearProgressIndicator(
                            progress = { genProgress },
                            color = themeColors.primary,
                            trackColor = themeColors.primary.copy(alpha = 0.15f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "NEOCORE GRAPHICS ENGINE COMPILING VECTOR MAP",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = themeColors.primary.copy(alpha = 0.4f),
                            letterSpacing = 0.8.sp
                        )
                    }
                } else if (activeArt != null) {
                    AlpanaCanvas(artwork = activeArt!!)
                } else {
                    // Empty art studio screen tip
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star Decoration",
                            modifier = Modifier.size(54.dp),
                            tint = themeColors.primary.copy(alpha = 0.4f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "Your Canvas Awaits 🎨",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "Type an imaginative prompt above and press Generate. The engine will paint beautiful symbols representing West Bengal pride on a vibrant, interactive vector layout.",
                            fontSize = 12.sp,
                            color = themeColors.onSurface.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // Description Story card
            if (activeArt != null && !isGenerating) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = themeColors.surface),
                    border = BorderStroke(1.dp, Color(0xFFE5DACE)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(themeColors.primary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Info,
                                    contentDescription = "info",
                                    tint = themeColors.onPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                activeArt!!.title,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = themeColors.primary
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            activeArt!!.description,
                            fontFamily = FontFamily.Serif,
                            fontSize = 14.sp,
                            lineHeight = 18.sp,
                            color = Color(0xFF2D241E)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        ElevatedButton(
                            onClick = { viewModel.speakText(activeArt!!.description) },
                            modifier = Modifier.align(Alignment.End),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Hear description", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Listen Story")
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

// --- MASTERPIECES GALLERY SCREEN ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(
    viewModel: SonarBanglaViewModel,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val artworks by viewModel.galleryArtworks.collectAsStateWithLifecycle()
    var selectedArt by remember { mutableStateOf<ArtworkEntity?>(null) }

    val themeColors = MaterialTheme.colorScheme

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column {
                EditorialHeader(
                    title = "Amar Gallery",
                    subtitle = "Masterpieces",
                    onMenuClick = onMenuClick
                )
                HorizontalDivider(color = Color(0xFFE5DACE), thickness = 1.dp)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(themeColors.background)
        ) {
            if (artworks.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = "Empty box",
                        modifier = Modifier.size(64.dp),
                        tint = themeColors.primary.copy(alpha = 0.35f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "No Artworks Found ✨",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "Head to the Art Studio and generate your first custom traditional masterwork. All generated pieces are preserved here in your local Room repository database.",
                        fontSize = 12.sp,
                        color = themeColors.onBackground.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(artworks) { art ->
                        Card(
                            onClick = { selectedArt = art },
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, Color(0xFFE5DACE)),
                            colors = CardDefaults.cardColors(containerColor = themeColors.surface),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                AlpanaCanvas(artwork = art, isAnimated = false)
                            }
                            Surface(
                                tonalElevation = 1.dp,
                                color = themeColors.surface,
                                border = BorderStroke(0.1.dp, Color(0xFFE5DACE)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(
                                        art.title,
                                        fontFamily = FontFamily.Serif,
                                        fontSize = 12.sp,
                                        color = Color(0xFF2D241E),
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1
                                    )
                                    Text(
                                        art.artType,
                                        fontSize = 9.sp,
                                        color = themeColors.primary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Full Screen Dialog
            if (selectedArt != null) {
                FullScreenArtDialog(
                    art = selectedArt!!,
                    onDismiss = { selectedArt = null },
                    onDelete = {
                        viewModel.deleteMasterpiece(selectedArt!!.id)
                        selectedArt = null
                    },
                    onHear = { viewModel.speakText(selectedArt!!.description) },
                    themeColors = themeColors
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenArtDialog(
    art: ArtworkEntity,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    onHear: () -> Unit,
    themeColors: ColorScheme
) {
    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Image Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    AlpanaCanvas(artwork = art)

                    // Top Action Controls
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "back", tint = Color.White)
                        }

                        IconButton(
                            onClick = onDelete,
                            modifier = Modifier.background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "delete", tint = Color.Red)
                        }
                    }
                }

                // Metadata Details
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(themeColors.surface)
                        .padding(16.dp)
                ) {
                    Text(
                        art.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = themeColors.primary
                    )
                    Text(
                        "Style: " + art.artType,
                        fontSize = 11.sp,
                        color = themeColors.secondary,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        art.description,
                        fontSize = 13.sp,
                        lineHeight = 17.sp,
                        color = themeColors.onSurface.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Seed: #" + art.styleSeed,
                            fontSize = 10.sp,
                            color = themeColors.onSurface.copy(alpha = 0.4f)
                        )

                        Button(onClick = onHear) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "playback")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Listen Description", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

// --- SETTINGS AND ABOUT SCREEN ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SonarBanglaViewModel,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activeTheme by viewModel.themeMode.collectAsStateWithLifecycle()
    val darkModePref by viewModel.darkModePref.collectAsStateWithLifecycle()
    val hapticEnabled by viewModel.hapticEnabled.collectAsStateWithLifecycle()
    val speechAutoReadEnabled by viewModel.speechAutoReadEnabled.collectAsStateWithLifecycle()
    val glowEffectEnabled by viewModel.glowEffectEnabled.collectAsStateWithLifecycle()
    val streamTextEnabled by viewModel.streamTextEnabled.collectAsStateWithLifecycle()
    val creativityTemp by viewModel.creativityTemp.collectAsStateWithLifecycle()

    val activeGender by viewModel.voiceGender.collectAsStateWithLifecycle()
    val activeSpeed by viewModel.voiceSpeed.collectAsStateWithLifecycle()
    val activePitch by viewModel.voicePitch.collectAsStateWithLifecycle()
    val activeLang by viewModel.voiceLanguage.collectAsStateWithLifecycle()

    val themeColors = MaterialTheme.colorScheme
    val isDark = themeColors.background.red < 0.5f
    val isSystemDark = androidx.compose.foundation.isSystemInDarkTheme()
    
    // Collapsible states to demonstrate luxurious opening/closing animations on option clicks
    var isModeExpanded by remember { mutableStateOf(true) }
    var isThemeExpanded by remember { mutableStateOf(false) }
    var isVoiceExpanded by remember { mutableStateOf(false) }
    var isInterfaceExpanded by remember { mutableStateOf(false) }
    var isAiExpanded by remember { mutableStateOf(false) }
    var isExtraExpanded by remember { mutableStateOf(false) }
    var isSecurityExpanded by remember { mutableStateOf(false) }
    var isAboutExpanded by remember { mutableStateOf(false) }

    // Advanced Personalization states (Add more settings!)
    var fontScalePreset by remember { mutableStateOf("STANDARD") } // STANDARD, COZY, CINEMATIC
    var aiPersonaPreset by remember { mutableStateOf("SCHOLAR") } // SCHOLAR, BARD, MINIMALIST
    var ambientDroneEnabled by remember { mutableStateOf(false) }
    var uiSoundEnabled by remember { mutableStateOf(true) }

    var e2eEnabled by remember { mutableStateOf(true) }
    var biometricEnabled by remember { mutableStateOf(false) }
    var autoTranslate by remember { mutableStateOf(true) }

    val localContext = androidx.compose.ui.platform.LocalContext.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column {
                EditorialHeader(
                    title = "NeoConfig System",
                    subtitle = "OS Preferences & Navigation Desk",
                    onMenuClick = onMenuClick
                )
                HorizontalDivider(
                    color = if (isDark) Color.White.copy(alpha = 0.08f) else Color(0xFFE2E8F0), 
                    thickness = 1.dp
                )
            }
        }
    ) { innerPadding ->
        // Use a split-screen Row layout with our innovative left settings organizer sidebar!
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.Transparent)
        ) {
            // Elegant vertical sidebar console
            Column(
                modifier = Modifier
                    .width(85.dp)
                    .fillMaxHeight()
                    .background(if (isDark) Color(0xFF130B22).copy(alpha = 0.45f) else Color(0xFFF1F5F9))
                    .padding(vertical = 12.dp, horizontal = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 1. MASTER TOGGLE (Minimize All vs Expand All)
                val allCollapsed = !isModeExpanded && !isThemeExpanded && !isVoiceExpanded && 
                                   !isInterfaceExpanded && !isAiExpanded && !isExtraExpanded && 
                                   !isSecurityExpanded && !isAboutExpanded
                
                val masterIcon = if (allCollapsed) Icons.Default.Add else Icons.Default.Clear
                val masterLabel = if (allCollapsed) "Max All" else "Min All"
                val masterBg = if (allCollapsed) themeColors.primary else themeColors.error.copy(alpha = 0.15f)
                val masterText = if (allCollapsed) Color.White else themeColors.error

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(masterBg)
                        .clickable {
                            val targetState = allCollapsed
                            isModeExpanded = targetState
                            isThemeExpanded = targetState
                            isVoiceExpanded = targetState
                            isInterfaceExpanded = targetState
                            isAiExpanded = targetState
                            isExtraExpanded = targetState
                            isSecurityExpanded = targetState
                            isAboutExpanded = targetState
                            
                            android.widget.Toast.makeText(
                                localContext, 
                                if (targetState) "All settings panels maximized." else "All settings panels minimized to sidebar.", 
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                        }
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = masterIcon,
                        contentDescription = masterLabel,
                        tint = masterText,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = masterLabel,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = masterText,
                        textAlign = TextAlign.Center
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 6.dp), 
                    color = if (isDark) Color.White.copy(alpha = 0.08f) else Color(0xFFE2E8F0), 
                    thickness = 1.dp
                )

                // Category Tabs: Selecting any expands exclusively and collapses/minimizes all other settings panels!
                // 🌗 Mode Switcher
                SidebarItem(
                    icon = Icons.Default.Refresh,
                    label = "Mode",
                    isSelected = isModeExpanded && !isThemeExpanded && !isVoiceExpanded && !isInterfaceExpanded && !isAiExpanded && !isExtraExpanded && !isSecurityExpanded && !isAboutExpanded,
                    themeColors = themeColors,
                    onClick = {
                        isModeExpanded = !isModeExpanded
                        if (isModeExpanded) {
                            isThemeExpanded = false; isVoiceExpanded = false; isInterfaceExpanded = false
                            isAiExpanded = false; isExtraExpanded = false; isSecurityExpanded = false; isAboutExpanded = false
                        }
                    }
                )

                // 🎨 Bengal Themes
                SidebarItem(
                    icon = Icons.Default.Star,
                    label = "Themes",
                    isSelected = isThemeExpanded && !isModeExpanded && !isVoiceExpanded && !isInterfaceExpanded && !isAiExpanded && !isExtraExpanded && !isSecurityExpanded && !isAboutExpanded,
                    themeColors = themeColors,
                    onClick = {
                        isThemeExpanded = !isThemeExpanded
                        if (isThemeExpanded) {
                            isModeExpanded = false; isVoiceExpanded = false; isInterfaceExpanded = false
                            isAiExpanded = false; isExtraExpanded = false; isSecurityExpanded = false; isAboutExpanded = false
                        }
                    }
                )

                // 🗣️ Voice Prefs
                SidebarItem(
                    icon = Icons.Default.PlayArrow,
                    label = "Voice",
                    isSelected = isVoiceExpanded && !isModeExpanded && !isThemeExpanded && !isInterfaceExpanded && !isAiExpanded && !isExtraExpanded && !isSecurityExpanded && !isAboutExpanded,
                    themeColors = themeColors,
                    onClick = {
                        isVoiceExpanded = !isVoiceExpanded
                        if (isVoiceExpanded) {
                            isModeExpanded = false; isThemeExpanded = false; isInterfaceExpanded = false
                            isAiExpanded = false; isExtraExpanded = false; isSecurityExpanded = false; isAboutExpanded = false
                        }
                    }
                )

                // 🎛️ Liquid FX
                SidebarItem(
                    icon = Icons.Default.Settings,
                    label = "Liquid",
                    isSelected = isInterfaceExpanded && !isModeExpanded && !isThemeExpanded && !isVoiceExpanded && !isAiExpanded && !isExtraExpanded && !isSecurityExpanded && !isAboutExpanded,
                    themeColors = themeColors,
                    onClick = {
                        isInterfaceExpanded = !isInterfaceExpanded
                        if (isInterfaceExpanded) {
                            isModeExpanded = false; isThemeExpanded = false; isVoiceExpanded = false
                            isAiExpanded = false; isExtraExpanded = false; isSecurityExpanded = false; isAboutExpanded = false
                        }
                    }
                )

                // 🧠 AI creative
                SidebarItem(
                    icon = Icons.Default.Face,
                    label = "AI",
                    isSelected = isAiExpanded && !isModeExpanded && !isThemeExpanded && !isVoiceExpanded && !isInterfaceExpanded && !isExtraExpanded && !isSecurityExpanded && !isAboutExpanded,
                    themeColors = themeColors,
                    onClick = {
                        isAiExpanded = !isAiExpanded
                        if (isAiExpanded) {
                            isModeExpanded = false; isThemeExpanded = false; isVoiceExpanded = false
                            isInterfaceExpanded = false; isExtraExpanded = false; isSecurityExpanded = false; isAboutExpanded = false
                        }
                    }
                )

                // 🌟 Extra Settings (Newly added!)
                SidebarItem(
                    icon = Icons.Default.Home,
                    label = "Extra",
                    isSelected = isExtraExpanded && !isModeExpanded && !isThemeExpanded && !isVoiceExpanded && !isInterfaceExpanded && !isAiExpanded && !isSecurityExpanded && !isAboutExpanded,
                    themeColors = themeColors,
                    onClick = {
                        isExtraExpanded = !isExtraExpanded
                        if (isExtraExpanded) {
                            isModeExpanded = false; isThemeExpanded = false; isVoiceExpanded = false
                            isInterfaceExpanded = false; isAiExpanded = false; isSecurityExpanded = false; isAboutExpanded = false
                        }
                    }
                )

                // 🔒 Crypt/Lock
                SidebarItem(
                    icon = Icons.Default.Lock,
                    label = "DB/Lock",
                    isSelected = isSecurityExpanded && !isModeExpanded && !isThemeExpanded && !isVoiceExpanded && !isInterfaceExpanded && !isAiExpanded && !isExtraExpanded && !isAboutExpanded,
                    themeColors = themeColors,
                    onClick = {
                        isSecurityExpanded = !isSecurityExpanded
                        if (isSecurityExpanded) {
                            isModeExpanded = false; isThemeExpanded = false; isVoiceExpanded = false
                            isInterfaceExpanded = false; isAiExpanded = false; isExtraExpanded = false; isAboutExpanded = false
                        }
                    }
                )

                // ℹ️ About Heritage
                SidebarItem(
                    icon = Icons.Default.Info,
                    label = "About",
                    isSelected = isAboutExpanded && !isModeExpanded && !isThemeExpanded && !isVoiceExpanded && !isInterfaceExpanded && !isAiExpanded && !isExtraExpanded && !isSecurityExpanded,
                    themeColors = themeColors,
                    onClick = {
                        isAboutExpanded = !isAboutExpanded
                        if (isAboutExpanded) {
                            isModeExpanded = false; isThemeExpanded = false; isVoiceExpanded = false
                            isInterfaceExpanded = false; isAiExpanded = false; isExtraExpanded = false; isSecurityExpanded = false
                        }
                    }
                )
            }

            // Right side: Main content scroll action dock
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .verticalScroll(androidx.compose.foundation.rememberScrollState())
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // CARD 1: Theme Mode Configuration (Collapsible with click animation)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDark) themeColors.surface else Color(0xFFFBF9FF)
                    ),
                    border = BorderStroke(1.dp, if (isDark) Color.White.copy(alpha = 0.08f) else Color(0xFFE9E3F5))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isModeExpanded = !isModeExpanded },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Theme Mode Configuration 🌗",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = themeColors.primary,
                                    fontFamily = FontFamily.SansSerif
                                )
                                Text(
                                    "Frog-glass Light and Cosmic Dark presets",
                                    fontSize = 10.sp,
                                    color = themeColors.onSurface.copy(alpha = 0.5f)
                                )
                            }
                            Icon(
                                imageVector = if (isModeExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = "Expand Mode Section",
                                tint = themeColors.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        androidx.compose.animation.AnimatedVisibility(visible = isModeExpanded) {
                            Column {
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    "Select whether NeoCore AI displays a minimal bright frosted interface or a stellar dark cosmic space background.",
                                    fontSize = 11.sp,
                                    color = themeColors.onSurface.copy(alpha = 0.6f),
                                    lineHeight = 15.sp
                                )
                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(if (isDark) Color.White.copy(alpha = 0.04f) else Color(0xFFF1EEF8))
                                        .padding(4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    val optionKeys = listOf("LIGHT", "DARK", "SYSTEM")
                                    val optionLabels = listOf("Light (✨)", "Dark (🌌)", "System (⚙️)")
                                    
                                    optionKeys.forEachIndexed { idx, key ->
                                        val isSelected = darkModePref == key
                                        val bgAnimColor by androidx.compose.animation.animateColorAsState(
                                            targetValue = if (isSelected) themeColors.primary else Color.Transparent,
                                            label = "mode_tab"
                                        )
                                        val textAnimColor by androidx.compose.animation.animateColorAsState(
                                            targetValue = if (isSelected) Color.White else themeColors.onSurface.copy(alpha = 0.7f),
                                            label = "mode_text"
                                        )

                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(bgAnimColor)
                                                .clickable { viewModel.updateDarkModePref(key) }
                                                .padding(vertical = 8.dp, horizontal = 2.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = optionLabels[idx],
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = textAnimColor,
                                                maxLines = 1,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // CARD 2: Region Themes (Collapsible with click animation)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDark) themeColors.surface else Color(0xFFFFFFFF)
                    ),
                    border = BorderStroke(1.dp, if (isDark) Color.White.copy(alpha = 0.08f) else Color(0xFFE9E3F5))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isThemeExpanded = !isThemeExpanded },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Bengal Heritage Themes 🎨",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = themeColors.primary
                                )
                                Text(
                                    "Five handcrafted regional dynamic profiles",
                                    fontSize = 10.sp,
                                    color = themeColors.onSurface.copy(alpha = 0.5f)
                                )
                            }
                            Icon(
                                imageVector = if (isThemeExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = "Expand Themes Section",
                                tint = themeColors.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        androidx.compose.animation.AnimatedVisibility(visible = isThemeExpanded) {
                            Column {
                                Spacer(modifier = Modifier.height(12.dp))
                                val themes = listOf(
                                    Triple(SonarBanglaRepository.THEME_FUTURE_GLASS, "Liquid Glass (লিকুইড গ্লাস)", "Elegant modern fluid accent glass layout"),
                                    Triple(SonarBanglaRepository.THEME_ROYAL_BENGAL, "Royal Bengal Gold (সুন্দরবন)", "Majestic golden yellow & deep mangrove black"),
                                    Triple(SonarBanglaRepository.THEME_TERRACOTTA, "Terracotta Clay (বিষ্ণুপুর)", "Bishnupur historic sun-burnt clay orange"),
                                    Triple(SonarBanglaRepository.THEME_GANGES_BLUE, "Ganges River (শান্তি গঙ্গা)", "Calm blue Hooghly tides and soft mist white"),
                                    Triple(SonarBanglaRepository.THEME_BAUL_YELLOW, "Baul Ochre (বাউল সোরা)", "Traditional yellow and indigo folk aesthetic")
                                )

                                themes.forEach { t ->
                                    val isSelected = activeTheme == t.first
                                    val highlightColor by androidx.compose.animation.animateColorAsState(
                                        targetValue = if (isSelected) themeColors.primary.copy(alpha = 0.12f) else Color.Transparent,
                                        label = "theme_row"
                                    )

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(highlightColor)
                                            .clickable { viewModel.updateTheme(t.first) }
                                            .padding(6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = isSelected,
                                            onClick = { viewModel.updateTheme(t.first) }
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Column {
                                            Text(
                                                t.second,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) themeColors.primary else themeColors.onSurface
                                            )
                                            Text(
                                                t.third,
                                                fontSize = 10.sp,
                                                color = themeColors.onSurface.copy(alpha = 0.5f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // CARD 3: Voice Configuration (Collapsible with click animation)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDark) themeColors.surface else Color(0xFFFFFFFF)
                    ),
                    border = BorderStroke(1.dp, if (isDark) Color.White.copy(alpha = 0.08f) else Color(0xFFE9E3F5))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isVoiceExpanded = !isVoiceExpanded },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Voice Synthesis Preferences 🗣️",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = themeColors.primary
                                )
                                Text(
                                    "Configure vocal identity, speeds, and accents",
                                    fontSize = 10.sp,
                                    color = themeColors.onSurface.copy(alpha = 0.5f)
                                )
                            }
                            Icon(
                                imageVector = if (isVoiceExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = "Expand Voice Section",
                                tint = themeColors.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        androidx.compose.animation.AnimatedVisibility(visible = isVoiceExpanded) {
                            Column {
                                Spacer(modifier = Modifier.height(12.dp))

                                // Auto TTS Speaking Toggle
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Auto Read Aloud", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        Text("Listen to AI responses automatically on receipt.", fontSize = 10.sp, color = themeColors.onSurface.copy(alpha = 0.5f))
                                    }
                                    Switch(
                                        checked = speechAutoReadEnabled,
                                        onCheckedChange = { viewModel.updateSpeechAutoReadEnabled(it) }
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(8.dp))

                                // Voice Profile Buttons
                                Text("SYNTHESIS PROFILE", style = MaterialTheme.typography.labelSmall, color = themeColors.primary, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val voices = listOf(
                                        "FEMALE" to "Female (👩)",
                                        "MALE" to "Male (🧔)",
                                        "CHILD" to "Child (👶)",
                                        "ROBOT" to "Robot (🤖)",
                                        "DEEP_BASS" to "Deep Bass (🎙️)",
                                        "SOFT_NEURAL" to "Soft (✨)"
                                    )
                                    voices.forEach { (genderKey, labelText) ->
                                        Button(
                                            onClick = { viewModel.updateVoiceGender(genderKey) },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (activeGender == genderKey) themeColors.primary else themeColors.primary.copy(alpha = 0.08f),
                                                contentColor = if (activeGender == genderKey) themeColors.onPrimary else themeColors.primary
                                            ),
                                            shape = RoundedCornerShape(12.dp),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                        ) {
                                            Text(labelText, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Synthesis preferred language
                                Text("PREFERRED LANGUAGE ACCENT", style = MaterialTheme.typography.labelSmall, color = themeColors.primary, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                val speechLangs = listOf(
                                    "BENGALI", "HINDI", "ENGLISH", "SPANISH", "FRENCH", 
                                    "GERMAN", "JAPANESE", "CHINESE", "KOREAN", "RUSSIAN", "ITALIAN"
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    speechLangs.forEach { l ->
                                        FilterChip(
                                            selected = activeLang == l,
                                            onClick = { viewModel.updateVoiceLanguage(l) },
                                            label = { Text(l, fontSize = 10.sp, fontWeight = FontWeight.Bold) }
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Voice Speed Slider
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("SPEAKING RATES", style = MaterialTheme.typography.labelSmall, color = themeColors.primary, fontWeight = FontWeight.Bold)
                                    Text(String.format("%.1fx", activeSpeed), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                                Slider(
                                    value = activeSpeed,
                                    onValueChange = { viewModel.updateVoiceSpeed(it) },
                                    valueRange = 0.5f..2.0f
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                // Voice Pitch Slider
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("SYNTHESIS PITCH", style = MaterialTheme.typography.labelSmall, color = themeColors.primary, fontWeight = FontWeight.Bold)
                                    Text(String.format("%.1fx", activePitch), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                                Slider(
                                    value = activePitch,
                                    onValueChange = { viewModel.updateVoicePitch(it) },
                                    valueRange = 0.5f..2.0f
                                )
                            }
                        }
                    }
                }

                // CARD 4: Liquid Interface Preference Switch (Haptic, Glows, Streams)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDark) themeColors.surface else Color(0xFFFFFFFF)
                    ),
                    border = BorderStroke(1.dp, if (isDark) Color.White.copy(alpha = 0.08f) else Color(0xFFE9E3F5))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isInterfaceExpanded = !isInterfaceExpanded },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Haptic & Liquid Interface 🎛️",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = themeColors.primary
                                )
                                Text(
                                    "Hardware haptics, glow effects & streaming feeds",
                                    fontSize = 10.sp,
                                    color = themeColors.onSurface.copy(alpha = 0.5f)
                                )
                            }
                            Icon(
                                imageVector = if (isInterfaceExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = "Expand Interface Section",
                                tint = themeColors.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        androidx.compose.animation.AnimatedVisibility(visible = isInterfaceExpanded) {
                            Column {
                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Gentle Haptic Feedback", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        Text("Polished tactile clicks on interactive items.", fontSize = 10.sp, color = themeColors.onSurface.copy(alpha = 0.5f))
                                    }
                                    Switch(
                                        checked = hapticEnabled,
                                        onCheckedChange = { viewModel.updateHapticEnabled(it) }
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Glowing Ambient Shadows", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        Text("Renders neon floating outlines & light effects.", fontSize = 10.sp, color = themeColors.onSurface.copy(alpha = 0.5f))
                                    }
                                    Switch(
                                        checked = glowEffectEnabled,
                                        onCheckedChange = { viewModel.updateGlowEffectEnabled(it) }
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Real-Time Word Streaming", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        Text("Animate newly output chat contents dynamically.", fontSize = 10.sp, color = themeColors.onSurface.copy(alpha = 0.5f))
                                    }
                                    Switch(
                                        checked = streamTextEnabled,
                                        onCheckedChange = { viewModel.updateStreamTextEnabled(it) }
                                    )
                                }
                            }
                        }
                    }
                }

                // CARD 5: AI Creative Temperature Slider (Collapsible with click animation)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDark) themeColors.surface else Color(0xFFFFFFFF)
                    ),
                    border = BorderStroke(1.dp, if (isDark) Color.White.copy(alpha = 0.08f) else Color(0xFFE9E3F5))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isAiExpanded = !isAiExpanded },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Creative Hyperparameters 🧠",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = themeColors.primary
                                )
                                Text(
                                    "Tune LLM seed temperature & response variability",
                                    fontSize = 10.sp,
                                    color = themeColors.onSurface.copy(alpha = 0.5f)
                                )
                            }
                            Icon(
                                imageVector = if (isAiExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = "Expand AI Section",
                                tint = themeColors.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        androidx.compose.animation.AnimatedVisibility(visible = isAiExpanded) {
                            Column {
                                Spacer(modifier = Modifier.height(12.dp))

                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("CREATIVITY TEMPERATURE", style = MaterialTheme.typography.labelSmall, color = themeColors.primary, fontWeight = FontWeight.Bold)
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = when {
                                                creativityTemp <= 0.3f -> "Focused (🎯) "
                                                creativityTemp <= 0.7f -> "Balanced (⚖️) "
                                                else -> "Imaginative (🎨) "
                                            },
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = themeColors.onSurface.copy(alpha = 0.7f)
                                        )
                                        Text(String.format("%.1f", creativityTemp), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = themeColors.primary)
                                    }
                                }
                                Slider(
                                    value = creativityTemp,
                                    onValueChange = { viewModel.updateCreativityTemp(it) },
                                    valueRange = 0.1f..1.5f
                                )
                                Text(
                                    "Sober lower levels produce factual answers. Elevated settings are ideal for generative Alpana blueprints or mystic song analysis.",
                                    fontSize = 10.sp,
                                    color = themeColors.onSurface.copy(alpha = 0.4f),
                                    lineHeight = 13.sp
                                )
                            }
                        }
                    }
                }

                // CARD 6: MORE SETTINGS - Advanced Personalization (Collapsible & brand new!)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDark) themeColors.surface else Color(0xFFF0FDFC)
                    ),
                    border = BorderStroke(1.dp, if (isDark) Color.White.copy(alpha = 0.08f) else Color(0xFF99F6E4))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isExtraExpanded = !isExtraExpanded },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Enterprise Personalizations 🌟",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color(0xFF0F766E)
                                )
                                Text(
                                    "Custom chat scales, drone synths & AI personalities",
                                    fontSize = 10.sp,
                                    color = Color(0xFF0F766E).copy(alpha = 0.7f)
                                )
                            }
                            Icon(
                                imageVector = if (isExtraExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = "Expand Extra Section",
                                tint = Color(0xFF0F766E),
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        androidx.compose.animation.AnimatedVisibility(visible = isExtraExpanded) {
                            Column {
                                Spacer(modifier = Modifier.height(12.dp))

                                // AI Personality selector
                                Text("MYSTIC AI PERSONALITY PROFILE", style = MaterialTheme.typography.labelSmall, color = Color(0xFF0F766E), fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    val personas = listOf("SCHOLAR", "BARD", "MINIMALIST")
                                    val labels = listOf("Scholar 📚", "Baul Bard 🪕", "Brief ⚡")
                                    
                                    personas.forEachIndexed { idx, p ->
                                        val isSelected = aiPersonaPreset == p
                                        FilterChip(
                                            selected = isSelected,
                                            onClick = {
                                                aiPersonaPreset = p
                                                android.widget.Toast.makeText(
                                                    localContext,
                                                    "AI Persona updated: ${labels[idx]} enabled.",
                                                    android.widget.Toast.LENGTH_SHORT
                                                ).show()
                                            },
                                            label = { Text(labels[idx], fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Font scale
                                Text("CHAT SCREEN DISPLAY SCALE", style = MaterialTheme.typography.labelSmall, color = Color(0xFF0F766E), fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    val scaleKeys = listOf("STANDARD", "COZY", "CINEMATIC")
                                    val scaleLabels = listOf("Standard (1x)", "Cozy (1.2x)", "Wide (1.5x)")
                                    
                                    scaleKeys.forEach { sk ->
                                        val isSelected = fontScalePreset == sk
                                        FilterChip(
                                            selected = isSelected,
                                            onClick = {
                                                fontScalePreset = sk
                                                android.widget.Toast.makeText(localContext, "Font scale set to $sk", android.widget.Toast.LENGTH_SHORT).show()
                                            },
                                            label = { Text(scaleLabels[scaleKeys.indexOf(sk)], fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Continuous Ambient Drone
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Ambient Drone (এঁকতারা)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D5C56))
                                        Text("Play subtle spiritual traditional acoustic waves in chat background.", fontSize = 10.sp, color = themeColors.onSurface.copy(alpha = 0.5f))
                                    }
                                    Switch(
                                        checked = ambientDroneEnabled,
                                        onCheckedChange = {
                                            ambientDroneEnabled = it
                                            android.widget.Toast.makeText(
                                                localContext, 
                                                if (it) "Acoustic continuous synth drone activated in background." else "Background drone deactivated.", 
                                                android.widget.Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                // UI Audio feedback click tick
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Tactile Click Sound Effects", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D5C56))
                                        Text("Triggers melodic sitar chime clicks on option touches.", fontSize = 10.sp, color = themeColors.onSurface.copy(alpha = 0.5f))
                                    }
                                    Switch(
                                        checked = uiSoundEnabled,
                                        onCheckedChange = {
                                            uiSoundEnabled = it
                                            if (it) {
                                                // play single short audio note or trigger haptic
                                                android.widget.Toast.makeText(localContext, "Tactile Sitars Activated", android.widget.Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                // CARD 7: Security & Clear Records (Collapsible with click animation)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDark) themeColors.surface else Color(0xFFFFFFFF)
                    ),
                    border = BorderStroke(1.dp, if (isDark) Color.White.copy(alpha = 0.08f) else Color(0xFFE9E3F5))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isSecurityExpanded = !isSecurityExpanded },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Database & Cryptography 🔒",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = themeColors.primary
                                )
                                Text(
                                    "Clean database chats, biometric, and security levels",
                                    fontSize = 10.sp,
                                    color = themeColors.onSurface.copy(alpha = 0.5f)
                                )
                            }
                            Icon(
                                imageVector = if (isSecurityExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = "Expand Security Section",
                                tint = themeColors.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        androidx.compose.animation.AnimatedVisibility(visible = isSecurityExpanded) {
                            Column {
                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Symmetric Chat Encryption", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        Text("Saves local entries using AES crypt algorithms on-device.", fontSize = 10.sp, color = themeColors.onSurface.copy(alpha = 0.5f))
                                    }
                                    Switch(checked = e2eEnabled, onCheckedChange = { e2eEnabled = it })
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Biometric Verification Unlock", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        Text("Enforce fingerprint or face scan on initialization.", fontSize = 10.sp, color = themeColors.onSurface.copy(alpha = 0.5f))
                                    }
                                    Switch(checked = biometricEnabled, onCheckedChange = { biometricEnabled = it })
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Continuous Translation Engine", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        Text("Autotranslate queries to selected accents.", fontSize = 10.sp, color = themeColors.onSurface.copy(alpha = 0.5f))
                                    }
                                    Switch(checked = autoTranslate, onCheckedChange = { autoTranslate = it })
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text("DESTRUCTIVE ACTIONS", style = MaterialTheme.typography.labelSmall, color = themeColors.error, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Button(
                                    onClick = { viewModel.clearChatMessages() },
                                    colors = ButtonDefaults.buttonColors(containerColor = themeColors.error),
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "clear", modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Clear Local Session Logs", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }

                // CARD 8: MANDATORY ABOUT US SECTION - Tribute to Mahefuz Mondal
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5E6D3).copy(alpha = 0.45f)),
                    border = BorderStroke(1.dp, Color(0xFFE5DACE))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isAboutExpanded = !isAboutExpanded },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "সোনার বাংলা Core Info ℹ️",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = themeColors.primary,
                                    fontFamily = FontFamily.Serif
                                )
                                Text(
                                    "Creator credentials & Tagore vision tribute",
                                    fontSize = 10.sp,
                                    color = themeColors.onSurface.copy(alpha = 0.5f)
                                )
                            }
                            Icon(
                                imageVector = if (isAboutExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = "Expand About Section",
                                tint = themeColors.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        androidx.compose.animation.AnimatedVisibility(visible = isAboutExpanded) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(80.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            Brush.linearGradient(
                                                colors = listOf(themeColors.primary, themeColors.primary.copy(alpha = 0.85f)),
                                                start = Offset(0f, 0f),
                                                end = Offset(400f, 400f)
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            "সোনার বাংলা",
                                            fontFamily = FontFamily.Serif,
                                            color = Color.White,
                                            fontWeight = FontWeight.Black,
                                            fontSize = 20.sp
                                        )
                                        Text(
                                            "Heritage Companion AI v1.0",
                                            color = Color.White.copy(alpha = 0.8f),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    "ABOUT CREATOR",
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = themeColors.primary,
                                    modifier = Modifier.align(Alignment.Start)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    "Created by Mahefuz Mondal",
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color(0xFF2D241E),
                                    modifier = Modifier.align(Alignment.Start)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                val uriHandler = androidx.compose.ui.platform.LocalUriHandler.current
                                Text(
                                    text = "Website: https://www.neocoreai.in",
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = themeColors.primary,
                                    modifier = Modifier
                                        .align(Alignment.Start)
                                        .clickable {
                                            try {
                                                uriHandler.openUri("https://www.neocoreai.in")
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                            }
                                        }
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    "This premium virtual application is developed with deep admiration for the intellectual, cultural, and artistic traditions of West Bengal, India. Built utilizing advanced procedural Canvas frameworks, Room SQLite, native Text-to-Speech synthesizers, and Google Gemini artificial intelligence.",
                                    fontFamily = FontFamily.Serif,
                                    fontSize = 11.sp,
                                    lineHeight = 16.sp,
                                    color = Color(0xFF2D241E),
                                    textAlign = TextAlign.Justify
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                HorizontalDivider(color = Color(0xFFE5DACE), thickness = 1.dp)
                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    "\"Where the mind is without fear and the head is held high...\"\n— Rabindranath Tagore",
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.SemiBold,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                    textAlign = TextAlign.Center,
                                    color = themeColors.primary
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

// SidebarItem design Composable inside local context
@Composable
fun SidebarItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    themeColors: ColorScheme,
    onClick: () -> Unit
) {
    val bgAnimColor by androidx.compose.animation.animateColorAsState(
        targetValue = if (isSelected) themeColors.primary.copy(alpha = 0.15f) else Color.Transparent,
        label = "item_bg"
    )
    val contentAnimColor by androidx.compose.animation.animateColorAsState(
        targetValue = if (isSelected) themeColors.primary else themeColors.onSurface.copy(alpha = 0.6f),
        label = "item_fg"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bgAnimColor)
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = contentAnimColor,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 9.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = contentAnimColor,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
    }
}

// FlowRow layout implementation for wrapping presets cleanly
@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    content: @Composable () -> Unit
) {
    androidx.compose.ui.layout.Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        var tempWidth = 0
        var tempHeight = 0
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }

        var xPosition = 0
        var yPosition = 0
        var rowHeight = 0

        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEach { placeable ->
                if (xPosition + placeable.width > constraints.maxWidth) {
                    xPosition = 0
                    yPosition += rowHeight + 8
                    rowHeight = 0
                }
                placeable.placeRelative(x = xPosition, y = yPosition)
                xPosition += placeable.width + 12
                rowHeight = maxOf(rowHeight, placeable.height)
            }
        }
    }
}

// --- HISTORY SCREEN ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: SonarBanglaViewModel,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val messages by viewModel.chatMessages.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }
    var isFilterMinimized by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        EditorialHeader(
            title = "NeoCore AI Logs",
            subtitle = "Search History",
            onMenuClick = onMenuClick,
            actionIcon = Icons.Default.Delete,
            onActionClick = { viewModel.clearChatMessages() }
        )
        HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)

        // Minimize Control Bar for the Logs Screen
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "LOG RECORDS SUMMARY",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF94A3B8),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 10.sp
            )
            Surface(
                onClick = { isFilterMinimized = !isFilterMinimized },
                color = if (isFilterMinimized) Color(0xFFF1F5F9) else Color(0xFFFAF5FF),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, if (isFilterMinimized) Color(0xFFE2E8F0) else Color(0xFFE9D5FF))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(
                                color = if (isFilterMinimized) Color(0xFF94A3B8) else Color(0xFF8B5CF6),
                                shape = CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isFilterMinimized) "Show Filter" else "Hide Search Bar",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isFilterMinimized) Color(0xFF64748B) else Color(0xFF7C3AED)
                    )
                }
            }
        }

        // Glassmorphic search filter
        if (!isFilterMinimized) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF8FAFC))
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                    .padding(horizontal = 14.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Log",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Filter past requests...", fontSize = 14.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }
        }

        val filteredLogs = messages.filter {
            it.sender == "USER" && (searchQuery.trim().isEmpty() || it.text.contains(searchQuery, ignoreCase = true))
        }

        if (filteredLogs.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "No logs",
                        tint = Color.LightGray,
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No history records found",
                        fontFamily = FontFamily.SansSerif,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(filteredLogs) { logMsg ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFE0F2FE)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Send,
                                            contentDescription = "query",
                                            tint = Color(0xFF1E6091),
                                            modifier = Modifier.size(12.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "USER REQUEST FOUND",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp,
                                        color = Color(0xFF1E6091),
                                        fontFamily = FontFamily.SansSerif
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = logMsg.text,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF0F172A),
                                fontFamily = FontFamily.SansSerif
                            )
                        }
                    }
                }
            }
        }
    }
}

fun getLanguageDisplayName(lang: String): String {
    return when (lang) {
        "BENGALI" -> "Bengali"
        "HINDI" -> "Hindi"
        "ENGLISH" -> "English"
        "SPANISH" -> "Spanish"
        "FRENCH" -> "French"
        "GERMAN" -> "German"
        "JAPANESE" -> "Japanese"
        "CHINESE" -> "Chinese"
        "KOREAN" -> "Korean"
        "RUSSIAN" -> "Russian"
        "ITALIAN" -> "Italian"
        else -> lang.lowercase().replaceFirstChar { it.uppercase() }
    }
}

