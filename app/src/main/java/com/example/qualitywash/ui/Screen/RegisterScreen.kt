package com.example.qualitywash.ui.Screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.example.qualitywash.ui.Data.UserRepository

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val gradientColors = listOf(
        Color(0xFF667eea),
        Color(0xFF764ba2),
        Color(0xFFf093fb)
    )

    Box(
        modifier = Modifier
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
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Logo
            Icon(
                imageVector = Icons.Filled.PersonAdd,
                contentDescription = "Logo",
                modifier = Modifier.size(100.dp),
                tint = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Título
            Text(
                text = "Crear Cuenta",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtítulo
            Text(
                text = "Regístrate para comenzar",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Card contenedor
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    // Nombre Input
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            nameError = validateName(it)
                        },
                        label = { Text("Nombre completo") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = "Nombre"
                            )
                        },
                        trailingIcon = {
                            if (name.isNotEmpty()) {
                                IconButton(onClick = { name = "" }) {
                                    Icon(
                                        imageVector = Icons.Filled.Clear,
                                        contentDescription = "Limpiar"
                                    )
                                }
                            }
                        },
                        isError = nameError != null,
                        supportingText = {
                            nameError?.let {
                                Text(
                                    text = it,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Email Input
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = validateEmail(it)
                        },
                        label = { Text("Email") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Email,
                                contentDescription = "Email"
                            )
                        },
                        trailingIcon = {
                            if (email.isNotEmpty()) {
                                IconButton(onClick = { email = "" }) {
                                    Icon(
                                        imageVector = Icons.Filled.Clear,
                                        contentDescription = "Limpiar"
                                    )
                                }
                            }
                        },
                        isError = emailError != null,
                        supportingText = {
                            emailError?.let {
                                Text(
                                    text = it,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password Input
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            passwordError = validatePassword(it)
                            // Re-validar confirmación si ya tiene contenido
                            if (confirmPassword.isNotEmpty()) {
                                confirmPasswordError = validateConfirmPassword(password, confirmPassword)
                            }
                        },
                        label = { Text("Contraseña") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Lock,
                                contentDescription = "Contraseña"
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible)
                                        Icons.Filled.RemoveRedEye
                                    else
                                        Icons.Filled.Lock,
                                    contentDescription = if (passwordVisible)
                                        "Ocultar contraseña"
                                    else
                                        "Mostrar contraseña"
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        isError = passwordError != null,
                        supportingText = {
                            if (passwordError != null) {
                                Text(
                                    text = passwordError!!,
                                    color = MaterialTheme.colorScheme.error
                                )
                            } else {
                                Text(
                                    text = "Mínimo 8 caracteres, 1 mayúscula, 1 número",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Confirm Password Input
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            confirmPasswordError = validateConfirmPassword(password, it)
                        },
                        label = { Text("Confirmar contraseña") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Lock,
                                contentDescription = "Confirmar contraseña"
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(
                                    imageVector = if (confirmPasswordVisible)
                                        Icons.Filled.RemoveRedEye
                                    else
                                        Icons.Filled.Lock,
                                    contentDescription = if (confirmPasswordVisible)
                                        "Ocultar contraseña"
                                    else
                                        "Mostrar contraseña"
                                )
                            }
                        },
                        visualTransformation = if (confirmPasswordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        isError = confirmPasswordError != null,
                        supportingText = {
                            confirmPasswordError?.let {
                                Text(
                                    text = it,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                attemptRegister(
                                    context,
                                    name, email, password, confirmPassword,
                                    nameError, emailError, passwordError, confirmPasswordError,
                                    onUpdateErrors = { n, e, p, c ->
                                        nameError = n
                                        emailError = e
                                        passwordError = p
                                        confirmPasswordError = c
                                    },
                                    onSuccess = onRegisterSuccess
                                )
                            }
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón de Registro
                    Button(
                        onClick = {
                            attemptRegister(
                                context,
                                name, email, password, confirmPassword,
                                nameError, emailError, passwordError, confirmPasswordError,
                                onUpdateErrors = { n, e, p, c ->
                                    nameError = n
                                    emailError = e
                                    passwordError = p
                                    confirmPasswordError = c
                                },
                                onSuccess = onRegisterSuccess
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF667eea)
                        )
                    ) {
                        Text(
                            text = "Registrarse",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Ya tengo cuenta
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿Ya tienes cuenta? ",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
                TextButton(onClick = onNavigateToLogin) {
                    Text(
                        text = "Inicia sesión",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// Funciones de validación
private fun validateName(name: String): String? {
    return when {
        name.isEmpty() -> "El nombre es requerido"
        name.length < 3 -> "El nombre debe tener al menos 3 caracteres"
        !name.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) -> "Solo se permiten letras"
        else -> null
    }
}

private fun validateEmail(email: String): String? {
    return when {
        email.isEmpty() -> "El email es requerido"
        !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Email inválido"
        else -> null
    }
}

private fun validatePassword(password: String): String? {
    return when {
        password.isEmpty() -> "La contraseña es requerida"
        password.length < 8 -> "Mínimo 8 caracteres"
        !password.any { it.isUpperCase() } -> "Debe contener al menos una mayúscula"
        !password.any { it.isDigit() } -> "Debe contener al menos un número"
        else -> null
    }
}

private fun validateConfirmPassword(password: String, confirmPassword: String): String? {
    return when {
        confirmPassword.isEmpty() -> "Confirma tu contraseña"
        password != confirmPassword -> "Las contraseñas no coinciden"
        else -> null
    }
}

private fun attemptRegister(
    context: android.content.Context,
    name: String,
    email: String,
    password: String,
    confirmPassword: String,
    nameError: String?,
    emailError: String?,
    passwordError: String?,
    confirmPasswordError: String?,
    onUpdateErrors: (String?, String?, String?, String?) -> Unit,
    onSuccess: () -> Unit
) {
    val finalNameError = validateName(name)
    val finalEmailError = validateEmail(email)
    val finalPasswordError = validatePassword(password)
    val finalConfirmPasswordError = validateConfirmPassword(password, confirmPassword)

    onUpdateErrors(
        finalNameError,
        finalEmailError,
        finalPasswordError,
        finalConfirmPasswordError
    )

    if (finalNameError == null &&
        finalEmailError == null &&
        finalPasswordError == null &&
        finalConfirmPasswordError == null) {

        // Registrar usuario en el repositorio
        val (success, message) = UserRepository.registerUser(name, email, password)

        if (success) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            onSuccess()
        } else {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            onUpdateErrors(null, message, null, null)
        }
    }
}