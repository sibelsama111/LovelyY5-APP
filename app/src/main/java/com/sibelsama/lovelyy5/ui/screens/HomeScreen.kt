package com.sibelsama.lovelyy5.ui.screens

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.sibelsama.lovelyy5.model.Product
import com.sibelsama.lovelyy5.ui.components.AppHeader
import com.sibelsama.lovelyy5.ui.components.ProductCard
import com.sibelsama.lovelyy5.ui.viewmodels.CartViewModel
import com.sibelsama.lovelyy5.ui.viewmodels.ProductViewModel
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri

@Composable
fun HomeScreen(
    onProductClick: (Product) -> Unit,
    onCartClick: () -> Unit,
    onOrdersClick: () -> Unit,
    onCategoryClick: (String) -> Unit,
    onSeeAllProducts: () -> Unit = {},
    onSeeAllCategories: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    cartViewModel: CartViewModel = viewModel()
) {
    val productVm: ProductViewModel = viewModel()
    val productItems by productVm.products.collectAsState()
    val sampleProducts = productItems.map { it.toProduct() }

    val sampleCategories = listOf(
        Category("Celulares", Icons.Default.PhoneAndroid),
        Category("Tablets", Icons.Default.TabletAndroid),
        Category("Computadores", Icons.Default.Computer),
        Category("Audífonos", Icons.Default.Headphones)
    )

    val context = LocalContext.current

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(16.dp)
    ) {
        AppHeader(isHome = true)
        Spacer(modifier = Modifier.height(20.dp))


        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            // Botón de perfil a la izquierda
            IconButton(onClick = onProfileClick) {
                Icon(Icons.Default.Person, contentDescription = "Mi Perfil")
            }

            // Botones de carrito y pedidos a la derecha
            Row {
                IconButton(onClick = onCartClick) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onOrdersClick) {
                    Icon(Icons.Default.Receipt, contentDescription = "Mis Pedidos")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Mis Pedidos")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        SectionHeader(title = "Categorías", onSeeAll = onSeeAllCategories)
        LazyRow(contentPadding = PaddingValues(vertical = 12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(sampleCategories) { category ->
                CategoryItem(category = category, onClick = { onCategoryClick(category.name) })
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        SectionHeader(title = "Productos destacados", onSeeAll = onSeeAllProducts)
        LazyRow(contentPadding = PaddingValues(vertical = 12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(items = sampleProducts.take(6)) { product ->
                val prodItem = productItems.find { it.id == product.id }
                val imgPath = prodItem?.imagenes?.firstOrNull()
                ProductCard(product = product, onProductClick = onProductClick, onAddToCartClick = { cartViewModel.addToCart(product) }, imagePath = imgPath)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Sobre Nosotros <3", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = """
                Lovely Y5 nació para unir experiencia técnica y trato humano. Ofrecemos venta de dispositivos y servicio técnico con énfasis en diagnóstico riguroso, soluciones duraderas y comunicación clara. Nuestro equipo, formado por profesionales y estudiantes avanzados de Ingeniería Informática, aplica conocimientos actuales para resolver problemas reales: no reemplazamos piezas sin investigar la causa, optimizamos rendimiento y priorizamos la confiabilidad. Creemos en la cercanía: una paleta amable, lenguaje sencillo y soporte cercano para que la tecnología sea accesible y comprensible. Lovely Y5 combina la agilidad de una tienda online con la atención personalizada de un local, respaldada por prácticas profesionales y ética. Nuestro compromiso es ofrecer productos verificados, asesoría honesta y un servicio técnico que realmente funcione. Gracias por confiar en nosotros: elegimos mejorar cada día para darte soluciones útiles, confiables y con visión de futuro.
            """.trimIndent(),
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxWidth().height(160.dp)) {
            AsyncImage(model = "file:///android_asset/images/banner_sobre_nos.png", contentDescription = "Banner Sobre Nosotros", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                IconButton(onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, "https://www.instagram.com/lovely.y5.tech".toUri())
                    context.startActivity(intent)
                }) { Icon(Icons.Default.Share, contentDescription = "Instagram") }

                IconButton(onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, "https://lovely-y5-webstore.vercel.app".toUri())
                    context.startActivity(intent)
                }) { Icon(Icons.Default.Public, contentDescription = "Website") }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Lovely Y5 <3 Todos los derechos reservados", style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun SectionHeader(title: String, onSeeAll: () -> Unit = {}) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(text = title, style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold), modifier = Modifier.clickable { onSeeAll() })
        IconButton(onClick = onSeeAll) { Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Ver más") }
    }
}

@Composable
fun CategoryItem(category: Category, onClick: () -> Unit = {}) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onClick() }) {
        Card(shape = CircleShape, modifier = Modifier.size(80.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
            Icon(category.icon, contentDescription = category.name, modifier = Modifier.padding(20.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(category.name, style = MaterialTheme.typography.bodyMedium)
    }
}

data class Category(val name: String, val icon: ImageVector)

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(onProductClick = {}, onCartClick = {}, onOrdersClick = {}, onCategoryClick = {}, onSeeAllProducts = {}, onSeeAllCategories = {})
}
