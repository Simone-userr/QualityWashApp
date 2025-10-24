package com.example.qualitywash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.qualitywash.ui.Screen.HomeScreen
import com.example.qualitywash.ui.Screen.LoginScreen
import com.example.qualitywash.ui.Screen.RegisterScreen
import com.example.qualitywash.ui.theme.QualityWashTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QualityWashTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    var currentScreen by remember { mutableStateOf("login") }

    when (currentScreen) {
        "login" -> {
            LoginScreen(
                onLoginSuccess = {
                    currentScreen = "home"
                },
                onNavigateToRegister = {
                    currentScreen = "register"
                }
            )
        }

        "register" -> {
            RegisterScreen(
                onRegisterSuccess = {
                    currentScreen = "home"
                },
                onNavigateToLogin = {
                    currentScreen = "login"
                }
            )
        }

        "home" -> {
            HomeScreen(
                onLogout = {
                    currentScreen = "login"
                }
            )
        }
    }
}