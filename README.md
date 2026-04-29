📱 Laboratorio 7:


🚀 Mejoras Implementadas

🔹 Gestión de Usuarios (Ejercicio 1)
* **Persistencia Completa:** Operaciones de inserción, listado y eliminación del último registro usando Room.
* **Interfaz Unificada:** Diseño basado en una paleta de colores fríos (`DeepBlue`, `IceWhite`).
* **Vistas Dinámicas:** Alternancia entre el formulario de registro y el listado de usuarios con tarjetas (`Cards`) sombreadas.

🔹 Editor de Actividades (Ejercicio 2)
* **Configuración de Pictogramas:** Selección de imágenes desde una biblioteca integrada.
* **Control de Tiempos:** Selector de minutos con botones incrementales.
* **CRUD de Tareas:** Soporte para crear, editar y eliminar actividades de forma intuitiva.

🔹 Navegación y UX
* **MainActivity Centralizada:** Implementación de un `Scaffold` con `BottomNavigationBar` para navegar entre los dos ejercicios.
* **TopAppBar Funcional:** Los botones de acción (Agregar, Listar, Eliminar y Navegación rápida) están integrados en la barra superior para mayor limpieza visual.


📂 Estructura del Proyecto
* `ScreenUser.kt`: Gestión de perfiles de usuario.
* `ScreenTask.kt`: Editor de pictogramas y actividades.
* `UserDatabase.kt`: Configuración central de la base de datos (Room).
