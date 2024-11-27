package com.example.rezanoteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rezanoteapp.ui.theme.RezaNoteAppTheme
import kotlinx.coroutines.launch


data class Note(
    val id: Int,
    var title: String,
    var detail: String,
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
    val navController = rememberNavController()
    val notes = remember {mutableStateListOf<Note>()}
    NavHost(navController = navController, startDestination = "notes") {
        composable("notes") {
            NoteList(navController, notes)
        }
        composable("addNote") {
            AddNote(navController, notes)
        }
        composable("detail/{noteId}") {
            backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("noteId")?.toIntOrNull()
            val note = notes.find { it.id == itemId }
            note?.let { NoteDetail(navController, it) }

        }

    }

}




//hejjjjj ny
//@Preview(showBackground = true)
//@Composable
//fun PreviewNoteList() {
//    val mockNavController = rememberNavController()
//    val mockNotes = remember {
//        mutableStateListOf(
//            Note(id = 0, title = "Grocery List", detail = "Milk, eggs, bread, cheese"),
//            Note(id = 1, title = "Meeting Notes", detail = "Discuss project progress with team"),
//            Note(id = 2, title = "Travel Plans", detail = "Book flights and hotels for vacation")
//        )
//    }
//
//    NoteList(navController = mockNavController, notes = mockNotes)
//}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteList(navController: NavController, notes: MutableList<Note>) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Note App")  },
                colors = TopAppBarDefaults

                    .topAppBarColors(containerColor = Color.Green, titleContentColor = MaterialTheme.colorScheme.onPrimary),
                modifier = Modifier.clip(RoundedCornerShape(6.dp))
            )

        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("addNote") },
                containerColor = Color.Green) {
                Icon(Icons.Default.Add, contentDescription = "Add")

            }

        }

    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(notes.sortedBy { it.title}) { item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth() // Gör att linjen fyller hela bredden
                        .height(5.dp) // Höjden på linjen
                        .background(Color.Gray) // Färgen på linjen
                )
                ListItem(
                    leadingContent = {
                        Checkbox(
                            checked = item.check.value,
                            onCheckedChange = {
                                item.check.value = !item.check.value
                            })
                    },
                    headlineContent = { Text(item.title) },
                    supportingContent = { Text(item.detail) },
                    trailingContent = {
                        Row {
                            IconButton(
                                onClick = { navController.navigate("detail/${item.id}")}
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
                        .height(5.dp) // Höjden på linjen
                        .background(Color.Gray) // Färgen på linjen
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewAddNote() {
//    val mockNavController = rememberNavController() // Mock av NavController
//    val mockNotes = remember { mutableListOf<Note>() } // Mocklista för anteckningar
//
//    AddNote(navController = mockNavController, notes = mockNotes)
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNote(navController: NavController, notes: MutableList<Note>) {
    var title by remember { mutableStateOf("") }
    var detail by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Add Note")},

                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (title.length !in 3 .. 20) {
                                scope.launch {
                                    snackbarHostState.showSnackbar(message = "Title must be between 3 and 20 characters",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                                return@IconButton
                            }
                            if(detail.length !in 5 .. 400) {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Detail must be between 3 and 500 characters",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                                return@IconButton
                            }
                            notes.add(Note(id = notes.size, title = title, detail = detail))
                            navController.popBackStack()
                        }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }

            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,

            )
        {
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 1.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(0.dp)) // andra till 0 dp
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,

            ) {
                TextField(
                    value = detail,
                    onValueChange = { detail = it },
                    label = { Text("Details") },
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                //Spacer(modifier = Modifier.height(2.dp)) // kommentera bort
                Button(
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                    onClick = {
                        if (title.length !in 3..20) {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Title must be 3-20 characters",
                                    duration = SnackbarDuration.Short
                                )
                            }
                            return@Button
                        }
                        if (detail.length !in 5..400) {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Detail must be 5-400 characters",
                                    duration = SnackbarDuration.Short
                                )
                            }
                            return@Button
                        }
                        notes.add(
                            Note(
                                id = notes.size,
                                title = title,
                                detail = detail
                            )
                        )
                        navController.popBackStack()
                    }) {
                    Text("Add Note", color = Color.Black)
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewNoteDetail() {
//    val mockNavController = rememberNavController() // Mockad NavController
//    val exampleNote = Note(
//        id = 1,
//        title = "Example Note",
//        detail = "This is an example note detail"
//    ) // Mockad Note
//
//    NoteDetail(navController = mockNavController, notes = exampleNote)
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetail(navController: NavController, notes: Note) {
    var title by remember { mutableStateOf(notes.title) }
    var detail by remember { mutableStateOf(notes.detail) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Edit Note") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title")},
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 1.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,

                ){
//Spacer(modifier = Modifier.height(1.dp))
                TextField(
                    value = detail,
                    onValueChange = { detail = it },
                    label = { Text("Details") },
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors( containerColor = Color.Green),
                    onClick = {
                        if (title.length !in 3..20) {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Title must be 3-20 characters",
                                    duration = SnackbarDuration.Short
                                )
                            }
                            return@Button
                        }
                        if (detail.length !in 5..400) {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Detail must be 5-400 characters",
                                    duration = SnackbarDuration.Short
                                )
                            }
                            return@Button
                        }
                        notes.title = title
                        notes.detail = detail
                        navController.popBackStack()
                    })
                {
                    Text("Done", color = Color.Black)
                }
            }

        }
    }
}

