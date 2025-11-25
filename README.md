App de Ventas "Lovely Y5"
1. Identificación del Proyecto
Nombre de la App: Lovely Y5 Store

Asignatura: Desarrollo de Aplicaciones Móviles (DSY1105)

Integrantes: Jacqueline Sibel Torti

Contexto: La aplicación "Lovely Y5" es una solución de comercio móvil enfocada en la venta de artículos tecnológicos. El proyecto busca digitalizar el catálogo de la tienda, permitiendo a los usuarios visualizar productos, gestionarlos en un carrito de compras y realizar pedidos, manteniendo la identidad visual de la marca (Color #FFACCA y fuente Consolas).

2. Definición Técnica y Arquitectura

Requisito: Estructura modular y MVVM.

El proyecto está construido en Android Studio utilizando Kotlin y Jetpack Compose. Se implementó el patrón de arquitectura MVVM (Model-View-ViewModel) para desacoplar la lógica de negocio de la interfaz gráfica:

Model (Data): Contiene la lógica de la base de datos (Room) y las data classes (ej. Product, CartItem, User).

View (UI): Pantallas construidas en Compose (ej. CatalogScreen, CartScreen) que solo reaccionan a los estados.

ViewModel: Gestiona el estado de la UI (StateFlow) y comunica la vista con el repositorio (ej. ProductViewModel, CartViewModel).

Repository: Abstrae la fuente de datos, conectando el ViewModel con la base de datos local (DAO).



3. Funcionalidades Desarrolladas
A. Interfaz y Navegación

Requisito: Navegación funcional y coherencia visual. La app cuenta con un sistema de navegación fluido (NavHost) que permite moverse entre el Login, el Catálogo de Productos, el Detalle del Producto y el Carrito de Compras. Se respetó la jerarquía visual y los colores corporativos.

B. Formularios y Validaciones

Requisito: Formularios con validación lógica y visual. Se implementó un formulario de Registro de Usuario / Checkout que incluye:

Validaciones Lógicas: Verificación de correo electrónico válido, contraseña segura y campos no vacíos en el ViewModel.

Feedback Visual: Uso de OutlinedTextField con estados de error (borde rojo y texto de advertencia) e íconos informativos cuando los datos son inválidos.


C. Animaciones

Requisito: Animaciones funcionales. Se integraron animaciones para mejorar la experiencia de compra:

Transiciones suaves entre el catálogo y el detalle del producto.

Animación de carga (CircularProgressIndicator) al procesar una compra o cargar la lista.

4. Persistencia de Datos (Base de Datos Local)

Requisito: Persistencia local integrada (Room).

Para asegurar que el usuario no pierda su selección, se implementó Room Database (SQLite).

Uso en la app: Se utiliza para persistir el Carrito de Compras (CartEntity). Si el usuario cierra la app y vuelve a entrar, sus productos seleccionados siguen ahí.

Estructura:

@Entity: Define la tabla de productos en el carrito.

@Dao: Interfaces para insert, delete y getAll productos del carrito.

5. Uso de Recursos Nativos

Requisito: Al menos dos recursos nativos.

La aplicación interactúa con el hardware del dispositivo para funciones clave:

Cámara (CameraX / Intent): Permite al usuario tomar una foto para establecer su Foto de Perfil en la cuenta de la tienda. Se gestionaron los permisos en el AndroidManifest.xml.

Vibración: El teléfono vibra levemente al agregar un producto al carrito para dar feedback táctil.
