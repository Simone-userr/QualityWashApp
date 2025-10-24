package com.example.qualitywash.ui.Data

// Modelo de Usuario
data class User(
    val id: String,
    val name: String,
    val email: String,
    val password: String
)

// Repositorio de usuarios en memoria (Base de datos ficticia)
object UserRepository {
    private val users = mutableListOf<User>()
    private var currentUser: User? = null

    init {
        // Agregar algunos usuarios de prueba
        users.add(
            User(
                id = "1",
                name = "Usuario Demo",
                email = "demo@mail.com",
                password = "Demo123"
            )
        )
        users.add(
            User(
                id = "2",
                name = "Admin Test",
                email = "admin@mail.com",
                password = "Admin123"
            )
        )
    }

    /**
     * Registra un nuevo usuario
     * @return Pair<Boolean, String> - (éxito, mensaje)
     */
    fun registerUser(name: String, email: String, password: String): Pair<Boolean, String> {
        // Verificar si el email ya existe
        if (users.any { it.email.equals(email, ignoreCase = true) }) {
            return Pair(false, "Este email ya está registrado")
        }

        // Crear nuevo usuario
        val newUser = User(
            id = (users.size + 1).toString(),
            name = name,
            email = email,
            password = password
        )

        users.add(newUser)
        currentUser = newUser

        return Pair(true, "Registro exitoso")
    }

    /**
     * Inicia sesión con email y contraseña
     * @return Pair<Boolean, String> - (éxito, mensaje)
     */
    fun loginUser(email: String, password: String): Pair<Boolean, String> {
        val user = users.find {
            it.email.equals(email, ignoreCase = true) && it.password == password
        }

        return if (user != null) {
            currentUser = user
            Pair(true, "Inicio de sesión exitoso")
        } else {
            Pair(false, "Email o contraseña incorrectos")
        }
    }

    /**
     * Cierra la sesión del usuario actual
     */
    fun logoutUser() {
        currentUser = null
    }

    /**
     * Obtiene el usuario actualmente logueado
     */
    fun getCurrentUser(): User? {
        return currentUser
    }

    /**
     * Verifica si hay un usuario logueado
     */
    fun isUserLoggedIn(): Boolean {
        return currentUser != null
    }

    /**
     * Obtiene todos los usuarios registrados (para debugging)
     */
    fun getAllUsers(): List<User> {
        return users.toList()
    }

    /**
     * Limpia todos los usuarios (excepto los de prueba)
     */
    fun clearUsers() {
        users.clear()
        currentUser = null
        // Re-agregar usuarios de prueba
        users.add(
            User(
                id = "1",
                name = "Usuario Demo",
                email = "demo@mail.com",
                password = "Demo123"
            )
        )
    }
}