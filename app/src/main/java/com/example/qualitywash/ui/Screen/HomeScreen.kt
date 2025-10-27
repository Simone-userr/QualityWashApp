package com.example.qualitywash.ui.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qualitywash.ui.Data.User
import com.example.qualitywash.ui.Data.UserRepository
import kotlinx.coroutines.launch


//OptIn anotacion que permite utilizar componentes o APIs de material3
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    //Parametros que recibira la funcion para concretar la navegacion entre Screen
    onLogout: () -> Unit = {},
    onNavigateToWash: () -> Unit = {},
    onNavigateToPerfil: () -> Unit,
    onNavigateToTienda: () -> Unit = {}
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed) //Crea y recuerda el estado del menu lateral (Drawer)
    val scope = rememberCoroutineScope()                                     //Obtiene un scope de corrutinas necesario para ejecutar acciones
    val currentUser = UserRepository.getCurrentUser()                        //Llama al repositorio de datos para obtener infomarcion del usuario actualmente iniciado

    //Componente principal que implementa el menu lateral desplegable
    ModalNavigationDrawer(
        drawerState = drawerState,
        //drawerContent: define el contenido del menú lateral, llamando al componente DrawerContent y pasándole la lógica para cerrar el menú y navegar/cerrar sesión.
        drawerContent = {
            DrawerContent(
                currentUser = currentUser,
                onCloseDrawer = {
                    scope.launch { drawerState.close() }
                },
                onLogout = {
                    scope.launch { drawerState.close() }
                    UserRepository.logoutUser()
                    onLogout()
                },
                onNavigateToWash = {
                    scope.launch { drawerState.close() }
                    onNavigateToWash()
                },
                onNavigateToPerfil = onNavigateToPerfil,
                onNavigateToTienda = {
                    scope.launch { drawerState.close() }
                    onNavigateToTienda()
                }
            )
        }
    ) {
        //La estructura base de la pantalla que organiza los componentes visuales: topBar
        Scaffold(
            //Define la barra de la aplicación en la parte superior.
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Quality Wash",
                            fontWeight = FontWeight.Bold
                        )
                    },
                    //Define el ícono de la izquierda que, al hacer clic, usa el scope.launch { drawerState.open() } para abrir el menú lateral.
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = "Menú"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF667eea),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            }
        ) { // El área de contenido principal de la pantalla, que se muestra debajo de la barra superior, recibiendo los paddings generados por el Scaffold
            paddingValues ->
            MainContent(
                modifier = Modifier.padding(paddingValues),
                userName = currentUser?.name ?: "Usuario"
            )
        }
    }
}

@Composable
fun DrawerContent(
    currentUser: User?,
    onCloseDrawer: () -> Unit,
    onLogout: () -> Unit,
    onNavigateToWash: () -> Unit = {},
    onNavigateToPerfil: () -> Unit,
    onNavigateToTienda: () -> Unit = {}

) {
    //Contenedor visual que le da estilo (color, forma) a la hoja del menú lateral.
    ModalDrawerSheet(
        drawerContainerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            //Un contenedor para la sección de bienvenida en la parte superior del menú, que muestra el ícono, nombre y correo del usuario.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF667eea),
                                Color(0xFF764ba2)
                            )
                        )
                    )
                    .padding(24.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Column {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Usuario",
                        modifier = Modifier.size(64.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = currentUser?.name ?: "Usuario",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = currentUser?.email ?: "",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Un componente reutilizable que se usa para cada opción del menú (Perfil, Tienda, Lavado, etc.).
            DrawerMenuItem(
                icon = Icons.Filled.Person,
                title = "Perfil",
                onClick = {
                    onCloseDrawer()
                    onNavigateToPerfil()
                }
            )

            DrawerMenuItem(
                icon = Icons.Filled.ShoppingCart,
                title = "Tienda",
                onClick = {
                    onCloseDrawer()
                    onNavigateToTienda()
                }
            )

            DrawerMenuItem(
                icon = Icons.Filled.Wash,
                title = "Lavado",
                onClick = {
                    onCloseDrawer()
                    onNavigateToWash()
                }
            )

            DrawerMenuItem(
                icon = Icons.Filled.Build,
                title = "Servicios",
                onClick = {
                    onCloseDrawer()
                }
            )

            //Una línea horizontal para separar grupos de elementos, como las opciones principales de "Cerrar Sesión".
            Divider(modifier = Modifier.padding(vertical = 8.dp))

            DrawerMenuItem(
                icon = Icons.Filled.ExitToApp,
                title = "Cerrar Sesión",
                onClick = onLogout,
                color = Color(0xFFF44336)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Footer
            Text(
                text = "Quality Wash v1.0",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun DrawerMenuItem(
    // Recibe los datos y la acción para crear el ítem.
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    color: Color = Color(0xFF667eea)
) {
    // Un componente de Material 3 diseñado específicamente para ítems dentro de un menú lateral.
    NavigationDrawerItem(
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color
            )
        },
        label = {
            Text(
                text = title,
                color = color,
                fontWeight = FontWeight.Medium
            )
        },
        //Indica que este ítem no está marcado como actualmente seleccionado
        selected = false,
        onClick = onClick,
        //Añade espacio horizontal y vertical alrededor del ítem para que no se pegue a los bordes.
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
    )
}

@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    userName: String
) {
    val gradientColors = listOf(
        Color(0xFF667eea),
        Color(0xFF764ba2),
        Color(0xFFf093fb)
    )
    //Establece un fondo con un degradado de color que ocupa toda la pantalla.
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = gradientColors
                )
            )
    ) {
        //Permite que el contenido sea desplazable verticalmente si es demasiado largo para la pantalla del dispositivo.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            //Se utiliza el componente Card de Material 3 para crear contenedores visualmente atractivos
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Bienvenida",
                        modifier = Modifier.size(60.dp),
                        tint = Color(0xFFFFD700)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    // Saluda al usuario
                    Text(
                        text = "¡Hola, $userName!",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF667eea),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Bienvenido a Quality Wash",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            //Contiene texto informativo sobre la empresa.
            // Quiénes Somos
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "Información",
                            tint = Color(0xFF667eea),
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "¿Quiénes Somos?",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF667eea)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Quality Wash es tu centro de lavado de confianza, comprometido con la excelencia y el cuidado de tus vehículos.",
                        fontSize = 15.sp,
                        color = Color.Black.copy(alpha = 0.8f),
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Justify
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Con más de 10 años de experiencia, ofrecemos servicios profesionales de lavado, encerado, pulido y detallado automotriz utilizando productos de la más alta calidad.",
                        fontSize = 15.sp,
                        color = Color.Black.copy(alpha = 0.8f),
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Justify
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Divider()

                    Spacer(modifier = Modifier.height(16.dp))

                    // Características
                    FeatureItem(
                        icon = Icons.Filled.CheckCircle,
                        text = "Personal capacitado y profesional"
                    )
                    FeatureItem(
                        icon = Icons.Filled.CheckCircle,
                        text = "Productos ecológicos de alta calidad"
                    )
                    FeatureItem(
                        icon = Icons.Filled.CheckCircle,
                        text = "Atención personalizada"
                    )
                    FeatureItem(
                        icon = Icons.Filled.CheckCircle,
                        text = "Precios competitivos"
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tarjetas de acceso rápido
            Text(
                text = "Acceso Rápido",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            // Un Row que contiene dos QuickAccessCards, permitiendo al usuario ir rápidamente a Tienda o Servicios. (no funcional preferible por el menu lateral)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickAccessCard(
                    icon = Icons.Filled.ShoppingCart,
                    title = "Tienda",
                    modifier = Modifier.weight(1f),
                )
                QuickAccessCard(
                    icon = Icons.Filled.Build,
                    title = "Servicios",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun FeatureItem(icon: ImageVector, text: String) {
    //Una Row simple que alinea un ícono y un texto, utilizado para las características de la empresa en la sección "Quiénes Somos".
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF4CAF50),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color.Black.copy(alpha = 0.7f)
        )
    }
}

//Un Card diseñado como un botón visual para navegación rápida, mostrando un ícono y un título centrados.
@Composable
fun QuickAccessCard(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color(0xFF667eea),
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF667eea)
            )
        }
    }
}