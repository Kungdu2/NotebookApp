package com.example.rezanoteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rezanoteapp.ui.theme.RezaNoteAppTheme
import kotlinx.coroutines.sync.Semaphore
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteList(navContoller: NavController, notes: MutableList<Note>) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Note App")  },
                colors = TopAppBarDefaults

                    .topAppBarColors(containerColor = Color.Green, titleContentColor = MaterialTheme.colorScheme.onPrimary)
            )

        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navContoller.navigate("addNote") }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }

    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(notes) { item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth() // Gör att linjen fyller hela bredden
                        .height(3.dp) // Höjden på linjen
                        .background(Color.Gray) // Färgen på linjen
                )
                ListItem(
                    leadingContent = {
                        Checkbox(
                            checked = item.check.value,
                            onCheckedChange = { isChecked ->
                                item.check.value = isChecked
                            })
                    },
                    headlineContent = { Text(item.title) },
                    supportingContent = { Text(item.detail) },
                    trailingContent = {
                        Row {
                            IconButton(
                                onClick = { navContoller.navigate("detail/${item.id}")}
                            ) {
                                Icon ( Icons.Filled.Edit , contentDescription = "Edit Note")
                            }
                            IconButton(
                                onClick = { notes.remove(item) }
                            ){
                                Icon(Icons.Filled.Delete, contentDescription = "Delete Note")
                            }
                        }
                    }
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth() // Gör att linjen fyller hela bredden
                        .height(1.dp) // Höjden på linjen
                        .background(Color.Gray) // Färgen på linjen
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNote(navContoller: NavController, notes: MutableList<Note>) {
    var title by remember { mutableStateOf("") }
    var detail by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Note") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (title.isNotBlank() && detail.isNotBlank()) {
                                notes.add(Note(id = notes.size, title = title, detail = detail))
                                navContoller.popBackStack()
                            }
                        }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ){ padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            //horizontalAlignment = Alignment.CenterHorizontally,
            //verticalArrangement = Arrangement.Center
        )
        {
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = detail,
                onValueChange = { detail = it },
                label = { Text("Details") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                if(title.isNotBlank() && detail.isNotBlank()) {
                    notes.add(Note(id = notes.size, title = title, detail = detail))
                    navContoller.popBackStack()
                }
                })
            {
                Text("Save")
            }
        }

    }
}


@Composable
fun NoteDetail(navContoller: NavController, notes: Note) {

}