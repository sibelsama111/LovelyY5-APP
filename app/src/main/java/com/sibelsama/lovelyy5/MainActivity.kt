package com.sibelsama.lovelyy5

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.sibelsama.lovelyy5.ui.theme.LovelyY5APPTheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sibelsama.lovelyy5.ui.viewmodels.CartViewModel
import com.sibelsama.lovelyy5.ui.viewmodels.ReviewViewModel
import com.sibelsama.lovelyy5.ui.viewmodels.ProductViewModel
import com.sibelsama.lovelyy5.ui.viewmodels.OrderViewModel
import com.sibelsama.lovelyy5.ui.viewmodels.ViewModelFactory
import com.sibelsama.lovelyy5.ui.screens.NavGraph
import com.sibelsama.lovelyy5.ui.screens.SplashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LovelyY5APPTheme {
                var showSplash by remember { mutableStateOf(true) }

                if (showSplash) {
                    SplashScreen(onFinished = { showSplash = false })
                } else {
                    val context = LocalContext.current
                    val factory = ViewModelFactory(context.applicationContext as Application)

                    val cartViewModel: CartViewModel = viewModel()
                    val reviewViewModel: ReviewViewModel = viewModel(factory = factory)
                    val productViewModel: ProductViewModel = viewModel(factory = factory)
                    val orderViewModel: OrderViewModel = viewModel(factory = factory)

                    NavGraph(cartViewModel, reviewViewModel, productViewModel, orderViewModel)
                }
            }
        }
    }
}



// Mi nombre es SibelSama, Tengo 23 años. Mi casa está en la sección Almendral de Valparaíso, donde está el congreso, y no estoy casada.
// Trabajo como empleado de los grandes deseos de surgir, y llego a casa todos los días a más tardar a las 23 p.m.
// Si fumo y ocasionalmente bebo.
// Estoy en la cama a las 12 de la noche y me aseguro de dormir ocho horas, pase lo que pase.
// Después de tomar un vaso de leche tibia y hacer unos veinte minutos de estiramientos antes de acostarme,
// generalmente no tengo problemas para dormir hasta la mañana.
// Al igual que un bebé, me levanto sin fatiga ni estrés por la mañana.
// Me dijeron que no había problemas en mi último chequeo.
// Estoy tratando de explicar que soy una persona que desea vivir una vida muy tranquila.
// Tengo cuidado de no molestarme con ningún enemigo, como ganar o perder, que me haría perder el sueño por la noche.
// Así es como trato con la sociedad, y sé que eso es lo que me trae felicidad.
// Aunque, si tuviera que pelear, no perdería con nadie.
// Así que ni se les ocurra interferir en mi camino.