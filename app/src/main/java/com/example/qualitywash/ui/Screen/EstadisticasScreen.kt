package com.example.qualitywash.ui.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qualitywash.ui.Data.Product
import com.example.qualitywash.ui.Data.ProductsRepository
import androidx.compose.ui.unit.sp
import com.github.ymedialabs.ycharts.BarChart
import com.github.ymedialabs.ycharts.model.BarChartData
import com.github.ymedialabs.ycharts.model.BarData
import com.github.ymedialabs.ycharts.model.ChartData
import com.github.ymedialabs.ycharts.model.ChartType
import com.github.ymedialabs.ycharts.model.LegendData
import com.github.ymedialabs.ycharts.model.LegendLabel
import com.github.ymedialabs.ycharts.model.YAxisData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstadisticasScreen(
    onNavigateBack: () -> Unit
) {
    val totalVentas = ProductsRepository.getTotalSales()
    val totalPedidos = ProductsRepository.getTotalOrders()
    val productosMasVendidos = ProductsRepository.getBestSellingProducts(5)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estadísticas de Ventas", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF00A896),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatCard(
                        title = "Ventas Totales",
                        value = "$${String.format("%.2f", totalVentas)}",
                        icon = Icons.Default.AttachMoney,
                        color = Color(0xFF4CAF50),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Total Pedidos",
                        value = totalPedidos.toString(),
                        icon = Icons.Default.ShoppingCart,
                        color = Color(0xFF2196F3),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                VentasPorProductoBarChart(productosMasVendidos)
            }

            item {
                Text(
                    text = "Productos Más Vendidos (Top 5)",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            items(productosMasVendidos) { (producto, cantidad) ->
                BestSellerItem(producto = producto, cantidadVendida = cantidad)
            }

            item {
                if (productosMasVendidos.isEmpty()) {
                    Text(
                        text = "No hay datos de ventas disponibles.",
                        color = Color.Gray,
                        modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(120.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(32.dp))
            Column {
                Text(title, fontSize = 14.sp, color = Color.Gray)
                Text(value, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = color)
            }
        }
    }
}

@Composable
fun VentasPorProductoBarChart(productosMasVendidos: List<Pair<Product, Int>>) {
    val barData = productosMasVendidos.mapIndexed { index, (producto, cantidad) ->
        BarData(
            point = cantidad.toFloat(),
            label = producto.nombre.split(" ").first(), // Usar solo la primera palabra del nombre como etiqueta
            color = Color(0xFF00A896).copy(alpha = 0.8f)
        )
    }

    val barChartData = BarChartData(
        chartData = barData,
        yAxisData = YAxisData(
            numberOfLabels = 5,
            labelAndAxisLineColor = Color.Gray,
            axisBottomPadding = 10.dp
        ),
        backgroundColor = Color.White,
        chartType = ChartType.Vertical,
        legendData = LegendData(
            legendLabelList = listOf(
                LegendLabel(
                    "Unidades Vendidas",
                    Color(0xFF00A896)
                )
            )
        )
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Gráfico de Ventas por Producto",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            BarChart(
                modifier = Modifier.fillMaxSize(),
                barChartData = barChartData
            )
        }
    }
}

@Composable
fun BestSellerItem(producto: Product, cantidadVendida: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(producto.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Categoría: ${producto.category.name}", fontSize = 12.sp, color = Color.Gray)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = cantidadVendida.toString(),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text("Vendidos", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}
