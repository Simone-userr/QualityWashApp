package com.example.qualitywash.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TiendaViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TiendaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TiendaViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// Si necesitas inyectar dependencias (repositorio, base de datos, etc.),
// puedes agregar parámetros al constructor:
/*
class TiendaViewModelFactory(
    private val repository: TiendaRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TiendaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TiendaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
*/