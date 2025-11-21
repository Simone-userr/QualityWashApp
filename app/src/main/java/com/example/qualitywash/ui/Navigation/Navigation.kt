package com.example.qualitywash.ui.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.qualitywash.ui.Data.UserRepository
import com.example.qualitywash.ui.Screen.HomeScreen
import com.example.qualitywash.ui.Screen.LoginScreen
import com.example.qualitywash.ui.Screen.PerfilScreen
import com.example.qualitywash.ui.Screen.RegisterScreen
import com.example.qualitywash.ui.Screen.TiendaScreen
import com.example.qualitywash.ui.Screen.WashScreen
import com.example.qualitywash.ui.Screen.HistoryScreen   // ✅ IMPORT REAL

// Definición de rutas
object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val WASH = "wash"
    const val PERFIL = "perfil"
    const val HISTORY = "history"
    const val TIENDA = "tienda"
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

        // 1. Pantalla Login
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        // 2. Pantalla Registro
        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // 3. Pantalla Home
        composable(Routes.HOME) {
            HomeScreen(
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToWash = {
                    navController.navigate(Routes.WASH)
                },
                onNavigateToPerfil = {
                    navController.navigate(Routes.PERFIL)
                },
                onNavigateToTienda = {
                    navController.navigate(Routes.TIENDA)
                }
            )
        }

        // 4. Pantalla Perfil
        composable(Routes.PERFIL) {
            PerfilScreen(
                onNavigateBack = { navController.popBackStack() },
                onLogoutComplete = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToHistory = {   // 👈 AGREGADO
                    navController.navigate(Routes.HISTORY)
                }
            )
        }

        // 5. Pantalla Lavado
        composable(Routes.WASH) {
            WashScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 6. Pantalla Tienda
        composable(Routes.TIENDA) {
            TiendaScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 7. Pantalla Historial
        composable(Routes.HISTORY) {
            val user = UserRepository.getCurrentUser()
            HistoryScreen(
                userId = user?.id ?: "1",
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
