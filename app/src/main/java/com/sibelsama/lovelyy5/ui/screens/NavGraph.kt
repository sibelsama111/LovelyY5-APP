package com.sibelsama.lovelyy5.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sibelsama.lovelyy5.model.Order
import com.sibelsama.lovelyy5.model.ShippingDetails
import com.sibelsama.lovelyy5.ui.components.AppHeader
import com.sibelsama.lovelyy5.ui.theme.LovelyY5APPTheme
import com.sibelsama.lovelyy5.ui.viewmodels.CartViewModel
import com.sibelsama.lovelyy5.ui.viewmodels.OrderViewModel

sealed class Screen {
    object Home : Screen()
    object Products : Screen()
    object Cart : Screen()
    object ShippingForm : Screen()
    object Checkout : Screen()
    object OrderList : Screen()
    data class OrderDetail(val orderId: String) : Screen()
}

@Composable
fun NavGraph(
    cartViewModel: CartViewModel = viewModel(),
    orderViewModel: OrderViewModel = viewModel()
) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    val orders by orderViewModel.orders.collectAsState()
    val cartItems by cartViewModel.cartItems.collectAsState()

    when (val screen = currentScreen) {
        is Screen.Home -> HomeScreen(
            onProductClick = { currentScreen = Screen.Products },
            onCartClick = { currentScreen = Screen.Cart },
            onOrdersClick = { currentScreen = Screen.OrderList },
            cartViewModel = cartViewModel
        )
        is Screen.Products -> ProductListScreen(
            onProductClick = { /* TODO: Implement product detail screen navigation */ },
            onCartClick = { currentScreen = Screen.Cart },
            cartViewModel = cartViewModel
        )
        is Screen.Cart -> CartScreen(
            onConfirmProducts = { currentScreen = Screen.ShippingForm },
            onClearCart = { cartViewModel.clearCart() },
            cartViewModel = cartViewModel
        )
        is Screen.ShippingForm -> ShippingFormScreen(
            onSubmit = { shippingDetails ->
                // Guardar datos y pasar a checkout
                currentScreen = Screen.Checkout
            },
            onCancel = { currentScreen = Screen.Cart }
        )
        is Screen.Checkout -> CheckoutFormScreen(
            onSubmit = { shippingDetails ->
                orderViewModel.createOrder(cartItems, shippingDetails)
                cartViewModel.clearCart()
                currentScreen = Screen.OrderList
            }
        )
        is Screen.OrderList -> OrderListScreen(
            orders = orders,
            onOrderClick = { orderId ->
                currentScreen = Screen.OrderDetail(orderId)
            }
        )
        is Screen.OrderDetail -> {
            var order by remember { mutableStateOf<com.sibelsama.lovelyy5.model.Order?>(null) }
            LaunchedEffect(screen.orderId) {
                order = orderViewModel.getOrderById(screen.orderId)
            }
            if (order != null) {
                OrderDetailScreen(order = order!!)
            } else {
                Text("Pedido no encontrado")
            }
        }
    }
}

@Composable
fun OrderListScreen(orders: List<Order>, onOrderClick: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        AppHeader()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Mis Pedidos",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        if (orders.isEmpty()) {
            Text("No tienes pedidos realizados", modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(orders) { order ->
                    OrderListItem(order = order, onOrderClick = onOrderClick)
                }
            }
        }
    }
}

@Composable
fun OrderListItem(order: Order, onOrderClick: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onOrderClick(order.id) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Order ID: ${order.id.take(8)}...", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Text("Total: $${order.totalAmount.toInt()} CLP", style = MaterialTheme.typography.bodyMedium)
            Text("Items: ${order.items.size}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun OrderDetailScreen(order: Order) {
    Column(modifier = Modifier.fillMaxSize()) {
        AppHeader()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Detalle del Pedido",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Order ID: ${order.id}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Text("Total: $${order.totalAmount.toInt()} CLP", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(8.dp))
            Text("Dirección de Envío:", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
            Text("  Nombre: ${order.shippingDetails.name}")
            Text("  Dirección: ${order.shippingDetails.address}")
            Text("  Ciudad: ${order.shippingDetails.city}")
            Text("  Código Postal: ${order.shippingDetails.postalCode}")
            Spacer(modifier = Modifier.height(16.dp))
            Text("Productos:", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
            order.items.forEach { (product, quantity) ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("${product.name} (x$quantity)")
                    Text("$${(product.price * quantity).toInt()} CLP")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NavGraphPreview() {
    LovelyY5APPTheme {
        NavGraph()
    }
}
