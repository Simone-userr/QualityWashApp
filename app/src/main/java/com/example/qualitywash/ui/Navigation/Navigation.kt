package com.example.qualitywash.ui.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.qualitywash.ui.Screen.GestionUsuariosScreen
import com.example.qualitywash.ui.Screen.HomeScreen
import com.example.qualitywash.ui.Screen.LoginScreen
import com.example.qualitywash.ui.Screen.PerfilScreen
import com.example.qualitywash.ui.Screen.RegisterScreen
import com.example.qualitywash.ui.Screen.EstadisticasScreen
import com.example.qualitywash.ui.Screen.TiendaScreen
import com.example.qualitywash.ui.Screen.WashScreen

// Definición de rutas
object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val WASH = "wash"
    const val PERFIL = "perfil"

    const val TIENDA = "tienda"

    const val GESTION_USUARIO = "gestion_usuario"
    const val GESTION_PRODUCTO = "gestion_producto"
    const val ESTADISTICAS = "estadisticas"
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

        // 1. Pantalla de Login (MVVM/Rutas Optimizadas)
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    // Navega a HOME y limpia la pila, eliminando Login
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

        // 2. Pantalla de Registro (MVVM/Rutas Optimizadas)
        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    // Navega a HOME y limpia la pila, eliminando Register
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToLogin = {
                    // Simplemente vuelve a la pantalla anterior (Login)
                    navController.popBackStack()
                }
            )
        }

        // 3. Pantalla Home (Rutas sin cambios)
        composable(Routes.HOME) {
            HomeScreen(
                onLogout = {
                    navController.navigate(Routes.LOGIN) { popUpTo(0) { inclusive = true } }
                },
                //Navega desde home a WashScreen
                onNavigateToWash = {
                    navController.navigate(Routes.WASH)
                },
                //Navega desde Home a PerfilScreen
                onNavigateToPerfil = {
                    navController.navigate(Routes.PERFIL)
                },
                //Navega desde home a TiendaScreen
                onNavigateToTienda = {
                    navController.navigate(Routes.TIENDA)
                },
                //Navega desde home a EstadisticasScreen
                onNavigateToEstadisticas = {
                    navController.navigate(Routes.ESTADISTICAS)
                }
            )
        }

        // 4. Pantalla de Perfil
        composable(Routes.PERFIL) {
            PerfilScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onLogoutComplete = {
                    // Navega al Login y limpia *toda* la pila de navegación.
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // 5. Pantalla de Lavado
        composable(Routes.WASH) {
            WashScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        //6. Pantalla de Tienda
        composable(Routes.TIENDA) {
            TiendaScreen (
                onNavigateBack = {
                    navController.popBackStack()
                }

            )
        }
        //7. Pantalla de Gestion de Usuarios
        composable(Routes.GESTION_USUARIO) {
            GestionUsuariosScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        //8. Pantalla de Estadísticas
        composable(Routes.ESTADISTICAS) {
            EstadisticasScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}