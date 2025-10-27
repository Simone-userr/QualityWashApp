// Archivo: AppNavigation.kt
package com.example.qualitywash.ui.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.qualitywash.ui.Screen.HomeScreen
import com.example.qualitywash.ui.Screen.LoginScreen
import com.example.qualitywash.ui.Screen.PerfilScreen // Asegúrate de tener este import
import com.example.qualitywash.ui.Screen.RegisterScreen
import com.example.qualitywash.ui.Screen.WashScreen

// Definición de rutas (asumiendo que PERFIL ya está aquí)
object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val WASH = "wash"
    const val PERFIL = "perfil"
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
        // Pantalla de Login (SIN CAMBIOS)
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) { popUpTo(Routes.LOGIN) { inclusive = true } }
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        // Pantalla de Registro (SIN CAMBIOS)
        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.HOME) { popUpTo(Routes.REGISTER) { inclusive = true } }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // Pantalla Home (SIN CAMBIOS)
        composable(Routes.HOME) {
            HomeScreen(
                onLogout = {
                    // Esta lógica de logout será reemplazada por la que viene de Perfil,
                    // pero la mantenemos por si la usas en otro lado.
                    navController.navigate(Routes.LOGIN) { popUpTo(0) { inclusive = true } }
                },
                onNavigateToWash = {
                    navController.navigate(Routes.WASH)
                },
                onNavigateToPerfil = {
                    navController.navigate(Routes.PERFIL)
                }
            )
        }

        // 🚨 PANTALLA DE PERFIL: ÚNICA SECCIÓN CON CAMBIOS
        composable(Routes.PERFIL) {
            PerfilScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                // Implementación de la navegación al completar el logout:
                onLogoutComplete = {
                    // Navega al Login y borra *toda* la pila de navegación (Home, Perfil, etc.)
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        // FIN DE LA ÚNICA SECCIÓN CON CAMBIOS

        // Pantalla de Lavado (SIN CAMBIOS)
        composable(Routes.WASH) {
            WashScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}