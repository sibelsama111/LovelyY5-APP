package com.sibelsama.lovelyy5.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.TabletAndroid
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sibelsama.lovelyy5.model.Product
import com.sibelsama.lovelyy5.ui.components.AppHeader
import com.sibelsama.lovelyy5.ui.components.ProductCard
import com.sibelsama.lovelyy5.ui.viewmodels.CartViewModel

@Composable
fun HomeScreen(
    onProductClick: (Product) -> Unit, 
    onCartClick: () -> Unit, 
    onOrdersClick: () -> Unit,
    cartViewModel: CartViewModel = viewModel()
) {
    val sampleProducts = listOf(
        Product(1, "iPhone 13 mini", "iPhone 13 mini", 130000.0),
        Product(2, "Honor K50 Gaming", "Honor K50 Gaming", 250000.0),
        Product(3, "iPhone 13", "iPhone 13", 130000.0)
    )

    val sampleCategories = listOf(
        Category("Celulares", Icons.Default.PhoneAndroid),
        Category("Tablets", Icons.Default.TabletAndroid),
        Category("Computadores", Icons.Default.Computer),
        Category("Audífonos", Icons.Default.Headphones)
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            AppHeader(isHome = true)
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(onClick = onOrdersClick) {
                    Icon(Icons.Default.Receipt, contentDescription = "Mis Pedidos", modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Mis Pedidos")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            SectionHeader(title = "Categorías")
            LazyRow(
                contentPadding = PaddingValues(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(sampleCategories) { category ->
                    CategoryItem(category)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            SectionHeader(title = "Productos destacados")
            LazyRow(
                contentPadding = PaddingValues(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(sampleProducts) { product ->
                    ProductCard(product = product, onProductClick = onProductClick, onAddToCartClick = { cartViewModel.addToCart(product) })
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Sobre Nosotros <3",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "La visión de Lovely Y5 es consolidarse como un referente confiable y accesible en el mundo de la tecnología, trascendiendo sus orígenes como un proyecto académico de Ingeniería Informática del DUOC UC. Nuestro propósito es doble: primero, ofrecer un servicio técnico excepcional y una cuidada selección de productos tecnológicos, garantizando calidad, transparencia y confianza a cada cliente. Segundo, materializar la aspiración de un modelo de negocio independiente, moderno y sostenible, que demuestre la viabilidad del trabajo remoto y la autonomía profesional. Lovely Y5 no es solo venta y reparación; es la aplicación práctica del conocimiento, la pasión por la innovación y el compromiso de construir una solución real para las necesidades digitales de hoy.",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        FloatingActionButton(
            onClick = onCartClick,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopEnd),
            shape = CircleShape
        ) {
            Icon(Icons.Default.ShoppingCart, contentDescription = "Ir al carrito")
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "Ver más"
        )
    }
}

@Composable
fun CategoryItem(category: Category) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            shape = CircleShape,
            modifier = Modifier.size(80.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = category.name,
                modifier = Modifier
                    .size(40.dp)
                    .padding(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = category.name, style = MaterialTheme.typography.bodyMedium)
    }
}

data class Category(val name: String, val icon: ImageVector)

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(onProductClick = {}, onCartClick = {}, onOrdersClick = {})
}
