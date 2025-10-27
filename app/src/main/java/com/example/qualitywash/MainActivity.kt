package com.example.qualitywash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.qualitywash.ui.Data.UserRepository
import com.example.qualitywash.ui.Navigation.AppNavigation
import com.example.qualitywash.ui.Navigation.Routes
import com.example.qualitywash.ui.theme.QualityWashTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            QualityWashTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Determina la pantalla inicial basándose en si hay usuario logueado
                    val startDestination = if (UserRepository.isUserLoggedIn()) {
                        Routes.HOME
                    } else {
                        Routes.LOGIN
                    }

                    AppNavigation(startDestination = startDestination)
                }
            }
        }
    }
}