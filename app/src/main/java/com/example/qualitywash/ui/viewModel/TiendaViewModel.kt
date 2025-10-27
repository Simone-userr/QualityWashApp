package com.example.qualitywash.ui.viewModel

import androidx.lifecycle.ViewModel
import com.example.qualitywash.ui.Data.ItemCarrito
import com.example.qualitywash.ui.Data.ProductosRepository
import com.example.qualitywash.ui.Data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.example.qualitywash.R

class TiendaViewModel : ViewModel() {

    // Lista de productos disponibles
    private val _productos = MutableStateFlow<List<ProductosRepository>>(emptyList())
    val productos: StateFlow<List<ProductosRepository>> = _productos.asStateFlow()

    // Carrito de compras
    private val _carrito = MutableStateFlow<List<ItemCarrito>>(emptyList())
    val carrito: StateFlow<List<ItemCarrito>> = _carrito.asStateFlow()

    // Estado del panel del carrito
    private val _mostrarPanelCarrito = MutableStateFlow(false)
    val mostrarPanelCarrito: StateFlow<Boolean> = _mostrarPanelCarrito.asStateFlow()

    // Total del carrito
    val totalCarrito: StateFlow<Double> = MutableStateFlow(0.0).apply {
        // Observar cambios en el carrito y calcular total
    }

    // Cantidad total de items en el carrito
    val cantidadTotalItems: StateFlow<Int> = MutableStateFlow(0).apply {
        // Observar cambios y contar items
    }

    init {
        cargarProductos()
    }

    private fun cargarProductos() {
        // Aquí cargarías los productos desde una fuente de datos
        // Por ahora usaremos datos de ejemplo
        _productos.value = listOf(
            ProductosRepository(
                id = 1,
                nombre = "Suavizante Soft",
                descripcion = "Suavizante Soft de 1 Litro, formulado para ofrecer suavidad a cada lavado",
                precio = 5.000,
                imagen = R.drawable.suavizante// Reemplazar con tu drawable
            ),
            ProductosRepository(
                id = 2,
                nombre = "Detergente Ariel",
                descripcion = "Detergente líquido concentrado para lavar ropa blanca y de color. ",
                precio = 10.000,
                imagen = R.drawable.detergente
            ),
            ProductosRepository(
                id = 3,
                nombre = "Quitamanchas",
                descripcion = "Atomizador quitamanchas profesional para manchas oscuras",
                precio = 15.000,
                imagen = R.drawable.quitamanchas
            ),
            ProductosRepository(
                id = 4,
                nombre = "Blanqueador",
                descripcion = "Notaras que tus ropas se pondran mas blanca",
                precio = 9.990,
                imagen = R.drawable.blanqueador
            ),
            ProductosRepository(
                id = 5,
                nombre = "Desinfectante",
                descripcion = "Aleja las bacterias con nuestro desinfectante",
                precio = 25.000,
                imagen = R.drawable.desinfectante
            )
        )
    }

    fun agregarAlCarrito(producto: ProductosRepository) {
        _carrito.update { carritoActual ->
            val itemExistente = carritoActual.find { it.producto.id == producto.id }

            if (itemExistente != null) {
                // Si el producto ya existe, incrementar cantidad
                carritoActual.map { item ->
                    if (item.producto.id == producto.id) {
                        item.copy(cantidad = item.cantidad + 1)
                    } else {
                        item
                    }
                }
            } else {
                // Si no existe, agregarlo al carrito
                carritoActual + ItemCarrito(producto = producto, cantidad = 1)
            }
        }
        actualizarTotales()
    }

    fun eliminarDelCarrito(productoId: Int) {
        _carrito.update { carritoActual ->
            carritoActual.filter { it.producto.id != productoId }
        }
        actualizarTotales()
    }

    fun actualizarCantidad(productoId: Int, nuevaCantidad: Int) {
        if (nuevaCantidad <= 0) {
            eliminarDelCarrito(productoId)
            return
        }

        _carrito.update { carritoActual ->
            carritoActual.map { item ->
                if (item.producto.id == productoId) {
                    item.copy(cantidad = nuevaCantidad)
                } else {
                    item
                }
            }
        }
        actualizarTotales()
    }

    fun togglePanelCarrito() {
        _mostrarPanelCarrito.value = !_mostrarPanelCarrito.value
    }

    fun cerrarPanelCarrito() {
        _mostrarPanelCarrito.value = false
    }

    fun vaciarCarrito() {
        _carrito.value = emptyList()
        actualizarTotales()
    }

    fun procesarCompra() {
        // Aquí implementarías la lógica de compra
        // Por ejemplo, enviar a un servidor, procesar pago, etc.
        vaciarCarrito()
        cerrarPanelCarrito()
    }

    private fun actualizarTotales() {
        // Esta función se llamará cada vez que el carrito cambie
        val total = _carrito.value.sumOf { it.subtotal }
        val cantidad = _carrito.value.sumOf { it.cantidad }

        (totalCarrito as MutableStateFlow).value = total
        (cantidadTotalItems as MutableStateFlow).value = cantidad
    }
}