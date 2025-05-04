package com.example.aplikacjabarmanska.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.roundToInt
import kotlin.math.abs

@Composable
fun CocktailTimer(
    formattedTime: String,
    isRunning: Boolean,
    onStart: (Long) -> Unit,
    onStop: () -> Unit,
    onReset: () -> Unit
) {
    var selectedSeconds by remember { mutableStateOf(0) }
    var angle by remember { mutableStateOf(0f) } // 0° to góra (12:00)
    var totalRotations by remember { mutableStateOf(0) } // Zaczynamy od 0 minut

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFE0D6C9))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Minutnik",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Wyświetlanie pozostałego czasu
        Text(
            text = formattedTime,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Okrągłe pokrętło (tylko gdy timer nie działa)
        if (!isRunning) {
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularTimerDial(
                    angle = angle,
                    totalRotations = totalRotations,
                    selectedSeconds = selectedSeconds,
                    onAngleChange = { newAngle, rotationChange ->
                        angle = newAngle

                        // Aktualizuj liczbę obrotów
                        totalRotations = (totalRotations + rotationChange).coerceAtLeast(0)

                        // Oblicz całkowity czas
                        val minutes = totalRotations
                        val seconds = ((angle / 360f) * 60).roundToInt()
                        selectedSeconds = minutes * 60 + seconds
                    }
                )

                // Wyświetlanie wybranego czasu w środku
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val minutes = selectedSeconds / 60
                    val seconds = selectedSeconds % 60
                    Text(
                        text = if (minutes > 0) {
                            "${minutes}:${"%02d".format(seconds)}"
                        } else {
                            "${seconds}s"
                        },
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6650a4)
                    )
                    Text(
                        text = if (totalRotations > 0 || selectedSeconds > 0) "${totalRotations} min" else "Obróć, aby ustawić",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            // Szybkie przyciski predefiniowane
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                QuickTimeChip(seconds = 30, onClick = {
                    selectedSeconds = 30
                    totalRotations = 0
                    angle = 180f // Pół obrotu = 30 sekund
                })
                QuickTimeChip(seconds = 60, onClick = {
                    selectedSeconds = 60
                    totalRotations = 1
                    angle = 0f // Pełny obrót = 1 minuta
                })
                QuickTimeChip(seconds = 120, onClick = {
                    selectedSeconds = 120
                    totalRotations = 2
                    angle = 0f // 2 pełne obroty = 2 minuty
                })
                QuickTimeChip(seconds = 180, onClick = {
                    selectedSeconds = 180
                    totalRotations = 3
                    angle = 0f // 3 pełne obroty = 3 minuty
                })
            }
        }

        // Duże przyciski kontrolne
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            LargeActionButton(
                text = if (!isRunning) "START" else "STOP",
                onClick = {
                    if (!isRunning) onStart(selectedSeconds.toLong())
                    else onStop()
                },
                backgroundColor = if (!isRunning) Color(0xFF4CAF50) else Color(0xFFF44336),
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            )

            LargeActionButton(
                text = "RESET",
                onClick = {
                    onReset()
                    selectedSeconds = 0
                    totalRotations = 0
                    angle = 0f
                },
                backgroundColor = Color(0xFF757575),
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            )
        }
    }
}

@Composable
private fun CircularTimerDial(
    angle: Float,
    totalRotations: Int,
    selectedSeconds: Int,
    onAngleChange: (Float, Int) -> Unit
) {
    val dialColor = Color(0xFF6650a4)
    val backgroundColor = Color(0xFFE0E0E0)
    var lastAngle by remember { mutableStateOf(angle) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val touchPosition = change.position

                    val newAngle = atan2(
                        touchPosition.x - center.x,
                        center.y - touchPosition.y  // Odwrócona oś Y
                    ).let { radians ->
                        var degrees = (radians * 180f / PI.toFloat())
                        if (degrees < 0) degrees += 360f
                        degrees
                    }

                    // Przyciąganie do znaczników co 10 sekund (co 60°)
                    val snappedAngle = (newAngle / 60f).roundToInt() * 60f

                    // Detekcja przekroczenia granicy 360° -> 0°
                    var rotationChange = 0
                    if (lastAngle > 270 && snappedAngle < 90) {
                        rotationChange = 1 // Obrót w prawo
                    } else if (lastAngle < 90 && snappedAngle > 270) {
                        rotationChange = -1 // Obrót w lewo
                    }

                    lastAngle = snappedAngle
                    onAngleChange(snappedAngle, rotationChange)
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val radius = size.minDimension / 2f
            val center = Offset(size.width / 2f, size.height / 2f)

            // Tło tarczy
            drawCircle(
                color = backgroundColor,
                radius = radius,
                center = center
            )

            // Koło obrotów (minuty)
            for (i in 0 until totalRotations.coerceIn(0, 5)) {
                drawCircle(
                    color = dialColor.copy(alpha = 0.3f - i * 0.05f),
                    radius = radius - 20f - i * 5f,
                    center = center,
                    style = Stroke(width = 10f)
                )
            }

            // Aktywna część tarczy (sekundy w bieżącej minucie)
            if (totalRotations < 5) {
                drawArc(
                    color = dialColor,
                    startAngle = -90f,
                    sweepAngle = angle,
                    useCenter = false,
                    style = Stroke(width = 20f),
                    size = size.copy(width = size.width - 40f, height = size.height - 40f),
                    topLeft = Offset(20f, 20f)
                )
            }

            // Wskaźnik
            rotate(angle, pivot = center) {
                drawLine(
                    color = dialColor,
                    start = center,
                    end = Offset(center.x, center.y - radius + 40f),
                    strokeWidth = 8f
                )

                // Kulka na końcu wskaźnika
                drawCircle(
                    color = dialColor,
                    radius = 16f,
                    center = Offset(center.x, center.y - radius + 40f)
                )
            }

            // Znaczniki czasu - co 10 sekund (co 60°)
            for (i in 0..5) {
                val markerAngle = i * 60f - 90f
                val markerRadians = markerAngle * PI.toFloat() / 180f
                val markerRadius = radius - 20f

                val markerLength = 18f
                val markerWidth = 4f

                val start = Offset(
                    center.x + markerRadius * cos(markerRadians),
                    center.y + markerRadius * sin(markerRadians)
                )
                val end = Offset(
                    center.x + (markerRadius - markerLength) * cos(markerRadians),
                    center.y + (markerRadius - markerLength) * sin(markerRadians)
                )

                drawLine(
                    color = Color.DarkGray,
                    start = start,
                    end = end,
                    strokeWidth = markerWidth
                )
            }

            // Etykiety czasowe
            val labels = listOf("0", "10", "20", "30", "40", "50")
            for (i in labels.indices) {
                val labelAngle = i * 60f - 90f
                val labelRadians = labelAngle * PI.toFloat() / 180f
                val labelRadius = radius - 45f

                val labelX = center.x + labelRadius * cos(labelRadians)
                val labelY = center.y + labelRadius * sin(labelRadians)

                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        labels[i],
                        labelX,
                        labelY + 10f,
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.DKGRAY
                            textSize = 32f
                            textAlign = android.graphics.Paint.Align.CENTER
                            typeface = android.graphics.Typeface.create(
                                android.graphics.Typeface.DEFAULT,
                                android.graphics.Typeface.BOLD
                            )
                        }
                    )
                }
            }

            // Znak 12:00 na górze
            drawCircle(
                color = Color.Red,
                radius = 8f,
                center = Offset(center.x, 25f)
            )
        }
    }
}

@Composable
private fun QuickTimeChip(
    seconds: Int,
    onClick: () -> Unit
) {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    val label = when {
        minutes > 0 && remainingSeconds > 0 -> "${minutes}:${"%02d".format(remainingSeconds)}"
        minutes > 0 -> "${minutes}:00"
        else -> "${seconds}s"
    }

    AssistChip(
        onClick = onClick,
        label = { Text(label) },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = Color(0xFFE8E0FF)
        )
    )
}

@Composable
private fun LargeActionButton(
    text: String,
    onClick: () -> Unit,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(64.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        ),
        shape = RoundedCornerShape(32.dp)
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}