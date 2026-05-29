package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import com.example.data.ArtworkEntity
import java.util.Random
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AlpanaCanvas(
    artwork: ArtworkEntity,
    modifier: Modifier = Modifier,
    isAnimated: Boolean = true
) {
    // Elegant spring/infinite wave animation phase for a dynamic digital AI drawing effect
    val infiniteTransition = rememberInfiniteTransition(label = "ArtAnimation")
    val animPhase by if (isAnimated) {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(15000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "AnimPhase"
        )
    } else {
        remember { mutableStateOf(0f) }
    }

    val pulseScale by if (isAnimated) {
        infiniteTransition.animateFloat(
            initialValue = 0.98f,
            targetValue = 1.02f,
            animationSpec = infiniteRepeatable(
                animation = tween(4000, easing = EaseInOutSine),
                repeatMode = RepeatMode.Reverse
            ),
            label = "PulseScale"
        )
    } else {
        remember { mutableStateOf(1f) }
    }

    // Set stable procedural colors
    val themeColors = MaterialTheme.colorScheme

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(themeColors.surfaceVariant.copy(alpha = 0.15f))
    ) {
        val width = size.width
        val height = size.height
        val center = Offset(width / 2f, height / 2f)
        val radius = minOf(width, height) * 0.42f

        // Initialize pseudo-randomizer locked to the artwork seed for consistent rendering
        val random = Random(artwork.styleSeed)

        when (artwork.artType) {
            "ALPANA" -> {
                // Draws traditional heritage symmetric circular folk art
                // Bengal's legendary white rice paste (Pithuli) designs on red clay
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFF8B0000).copy(alpha = 0.95f), Color(0xFF4A0000)),
                        center = center,
                        radius = radius * 1.5f
                    )
                )

                // Render concentric rings
                drawCircle(
                    color = Color.White.copy(alpha = 0.15f),
                    radius = radius * 1.1f,
                    center = center,
                    style = Stroke(width = 4f)
                )

                drawCircle(
                    color = Color(0xFFF9A825).copy(alpha = 0.5f), // Marigold orange
                    radius = radius * 0.95f,
                    center = center,
                    style = Stroke(width = 6f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f))
                )

                // Multi-petalled lotus rotating with animation
                val petalCount = 8 + random.nextInt(9) // 8 to 16 petals
                rotate(degrees = animPhase, pivot = center) {
                    for (i in 0 until petalCount) {
                        val angle = (360f / petalCount) * i
                        rotate(degrees = angle, pivot = center) {
                            val petalPath = Path().apply {
                                moveTo(center.x, center.y)
                                cubicTo(
                                    center.x - radius * 0.3f, center.y - radius * 0.6f * pulseScale,
                                    center.x + radius * 0.3f, center.y - radius * 0.6f * pulseScale,
                                    center.x, center.y
                                )
                            }
                            // Fill and stroke traditional Bengali lotus petals (Padma)
                            drawPath(
                                path = petalPath,
                                color = Color.White.copy(alpha = 0.3f)
                            )
                            drawPath(
                                path = petalPath,
                                color = Color.White,
                                style = Stroke(width = 5f)
                            )

                            // Inner intricate leaf lines
                            drawLine(
                                color = Color(0xFFFFF9C4),
                                start = center,
                                end = Offset(center.x, center.y - radius * 0.5f * pulseScale),
                                strokeWidth = 3f
                            )
                        }
                    }
                }

                // Core mandala center (with vermillion sindoor & rice powder)
                drawCircle(
                    color = Color(0xFFF9A825),
                    radius = radius * 0.15f,
                    center = center
                )
                drawCircle(
                    color = Color.White,
                    radius = radius * 0.15f,
                    center = center,
                    style = Stroke(width = 5f)
                )
                drawCircle(
                    color = Color(0xFFA30013),
                    radius = radius * 0.05f,
                    center = center
                )

                // Dynamic outer swirling leaf vines (Lata)
                val vineCount = 24
                rotate(degrees = -animPhase * 0.4f, pivot = center) {
                    for (i in 0 until vineCount) {
                        val angle = (360f / vineCount) * i
                        rotate(degrees = angle, pivot = center) {
                            val leafX = center.x
                            val leafY = center.y - radius * 0.82f
                            // Leaf patterns
                            val leafPath = Path().apply {
                                moveTo(leafX, leafY)
                                quadraticTo(leafX - 25f, leafY - 40f, leafX, leafY - 60f)
                                quadraticTo(leafX + 25f, leafY - 40f, leafX, leafY)
                            }
                            drawPath(
                                path = leafPath,
                                color = Color.White.copy(alpha = 0.8f),
                                style = Stroke(width = 3f)
                            )
                            drawCircle(
                                color = Color(0xFFF9A825),
                                radius = 6f,
                                center = Offset(leafX, leafY - 60f)
                            )
                        }
                    }
                }
            }

            "DURGA", "CYBER_GRID" -> {
                // Futuristic Cyber Grid AI Network Visualization (Pure Artificial Intelligence)
                // Linear/Radial dark cyan-blue space theme background
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF0F172A), Color(0xFF020617), Color(0xFF000000))
                    )
                )

                // Draw central processing core (Sentient AI Hub)
                drawCircle(
                    color = Color(0xFF1E6091).copy(alpha = 0.12f),
                    radius = radius * 1.1f * pulseScale,
                    center = center
                )
                drawCircle(
                    color = Color(0xFF38BDF8).copy(alpha = 0.2f),
                    radius = radius * 0.7f * pulseScale,
                    center = center
                )

                // Interactive scanning radar circle sweeps
                drawCircle(
                    color = Color(0xFF38BDF8).copy(alpha = 0.4f),
                    radius = radius * 0.4f * (1.2f - (animPhase % 30f) / 30f),
                    center = center,
                    style = Stroke(width = 3f)
                )

                // Outer decorative high-tech tech ring
                drawCircle(
                    color = Color(0xFF0284C7).copy(alpha = 0.3f),
                    radius = radius * 0.9f,
                    center = center,
                    style = Stroke(width = 2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f)))
                )

                // Concentric data arcs (Scanning HUD elements)
                rotate(degrees = animPhase * 0.6f, pivot = center) {
                    drawArc(
                        color = Color(0xFF34D399).copy(alpha = 0.6f),
                        startAngle = 0f,
                        sweepAngle = 120f,
                        useCenter = false,
                        topLeft = Offset(center.x - radius * 0.5f, center.y - radius * 0.5f),
                        size = androidx.compose.ui.geometry.Size(radius, radius),
                        style = Stroke(width = 4f)
                    )
                    drawArc(
                        color = Color(0xFF6366F1).copy(alpha = 0.6f),
                        startAngle = 180f,
                        sweepAngle = 90f,
                        useCenter = false,
                        topLeft = Offset(center.x - radius * 0.5f, center.y - radius * 0.5f),
                        size = androidx.compose.ui.geometry.Size(radius, radius),
                        style = Stroke(width = 4f)
                    )
                }

                // Programmatically draw interconnected neural network intelligence nodes
                val nodeCount = 10
                val nodePositions = mutableListOf<Offset>()
                for (i in 0 until nodeCount) {
                    val angle = (2.0 * Math.PI / nodeCount) * i + (animPhase * 0.005f)
                    val r = radius * (0.4f + 0.3f * (i % 2))
                    val nx = center.x + (r * Math.cos(angle)).toFloat()
                    val ny = center.y + (r * Math.sin(angle)).toFloat()
                    nodePositions.add(Offset(nx, ny))
                }

                // Draw network transmission data connection lines between all active nodes
                for (i in 0 until nodePositions.size) {
                    val startNode = nodePositions[i]
                    // Connect each node with adjacent neighbors and a central hub connection
                    val target1 = nodePositions[(i + 1) % nodePositions.size]
                    val target2 = nodePositions[(i + 3) % nodePositions.size]

                    drawLine(
                        color = Color(0xFF38BDF8).copy(alpha = 0.35f),
                        start = startNode,
                        end = target1,
                        strokeWidth = 2f
                    )
                    drawLine(
                        color = Color(0xFF6366F1).copy(alpha = 0.2f),
                        start = startNode,
                        end = target2,
                        strokeWidth = 1.5f
                    )
                    drawLine(
                        color = Color(0xFF34D399).copy(alpha = 0.4f),
                        start = startNode,
                        end = center,
                        strokeWidth = 2f
                    )
                }

                // Draw solid intelligence nodes with pulsing data halos
                nodePositions.forEachIndexed { idx, node ->
                    val nodeColor = when (idx % 3) {
                        0 -> Color(0xFF38BDF8) // Azure Blue
                        1 -> Color(0xFF34D399) // Emerald Mint
                        else -> Color(0xFF6366F1) // Indigo Violet
                    }
                    
                    // Draw outer pulsing halo
                    drawCircle(
                        color = nodeColor.copy(alpha = 0.25f),
                        radius = 22f + 10f * ((animPhase + idx * 8) % 15f) / 15f,
                        center = node
                    )
                    // Draw solid central hardware chip node
                    drawCircle(
                        color = nodeColor,
                        radius = 12f,
                        center = node
                    )
                    // Core point light
                    drawCircle(
                        color = Color.White,
                        radius = 4f,
                        center = node
                    )
                }

                // Central high-frequency supercomputer core processing center
                drawCircle(
                    color = Color.White,
                    radius = 18f,
                    center = center
                )
                drawCircle(
                    color = Color(0xFF1E293B),
                    radius = 14f,
                    center = center
                )
                drawCircle(
                    color = Color(0xFF34D399),
                    radius = 8f,
                    center = center
                )
            }

            "TIGER" -> {
                // Royal Bengal Tiger in Sunderbans mangrove swamp
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF2E7D32), Color(0xFF0F3D1F), Color(0xFF000F05))
                    )
                )

                // Majestic sunset sun hovering in swamp background
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFFE65100), Color(0xFFFFB74D).copy(alpha = 0.4f), Color.Transparent),
                        center = Offset(center.x, center.y - radius * 0.2f),
                        radius = radius * 0.8f * pulseScale
                    ),
                    radius = radius * 0.8f * pulseScale,
                    center = Offset(center.x, center.y - radius * 0.2f)
                )

                // Shimmering Sunderban marsh tidal waters
                for (i in 0 until 5) {
                    val rippleY = center.y + radius * 0.4f + i * 40f
                    val path = Path().apply {
                        moveTo(0f, rippleY)
                        cubicTo(
                            width * 0.25f, rippleY - 20f * sin(animPhase * 0.05f + i),
                            width * 0.75f, rippleY + 20f * sin(animPhase * 0.05f + i),
                            width, rippleY
                        )
                    }
                    drawPath(
                        path = path,
                        color = Color(0xFF00E676).copy(alpha = 0.25f),
                        style = Stroke(width = 4f)
                    )
                }

                // Rising pneumatophore mangrove roots (pointed vertical lines)
                for (x in stepFlow(50f, width - 50f, 100f)) {
                    val rootHeight = (90f + random.nextInt(120)).toFloat()
                    val startX = x + 15f * sin(animPhase * 0.03f + x)
                    val rootPath = Path().apply {
                        moveTo(startX, height)
                        lineTo(startX - 10f, height - rootHeight * 0.5f)
                        lineTo(startX, height - rootHeight)
                        lineTo(startX + 10f, height - rootHeight * 0.5f)
                        close()
                    }
                    drawPath(path = rootPath, color = Color(0xFF5D4037).copy(alpha = 0.8f))
                    drawPath(path = rootPath, color = Color(0xFF3E2723), style = Stroke(width = 3f))
                }

                // Stylized Tiger Face mask centered on screen
                val tCenter = Offset(center.x, center.y - radius * 0.1f)
                val tRadius = radius * 0.42f

                // Tiger ears
                val leftEar = Path().apply {
                    moveTo(tCenter.x - tRadius * 0.9f, tCenter.y - tRadius * 0.5f)
                    lineTo(tCenter.x - tRadius * 0.8f, tCenter.y - tRadius * 1.3f)
                    lineTo(tCenter.x - tRadius * 0.2f, tCenter.y - tRadius * 0.9f)
                    close()
                }
                val rightEar = Path().apply {
                    moveTo(tCenter.x + tRadius * 0.9f, tCenter.y - tRadius * 0.5f)
                    lineTo(tCenter.x + tRadius * 0.8f, tCenter.y - tRadius * 1.3f)
                    lineTo(tCenter.x + tRadius * 0.2f, tCenter.y - tRadius * 0.9f)
                    close()
                }
                drawPath(path = leftEar, color = Color(0xFFD84315))
                drawPath(path = leftEar, color = Color.Black, style = Stroke(width = 6f))
                drawPath(path = rightEar, color = Color(0xFFD84315))
                drawPath(path = rightEar, color = Color.Black, style = Stroke(width = 6f))

                // Tiger Face Silhouette Shield
                val facePath = Path().apply {
                    moveTo(tCenter.x - tRadius, tCenter.y - tRadius * 0.2f)
                    cubicTo(
                        tCenter.x - tRadius * 1.1f, tCenter.y + tRadius * 0.5f,
                        tCenter.x - tRadius * 0.5f, tCenter.y + tRadius,
                        tCenter.x, tCenter.y + tRadius * 1.1f
                    )
                    cubicTo(
                        tCenter.x + tRadius * 0.5f, tCenter.y + tRadius,
                        tCenter.x + tRadius * 1.1f, tCenter.y + tRadius * 0.5f,
                        tCenter.x + tRadius, tCenter.y - tRadius * 0.2f
                    )
                    cubicTo(
                        tCenter.x + tRadius * 0.5f, tCenter.y - tRadius * 0.8f,
                        tCenter.x - tRadius * 0.5f, tCenter.y - tRadius * 0.8f,
                        tCenter.x - tRadius, tCenter.y - tRadius * 0.2f
                    )
                }
                drawPath(path = facePath, color = Color(0xFFFF8C00)) // Sunset saffron tiger coat
                drawPath(path = facePath, color = Color.Black, style = Stroke(width = 8f))

                // White face cheeks (patches)
                val leftCheek = Path().apply {
                    moveTo(tCenter.x - tRadius * 0.95f, tCenter.y + tRadius * 0.2f)
                    quadraticTo(tCenter.x - tRadius * 0.4f, tCenter.y + tRadius * 0.4f, tCenter.x, tCenter.y + tRadius * 0.9f)
                    quadraticTo(tCenter.x - tRadius * 0.5f, tCenter.y + tRadius * 1.05f, tCenter.x - tRadius * 0.76f, tCenter.y + tRadius * 0.85f)
                    close()
                }
                val rightCheek = Path().apply {
                    moveTo(tCenter.x + tRadius * 0.95f, tCenter.y + tRadius * 0.2f)
                    quadraticTo(tCenter.x + tRadius * 0.4f, tCenter.y + tRadius * 0.4f, tCenter.x, tCenter.y + tRadius * 0.9f)
                    quadraticTo(tCenter.x + tRadius * 0.5f, tCenter.y + tRadius * 1.05f, tCenter.x + tRadius * 0.76f, tCenter.y + tRadius * 0.85f)
                    close()
                }
                drawPath(path = leftCheek, color = Color.White)
                drawPath(path = rightCheek, color = Color.White)

                // Striking Tiger Stripes (Kalo Dag)
                val drawStripe = { x1: Float, y1: Float, x2: Float, y2: Float, ctrlX: Float, ctrlY: Float ->
                    val stripe = Path().apply {
                        moveTo(x1, y1)
                        quadraticTo(ctrlX, ctrlY, x2, y2)
                    }
                    drawPath(path = stripe, color = Color.Black, style = Stroke(width = 12f, cap = StrokeCap.Round))
                }
                // Forehead stripes
                drawStripe(tCenter.x - 30f, tCenter.y - tRadius * 0.6f, tCenter.x + 30f, tCenter.y - tRadius * 0.6f, tCenter.x, tCenter.y - tRadius * 0.7f)
                drawStripe(tCenter.x - 15f, tCenter.y - tRadius * 0.4f, tCenter.x + 15f, tCenter.y - tRadius * 0.4f, tCenter.x, tCenter.y - tRadius * 0.5f)
                drawStripe(tCenter.x - 4f, tCenter.y - tRadius * 0.2f, tCenter.x + 4f, tCenter.y - tRadius * 0.2f, tCenter.x, tCenter.y - tRadius * 0.25f)

                // Side cheek stripes
                drawStripe(tCenter.x - tRadius * 0.9f, tCenter.y, tCenter.x - tRadius * 0.5f, tCenter.y + 10f, tCenter.x - tRadius * 0.7f, tCenter.y - 10f)
                drawStripe(tCenter.x - tRadius * 0.85f, tCenter.y + tRadius * 0.3f, tCenter.x - tRadius * 0.45f, tCenter.y + tRadius * 0.35f, tCenter.x - tRadius * 0.65f, tCenter.y + tRadius * 0.2f)
                drawStripe(tCenter.x + tRadius * 0.9f, tCenter.y, tCenter.x + tRadius * 0.5f, tCenter.y + 10f, tCenter.x + tRadius * 0.7f, tCenter.y - 10f)
                drawStripe(tCenter.x + tRadius * 0.85f, tCenter.y + tRadius * 0.3f, tCenter.x + tRadius * 0.45f, tCenter.y + tRadius * 0.35f, tCenter.x + tRadius * 0.65f, tCenter.y + tRadius * 0.2f)

                // Flashing yellow wild predator eyes
                drawCircle(Color(0xFFF1C40F), radius = 18f, center = Offset(tCenter.x - tRadius * 0.38f, tCenter.y - tRadius * 0.05f))
                drawCircle(Color.Black, radius = 6f, center = Offset(tCenter.x - tRadius * 0.38f, tCenter.y - tRadius * 0.05f))
                drawCircle(Color(0xFFF1C40F), radius = 18f, center = Offset(tCenter.x + tRadius * 0.38f, tCenter.y - tRadius * 0.05f))
                drawCircle(Color.Black, radius = 6f, center = Offset(tCenter.x + tRadius * 0.38f, tCenter.y - tRadius * 0.05f))

                // Black nose triangle
                val nosePath = Path().apply {
                    moveTo(tCenter.x - 20f, tCenter.y + tRadius * 0.3f)
                    lineTo(tCenter.x + 20f, tCenter.y + tRadius * 0.3f)
                    lineTo(tCenter.x, tCenter.y + tRadius * 0.48f)
                    close()
                }
                drawPath(path = nosePath, color = Color.Black)
            }

            "BAUL" -> {
                // Wandering Baul Mystic folk singer with ektara
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF3F51B5), Color(0xFF673AB7), Color(0xFFFF9800))
                    )
                )

                // Giant mystical rural crimson sun disk (Suryadeo)
                drawCircle(
                    color = Color(0xFFD84315).copy(alpha = 0.5f),
                    radius = radius * 0.88f * pulseScale,
                    center = Offset(center.x, center.y + radius * 0.1f)
                )

                // Big broad Banyan hanging branches (Bot Gaach)
                val treePath = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(width, 0f)
                    quadraticTo(width * 0.75f, radius * 0.3f, width * 0.5f, radius * 0.2f)
                    quadraticTo(width * 0.25f, radius * 0.4f, 0f, radius * 0.1f)
                    close()
                }
                drawPath(path = treePath, color = Color(0xFF4E342E))

                // Hanging roots (Jhuri)
                for (j in 0 until 6) {
                    val rootX = width * 0.18f * (j + 1)
                    drawLine(
                        color = Color(0xFF3E2723),
                        start = Offset(rootX, radius * 0.15f),
                        end = Offset(rootX + 10f * sin(animPhase * 0.04f + j), radius * 0.4f + j * 10f),
                        strokeWidth = 4f
                    )
                }

                // Generates beautiful animated notes indicating mysticism/music (Baul gaan)
                for (n in 0 until 5) {
                    val noteX = center.x + radius * 0.6f * cos(n + animPhase * 0.015f)
                    val noteY = center.y + radius * 0.4f * sin(n + animPhase * 0.02f)
                    drawCircle(
                        color = Color(0xFFFFF9C4).copy(alpha = 0.8f),
                        radius = 12f,
                        center = Offset(noteX, noteY)
                    )
                    drawLine(
                        color = Color(0xFFFFF9C4),
                        start = Offset(noteX, noteY),
                        end = Offset(noteX + 10f, noteY - 26f),
                        strokeWidth = 3f
                    )
                }

                // Artistic silhouette of the dancing Baul, wearing patched orange robes (Alkhalla)
                val bCenter = Offset(center.x, center.y + radius * 0.3f)
                val baulHeight = radius * 0.75f

                // flowing Saffron robe (Alkhalla)
                val robePath = Path().apply {
                    moveTo(bCenter.x, bCenter.y - baulHeight * 0.5f) // Neck
                    cubicTo(
                        bCenter.x - radius * 0.45f * pulseScale, bCenter.y + baulHeight * 0.1f,
                        bCenter.x - radius * 0.3f, bCenter.y + baulHeight * 1.1f,
                        bCenter.x, bCenter.y + baulHeight * 1.1f
                    ) // Left sleeve & skirt
                    cubicTo(
                        bCenter.x + radius * 0.3f, bCenter.y + baulHeight * 1.1f,
                        bCenter.x + radius * 0.45f * pulseScale, bCenter.y + baulHeight * 0.1f,
                        bCenter.x, bCenter.y - baulHeight * 0.5f
                    ) // Right sleeve
                }
                drawPath(path = robePath, color = Color(0xFFEF6C00)) // Sunset Baul orange alkhalla
                drawPath(path = robePath, color = Color.Black, style = Stroke(width = 5f))

                // Baul Head silhouetted looking upwards
                val headCenter = Offset(bCenter.x, bCenter.y - baulHeight * 0.68f)
                drawCircle(Color(0xFF4E342E), radius = radius * 0.12f, center = headCenter)

                // Top knotted hair curve (Baul Khpa)
                val hairPath = Path().apply {
                    addArc(
                        oval = Rect(headCenter.x - 30f, headCenter.y - radius * 0.18f, headCenter.x + 30f, headCenter.y - radius * 0.08f),
                        startAngleDegrees = 0f,
                        sweepAngleDegrees = 360f
                    )
                }
                drawPath(path = hairPath, color = Color(0xFF1A0C08))

                // Instrumental Ektara stringed instrument!
                val ektaraPath = Path().apply {
                    // Curved gourd resonance box (tumbi) at bottom
                    addOval(Rect(bCenter.x + 40f, bCenter.y - 80f, bCenter.x + 130f, bCenter.y + 10f))
                }
                drawPath(path = ektaraPath, color = Color(0xFF8D6E63))
                drawPath(path = ektaraPath, color = Color.Black, style = Stroke(width = 4f))

                // Dual bamboo split frame of ektara stretching upwards
                drawLine(
                    color = Color(0xFFFFD54F),
                    start = Offset(bCenter.x + 85f, bCenter.y + 10f),
                    end = Offset(bCenter.x + 50f, bCenter.y - baulHeight * 0.8f),
                    strokeWidth = 6f
                )
                drawLine(
                    color = Color(0xFFFFD54F),
                    start = Offset(bCenter.x + 85f, bCenter.y + 10f),
                    end = Offset(bCenter.x + 100f, bCenter.y - baulHeight * 0.8f),
                    strokeWidth = 6f
                )

                // Single central vibrating wire loop (Ek Tara)
                drawLine(
                    color = Color.White,
                    start = Offset(bCenter.x + 85f, bCenter.y - 40f),
                    end = Offset(bCenter.x + 75f, bCenter.y - baulHeight * 0.78f),
                    strokeWidth = 2.5f
                )
            }

            "HOWRAH" -> {
                // The Hooghly River & Modern Cantilever Howrah Bridge
                // Shimmering twilight background
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF0D1B2A), Color(0xFF1B263B), Color(0xFF415A77))
                    )
                )

                // Shimmering stars in background sky
                for (s in 0 until 12) {
                    val starX = (50f + random.nextInt(width.toInt() - 100)).toFloat()
                    val starY = (30f + random.nextInt((height * 0.45f).toInt())).toFloat()
                    val starRadius = 3f + 4f * sin(animPhase * 0.08f + s)
                    drawCircle(Color.White.copy(alpha = 0.8f), radius = starRadius, center = Offset(starX, starY))
                }

                // Shimmering deep blue Hooghly water in foreground
                val hWaterY = height * 0.65f
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF1E88E5), Color(0xFF0D47A1))
                    ),
                    topLeft = Offset(0f, hWaterY),
                    size = Size(width, height - hWaterY)
                )

                // Howrah Bridge Steel Cantilever grid lines
                val bTowerY = height * 0.35f
                val leftPylonX = width * 0.15f
                val rightPylonX = width * 0.85f

                // Draw the vertical main support towers
                val pylonPath = { pX: Float ->
                    Path().apply {
                        moveTo(pX - 25f, hWaterY)
                        lineTo(pX - 10f, bTowerY)
                        lineTo(pX + 10f, bTowerY)
                        lineTo(pX + 25f, hWaterY)
                        close()
                    }
                }
                drawPath(path = pylonPath(leftPylonX), color = Color(0xFF90A4AE))
                drawPath(path = pylonPath(leftPylonX), color = Color(0xFF37474F), style = Stroke(width = 4f))
                drawPath(path = pylonPath(rightPylonX), color = Color(0xFF90A4AE))
                drawPath(path = pylonPath(rightPylonX), color = Color(0xFF37474F), style = Stroke(width = 4f))

                // Cantilever top horizontal bridge deck girder
                drawLine(
                    color = Color(0xFFCFD8DC),
                    start = Offset(0f, bTowerY + 20f),
                    end = Offset(width, bTowerY + 20f),
                    strokeWidth = 14f
                )
                drawLine(
                    color = Color(0xFF37474F),
                    start = Offset(0f, bTowerY + 20f),
                    end = Offset(width, bTowerY + 20f),
                    strokeWidth = 3f
                )

                // Suspension main load-bearing curved steel arches
                val archPath = Path().apply {
                    moveTo(0f, bTowerY + 20f)
                    quadraticTo(width * 0.5f, hWaterY - 50f, width, bTowerY + 20f)
                }
                drawPath(path = archPath, color = Color(0xFFCFD8DC), style = Stroke(width = 12f))
                drawPath(path = archPath, color = Color(0xFF37474F), style = Stroke(width = 3f))

                // Steel truss triangulations
                for (t in 0 until 16) {
                    val pX = width * (t / 16f)
                    drawLine(
                        color = Color(0xFF78909C),
                        start = Offset(pX, bTowerY + 20f),
                        end = Offset(pX, hWaterY),
                        strokeWidth = 3f
                    )
                    // Diagonal crossed girders
                    drawLine(
                        color = Color(0xFF78909C),
                        start = Offset(pX, bTowerY + 20f),
                        end = Offset(pX + (width / 16f), hWaterY),
                        strokeWidth = 2f
                    )
                }

                // Animated Traditional Bengali country boat (Kheya Nauko) floating
                val boatX = width * 0.45f + 40f * sin(animPhase * 0.02f)
                val boatY = hWaterY + 60f
                val boatW = 90f
                val boatH = 25f

                // Boat hull (wood body)
                val boatHull = Path().apply {
                    moveTo(boatX - boatW * 0.5f, boatY - 5f)
                    cubicTo(
                        boatX - boatW * 0.4f, boatY + boatH,
                        boatX + boatW * 0.4f, boatY + boatH,
                        boatX + boatW * 0.5f, boatY - 5f
                    )
                    close()
                }
                drawPath(path = boatHull, color = Color(0xFF5D4037))
                drawPath(path = boatHull, color = Color.Black, style = Stroke(width = 3.5f))

                // Covered wooden arched roof compartment (Golai)
                drawArc(
                    color = Color(0xFF1A1A1A),
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(boatX - 22f, boatY - 22f),
                    size = Size(44f, 40f)
                )

                // White triangular canvas sail (Layl/Mastan)
                val sailPath = Path().apply {
                    moveTo(boatX - 15f, boatY - 20f)
                    lineTo(boatX - 35f, boatY - 70f)
                    lineTo(boatX + 15f, boatY - 20f)
                    close()
                }
                drawPath(path = sailPath, color = Color.White.copy(alpha = 0.9f))
                drawPath(path = sailPath, color = Color.Black, style = Stroke(width = 2f))
            }
        }
    }
}

// Helper to iterate float ranges cleanly in Composable canvas layouts
private fun stepFlow(start: Float, end: Float, step: Float): List<Float> {
    if (step <= 0f) return emptyList()
    val result = mutableListOf<Float>()
    var current = start
    while (current <= end) {
        result.add(current)
        current += step
    }
    return result
}
