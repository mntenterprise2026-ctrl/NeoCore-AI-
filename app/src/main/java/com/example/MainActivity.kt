package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.SonarBanglaViewModel
import com.example.ui.screens.*
import com.example.ui.theme.SonarBanglaTheme
import com.example.ui.theme.GoogleSans
import com.example.ui.theme.GoogleSansBold
import kotlinx.coroutines.launch
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.BorderStroke

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: SonarBanglaViewModel = viewModel()
            val activeTheme by viewModel.themeMode.collectAsState()
            val darkModePref by viewModel.darkModePref.collectAsState()
            val systemDark = isSystemInDarkTheme()
            val isDark = when (darkModePref) {
                "LIGHT" -> false
                "DARK" -> true
                else -> systemDark
            }

            SonarBanglaTheme(themeMode = activeTheme, darkTheme = isDark) {
                val themeColors = MaterialTheme.colorScheme
                var currentTab by remember { mutableIntStateOf(0) }
                var isSidebarOpen by remember { mutableStateOf(false) }
                var showMemoryDialog by remember { mutableStateOf(false) }
                var showAboutDialog by remember { mutableStateOf(false) }
                val scope = rememberCoroutineScope()
                val uriHandler = androidx.compose.ui.platform.LocalUriHandler.current
                val context = androidx.compose.ui.platform.LocalContext.current

                Box(modifier = Modifier.fillMaxSize()) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize()
                    ) { innerPadding ->
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            when (currentTab) {
                                0 -> ChatScreen(
                                    viewModel = viewModel, 
                                    onMenuClick = { isSidebarOpen = true }
                                )
                                1 -> HistoryScreen(
                                    viewModel = viewModel,
                                    onMenuClick = { isSidebarOpen = true }
                                )
                                2 -> ArtStudioScreen(
                                    viewModel = viewModel,
                                    onMenuClick = { isSidebarOpen = true }
                                )
                                3 -> GalleryScreen(
                                    viewModel = viewModel,
                                    onMenuClick = { isSidebarOpen = true }
                                )
                                4 -> SettingsScreen(
                                    viewModel = viewModel,
                                    onMenuClick = { isSidebarOpen = true }
                                )
                            }
                        }
                    }

                    // Scrim overlay
                    AnimatedVisibility(
                        visible = isSidebarOpen,
                        enter = fadeIn(animationSpec = tween(300)),
                        exit = fadeOut(animationSpec = tween(250))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.4f))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    isSidebarOpen = false
                                }
                        )
                    }

                    // Floating Glass Panel Sidebar
                    AnimatedVisibility(
                        visible = isSidebarOpen,
                        enter = slideInHorizontally(
                            initialOffsetX = { -it },
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        ) + fadeIn(animationSpec = tween(300)),
                        exit = slideOutHorizontally(
                            targetOffsetX = { -it },
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioNoBouncy,
                                stiffness = Spring.StiffnessMedium
                            )
                        ) + fadeOut(animationSpec = tween(200)),
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(280.dp)
                            .padding(16.dp) // External padding creates detached look
                            .align(Alignment.CenterStart)
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            // Ambient lavender & purple glow shadow layer
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .offset(x = 1.dp, y = 3.dp)
                                    .background(
                                        Brush.radialGradient(
                                            colors = listOf(
                                                Color(0x2E8B5CF6), // Purple glow
                                                Color(0x0FCE4899), // Pink glow
                                                Color.Transparent
                                            )
                                        ),
                                        shape = RoundedCornerShape(32.dp)
                                    )
                            )

                            // Glass Panel Container
                            Card(
                                modifier = Modifier.fillMaxSize(),
                                shape = RoundedCornerShape(32.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xF2FDFBFF) // White & light lavender translucency vibe
                                ),
                                border = BorderStroke(1.dp, Color(0x288B5CF6))
                            ) {
                                Column(modifier = Modifier.fillMaxSize()) {
                                    // Header
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 24.dp, bottom = 12.dp, start = 18.dp, end = 18.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Futuristic rounded "N" logo inside a glass badge
                                        Box(
                                            modifier = Modifier
                                                .size(44.dp)
                                                .clip(RoundedCornerShape(14.dp))
                                                .background(
                                                    Brush.linearGradient(
                                                        colors = listOf(
                                                            Color(0xFF8B5CF6),
                                                            Color(0xFFD8B4FE)
                                                        )
                                                    )
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "N",
                                                color = Color.White,
                                                fontWeight = FontWeight.Black,
                                                fontSize = 22.sp,
                                                fontFamily = GoogleSans
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = "NeoCore AI",
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Black,
                                                fontFamily = GoogleSansBold,
                                                color = Color.Black,
                                                letterSpacing = (-0.5).sp
                                            )
                                            Text(
                                                text = "Futuristic Assistant",
                                                fontSize = 11.sp,
                                                fontFamily = GoogleSans,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF7C3AED)
                                            )
                                        }
                                    }

                                    HorizontalDivider(color = Color(0x108B5CF6), thickness = 1.dp, modifier = Modifier.padding(horizontal = 18.dp))
                                    Spacer(modifier = Modifier.height(14.dp))

                                    // Navigation Menu
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .verticalScroll(rememberScrollState())
                                            .padding(horizontal = 8.dp)
                                    ) {
                                        val hasNoMessages = viewModel.chatMessages.collectAsState().value.isEmpty()
                                        
                                        // 1. New Chat
                                        FloatingSidebarItem(
                                            label = "New Chat",
                                            icon = Icons.Default.Add,
                                            isSelected = currentTab == 0 && hasNoMessages,
                                            onClick = {
                                                viewModel.clearChatMessages()
                                                currentTab = 0
                                                isSidebarOpen = false
                                            }
                                        )

                                        // 2. Chats History
                                        FloatingSidebarItem(
                                            label = "Chats History",
                                            icon = Icons.AutoMirrored.Filled.List,
                                            isSelected = currentTab == 1,
                                            onClick = {
                                                currentTab = 1
                                                isSidebarOpen = false
                                            }
                                        )

                                        // 3. Image Studio
                                        FloatingSidebarItem(
                                            label = "Image Studio",
                                            icon = Icons.Default.Edit,
                                            isSelected = currentTab == 2,
                                            onClick = {
                                                currentTab = 2
                                                isSidebarOpen = false
                                            }
                                        )

                                        // 4. Voice Mode
                                        FloatingSidebarItem(
                                            label = "Voice Mode",
                                            icon = Icons.Default.PlayArrow,
                                            isSelected = currentTab == 4,
                                            onClick = {
                                                currentTab = 4
                                                isSidebarOpen = false
                                            }
                                        )

                                        // 5. Gallery
                                        FloatingSidebarItem(
                                            label = "Gallery",
                                            icon = Icons.Default.Star,
                                            isSelected = currentTab == 3,
                                            onClick = {
                                                currentTab = 3
                                                isSidebarOpen = false
                                            }
                                        )

                                        // 6. Memory
                                        FloatingSidebarItem(
                                            label = "Memory",
                                            icon = Icons.Default.Face,
                                            isSelected = false,
                                            onClick = {
                                                showMemoryDialog = true
                                                isSidebarOpen = false
                                            }
                                        )

                                        // 7. Settings
                                        FloatingSidebarItem(
                                            label = "Settings",
                                            icon = Icons.Default.Settings,
                                            isSelected = currentTab == 4,
                                            onClick = {
                                                currentTab = 4
                                                isSidebarOpen = false
                                            }
                                        )

                                        // 8. About NeoCore AI
                                        FloatingSidebarItem(
                                            label = "About NeoCore AI",
                                            icon = Icons.Default.Info,
                                            isSelected = false,
                                            onClick = {
                                                showAboutDialog = true
                                                isSidebarOpen = false
                                            }
                                        )
                                    }

                                    // Tribute Footer
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 18.dp, vertical = 18.dp)
                                    ) {
                                        HorizontalDivider(color = Color(0x108B5CF6), thickness = 1.dp)
                                        Spacer(modifier = Modifier.height(14.dp))
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(32.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(0x158B5CF6)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Info,
                                                    contentDescription = null,
                                                    tint = Color(0xFF6D28D9),
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Column {
                                                Text(
                                                    text = "Mahefuz Mondal",
                                                    fontFamily = GoogleSansBold,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 11.5.sp,
                                                    color = Color.Black
                                                )
                                                Text(
                                                    text = "neocoreai.in",
                                                    fontSize = 10.sp,
                                                    fontFamily = GoogleSans,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF7C3AED),
                                                    modifier = Modifier.clickable {
                                                        try {
                                                            uriHandler.openUri("https://www.neocoreai.in")
                                                        } catch (e: Exception) {
                                                            e.printStackTrace()
                                                        }
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Memory Dialog
                if (showMemoryDialog) {
                    AlertDialog(
                        onDismissRequest = { showMemoryDialog = false },
                        confirmButton = {
                            TextButton(onClick = { showMemoryDialog = false }) {
                                Text("Done", color = Color(0xFF6D28D9), fontWeight = FontWeight.Bold)
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    android.widget.Toast.makeText(context, "AI Memory cleared & flushed", android.widget.Toast.LENGTH_SHORT).show()
                                    showMemoryDialog = false
                                }
                            ) {
                                Text("Clear Memory", color = Color.Red.copy(alpha = 0.8f))
                            }
                        },
                        title = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Face,
                                    contentDescription = null,
                                    tint = Color(0xFF8B5CF6),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    "NeoCore AI Memory",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.Black
                                )
                            }
                        },
                        text = {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text(
                                    "NeoCore AI maintains a secure context of your localized workspace preferences to personalize responses automatically:",
                                    fontSize = 13.sp,
                                    color = Color(0xFF475569)
                                )
                                
                                val memories = listOf(
                                    "🗣️ Active Voice: Custom settings sync template",
                                    "🌎 Lang Profile: Bengali / English blend",
                                    "🎨 Canvas Target: Custom White Alpana vector graphics",
                                    "⚡ System Speed: 1.0x Realtime TTS assistant playback",
                                    "🔒 Privacy State: Localized context sandbox (On-Device)"
                                )
                                
                                memories.forEach { memory ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color(0xFFF5F3FF), RoundedCornerShape(8.dp))
                                            .padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = memory,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFF6D28D9)
                                        )
                                    }
                                }
                            }
                        },
                        containerColor = Color.White,
                        shape = RoundedCornerShape(24.dp)
                    )
                }

                // About Dialog
                if (showAboutDialog) {
                    AlertDialog(
                        onDismissRequest = { showAboutDialog = false },
                        confirmButton = {
                            TextButton(onClick = { showAboutDialog = false }) {
                                Text("Close", color = Color(0xFF6D28D9), fontWeight = FontWeight.Bold)
                            }
                        },
                        title = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = Color(0xFF8B5CF6),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    "About NeoCore AI",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.Black
                                )
                            }
                        },
                        text = {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    "NeoCore AI is a futuristic Liquid Glass powered assistant designed with deep West Bengal cultural expertise, Alpana art studios, traditional logs, and high-fidelity speech synthesis.",
                                    fontSize = 13.sp,
                                    color = Color(0xFF475569)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "• Developer: Mahefuz Mondal",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E293B)
                                )
                                Text(
                                    "• Region: West Bengal, India",
                                    fontSize = 12.sp,
                                    color = Color(0xFF475569)
                                )
                                Text(
                                    "• Framework: Jetpack Compose & Material 3",
                                    fontSize = 12.sp,
                                    color = Color(0xFF475569)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Surface(
                                    onClick = {
                                        try {
                                            uriHandler.openUri("https://www.neocoreai.in")
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                    },
                                    color = Color(0x1F8B5CF6),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            "Visit neocoreai.in 🌐",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF6D28D9)
                                        )
                                    }
                                }
                            }
                        },
                        containerColor = Color.White,
                        shape = RoundedCornerShape(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun FloatingSidebarItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgAlpha by animateFloatAsState(targetValue = if (isSelected) 0.12f else 0f, label = "bgAlpha")
    val contentColor = if (isSelected) Color(0xFF6D28D9) else Color(0xFF475569)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 3.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF8B5CF6).copy(alpha = bgAlpha))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = contentColor,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            fontFamily = GoogleSans,
            color = if (isSelected) Color.Black else Color(0xFF475569),
            modifier = Modifier.weight(1f)
        )
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF8B5CF6))
            )
        }
    }
}
