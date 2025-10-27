package com.example.qualitywash.ui.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.qualitywash.ui.Screen.HomeScreen
import com.example.qualitywash.ui.Screen.LoginScreen
import com.example.qualitywash.ui.Screen.RegisterScreen
import com.example.qualitywash.ui.Screen.WashScreen

// Definición de rutas
object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val WASH = "wash"
}

@Composable
fun AppNavigation(
    startDestination: String = Routes.LOGIN
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Pantalla de Login
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        // Pantalla de Registro
        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // Pantalla Home
        composable(Routes.HOME) {
            HomeScreen(
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToWash = {
                    navController.navigate(Routes.WASH)
                }
            )
        }

        // Pantalla de Lavado
        composable(Routes.WASH) {
            WashScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}