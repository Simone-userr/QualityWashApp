package com.example.qualitywash.ui.Data


enum class UserRole {
    USER,       // Usuario normal (por defecto)
    ADMIN,      // Administrador con permisos especiales
    MANAGER     // Encargado de máquinas
}

data class User(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    val role: UserRole = UserRole.USER,
    val photoUri: String? = null, // URI de la foto de perfil (opcional)
    val phoneNumber: String? = null,
    val address: String? = null
)


object UserRepository {
    private val users = mutableListOf<User>()
    private var currentUser: User? = null

    init {
        // Inicializa con usuarios de prueba para cada rol
        users.add(
            User(
                id = "1",
                name = "Usuario Demo",
                email = "demo@mail.com",
                password = "Demo1234@",
                role = UserRole.USER
            )
        )
        users.add(
            User(
                id = "2",
                name = "Admin Quality",
                email = "admin@mail.com",
                password = "Admin123@",
                role = UserRole.ADMIN
            )
        )
        users.add(
            User(
                id = "3",
                name = "Manager Wash",
                email = "manager@mail.com",
                password = "Manager123@",
                role = UserRole.MANAGER
            )
        )
    }


    fun registerUser(name: String, email: String, password: String): Pair<Boolean, String> {
        if (users.any { it.email.equals(email, ignoreCase = true) }) {
            return Pair(false, "Este email ya está registrado")
        }

        val newId = (users.size + 1).toString()
        val newUser = User(
            id = newId,
            name = name,
            email = email,
            password = password,
            role = UserRole.USER // Por defecto, los nuevos usuarios son USER
        )

        users.add(newUser)
        currentUser = newUser

        return Pair(true, "Registro exitoso")
    }

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

    //Cierra la sesión del usuario actual.
    fun logoutUser() {
        currentUser = null
    }

    // Obtiene el usuario actualmente logueado.
    fun getCurrentUser(): User? {
        return currentUser
    }

    // Verifica si hay un usuario actualmente logueado.
    fun isUserLoggedIn(): Boolean {
        return currentUser != null
    }

    // Obtiene una lista inmutable de todos los usuarios registrados.
    fun getAllUsers(): List<User> {
        return users.toList()
    }
    //  Actualiza la URI de la foto de perfil del usuario actual.
    // @return true si la actualización fue exitosa, false si no hay usuario logueado.

    fun updateProfilePhoto(photoUri: String): Boolean {
        val user = currentUser ?: return false

        val updatedUser = user.copy(photoUri = photoUri)

        // Actualiza en la lista y como usuario actual
        val index = users.indexOfFirst { it.id == user.id }
        if (index != -1) {
            users[index] = updatedUser
            currentUser = updatedUser
            return true
        }
        return false
    }

    //Actualiza el nombre, número de teléfono y dirección del usuario actual.
    //@return true si la actualización fue exitosa, false si no hay usuario logueado.

    fun updateUserProfile(name: String, phoneNumber: String?, address: String?): Boolean {
        val user = currentUser ?: return false

        val updatedUser = user.copy(
            name = name,
            phoneNumber = phoneNumber,
            address = address
        )

        val index = users.indexOfFirst { it.id == user.id }
        if (index != -1) {
            users[index] = updatedUser
            currentUser = updatedUser
            return true
        }
        return false
    }


    //Verifica si el usuario actual tiene un rol específico.

    fun hasRole(role: UserRole): Boolean {
        return currentUser?.role == role
    }

    //Verifica si el usuario actual tiene permisos de administrador o manager.

    fun isAdminOrHigher(): Boolean {
        return currentUser?.role == UserRole.ADMIN || currentUser?.role == UserRole.MANAGER
    }

    //*
    // Limpia la lista de usuarios y la vuelve a poblar con los usuarios de prueba iniciales.


    fun clearUsers() {
        users.clear()
        currentUser = null
        // Re-agregar usuarios de prueba (copiados del bloque 'init')
        users.add(
            User(
                id = "1",
                name = "Usuario Demo",
                email = "demo@mail.com",
                password = "Demo1234@",
                role = UserRole.USER
            )
        )
        users.add(
            User(
                id = "2",
                name = "Admin Quality",
                email = "admin@mail.com",
                password = "Admin123@",
                role = UserRole.ADMIN
            )
        )
        users.add(
            User(
                id = "3",
                name = "Manager Wash",
                email = "manager@mail.com",
                password = "Manager123@",
                role = UserRole.MANAGER
            )
        )
    }
}