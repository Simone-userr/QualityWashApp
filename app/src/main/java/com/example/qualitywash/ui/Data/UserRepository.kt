package com.example.qualitywash.ui.Data

import android.util.Log

// Enumeración de roles simplificada
enum class UserRole {
    Cliente,   // Usuario que compra productos
    ADMIN      // Administrador del sistema
}

// Modelo de Usuario
data class User(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    val role: UserRole = UserRole.Cliente,
    val photoUri: String? = null,
    val phoneNumber: String? = null,
    val addresss: String? = null
)

// Repositorio de usuarios
object UserRepository {
    private val users = mutableListOf<User>()
    private var currentUser: User? = null

    private const val TAG = "UserRepository"

    init {
        // Usuario Cliente Demo
        val clienteDemo = User(
            id = "1",
            name = "Cliente Demo",
            email = "demo@mail.com",
            password = "Demo123@",
            role = UserRole.Cliente
        )
        users.add(clienteDemo)
        Log.d(TAG, "Cliente Demo agregado: ${clienteDemo.email}")

        // Usuario Administrador
        val admin = User(
            id = "2",
            name = "Administrador",
            email = "admin@mail.com",
            password = "Admin123@",
            role = UserRole.ADMIN
        )
        users.add(admin)
        Log.d(TAG, "Admin agregado: ${admin.email}")

        Log.d(TAG, "Total usuarios inicializados: ${users.size}")
    }

    fun registerUser(name: String, email: String, password: String): Pair<Boolean, String> {
        Log.d(TAG, "Intentando registrar: $email")

        if (users.any { it.email.equals(email, ignoreCase = true) }) {
            Log.w(TAG, "Email ya existe: $email")
            return Pair(false, "Este email ya está registrado")
        }

        val newUser = User(
            id = (users.size + 1).toString(),
            name = name,
            email = email,
            password = password,
            role = UserRole.Cliente  // Los nuevos registros siempre son clientes
        )

        users.add(newUser)
        currentUser = newUser
        Log.d(TAG, "Usuario registrado exitosamente: $email como Cliente")

        return Pair(true, "Registro exitoso")
    }

    fun loginUser(email: String, password: String): Pair<Boolean, String> {
        Log.d(TAG, "=== INTENTO DE LOGIN ===")
        Log.d(TAG, "Email: $email")

        val user = users.find {
            it.email.equals(email, ignoreCase = true) && it.password == password
        }

        return if (user != null) {
            currentUser = user
            Log.d(TAG, "✓ LOGIN EXITOSO: ${user.email} | Rol: ${user.role}")
            Pair(true, "Inicio de sesión exitoso")
        } else {
            Log.e(TAG, "✗ LOGIN FALLIDO")
            Pair(false, "Email o contraseña incorrectos")
        }
    }

    fun logoutUser() {
        Log.d(TAG, "Logout: ${currentUser?.email}")
        currentUser = null
    }

    fun getCurrentUser(): User? = currentUser

    fun isUserLoggedIn(): Boolean = currentUser != null

    fun getAllUsers(): List<User> = users.toList()

    fun isAdmin(): Boolean = currentUser?.role == UserRole.ADMIN

    fun updateProfilePhoto(photoUri: String): Boolean {
        val user = currentUser ?: return false
        val updatedUser = user.copy(photoUri = photoUri)
        val index = users.indexOfFirst { it.id == user.id }
        if (index != -1) {
            users[index] = updatedUser
            currentUser = updatedUser
            Log.d(TAG, "Foto actualizada: ${user.email}")
            return true
        }
        return false
    }

    fun updateUserProfile(name: String, phoneNumber: String?, address: String?): Boolean {
        val user = currentUser ?: return false
        val updatedUser = user.copy(
            name = name,
            phoneNumber = phoneNumber,
            addresss = address
        )
        val index = users.indexOfFirst { it.id == user.id }
        if (index != -1) {
            users[index] = updatedUser
            currentUser = updatedUser
            Log.d(TAG, "Perfil actualizado: ${user.email}")
            return true
        }
        return false
    }

    // FUNCIONES ADMIN PARA GESTIONAR USUARIOS
    fun createUser(name: String, email: String, password: String, role: UserRole): Pair<Boolean, String> {
        if (!isAdmin()) {
            return Pair(false, "No tienes permisos para crear usuarios")
        }

        if (users.any { it.email.equals(email, ignoreCase = true) }) {
            return Pair(false, "Este email ya está registrado")
        }

        val newUser = User(
            id = (users.size + 1).toString(),
            name = name,
            email = email,
            password = password,
            role = role
        )

        users.add(newUser)
        Log.d(TAG, "Admin creó usuario: $email con rol $role")
        return Pair(true, "Usuario creado exitosamente")
    }

    fun updateUserRole(userId: String, newRole: UserRole): Boolean {
        if (!isAdmin()) return false

        val index = users.indexOfFirst { it.id == userId }
        if (index != -1) {
            val user = users[index]
            users[index] = user.copy(role = newRole)
            Log.d(TAG, "Admin actualizó rol de ${user.email} a $newRole")
            return true
        }
        return false
    }

    fun deleteUser(userId: String): Boolean {
        if (!isAdmin()) return false
        if (currentUser?.id == userId) return false // No puede eliminarse a sí mismo

        val removed = users.removeIf { it.id == userId }
        if (removed) {
            Log.d(TAG, "Admin eliminó usuario con ID: $userId")
        }
        return removed
    }

    fun clearUsers() {
        users.clear()
        currentUser = null

        users.add(
            User(
                id = "1",
                name = "Cliente Demo",
                email = "demo@mail.com",
                password = "Demo123@",
                role = UserRole.Cliente
            )
        )
        users.add(
            User(
                id = "2",
                name = "Administrador",
                email = "admin@mail.com",
                password = "Admin123@",
                role = UserRole.ADMIN
            )
        )
        Log.d(TAG, "Usuarios reinicializados")
    }
}