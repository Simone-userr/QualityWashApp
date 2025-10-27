package com.example.qualitywash.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qualitywash.ui.Data.User
import com.example.qualitywash.ui.Data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    // Recibe el UserRepository (que es un object) como dependencia para un mejor testing
    private val userRepository: UserRepository
) : ViewModel() {

    fun logout() {
        userRepository.logoutUser()
        _userState.value = User(id="0", name="Invitado", email="No logueado", password="")
    }

    private val _userState = MutableStateFlow(
        userRepository.getCurrentUser() ?: User(id="0", name="Invitado", email="No logueado", password="")
    )
    // Se expone como StateFlow inmutable para que la UI solo pueda leerlo.
    val userState: StateFlow<User> = _userState

    init {
        // Carga los datos al crear el ViewModel
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        // Usa viewModelScope para ejecutar operaciones en segundo plano si fuera necesario.
        viewModelScope.launch {
            val user = userRepository.getCurrentUser()
            // Si hay un usuario logueado (después del login), actualiza el estado.
            if (user != null) {
                _userState.value = user
            }
        }
    }


}