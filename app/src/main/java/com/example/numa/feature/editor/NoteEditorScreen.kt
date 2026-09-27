package com.example.numa.feature.editor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val NumaBackground = Color(0xFFF7F7F6)
private val NumaCharcoal = Color(0xFF373736)

@Composable
fun NoteEditorScreen(
    viewModel: NoteEditorViewModel,
    onDeleted: () -> Unit = {}
) {
    val title by viewModel.title.collectAsState()
    val content by viewModel.content.collectAsState()

    var showOptions by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.saveNow()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NumaBackground)
    ) {

        // =========================================================
        // EDITOR
        // =========================================================

        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (showOptions) {
                        Modifier.blur(7.dp)
                    } else {
                        Modifier
                    }
                )
                .padding(horizontal = 24.dp)
        ) {

            // -----------------------------------------------------
            // THREE DOTS
            // -----------------------------------------------------

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 52.dp),
                horizontalArrangement = Arrangement.End
            ) {

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            showOptions = !showOptions
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⋮",
                        color = NumaCharcoal,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // -----------------------------------------------------
            // TITLE
            // -----------------------------------------------------

            BasicTextField(
                value = title,
                onValueChange = viewModel::onTitleChanged,
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    color = NumaCharcoal,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                ),
                decorationBox = { innerTextField ->

                    Box {

                        if (title.isEmpty()) {
                            Text(
                                text = "Title",
                                color = Color(0xFFB8B8B6),
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        innerTextField()
                    }
                }
            )

            Spacer(modifier = Modifier.height(28.dp))

            // -----------------------------------------------------
            // BODY
            // -----------------------------------------------------

            BasicTextField(
                value = content,
                onValueChange = viewModel::onContentChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                textStyle = TextStyle(
                    color = NumaCharcoal,
                    fontSize = 15.sp,
                    lineHeight = 24.sp
                ),
                decorationBox = { innerTextField ->

                    Box {

                        if (content.isEmpty()) {
                            Text(
                                text = "Let's write something",
                                color = Color(0xFFB8B8B6),
                                fontSize = 15.sp
                            )
                        }

                        innerTextField()
                    }
                }
            )
        }

        // =========================================================
        // NUMA OPTIONS OVERLAY
        // =========================================================

        if (showOptions) {

            // -----------------------------------------------------
            // OUTSIDE AREA
            // -----------------------------------------------------

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        showOptions = false
                    }
            )

            // -----------------------------------------------------
            // CUSTOM NUMA PANEL
            // -----------------------------------------------------

            AnimatedVisibility(
                visible = showOptions,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(
                        top = 102.dp,
                        end = 24.dp
                    ),
                enter = expandIn(
                    expandFrom = Alignment.TopEnd,
                    animationSpec = spring(
                        dampingRatio = 0.7f,
                        stiffness = 450f
                    )
                ) + fadeIn(),

                exit = shrinkOut(
                    shrinkTowards = Alignment.TopEnd,
                    animationSpec = spring(
                        dampingRatio = 0.8f,
                        stiffness = 450f
                    )
                ) + fadeOut()
            ) {

                Column(
                    modifier = Modifier
                        .width(150.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(18.dp)
                        )
                        .background(
                            color = NumaCharcoal,
                            shape = RoundedCornerShape(18.dp)
                        )
                        .padding(
                            horizontal = 20.dp,
                            vertical = 17.dp
                        )
                ) {

                    Text(
                        text = "Delete",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showOptions = false
                                showDeleteConfirmation = true
                            }
                    )
                }
            }
        }

        // =========================================================
        // DELETE CONFIRMATION
        // =========================================================

        if (showDeleteConfirmation) {

            AlertDialog(
                onDismissRequest = {
                    showDeleteConfirmation = false
                },

                title = {
                    Text("Delete note?")
                },

                text = {
                    Text("This note will be permanently deleted.")
                },

                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteConfirmation = false

                            viewModel.deleteNote {
                                onDeleted()
                            }
                        }
                    ) {
                        Text("Delete")
                    }
                },

                dismissButton = {
                    TextButton(
                        onClick = {
                            showDeleteConfirmation = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}