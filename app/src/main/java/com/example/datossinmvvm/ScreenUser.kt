package com.example.datossinmvvm

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenUser() {
    val context = LocalContext.current
    val db = remember { Room.databaseBuilder(context, UserDatabase::class.java, "user_db").build() }
    val dao = db.userDao()
    val coroutineScope = rememberCoroutineScope()

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var dataUser by remember { mutableStateOf("") }

    // Implementación de Scaffold según el ejercicio
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Datos Sin MVVM") },
                actions = {
                    // Botón para Agregar Usuario
                    IconButton(onClick = {
                        val user = User(0, firstName, lastName)
                        coroutineScope.launch {
                            dao.insert(user)
                            firstName = ""
                            lastName = ""
                        }
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Agregar Usuario")
                    }

                    // Botón para Listar Usuarios
                    IconButton(onClick = {
                        coroutineScope.launch {
                            val users = dao.getAll()
                            dataUser = users.joinToString("\n") { "${it.firstName} ${it.lastName}" }
                        }
                    }) {
                        Icon(Icons.Default.List, contentDescription = "Listar Usuarios")
                    }

                    // Botón para Eliminar Último Registro (Funcionalidad Ejercicio 1)
                    IconButton(onClick = {
                        coroutineScope.launch {
                            val lastUser = dao.getLastUser()
                            if (lastUser != null) {
                                dao.delete(lastUser)
                                // Actualizamos la vista después de borrar
                                val users = dao.getAll()
                                dataUser = users.joinToString("\n") { "${it.firstName} ${it.lastName}" }
                            }
                        }
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar Último")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            TextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = dataUser,
                modifier = Modifier.padding(top = 20.dp),
                fontSize = 18.sp
            )
        }
    }
}