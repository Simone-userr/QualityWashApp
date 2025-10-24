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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogout: () -> Unit = {}
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentUser = UserRepository.getCurrentUser()

    ModalNavigationDrawer(
        drawerState = drawerState,
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
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Quality Wash",
                            fontWeight = FontWeight.Bold
                        )
                    },
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
        ) { paddingValues ->
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
    onLogout: () -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Header del drawer
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

            // Opciones del menú
            DrawerMenuItem(
                icon = Icons.Filled.Person,
                title = "Perfil",
                onClick = {
                    onCloseDrawer()
                    // Navegar a perfil
                }
            )

            DrawerMenuItem(
                icon = Icons.Filled.ShoppingCart,
                title = "Tienda",
                onClick = {
                    onCloseDrawer()
                    // Navegar a tienda
                }
            )

            DrawerMenuItem(
                icon = Icons.Filled.Build,
                title = "Servicios",
                onClick = {
                    onCloseDrawer()
                    // Navegar a servicios
                }
            )

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
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    color: Color = Color(0xFF667eea)
) {
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
        selected = false,
        onClick = onClick,
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

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = gradientColors
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            // Saludo
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickAccessCard(
                    icon = Icons.Filled.ShoppingCart,
                    title = "Tienda",
                    modifier = Modifier.weight(1f)
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