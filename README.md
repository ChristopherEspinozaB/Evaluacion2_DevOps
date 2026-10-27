# Supletanes App — Gestión de Suplementos y Seguimiento Nutricional

Aplicación móvil diseñada para ayudar a los usuarios a gestionar su inventario de suplementos, consultar información nutricional y realizar un seguimiento de su plan de alimentación.

## Autor

- **Christopher Espinoza**

## Repositorio y estructura del proyecto

- Rama utilizada para el pipeline CI/CD: `main`
- Frontend Android: `frontend/`
- Backend Spring Boot: `backend/`
- Workflow CI/CD: `.github/workflows/main.yml`
- Dockerfile: `backend/Dockerfile`
- Configuración Docker Compose: `backend/docker-compose.yml`

## Aplicación y documentación de la API

- **Swagger UI:** [Documentación y pruebas de endpoints](https://supletanesappfinal-production.up.railway.app/swagger-ui/index.html#)
- **Backend público:** [Supletanes App en Railway](https://supletanesappfinal-production.up.railway.app/)

## Arquitectura y tecnologías

### Frontend — Android

- **Lenguaje:** Kotlin.
- **Arquitectura:** MVVM (Model-View-ViewModel).
- **Interfaz:** Jetpack Compose.
- **Concurrencia:** Coroutines y Flow.
- **Comunicación HTTP:** Retrofit.
- **Serialización JSON:** Gson.
- **Pruebas unitarias:** MockK y Coroutine Test.
- **Mapas:** OSMDroid y OpenStreetMap.

Archivos principales:

- `frontend/app/src/main/java/com/example/supletanes/data/api/FoodApi.kt`
- `frontend/app/src/main/java/com/example/supletanes/ui/screens/plan/PlanScreen.kt`
- `frontend/app/src/main/java/com/example/supletanes/ui/screens/plan/viewmodel/PlanViewModel.kt`

### Backend — API REST

- **Lenguaje:** Java 21.
- **Framework:** Spring Boot.
- **Construcción y pruebas:** Maven.
- **Base de datos:** PostgreSQL 16.
- **Documentación de endpoints:** Swagger UI.
- **Verificación de salud:** Spring Boot Actuator, mediante `/actuator/health`.

Código fuente relacionado con alimentos:

`backend/src/main/java/cl/supletanes/supletanes_app/foods`

### API externa

- **Open Food Facts:** consulta de información nutricional y productos mediante su código de barras.

## Endpoints disponibles

### Productos

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/productos` | Obtener todos los productos |
| GET | `/api/productos/{id}` | Obtener un producto por ID |
| GET | `/api/productos/categoria/{categoria}` | Filtrar productos por categoría |
| POST | `/api/productos` | Crear un producto |
| PUT | `/api/productos/{id}` | Actualizar un producto |
| DELETE | `/api/productos/{id}` | Eliminar un producto |

### Recordatorios

| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/api/v1/recordatorios` | Crear un recordatorio |
| GET | `/api/v1/recordatorios/usuario/{idUsuario}` | Consultar recordatorios de un usuario |
| PUT | `/api/v1/recordatorios/{id}` | Actualizar un recordatorio |
| DELETE | `/api/v1/recordatorios/{id}` | Eliminar un recordatorio |

### Alimentos

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/food/{barcode}` | Consultar un alimento mediante su código de barras |
| GET | `/api/food/api/food/search` | Buscar alimentos |

Para revisar los parámetros y formatos de solicitud y respuesta, consulta Swagger UI.

## Pipeline CI/CD con GitHub Actions

El proyecto utiliza GitHub Actions para automatizar la validación, construcción y despliegue del backend. El workflow se encuentra en `.github/workflows/main.yml`.

### Etapas del pipeline

1. **Integración continua:** los cambios subidos a la rama `main` activan el workflow. También se ejecutan validaciones para solicitudes de integración a `main`, según los eventos configurados.
2. **Compilación y pruebas:** Maven ejecuta `./mvnw -B clean verify` para compilar el proyecto y ejecutar las pruebas automatizadas.
3. **Cobertura:** JaCoCo genera el reporte de cobertura de pruebas.
4. **Calidad del código:** SonarCloud analiza el código y permite identificar problemas de calidad.
5. **Seguridad de dependencias:** Dependabot permite detectar dependencias vulnerables y proponer actualizaciones cuando está habilitado en el repositorio.
6. **Construcción de imagen:** Docker Buildx construye la imagen usando `backend/Dockerfile`.
7. **Publicación:** la imagen se publica en Docker Hub con una etiqueta basada en el SHA del commit y con la etiqueta `latest`.
8. **Despliegue:** Docker Compose levanta los servicios definidos para el entorno de despliegue simulado.
9. **Verificación:** se comprueba la disponibilidad de PostgreSQL y que Spring Boot responda correctamente.
10. **Notificación de errores:** cuando se cumplen las condiciones configuradas, el workflow intenta generar una incidencia de GitHub con los resultados de los trabajos.

Los trabajos del pipeline se relacionan mediante dependencias, de modo que las etapas posteriores no continúan si falla una etapa obligatoria de la que dependen.

## Docker y orquestación de contenedores

Docker permite empaquetar el backend y sus dependencias en una imagen reproducible. El archivo `backend/Dockerfile` define la construcción de la imagen Java y la ejecución de Spring Boot.

Docker Compose coordina los servicios del entorno de despliegue:

- **Aplicación Spring Boot:** ejecuta el backend en el puerto interno `8080`.
- **PostgreSQL 16:** proporciona la base de datos para la aplicación.

La configuración de despliegue contempla dos instancias del contenedor de la aplicación. Esto permite ejecutar más de una instancia del backend, mientras Docker Compose coordina los servicios definidos.

Docker Hub almacena las imágenes construidas y permite identificar una versión mediante la etiqueta SHA asociada al commit.

### Herramientas y entornos

| Herramienta | Función |
|---|---|
| GitHub | Repositorio y control de versiones |
| GitHub Actions | Automatización del pipeline CI/CD |
| Maven | Compilación y ejecución de pruebas |
| JaCoCo | Reporte de cobertura |
| SonarCloud | Análisis de calidad del código |
| Dependabot | Alertas y actualizaciones de dependencias, si está habilitado |
| Docker | Construcción y ejecución de contenedores |
| Docker Hub | Registro de imágenes Docker |
| Docker Compose | Coordinación de la aplicación y PostgreSQL |
| PostgreSQL | Persistencia de datos |
| Railway | Alojamiento público del backend |

El pipeline utiliza Docker Compose como entorno de despliegue simulado. La URL de Railway corresponde al backend público indicado en este documento; no implica que el workflow de GitHub Actions despliegue automáticamente en Railway.

## Pruebas y manejo de errores

### Caso correcto

1. Se realiza un commit y se suben los cambios a la rama `main`.
2. GitHub Actions ejecuta la compilación, las pruebas y los análisis configurados.
3. Si las validaciones obligatorias pasan, se construye y publica la imagen Docker.
4. El pipeline levanta los servicios mediante Docker Compose.
5. Se comprueba que Spring Boot responda correctamente y que los servicios necesarios estén disponibles.

**Resultado esperado:** la ejecución termina correctamente y permite verificar la relación entre el commit, la imagen Docker y el despliegue.

### Caso de error controlado

El workflow permite ejecutar una prueba manual mediante `workflow_dispatch`, seleccionando `error` en el parámetro `modo_prueba`.

1. Se abre la pestaña **Actions** del repositorio.
2. Se selecciona el workflow CI/CD.
3. Se presiona **Run workflow**.
4. Se selecciona `error` en `modo_prueba`.
5. Se ejecuta el workflow y se revisa el resultado.

La prueba provoca intencionalmente un error para comprobar que GitHub Actions identifica el fallo, marca la ejecución como fallida y activa el mecanismo de notificación configurado. También genera el artefacto `error-controlado-log`.

Esta prueba manual no cancela por sí sola un commit ni bloquea automáticamente un merge. Para impedir la integración de cambios que no superen las validaciones, se deben configurar las comprobaciones obligatorias y las reglas de protección de la rama en GitHub.

## Trazabilidad

La trazabilidad se mantiene mediante:

- El historial de commits del repositorio.
- Los resultados de cada ejecución de GitHub Actions.
- Las etiquetas SHA de las imágenes publicadas en Docker Hub.
- Los resultados de la verificación posterior al despliegue.

Esto permite relacionar el código enviado con las validaciones ejecutadas, la imagen construida y la versión desplegada.

## Seguridad y dependencias

El pipeline incorpora controles para mejorar la calidad y seguridad del proyecto:

- **SonarCloud:** análisis de calidad del código.
- **Dependabot:** alertas y propuestas de actualización de dependencias cuando está habilitado.
- **GitHub Actions:** ejecución automatizada de validaciones y control del avance entre trabajos.
- **Secretos de GitHub:** almacenamiento de credenciales y tokens utilizados por el workflow, evitando incluirlos directamente en el código.

Las alertas y las comprobaciones deben configurarse de acuerdo con las políticas de seguridad del repositorio. Dependabot no bloquea automáticamente el despliegue por el solo hecho de estar habilitado.

## Cómo utilizar la aplicación

### Aplicación Android

1. Iniciar la aplicación.
2. Presionar el botón **Continuar**.
3. Iniciar sesión o continuar como invitado.
4. Acceder a las diferentes opciones y pantallas disponibles.

### Pruebas de la API

1. Abrir [Swagger UI](https://supletanesappfinal-production.up.railway.app/swagger-ui/index.html#).
2. Seleccionar el endpoint que se desea probar.
3. Presionar **Try it out**.
4. Completar los parámetros requeridos.
5. Presionar **Execute** y revisar la respuesta.

### Ejecución del pipeline

**Caso normal:**

1. Realizar un commit.
2. Subir los cambios a `main`.
3. Abrir la pestaña **Actions**.
4. Revisar las pruebas, los análisis, la construcción de la imagen y el despliegue.

**Caso de error controlado:**

1. Abrir **Actions**.
2. Seleccionar el workflow CI/CD.
3. Presionar **Run workflow**.
4. Seleccionar `error` en `modo_prueba`.
5. Revisar el trabajo fallido y el artefacto de registro.

## Objetivos del pipeline

- Automatizar la compilación y las pruebas del backend.
- Incorporar análisis de calidad y controles de seguridad.
- Contenerizar el microservicio para facilitar su ejecución reproducible.
- Coordinar la aplicación y la base de datos mediante Docker Compose.
- Automatizar la construcción y publicación de imágenes Docker.
- Verificar el funcionamiento de la aplicación después del despliegue.
- Mantener trazabilidad desde el commit hasta la imagen publicada.
- Comprobar el manejo de fallos mediante una prueba de error controlado.