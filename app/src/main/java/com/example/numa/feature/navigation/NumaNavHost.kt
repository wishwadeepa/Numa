package com.example.numa.feature.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.numa.data.NoteRepository
import com.example.numa.data.local.NumaDatabase
import com.example.numa.feature.editor.NoteEditorScreen
import com.example.numa.feature.editor.NoteEditorViewModel
import com.example.numa.feature.editor.NoteEditorViewModelFactory
import com.example.numa.feature.home.HomeViewModel
import com.example.numa.feature.home.HomeViewModelFactory
import com.example.numa.feature.home.NumaHome

object NumaRoutes {

    const val HOME = "home"

    const val EDITOR = "editor"

    const val EDITOR_WITH_ID = "editor/{noteId}"
}

@Composable
fun NumaNavHost() {

    val navController = rememberNavController()

    val context = LocalContext.current

    val database = NumaDatabase.getInstance(context)

    val repository = NoteRepository(
        database.noteDao()
    )

    NavHost(
        navController = navController,
        startDestination = NumaRoutes.HOME
    ) {

        // =====================================================
        // HOME
        // =====================================================

        composable(NumaRoutes.HOME) {

            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(
                    repository = repository
                )
            )

            val notes by homeViewModel.notes.collectAsState()

            NumaHome(
                notes = notes,

                // New note
                onWriteClick = {
                    navController.navigate(
                        NumaRoutes.EDITOR
                    )
                },

                // Open existing note
                onNoteClick = { noteId ->

                    navController.navigate(
                        "editor/$noteId"
                    )
                }
            )
        }

        // =====================================================
        // NEW EDITOR
        // =====================================================

        composable(
            route = NumaRoutes.EDITOR
        ) {

            val viewModel: NoteEditorViewModel = viewModel(
                factory = NoteEditorViewModelFactory(
                    repository = repository
                )
            )

            NoteEditorScreen(
                viewModel = viewModel,

                // If the note is deleted,
                // return to Home.
                onDeleted = {
                    navController.popBackStack()
                }
            )
        }

        // =====================================================
        // EXISTING NOTE EDITOR
        // =====================================================

        composable(
            route = NumaRoutes.EDITOR_WITH_ID,
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val noteId = backStackEntry.arguments
                ?.getString("noteId")

            val viewModel: NoteEditorViewModel = viewModel(
                factory = NoteEditorViewModelFactory(
                    repository = repository,
                    existingNoteId = noteId
                )
            )

            NoteEditorScreen(
                viewModel = viewModel,

                // If the note is deleted,
                // return to Home.
                onDeleted = {
                    navController.popBackStack()
                }
            )
        }
    }
}