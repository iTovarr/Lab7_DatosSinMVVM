package com.example.datossinmvvm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenTask() {
    val context = LocalContext.current
    val db = remember {
        Room.databaseBuilder(context, UserDatabase::class.java, "user_db")
            .fallbackToDestructiveMigration()
            .build()
    }
    val dao = db.taskDao()
    val coroutineScope = rememberCoroutineScope()

    var taskTitle by remember { mutableStateOf("") }
    var minutes by remember { mutableIntStateOf(10) }
    var taskList by remember { mutableStateOf(listOf<Task>()) }

    // Color crema del fondo según tu diseño
    val bgCream = Color(0xFFF5F0E6)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("EDITOR DE TAREAS", fontWeight = FontWeight.ExtraBold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = bgCream)
            )
        }
    ) { p ->
        Column(modifier = Modifier.padding(p).fillMaxSize().background(bgCream).padding(16.dp)) {
            Text("Nombre de tarea", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = taskTitle,
                onValueChange = { taskTitle = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("Duración estimada", modifier = Modifier.align(Alignment.CenterHorizontally), fontWeight = FontWeight.Bold)
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                IconButton(onClick = { if (minutes > 1) minutes-- }) {
                    Icon(Icons.Default.KeyboardArrowLeft, null, modifier = Modifier.size(40.dp))
                }
                Text("$minutes\nmin", fontSize = 28.sp, fontWeight = FontWeight.Bold, lineHeight = 30.sp)
                IconButton(onClick = { minutes++ }) {
                    Icon(Icons.Default.KeyboardArrowRight, null, modifier = Modifier.size(40.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val newTask = Task(title = taskTitle, duration = "$minutes min", type = "Rutina")
                    coroutineScope.launch {
                        dao.insertTask(newTask)
                        taskTitle = ""
                        taskList = dao.getAllTasks()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5B8A8A)),
                shape = RoundedCornerShape(12.dp)
            ) { Text("GUARDAR TAREA", fontSize = 16.sp, fontWeight = FontWeight.Bold) }

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(taskList) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(15.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF81C784), modifier = Modifier.size(30.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text("${item.duration}", fontSize = 12.sp, color = Color.Gray)
                            }
                            IconButton(onClick = {
                                coroutineScope.launch {
                                    dao.deleteTask(item)
                                    taskList = dao.getAllTasks()
                                }
                            }) {
                                Icon(Icons.Default.Delete, null, tint = Color.LightGray)
                            }
                        }
                    }
                }
            }
        }
    }
}