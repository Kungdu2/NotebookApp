package com.example.rezanoteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rezanoteapp.ui.theme.RezaNoteAppTheme

data class Note(
    val id: Int,
    val title: String,
    val detail: String,
    val check : MutableState<Boolean> = mutableStateOf(false)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NoteApp()
        }
    }
}




@Composable
fun NoteApp() {
    val navContoller = rememberNavController()
    val notes = mutableListOf<Note>()
    NavHost(navController = navContoller, startDestination = "home") {
        composable("NoteApp") {
            NoteList(navContoller, notes)
        }
        composable("addNote") {
            AddNote(navContoller, notes)
        }
        composable("detail/{noteId}") {
            val itemId = it.arguments?.getString("noteId")?.toIntOrNull()
            val note = notes.find { it.id == itemId }
            note?.let { NoteDetail(navContoller, it) }

        }

    }

}

//hejjjjj ny

@Composable
fun NoteList(navContoller: NavController, notes: MutableList<Note>) {
    TODO("Not yet implemented")
}







@Composable
fun AddNote(navContoller: NavController, notes: MutableList<Note>) {

}

@Composable
fun NoteDetail(navContoller: NavController, it: Note) {

}

