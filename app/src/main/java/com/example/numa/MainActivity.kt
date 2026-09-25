package com.example.numa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.statusBarsPadding

private val NumaBackground = Color(0xFFF7F7F6)
private val NumaCharcoal = Color(0xFF373736)
private val NumaYellow = Color(0xFFFFFF7D)

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
                    color = NumaCharcoal,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "⋮",
                    color = NumaCharcoal,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // ---------------------------------------------
            // Empty content area
            // ---------------------------------------------

            Spacer(
                modifier = Modifier.weight(1f)
            )

            // ---------------------------------------------
            // Bottom controls
            // ---------------------------------------------

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Search bar

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

                    Text(
                        text = "⌕",
                        color = Color.White,
                        fontSize = 25.sp
                    )

                    Spacer(
                        modifier = Modifier.size(10.dp)
                    )

                    Text(
                        text = "Looking for something?",
                        color = Color.White.copy(alpha = 0.75f),
                        fontSize = 15.sp
                    )
                }

                // Add button

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(NumaCharcoal),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "+",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
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