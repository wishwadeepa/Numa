package com.example.numa.feature.home

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.numa.R
import com.example.numa.data.local.Note

// =============================================================
// NUMA COLORS
// =============================================================

private val NumaBackground = Color(0xFFF7F7F6)
private val NumaCharcoal = Color(0xFF373736)
private val NumaYellow = Color(0xFFFFFF7D)

// =============================================================
// NUMA TYPOGRAPHY
// =============================================================

private val Prompt = FontFamily(
    Font(
        resId = R.font.prompt_regular,
        weight = FontWeight.Normal
    ),
    Font(
        resId = R.font.prompt_medium,
        weight = FontWeight.Medium
    ),
    Font(
        resId = R.font.prompt_semibold,
        weight = FontWeight.SemiBold
    ),
    Font(
        resId = R.font.prompt_bold,
        weight = FontWeight.Bold
    )
)

private val NumaTitleStyle = TextStyle(
    fontFamily = Prompt,
    fontWeight = FontWeight.Bold,
    fontSize = 32.sp
)

private val NumaBodyStyle = TextStyle(
    fontFamily = Prompt,
    fontWeight = FontWeight.Normal,
    fontSize = 15.sp
)

// =============================================================
// HOME
// =============================================================

@Composable
fun NumaHome(
    notes: List<Note>,
    onWriteClick: () -> Unit = {},
    onNoteClick: (String) -> Unit = {}
) {

    var showNewMenu by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NumaBackground)
    ) {

        // =====================================================
        // HOME CONTENT
        // =====================================================

        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(
                    radius = if (showNewMenu) {
                        7.dp
                    } else {
                        0.dp
                    }
                )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(
                        horizontal = 24.dp,
                        vertical = 20.dp
                    )
            ) {

                // =================================================
                // HEADER
                // =================================================

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "My Numa",
                        style = NumaTitleStyle,
                        color = NumaCharcoal
                    )

                    Spacer(
                        modifier = Modifier.weight(1f)
                    )

                    Image(
                        painter = painterResource(
                            R.drawable.ic_more
                        ),
                        contentDescription = "More options",
                        modifier = Modifier.size(24.dp)
                    )
                }

                // =================================================
                // CONTENT
                // =================================================

                if (notes.isEmpty()) {

                    Spacer(
                        modifier = Modifier.weight(1f)
                    )

                } else {

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {

                        Spacer(
                            modifier = Modifier.height(32.dp)
                        )

                        Text(
                            text = "Recents",
                            style = NumaBodyStyle.copy(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = NumaCharcoal
                        )

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        // =================================================
                        // SUBTLE MASONRY RECENTS
                        // =================================================
                        //
                        // This replaces the old LazyVerticalGrid.
                        //
                        // The two columns flow independently, so one
                        // taller card does not force the neighboring
                        // card to have the same height.
                        //
                        // =================================================

                        MasonryNotes(
                            notes = notes,
                            onNoteClick = onNoteClick
                        )
                    }
                }

                // =================================================
                // BOTTOM CONTROLS
                // =================================================

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // SEARCH BAR

                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .clip(
                                RoundedCornerShape(28.dp)
                            )
                            .background(NumaCharcoal)
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Image(
                            painter = painterResource(
                                R.drawable.ic_search
                            ),
                            contentDescription = "Search",
                            modifier = Modifier.size(18.dp),
                            colorFilter = ColorFilter.tint(
                                Color.White
                            )
                        )

                        Spacer(
                            modifier = Modifier.size(10.dp)
                        )

                        Text(
                            text = "Looking for something?",
                            style = NumaBodyStyle,
                            color = Color.White.copy(
                                alpha = 0.75f
                            )
                        )
                    }

                    // Intentional whitespace

                    Spacer(
                        modifier = Modifier.size(16.dp)
                    )

                    // Reserved position for New button

                    Box(
                        modifier = Modifier.size(56.dp),
                        contentAlignment = Alignment.Center
                    ) {
                    }
                }
            }
        }

        // =====================================================
        // NEW MENU
        // =====================================================

        NewMenu(
            expanded = showNewMenu,
            onToggle = {
                showNewMenu = !showNewMenu
            },
            onWriteClick = {
                showNewMenu = false
                onWriteClick()
            }
        )
    }
}

// =============================================================
// MASONRY RECENTS
// =============================================================

@Composable
private fun MasonryNotes(
    notes: List<Note>,
    onNoteClick: (String) -> Unit
) {

    /*
     * Split the notes into two independent columns.
     *
     * Even-numbered notes go left.
     * Odd-numbered notes go right.
     *
     * Because these are normal Columns inside a scrolling
     * container, the cards can have different heights without
     * creating the rigid row structure of LazyVerticalGrid.
     */

    val leftNotes = remember(notes) {
        notes.filterIndexed { index, _ ->
            index % 2 == 0
        }
    }

    val rightNotes = remember(notes) {
        notes.filterIndexed { index, _ ->
            index % 2 == 1
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(
            bottom = 20.dp
        )
    ) {

        item {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {

                // =================================================
                // LEFT COLUMN
                // =================================================

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    leftNotes.forEach { note ->

                        NoteCard(
                            note = note,
                            onClick = {
                                onNoteClick(note.id)
                            }
                        )
                    }
                }

                // =================================================
                // RIGHT COLUMN
                // =================================================

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    rightNotes.forEach { note ->

                        NoteCard(
                            note = note,
                            onClick = {
                                onNoteClick(note.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

// =============================================================
// NOTE CARD
// =============================================================

@Composable
private fun NoteCard(
    note: Note,
    onClick: () -> Unit
) {

    /*
     * Card height is currently based on content density.
     *
     * This is deliberately subtle:
     *
     * Short  → 132dp
     * Medium → 160dp
     * Dense  → 190dp
     *
     * Later, Numa's intelligence layer can influence this
     * using relationships, importance, topics, etc.
     */

    val contentLength =
        note.title.length + note.content.length

    val cardHeight = when {

        contentLength < 80 ->
            132.dp

        contentLength < 220 ->
            160.dp

        else ->
            190.dp
    }

    val previewLines = when {

        contentLength < 80 ->
            2

        contentLength < 220 ->
            3

        else ->
            4
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight)
            .clip(
                RoundedCornerShape(20.dp)
            )
            .background(NumaCharcoal)
            .clickable {
                onClick()
            }
            .padding(18.dp)
    ) {

        // =====================================================
        // TITLE
        // =====================================================

        Text(
            text = if (note.title.isBlank()) {
                "Untitled"
            } else {
                note.title
            },
            fontFamily = Prompt,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        // =====================================================
        // BODY PREVIEW
        // =====================================================

        if (note.content.isNotBlank()) {

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = note.content,
                fontFamily = Prompt,
                fontSize = 13.sp,
                color = Color.White.copy(
                    alpha = 0.75f
                ),
                maxLines = previewLines,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// =============================================================
// NEW MENU
// =============================================================

@Composable
private fun NewMenu(
    expanded: Boolean,
    onToggle: () -> Unit,
    onWriteClick: () -> Unit
) {

    val transition = updateTransition(
        targetState = expanded,
        label = "New menu transition"
    )

    val menuWidth by transition.animateDp(
        transitionSpec = {
            spring(
                dampingRatio = 0.70f,
                stiffness = 400f
            )
        },
        label = "New width"
    ) { isExpanded ->

        if (isExpanded) {
            240.dp
        } else {
            56.dp
        }
    }

    val menuHeight by transition.animateDp(
        transitionSpec = {
            spring(
                dampingRatio = 0.70f,
                stiffness = 400f
            )
        },
        label = "New height"
    ) { isExpanded ->

        if (isExpanded) {
            230.dp
        } else {
            56.dp
        }
    }

    val contentAlpha by transition.animateFloat(
        transitionSpec = {
            spring(
                dampingRatio = 0.85f,
                stiffness = 320f
            )
        },
        label = "New content alpha"
    ) { isExpanded ->

        if (isExpanded) {
            1f
        } else {
            0f
        }
    }

    // =========================================================
    // FULL SCREEN LAYER
    // =========================================================

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        if (expanded) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Color.White.copy(alpha = 0.04f)
                    )
                    .clickable {
                        onToggle()
                    }
            )
        }

        // =====================================================
        // MORPHING NEW SURFACE
        // =====================================================

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    end = 24.dp,
                    bottom = 20.dp
                ),
            contentAlignment = Alignment.BottomEnd
        ) {

            Box(
                modifier = Modifier
                    .size(
                        width = menuWidth,
                        height = menuHeight
                    )
                    .shadow(
                        elevation = if (expanded) {
                            24.dp
                        } else {
                            0.dp
                        },
                        shape = RoundedCornerShape(28.dp),
                        clip = false
                    )
                    .clip(
                        RoundedCornerShape(28.dp)
                    )
                    .background(NumaCharcoal)
                    .clickable {
                        if (!expanded) {
                            onToggle()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {

                // =================================================
                // CLOSED STATE
                // =================================================

                if (!expanded) {

                    Image(
                        painter = painterResource(
                            R.drawable.ic_add
                        ),
                        contentDescription = "New",
                        modifier = Modifier.size(24.dp),
                        colorFilter = ColorFilter.tint(
                            Color.White
                        )
                    )
                }

                // =================================================
                // EXPANDED STATE
                // =================================================

                if (expanded) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                horizontal = 30.dp,
                                vertical = 20.dp
                            ),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {

                        NewMenuItem(
                            icon = R.drawable.ic_write,
                            label = "Write",
                            alpha = contentAlpha,
                            onClick = onWriteClick
                        )

                        NewMenuItem(
                            icon = R.drawable.ic_speak,
                            label = "Speak",
                            alpha = contentAlpha
                        )

                        NewMenuItem(
                            icon = R.drawable.ic_doodle,
                            label = "Doodle",
                            alpha = contentAlpha
                        )
                    }
                }
            }
        }
    }
}

// =============================================================
// NEW MENU ITEM
// =============================================================

@Composable
private fun NewMenuItem(
    icon: Int,
    label: String,
    alpha: Float,
    onClick: (() -> Unit)? = null
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .clickable(
                enabled = onClick != null,
                onClick = {
                    onClick?.invoke()
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(icon),
            contentDescription = null,
            colorFilter = ColorFilter.tint(
                NumaYellow.copy(
                    alpha = alpha
                )
            ),
            modifier = Modifier.size(42.dp)
        )

        Spacer(
            modifier = Modifier.size(16.dp)
        )

        Text(
            text = label,
            fontFamily = Prompt,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White.copy(
                alpha = alpha
            )
        )
    }
}