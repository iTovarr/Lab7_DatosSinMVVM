package com.example.datossinmvvm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenUser(onNavigateToTasks: () -> Unit = {}) {
    val context = LocalContext.current
    val db = remember { Room.databaseBuilder(context, UserDatabase::class.java, "user_db").fallbackToDestructiveMigration().build() }
    val dao = db.userDao()
    val coroutineScope = rememberCoroutineScope()

    // Paleta de Colores Fríos
    val DeepBlue = Color(0xFF1A374D)
    val BlueMedium = Color(0xFF406882)
    val BlueLight = Color(0xFF6998AB)
    val IceWhite = Color(0xFFF1F6F9)

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var userList by remember { mutableStateOf(listOf<User>()) }
    var currentView by remember { mutableStateOf("REGISTRO") }

    LaunchedEffect(currentView) {
        if (currentView == "LISTA") userList = dao.getAll()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (currentView == "REGISTRO") "Nuevo Usuario" else "Usuarios Registrados",
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                },
                actions = {
                    // Botón para ir a Actividades (Usa Event porque DateRange a veces falla)
                    IconButton(onClick = onNavigateToTasks) {
                        Icon(Icons.Default.Event, "Actividades", tint = Color.White)
                    }

                    // Botón alternar Registro/Lista
                    IconButton(onClick = {
                        currentView = if (currentView == "REGISTRO") "LISTA" else "REGISTRO"
                    }) {
                        Icon(
                            if (currentView == "REGISTRO") Icons.Default.List else Icons.Default.PersonAdd,
                            null, tint = Color.White
                        )
                    }

                    // Botón Borrar Último
                    IconButton(onClick = {
                        coroutineScope.launch {
                            val lastUser = dao.getLastUser()
                            if (lastUser != null) {
                                dao.delete(lastUser)
                                if (currentView == "LISTA") userList = dao.getAll()
                            }
                        }
                    }) {
                        Icon(Icons.Default.DeleteForever, "Borrar Último", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepBlue)
            )
        }
    ) { p ->
        Box(modifier = Modifier.padding(p).fillMaxSize().background(IceWhite)) {
            if (currentView == "REGISTRO") {
                Column(
                    modifier = Modifier.padding(24.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .shadow(4.dp, CircleShape)
                            .background(Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, null, modifier = Modifier.size(60.dp), tint = BlueMedium)
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    OutlinedTextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        placeholder = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = BlueMedium
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        placeholder = { Text("Apellido") },
                        modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = BlueMedium
                        )
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            if (firstName.isNotBlank() && lastName.isNotBlank()) {
                                coroutineScope.launch {
                                    dao.insert(User(firstName = firstName, lastName = lastName))
                                    firstName = ""; lastName = ""
                                    currentView = "LISTA"
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(60.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepBlue),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Icon(Icons.Default.Check, null)
                        Spacer(Modifier.width(8.dp))
                        Text("REGISTRAR USUARIO", fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(userList) { user ->
                        Card(
                            modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(20.dp)),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            ListItem(
                                leadingContent = {
                                    Box(Modifier.size(50.dp).background(IceWhite, CircleShape), contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.AccountCircle, null, tint = BlueLight)
                                    }
                                },
                                headlineContent = {
                                    Text("${user.firstName} ${user.lastName}", fontWeight = FontWeight.ExtraBold, color = DeepBlue)
                                },
                                supportingContent = { Text("ID: #${user.uid}", color = BlueMedium) },
                                trailingContent = {
                                    IconButton(onClick = {
                                        coroutineScope.launch {
                                            dao.delete(user)
                                            userList = dao.getAll()
                                        }
                                    }) {
                                        Icon(Icons.Default.Delete, null, tint = Color(0xFFCFD8DC))
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