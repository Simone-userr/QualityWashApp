package com.example.qualitywash.ui.Screen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.qualitywash.ui.Data.PermissionsRepository
import com.example.qualitywash.ui.Data.UserRole
import com.example.qualitywash.ui.theme.getRoleColor
import com.example.qualitywash.ui.theme.getRoleText
import com.example.qualitywash.ui.viewModel.ProfileViewModel
import com.example.qualitywash.ui.viewModel.ProfileViewModelFactory
import java.io.File
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    onNavigateBack: () -> Unit,
    onLogoutComplete: () -> Unit
) {
    val viewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory())
    val user by viewModel.userState.collectAsState()
    val context = LocalContext.current

    var showEditDialog by remember { mutableStateOf(false) }
    var showImageSourceDialog by remember { mutableStateOf(false) }
    var showPermissionsExpanded by remember { mutableStateOf(false) }
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var tempPhotoUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(user.photoUri) {
        photoUri = if (user.photoUri != null) user.photoUri!!.toUri() else null
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempPhotoUri != null) {
            photoUri = tempPhotoUri
            viewModel.updateProfilePhoto(tempPhotoUri.toString())
            Toast.makeText(context, "Foto actualizada", Toast.LENGTH_SHORT).show()
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            photoUri = it
            viewModel.updateProfilePhoto(it.toString())
            Toast.makeText(context, "Foto actualizada", Toast.LENGTH_SHORT).show()
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            tempPhotoUri = createImageUri(context)
            tempPhotoUri?.let { cameraLauncher.launch(it) }
        } else {
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF00A896),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Foto de perfil
            Box(
                modifier = Modifier.size(150.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF00A896).copy(alpha = 0.1f))
                        .border(4.dp, Color(0xFF00A896), CircleShape)
                        .clickable { showImageSourceDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    if (photoUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(photoUri),
                            contentDescription = "Foto de perfil",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Sin foto",
                            modifier = Modifier.size(80.dp),
                            tint = Color(0xFF00A896)
                        )
                    }
                }

                FloatingActionButton(
                    onClick = { showImageSourceDialog = true },
                    modifier = Modifier.size(45.dp),
                    containerColor = Color(0xFF667eea)
                ) {
                    Icon(
                        imageVector = Icons.Filled.CameraAlt,
                        contentDescription = "Cambiar foto",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Badge de rol
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = getRoleColor(user.role).copy(alpha = 0.2f),
                border = androidx.compose.foundation.BorderStroke(1.dp, getRoleColor(user.role))
            ) {
                Text(
                    text = getRoleText(user.role),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    color = getRoleColor(user.role),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Tarjeta de información básica
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    ProfileInfoRow(
                        icon = Icons.Filled.Person,
                        label = "Nombre",
                        value = user.name
                    )

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    ProfileInfoRow(
                        icon = Icons.Filled.Email,
                        label = "Correo",
                        value = user.email
                    )

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    ProfileInfoRow(
                        icon = Icons.Filled.Shield,
                        label = "Rol",
                        value = getRoleText(user.role)
                    )

                    if (user.phoneNumber != null) {
                        Divider(modifier = Modifier.padding(vertical = 12.dp))
                        ProfileInfoRow(
                            icon = Icons.Filled.Phone,
                            label = "Teléfono",
                            value = user.phoneNumber!!
                        )
                    }

                    if (user.addresss != null) {
                        Divider(modifier = Modifier.padding(vertical = 12.dp))
                        ProfileInfoRow(
                            icon = Icons.Filled.LocationOn,
                            label = "Dirección",
                            value = user.addresss!!
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta de permisos expandible
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = getRoleColor(user.role).copy(alpha = 0.05f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showPermissionsExpanded = !showPermissionsExpanded },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Security,
                                contentDescription = null,
                                tint = getRoleColor(user.role),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Mis Permisos",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = getRoleColor(user.role)
                            )
                        }
                        Icon(
                            imageVector = if (showPermissionsExpanded)
                                Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                            contentDescription = if (showPermissionsExpanded) "Colapsar" else "Expandir",
                            tint = getRoleColor(user.role)
                        )
                    }

                    AnimatedVisibility(
                        visible = showPermissionsExpanded,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Column(modifier = Modifier.padding(top = 16.dp)) {
                            val permissions = PermissionsRepository.getPermissionsByRole(user.role)
                            permissions.forEach { permission ->
                                PermissionItem(permission = permission)
                                if (permission != permissions.last()) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón editar perfil
            Button(
                onClick = { showEditDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF667eea)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Filled.Edit, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Editar Perfil", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botón cerrar sesión
            OutlinedButton(
                onClick = {
                    viewModel.logout()
                    onLogoutComplete()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Filled.ExitToApp, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar Sesión", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // Diálogos...
    if (showImageSourceDialog) {
        AlertDialog(
            onDismissRequest = { showImageSourceDialog = false },
            title = { Text("Seleccionar foto") },
            text = {
                Column {
                    TextButton(
                        onClick = {
                            showImageSourceDialog = false
                            when (PackageManager.PERMISSION_GRANTED) {
                                ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) -> {
                                    tempPhotoUri = createImageUri(context)
                                    tempPhotoUri?.let { cameraLauncher.launch(it) }
                                }
                                else -> cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.CameraAlt, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Tomar foto")
                    }

                    TextButton(
                        onClick = {
                            showImageSourceDialog = false
                            galleryLauncher.launch("image/*")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.Photo, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Elegir de galería")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showImageSourceDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showEditDialog) {
        EditProfileDialog(
            user = user,
            onDismiss = { showEditDialog = false },
            onSave = { name, phone, address ->
                viewModel.updateProfile(name, phone, address)
                showEditDialog = false
                Toast.makeText(context, "Perfil actualizado", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@Composable
fun PermissionItem(permission: com.example.qualitywash.ui.Data.Permission) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = permission.icon,
            contentDescription = null,
            tint = Color(0xFF4CAF50),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = permission.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black.copy(alpha = 0.87f)
            )
            Text(
                text = permission.description,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = "Permitido",
            tint = Color(0xFF4CAF50),
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun ProfileInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF00A896),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun EditProfileDialog(
    user: com.example.qualitywash.ui.Data.User,
    onDismiss: () -> Unit,
    onSave: (String, String?, String?) -> Unit
) {
    var name by remember { mutableStateOf(user.name) }
    var phone by remember { mutableStateOf(user.phoneNumber ?: "") }
    var address by remember { mutableStateOf(user.addresss ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Perfil") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Teléfono (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Dirección (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(name, phone.ifBlank { null }, address.ifBlank { null })
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

fun createImageUri(context: Context): Uri {
    val timeStamp = System.currentTimeMillis()
    val imageFile = File(context.cacheDir, "profile_$timeStamp.jpg")
    return FileProvider.getUriForFile(
        context,
        "com.example.qualitywash.fileprovider",
        imageFile
    )
}