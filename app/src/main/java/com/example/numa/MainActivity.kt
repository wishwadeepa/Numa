package com.example.numa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.statusBarsPadding

private val NumaBackground = Color(0xFFF7F7F6)
private val NumaCharcoal = Color(0xFF373736)
private val NumaYellow = Color(0xFFFFFF7D)

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

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            NumaHome()
        }
    }
}

@Composable
fun NumaHome() {

    var showNewMenu by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NumaBackground)
            .statusBarsPadding()
            .padding(
                horizontal = 24.dp,
                vertical = 20.dp
            )
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            // ---------------------------------------------
            // Header
            // ---------------------------------------------

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
                    painter = painterResource(R.drawable.ic_more),
                    contentDescription = "More options",
                    modifier = Modifier.size(24.dp)
                )
            }

            // ---------------------------------------------
            // Empty content area
            // ---------------------------------------------

            Spacer(
                modifier = Modifier.weight(1f)
            )

            // ---------------------------------------------
            // New menu + bottom controls
            // ---------------------------------------------

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {

                // New menu

                AnimatedVisibility(
                    visible = showNewMenu,
                    enter = fadeIn() + expandVertically(
                        expandFrom = Alignment.Bottom
                    ),
                    exit = fadeOut() + shrinkVertically(
                        shrinkTowards = Alignment.Bottom
                    )
                ) {

                    Column(
                        modifier = Modifier
                            .padding(bottom = 12.dp)
                            .clip(
                                RoundedCornerShape(20.dp)
                            )
                            .background(NumaCharcoal)
                            .padding(
                                horizontal = 20.dp,
                                vertical = 12.dp
                            ),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {

                        NewMenuItem(
                            icon = R.drawable.ic_write,
                            label = "Write"
                        )

                        NewMenuItem(
                            icon = R.drawable.ic_speak,
                            label = "Speak"
                        )

                        NewMenuItem(
                            icon = R.drawable.ic_doodle,
                            label = "Doodle"
                        )
                    }
                }

                // Bottom controls

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // Search

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
                            painter = painterResource(R.drawable.ic_search),
                            contentDescription = "Search",
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(
                            modifier = Modifier.size(10.dp)
                        )

                        Text(
                            text = "Looking for something?",
                            style = NumaBodyStyle,
                            color = Color.White.copy(alpha = 0.75f)
                        )
                    }

                    // New button

                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(NumaCharcoal),
                        contentAlignment = Alignment.Center
                    ) {

                        Image(
                            painter = painterResource(R.drawable.ic_add),
                            contentDescription = "New",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NewMenuItem(
    icon: Int,
    label: String
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(NumaYellow),
            contentAlignment = Alignment.Center
        ) {

            Image(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(19.dp)
            )
        }

        Spacer(
            modifier = Modifier.size(12.dp)
        )

        Text(
            text = label,
            fontFamily = Prompt,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }
}

@Preview(
    showBackground = true,
    widthDp = 412,
    heightDp = 915
)
@Composable
private fun NumaHomePreview() {
    NumaHome()
}