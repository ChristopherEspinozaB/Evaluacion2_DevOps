Supletanes App - Gestión de Suplementos y Seguimiento Nutricional
Aplicación móvil diseñada para ayudar a los usuarios a llevar un seguimiento de su plan de alimentación y gestionar su inventario de suplementos.

Integrantes:
- Francisco Olate
- Nicolas Caro
- Christopher Espinoza

Ramas:

Nicolas Caro: RamaCaro
Christopher Espinoza/msgemmon-tech: RamaCHR
Francisco Olate: RamaFObackend

Ruta para probar ENDPOINTS:

https://supletanesappfinal-production.up.railway.app/swagger-ui/index.html#
https://supletanesappfinal-production.up.railway.app/

Productos:
- GET /api/productos
- GET /api/productos/{id}
- GET /api/productos/categoria/{categoria}
- POST /api/productos
- PUT /api/productos/{id}
- DELETE /api/productos/{id}

Recordatorios:
- POST /api/v1/recordatorios
- GET /api/v1/recordatorios/usuario/{idUsuario}
- PUT /api/v1/recordatorios/{id}
- DELETE /api/v1/recordatorios/{id}

Food:
- GET /api/food/{barcode}
- GET /api/food/api/food/search

Codigo fuente micro servicio:

Frontend:
- frontend/app/src/main/java/com/example/supletanes/data/api/FoodApi.kt
- frontend/app/src/main/java/com/example/supletanes/ui/screens/plan/PlanScreen.kt
- frontend/app/src/main/java/com/example/supletanes/ui/screens/plan/viewmodel/PlanViewModel.kt

Backend:
- backend/src/main/java/cl/supletanes/supletanes_app/foods

Arquitectura y Tecnologías

Frontend (Android):
  - Lenguaje: Kotlin
  - Arquitectura: MVVM (Model-View-ViewModel)
  - Librerías Clave:
    - Coroutines / Flow: Gestión de concurrencia y flujo de datos asíncrono.
    - Jetpack Compose: LiveData/State, ViewModel, Navigation.
    - Retrofit: Comunicación HTTP con el backend.
    - MockK / Coroutine Test: Pruebas unitarias de ViewModels.
    - OSMDroid: Integración de mapas de OpenStreetMap.
    - Gson: Serialización/deserialización de JSON.

Backend (Servicio API REST):
  - Framework: Spring Boot
  - Lenguaje: Java
  - Despliegue Público: Servicio alojado en Railway.
  - Base de Datos: PostgreSQL, también gestionada desde el entorno de Railway.

Api externa:
 - Open food facts

Pasos para ejecutar:
- Iniciar app
- Presionar botón de continuar
- Iniciar sesión o continuar como invitado
- Acceder a las diferentes opciones y pantallas disponibles
