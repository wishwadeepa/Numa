package com.example.numa.feature.doodle

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.numa.R
import androidx.compose.ui.geometry.Offset

private val NumaBackground = Color(0xFFF7F7F6)
private val NumaCharcoal = Color(0xFF373736)
private val NumaYellow = Color(0xFFFFFF7D)

private data class DoodleStroke(
    val points: List<Offset>,
    val isEraser: Boolean = false
)

@Composable
fun DoodleScreen(
    onDone: () -> Unit,
    onBack: () -> Unit
) {
    val strokes = remember {
        mutableStateListOf<DoodleStroke>()
    }

    val undoneStrokes = remember {
        mutableStateListOf<DoodleStroke>()
    }

    var currentPoints by remember {
        mutableStateOf<List<Offset>>(emptyList())
    }

    var eraserEnabled by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NumaBackground)
    ) {

        // =====================================================
        // DRAWING CANVAS
        // =====================================================

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 24.dp,
                    top = 76.dp,
                    end = 24.dp,
                    bottom = 108.dp
                )
                .clip(
                    RoundedCornerShape(28.dp)
                )
                .background(Color.White)
                .pointerInput(eraserEnabled) {

                    detectDragGestures(

                        onDragStart = { position ->
                            currentPoints = listOf(position)
                        },

                        onDrag = { change, _ ->
                            change.consume()

                            currentPoints =
                                currentPoints + change.position
                        },

                        onDragEnd = {

                            if (currentPoints.isNotEmpty()) {

                                strokes.add(
                                    DoodleStroke(
                                        points = currentPoints,
                                        isEraser = eraserEnabled
                                    )
                                )

                                undoneStrokes.clear()
                                currentPoints = emptyList()
                            }
                        },

                        onDragCancel = {
                            currentPoints = emptyList()
                        }
                    )
                }
        ) {

            // Existing strokes
            strokes.forEach { stroke ->

                if (stroke.points.size >= 2) {

                    val path = Path()

                    path.moveTo(
                        stroke.points.first().x,
                        stroke.points.first().y
                    )

                    for (point in stroke.points.drop(1)) {
                        path.lineTo(point.x, point.y)
                    }

                    drawPath(
                        path = path,
                        color = if (stroke.isEraser) {
                            Color.White
                        } else {
                            NumaCharcoal
                        },
                        style = Stroke(
                            width = if (stroke.isEraser) {
                                32f
                            } else {
                                6f
                            },
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )
                }
            }

            // Current stroke
            if (currentPoints.size >= 2) {

                val path = Path()

                path.moveTo(
                    currentPoints.first().x,
                    currentPoints.first().y
                )

                for (point in currentPoints.drop(1)) {
                    path.lineTo(point.x, point.y)
                }

                drawPath(
                    path = path,
                    color = if (eraserEnabled) {
                        Color.White
                    } else {
                        NumaCharcoal
                    },
                    style = Stroke(
                        width = if (eraserEnabled) 32f else 6f,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }
        }

        // =====================================================
        // CLOSE
        // =====================================================

        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(
                    top = 20.dp,
                    end = 20.dp
                )
                .size(40.dp)
        ) {

            Icon(
                painter = painterResource(
                    R.drawable.ic_close
                ),
                contentDescription = "Close",
                tint = NumaCharcoal,
                modifier = Modifier.size(22.dp)
            )
        }

        // =====================================================
        // DRAWING CONTROLS
        // =====================================================

        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(22.dp),
            color = NumaCharcoal
        ) {

            Row(
                modifier = Modifier.padding(
                    horizontal = 6.dp,
                    vertical = 6.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Undo
                IconButton(
                    onClick = {

                        if (strokes.isNotEmpty()) {

                            val last =
                                strokes.removeAt(
                                    strokes.lastIndex
                                )

                            undoneStrokes.add(last)
                        }
                    },
                    modifier = Modifier.size(44.dp)
                ) {

                    Icon(
                        painter = painterResource(
                            R.drawable.ic_undo
                        ),
                        contentDescription = "Undo",
                        tint = Color.White,
                        modifier = Modifier.size(21.dp)
                    )
                }

                // Redo
                IconButton(
                    onClick = {

                        if (undoneStrokes.isNotEmpty()) {

                            val stroke =
                                undoneStrokes.removeAt(
                                    undoneStrokes.lastIndex
                                )

                            strokes.add(stroke)
                        }
                    },
                    modifier = Modifier.size(44.dp)
                ) {

                    Icon(
                        painter = painterResource(
                            R.drawable.ic_redo
                        ),
                        contentDescription = "Redo",
                        tint = Color.White,
                        modifier = Modifier.size(21.dp)
                    )
                }

                // Eraser
                Surface(
                    shape = CircleShape,
                    color = if (eraserEnabled) {
                        NumaYellow
                    } else {
                        Color.Transparent
                    }
                ) {

                    IconButton(
                        onClick = {
                            eraserEnabled = !eraserEnabled
                        },
                        modifier = Modifier.size(44.dp)
                    ) {

                        Icon(
                            painter = painterResource(
                                R.drawable.ic_eraser
                            ),
                            contentDescription = "Eraser",
                            tint = if (eraserEnabled) {
                                NumaCharcoal
                            } else {
                                Color.White
                            },
                            modifier = Modifier.size(21.dp)
                        )
                    }
                }

                // Clear
                IconButton(
                    onClick = {
                        strokes.clear()
                        undoneStrokes.clear()
                        currentPoints = emptyList()
                    },
                    modifier = Modifier.size(44.dp)
                ) {

                    Icon(
                        painter = painterResource(
                            R.drawable.ic_close
                        ),
                        contentDescription = "Clear",
                        tint = Color.White,
                        modifier = Modifier.size(21.dp)
                    )
                }

                // Done
                Surface(
                    shape = CircleShape,
                    color = NumaYellow
                ) {

                    IconButton(
                        onClick = onDone,
                        modifier = Modifier.size(44.dp)
                    ) {

                        Icon(
                            painter = painterResource(
                                R.drawable.ic_check
                            ),
                            contentDescription = "Done",
                            tint = NumaCharcoal,
                            modifier = Modifier.size(21.dp)
                        )
                    }
                }
            }
        }
    }
}