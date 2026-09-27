package com.example.numa.feature.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.numa.R

private val NumaBackground = Color(0xFFF7F7F6)
private val NumaCharcoal = Color(0xFF373736)
private val NumaPlaceholder = Color(0xFF999998)

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

@Composable
fun NoteEditorScreen(
    viewModel: NoteEditorViewModel
) {

    val title by viewModel.title.collectAsState()
    val content by viewModel.content.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            viewModel.saveNow()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NumaBackground)
            .statusBarsPadding()
            .padding(
                horizontal = 24.dp,
                vertical = 20.dp
            )
    ) {

        TextField(
            value = title,
            onValueChange = viewModel::onTitleChanged,
            modifier = Modifier,
            placeholder = {
                Text(
                    text = "Title",
                    fontFamily = Prompt,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = NumaPlaceholder
                )
            },
            textStyle = TextStyle(
                fontFamily = Prompt,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = NumaCharcoal
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = NumaCharcoal
            )
        )

        TextField(
            value = content,
            onValueChange = viewModel::onContentChanged,
            modifier = Modifier.fillMaxSize(),
            placeholder = {
                Text(
                    text = "Let's write something",
                    fontFamily = Prompt,
                    fontSize = 15.sp,
                    color = NumaPlaceholder
                )
            },
            textStyle = TextStyle(
                fontFamily = Prompt,
                fontSize = 15.sp,
                color = NumaCharcoal
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = NumaCharcoal
            )
        )
    }
}