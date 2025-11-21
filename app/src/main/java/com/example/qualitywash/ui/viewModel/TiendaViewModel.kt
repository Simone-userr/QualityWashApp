package com.example.qualitywash.ui.viewModel

import androidx.lifecycle.ViewModel
import com.example.qualitywash.ui.Data.ItemCarrito
import com.example.qualitywash.ui.Data.ProductosRepository
import com.example.qualitywash.ui.Data.UserRepository
import com.example.qualitywash.ui.Data.HistoryRepository
import com.example.qualitywash.ui.Data.Product
import com.example.qualitywash.ui.Data.ProductType
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
    private val _totalCarrito = MutableStateFlow(0.0)
    val totalCarrito: StateFlow<Double> = _totalCarrito.asStateFlow()

    // Cantidad total de items en el carrito
    private val _cantidadTotalItems = MutableStateFlow(0)
    val cantidadTotalItems: StateFlow<Int> = _cantidadTotalItems.asStateFlow()

    init {
        cargarProductos()
    }

    private fun cargarProductos() {
        _productos.value = listOf(
            ProductosRepository(
                id = 1,
                nombre = "Suavizante Soft",
                descripcion = "Suavizante Soft de 1 Litro, formulado para ofrecer suavidad a cada lavado",
                precio = 5.000,
                imagen = R.drawable.suavizante
            ),
            ProductosRepository(
                id = 2,
                nombre = "Detergente Ariel",
                descripcion = "Detergente líquido concentrado para lavar ropa blanca y de color.",
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
                carritoActual.map { item ->
                    if (item.producto.id == producto.id) {
                        item.copy(cantidad = item.cantidad + 1)
                    } else item
                }
            } else {
                carritoActual + ItemCarrito(producto = producto, cantidad = 1)
            }
        }
        actualizarTotales()
    }

    fun eliminarDelCarrito(productoId: Int) {
        _carrito.update { it.filter { item -> item.producto.id != productoId } }
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
                } else item
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

    // ✅✅✅ COMPRA REAL + REGISTRO EN HISTORIAL
    fun procesarCompra() {
        val user = UserRepository.getCurrentUser() ?: return

        _carrito.value.forEach { item ->
            HistoryRepository.addPurchase(
                userId = user.id,
                product = item.producto.toProduct(),
                quantity = item.cantidad
            )
        }

        vaciarCarrito()
        cerrarPanelCarrito()
    }

    private fun actualizarTotales() {
        _totalCarrito.value = _carrito.value.sumOf { it.subtotal }
        _cantidadTotalItems.value = _carrito.value.sumOf { it.cantidad }
    }
}

// ✅✅✅ EXTENSIÓN PARA CONVERTIR TU PRODUCTO VIEJO AL MODELO NUEVO
fun ProductosRepository.toProduct(): Product {
    return Product(
        id = this.id.toString(),
        name = this.nombre,
        type = ProductType.PRODUCTO_FISICO,
        price = this.precio,
        stock = 999, // temporal hasta que hagas inventario real
        description = this.descripcion
    )
}