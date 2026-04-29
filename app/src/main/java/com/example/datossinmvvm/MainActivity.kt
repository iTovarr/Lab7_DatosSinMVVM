package com.example.datossinmvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.datossinmvvm.ui.theme.DatosSinMVVMTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DatosSinMVVMTheme {
                // Estado para controlar qué pantalla ver: 0 para Usuarios, 1 para Tareas
                var currentTab by remember { mutableIntStateOf(0) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar(
                            containerColor = Color(0xFF1A374D), // Mismo azul de tus pantallas
                            contentColor = Color.White
                        ) {
                            NavigationBarItem(
                                selected = currentTab == 0,
                                onClick = { currentTab = 0 },
                                label = { Text("Usuarios", color = Color.White) },
                                icon = { Icon(Icons.Default.Person, null, tint = Color.White) },
                                colors = NavigationBarItemDefaults.colors(indicatorColor = Color(0xFF406882))
                            )
                            NavigationBarItem(
                                selected = currentTab == 1,
                                onClick = { currentTab = 1 },
                                label = { Text("Actividades", color = Color.White) },
                                icon = { Icon(Icons.Default.DateRange, null, tint = Color.White) },
                                colors = NavigationBarItemDefaults.colors(indicatorColor = Color(0xFF406882))
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (currentTab) {
                            // Al hacer clic en el calendario de ScreenUser, cambiamos currentTab a 1
                            0 -> ScreenUser(onNavigateToTasks = { currentTab = 1 })
                            1 -> ScreenTask()
                        }
                    }
                }
            }
        }
    }
}