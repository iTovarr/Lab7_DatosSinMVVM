package com.example.datossinmvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.datossinmvvm.ui.theme.DatosSinMVVMTheme
import com.example.datossinmvvm.ScreenUser
import com.example.datossinmvvm.ui.ScreenProduct

class MainActivity : ComponentActivity() { // DEBE ser ComponentActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DatosSinMVVMTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // ScreenUser() para el Ejercicio 1
                    // ScreenProduct() para el Ejercicio 2
                    ScreenProduct()
                }
            }
        }
    }
}