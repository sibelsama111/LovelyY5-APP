App de Ventas "Lovely Y5"

Identificación del Proyecto
Nombre de la App: Lovely Y5 Store

Asignatura: Desarrollo de Aplicaciones Móviles (DSY1105)

Integrantes: Jacqueline Sibel Torti

Contexto: La aplicación Lovely Y5 es una solución de comercio móvil enfocada en la venta de artículos tecnológicos. El proyecto digitaliza el catálogo de la tienda, permitiendo a los usuarios visualizar productos, gestionarlos en un carrito de compras y realizar pedidos, manteniendo la identidad visual de la marca (color #FFACCA y fuente Consolas).​

Definición Técnica y Arquitectura

Requisito: Estructura modular y MVVM.​

El proyecto está construido en Android Studio utilizando Kotlin y Jetpack Compose. Se implementa el patrón de arquitectura MVVM (Model-View-ViewModel) para desacoplar la lógica de negocio de la interfaz gráfica.​

Model (Data): Contiene la lógica de la base de datos (Room) y las data classes, por ejemplo Product, CartItem y User.​
View (UI): Pantallas construidas en Compose, como CatalogScreen y CartScreen, que reaccionan únicamente a los estados expuestos por los ViewModel.​
ViewModel: Gestiona el estado de la UI mediante StateFlow y comunica la vista con el repositorio, por ejemplo ProductViewModel y CartViewModel.​
Repository: Abstrae la fuente de datos y conecta los ViewModel con la base de datos local a través de los DAO.​

Funcionalidades Desarrolladas

A. Interfaz y Navegación

Requisito: Navegación funcional y coherencia visual.​

La aplicación cuenta con un sistema de navegación fluido basado en NavHost que permite moverse entre Login, Catálogo de Productos, Detalle del Producto y Carrito de Compras, respetando la jerarquía visual.​
Se mantiene la coherencia de colores, tipografía y estilo general para reflejar la identidad de Lovely Y5.​

B. Formularios y Validaciones

Requisito: Formularios con validación lógica y visual.​

Se implementa un formulario de Registro de Usuario / Checkout con validación de correo electrónico, contraseña segura y campos obligatorios no vacíos desde la capa de ViewModel.​
Se utiliza feedback visual mediante OutlinedTextField con estados de error (borde rojo, mensaje de advertencia) e íconos informativos cuando los datos ingresados no son válidos.​

C. Animaciones

Requisito: Animaciones funcionales.​

Se incluyen transiciones suaves entre el catálogo y el detalle del producto para mejorar la experiencia de navegación.​
Se muestra un CircularProgressIndicator durante la carga de listas o mientras se procesa una compra.​

Persistencia de Datos (Base de Datos Local)

Requisito: Persistencia local integrada (Room).​

Se utiliza Room Database (SQLite) para asegurar que la selección de productos del usuario no se pierda.​
El carrito de compras se persiste mediante CartEntity, de modo que, al cerrar y reabrir la aplicación, los productos seleccionados permanecen guardados.​

Estructura:

@Entity: Define la tabla de productos en el carrito.​

@Dao: Contiene las operaciones para insertar, eliminar y obtener todos los productos del carrito.​

Uso de Recursos Nativos

Requisito: Al menos dos recursos nativos.​

La aplicación interactúa con el hardware del dispositivo para mejorar la experiencia de uso.​
Se utiliza la cámara (CameraX o Intent) para permitir al usuario tomar una foto y establecer su foto de perfil en la cuenta de la tienda, gestionando los permisos correspondientes en AndroidManifest.xml.​
Se emplea la vibración del dispositivo como feedback táctil al agregar un producto al carrito.​

Novedades del Commit Actual

En el commit actual se incorporan mejoras centradas en extender la funcionalidad manteniendo la arquitectura establecida.​
Se realizan ajustes en la estructura del código para reforzar la separación de responsabilidades y facilitar el mantenimiento de la app.​
Se añaden optimizaciones visuales en pantallas clave para mejorar la legibilidad y la coherencia con la identidad de Lovely Y5.​
Como novedad destacada, se integra una API externa de gatos, utilizada como ejemplo práctico de consumo de servicios REST dentro del mismo ecosistema MVVM + Repository.​

Integración de la API de Gatos

La API de gatos se integra como un módulo demostrativo que muestra imágenes de gatos dentro de la aplicación, ofreciendo un componente lúdico y al mismo tiempo didáctico para el uso de llamadas HTTP.​

7.1 Uso de la API

En la capa de datos se define un servicio de red (por ejemplo, mediante Retrofit) que consume un endpoint público de imágenes de gatos y lo mapea a data classes de Kotlin.​
La información obtenida desde la API se expone a la interfaz a través de un ViewModel dedicado, siguiendo el mismo patrón de estados (carga, éxito, error) ya utilizado en el resto del proyecto.​
En la UI basada en Jetpack Compose se incluye una pantalla o sección específica donde se muestran las imágenes de gatos, actualizadas a partir del estado del ViewModel.​

7.2 Ubicación dentro de la Arquitectura

Capa Data:

Servicio de red (por ejemplo, CatApiService) encargado de las solicitudes HTTP hacia la API de gatos.​

Repositorio (CatRepository o extensión del repositorio existente) que encapsula la lógica de obtención de datos desde la API.​

Capa ViewModel:

ViewModel responsable de solicitar los datos al repositorio, manejar errores y exponer el estado a la interfaz mediante StateFlow o LiveData.​

Capa UI (Compose):

Composable específico (por ejemplo, CatScreen o una sección dentro de otra pantalla) que observa el estado del ViewModel y muestra:

Un indicador de carga mientras se realiza la petición.

La imagen o lista de imágenes de gatos cuando la llamada es exitosa.

Un mensaje de error en caso de que falle la comunicación con la API.​

Con esta integración, el proyecto demuestra de forma práctica cómo combinar persistencia local con Room y consumo de APIs REST externas, manteniendo la arquitectura MVVM y las buenas prácticas de desarrollo en Android.