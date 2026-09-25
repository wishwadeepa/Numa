package com.example.numa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.statusBarsPadding

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
// MAIN ACTIVITY
// =============================================================

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            NumaHome()
        }
    }
}

// =============================================================
// HOME
// =============================================================

@Composable
fun NumaHome() {

    var showNewMenu by remember {
        mutableStateOf(false)
    }

    /*
     * IMPORTANT:
     *
     * This Box is now the TRUE full-screen container.
     *
     * Previously our padding was applied before the blur,
     * which caused the blur to stop around the inner content.
     */

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
                // EMPTY SPACE
                // =================================================

                Spacer(
                    modifier = Modifier.weight(1f)
                )

                // =================================================
                // BOTTOM CONTROLS
                // =================================================

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // -------------------------------------------------
                    // SEARCH BAR
                    // -------------------------------------------------

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
                            modifier = Modifier.size(18.dp)
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

                    // -------------------------------------------------
                    // EXPLICIT WHITESPACE
                    //
                    // This is intentional.
                    // The mockup has a visible gap between the
                    // search pill and the New button.
                    // -------------------------------------------------

                    Spacer(
                        modifier = Modifier.size(16.dp)
                    )

                    // -------------------------------------------------
                    // NEW BUTTON
                    // -------------------------------------------------

                    Box(
                        modifier = Modifier.size(56.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        // The actual button is controlled by NewMenu.
                        //
                        // This spacer simply reserves its position.
                    }
                }
            }
        }

        // =====================================================
        // NEW MENU / MORPHING BUTTON
        // =====================================================

        NewMenu(
            expanded = showNewMenu,
            onToggle = {
                showNewMenu = !showNewMenu
            }
        )
    }
}

// =============================================================
// NEW MENU
// =============================================================

@Composable
private fun NewMenu(
    expanded: Boolean,
    onToggle: () -> Unit
) {

    val transition = updateTransition(
        targetState = expanded,
        label = "New menu transition"
    )

    // ---------------------------------------------------------
    // WIDTH
    // ---------------------------------------------------------

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

    // ---------------------------------------------------------
    // HEIGHT
    // ---------------------------------------------------------

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

    // ---------------------------------------------------------
    // CONTENT ALPHA
    // ---------------------------------------------------------

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

        // =====================================================
        // FULL SCREEN TOUCH / DIM LAYER
        //
        // This now covers the ENTIRE screen.
        // The underlying Home layer is already blurred.
        // =====================================================

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
        //
        // Anchored to the exact bottom-right position.
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
                        modifier = Modifier.size(24.dp)
                    )
                }

                // =================================================
                // OPEN STATE
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
                            alpha = contentAlpha
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
    alpha: Float
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(icon),
            contentDescription = null,
            colorFilter = ColorFilter.tint(
                NumaYellow.copy(alpha = alpha)
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

// =============================================================
// PREVIEW
// =============================================================

@Preview(
    showBackground = true,
    widthDp = 412,
    heightDp = 915
)
@Composable
private fun NumaHomePreview() {
    NumaHome()
}