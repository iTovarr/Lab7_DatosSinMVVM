package com.example.datossinmvvm

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenTask() {
    val context = LocalContext.current
    val db = remember { Room.databaseBuilder(context, UserDatabase::class.java, "user_db").fallbackToDestructiveMigration().build() }
    val dao = db.taskDao()
    val coroutineScope = rememberCoroutineScope()

    // Paleta de Colores Fríos
    val DeepBlue = Color(0xFF1A374D)
    val BlueMedium = Color(0xFF406882)
    val BlueLight = Color(0xFF6998AB)
    val IceWhite = Color(0xFFF1F6F9)
    val MintSafe = Color(0xFFB1D0E0)

    var taskTitle by remember { mutableStateOf("") }
    var minutes by remember { mutableIntStateOf(10) }
    var selectedImageName by remember { mutableStateOf("ic_launcher_foreground") }
    var taskList by remember { mutableStateOf(listOf<Task>()) }
    var currentView by remember { mutableStateOf("EDITOR") }
    var menuExpanded by remember { mutableStateOf(false) }
    var taskToEdit by remember { mutableStateOf<Task?>(null) }

    val bibliotecaImagenes = listOf("lavarse", "vestirse", "comer", "ducharse", "ic_launcher_foreground")

    LaunchedEffect(currentView) { if (currentView == "LISTA") taskList = dao.getAllTasks() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (currentView == "EDITOR") "Configurar Tarea" else "Mis Actividades",
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                },
                actions = {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(Icons.Default.MoreVert, null, tint = Color.White)
                    }
                    DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                        DropdownMenuItem(
                            text = { Text("Ver Actividades") },
                            onClick = { currentView = "LISTA"; menuExpanded = false },
                            leadingIcon = { Icon(Icons.Default.List, null, tint = BlueMedium) }
                        )
                        DropdownMenuItem(
                            text = { Text("Nueva Tarea") },
                            onClick = {
                                taskToEdit = null; taskTitle = ""; minutes = 10;
                                selectedImageName = "ic_launcher_foreground"; currentView = "EDITOR"; menuExpanded = false
                            },
                            leadingIcon = { Icon(Icons.Default.AddCircle, null, tint = BlueMedium) }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepBlue)
            )
        }
    ) { p ->
        Box(modifier = Modifier.padding(p).fillMaxSize().background(IceWhite)) {
            when (currentView) {
                "GALERIA" -> {
                    Column(Modifier.padding(20.dp)) {
                        Text("Biblioteca de Pictogramas", fontWeight = FontWeight.Bold, color = DeepBlue, fontSize = 20.sp)
                        Spacer(Modifier.height(16.dp))
                        LazyVerticalGrid(columns = GridCells.Fixed(3), verticalArrangement = Arrangement.spacedBy(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(bibliotecaImagenes) { img ->
                                val resId = context.resources.getIdentifier(img, "drawable", context.packageName)
                                Card(
                                    modifier = Modifier.clickable { selectedImageName = img; currentView = "EDITOR" },
                                    shape = RoundedCornerShape(16.dp),
                                    elevation = CardDefaults.cardElevation(4.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = if(resId != 0) resId else android.R.drawable.ic_menu_gallery),
                                        contentDescription = null,
                                        modifier = Modifier.size(100.dp).padding(12.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                "EDITOR" -> {
                    Column(Modifier.padding(24.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        // Input Estilizado
                        OutlinedTextField(
                            value = taskTitle,
                            onValueChange = { taskTitle = it },
                            placeholder = { Text("Nombre de la actividad...") },
                            modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(12.dp)),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedBorderColor = BlueMedium
                            )
                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        // Selector de Imagen Circular
                        Box(
                            modifier = Modifier
                                .size(160.dp)
                                .shadow(8.dp, CircleShape)
                                .background(Color.White, CircleShape)
                                .border(2.dp, BlueLight, CircleShape)
                                .clickable { currentView = "GALERIA" },
                            contentAlignment = Alignment.Center
                        ) {
                            val resId = context.resources.getIdentifier(selectedImageName, "drawable", context.packageName)
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Image(
                                    painter = painterResource(id = if(resId != 0) resId else android.R.drawable.ic_menu_camera),
                                    contentDescription = null,
                                    modifier = Modifier.size(90.dp)
                                )
                                Text("Pictograma", fontSize = 11.sp, color = BlueMedium, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        // Tiempo con Estilo "Cool"
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MintSafe),
                            shape = RoundedCornerShape(50.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                                IconButton(onClick = { if(minutes > 1) minutes-- }) { Icon(Icons.Default.KeyboardArrowLeft, null, tint = DeepBlue, modifier = Modifier.size(32.dp)) }
                                Text("$minutes min", fontSize = 28.sp, fontWeight = FontWeight.Black, color = DeepBlue, modifier = Modifier.padding(horizontal = 16.dp))
                                IconButton(onClick = { minutes++ }) { Icon(Icons.Default.KeyboardArrowRight, null, tint = DeepBlue, modifier = Modifier.size(32.dp)) }
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Button(
                            onClick = {
                                if (taskTitle.isNotBlank()) {
                                    val task = Task(id = taskToEdit?.id ?: 0, title = taskTitle, duration = "$minutes min", type = "Rutina", imageResName = selectedImageName)
                                    coroutineScope.launch {
                                        if (taskToEdit == null) dao.insertTask(task) else dao.updateTask(task)
                                        currentView = "LISTA"
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(60.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = DeepBlue),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Icon(Icons.Default.Done, null)
                            Spacer(Modifier.width(8.dp))
                            Text(if (taskToEdit == null) "CONFIRMAR ACTIVIDAD" else "ACTUALIZAR CAMBIOS", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                "LISTA" -> {
                    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(taskList) { item ->
                            val itemResId = context.resources.getIdentifier(item.imageResName, "drawable", context.packageName)
                            Card(
                                modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(20.dp)),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                ListItem(
                                    leadingContent = {
                                        Box(Modifier.size(60.dp).background(IceWhite, CircleShape).padding(8.dp), contentAlignment = Alignment.Center) {
                                            Image(
                                                painter = painterResource(id = if(itemResId != 0) itemResId else android.R.drawable.ic_menu_gallery),
                                                contentDescription = null
                                            )
                                        }
                                    },
                                    headlineContent = { Text(item.title, fontWeight = FontWeight.ExtraBold, color = DeepBlue) },
                                    supportingContent = { Text("⏱ ${item.duration}", color = BlueMedium) },
                                    trailingContent = {
                                        Row {
                                            IconButton(onClick = {
                                                taskToEdit = item; taskTitle = item.title
                                                minutes = item.duration.replace(" min", "").toInt()
                                                selectedImageName = item.imageResName; currentView = "EDITOR"
                                            }) { Icon(Icons.Default.Edit, null, tint = BlueMedium) }

                                            IconButton(onClick = { coroutineScope.launch { dao.deleteTask(item); taskList = dao.getAllTasks() } }) {
                                                Icon(Icons.Default.Delete, null, tint = Color(0xFFCFD8DC))
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}