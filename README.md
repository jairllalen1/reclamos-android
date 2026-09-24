# Reclamos Android

Aplicación Android nativa desarrollada en Kotlin para el **Sistema Nacional de Reclamos**. El proyecto implementa el CRUD de la entidad **EntityType (Tipo de Entidad)** y consume una API REST desarrollada con Spring Boot.

> Proyecto académico — Programación III — Desarrollo de aplicaciones móviles nativas.

---

## 1. Descripción

`reclamos-android` es el cliente móvil Android del Sistema Nacional de Reclamos. Su responsabilidad dentro del proyecto es permitir consultar, crear, modificar y eliminar registros de **EntityType** mediante los endpoints de la API del backend.

La aplicación está diseñada para:

- consumir datos desde la API REST;
- mostrar estados de carga, éxito, vacío y error;
- validar datos antes de enviarlos al servidor;
- evitar operaciones duplicadas por pulsaciones rápidas;
- confirmar las eliminaciones antes de ejecutar el `DELETE`;
- manejar correctamente la pérdida de conexión;
- mantener una separación clara entre interfaz, lógica de presentación, repositorio y red;
- funcionar sobre un dispositivo Android físico durante las pruebas.

---

## 2. Funcionalidades

### CRUD de EntityType

| Operación | HTTP | Endpoint | Resultado esperado |
|---|---|---|---|
| Listar | `GET` | `/api/entity-types` | `200 OK` + lista JSON |
| Obtener uno | `GET` | `/api/entity-types/{id}` | `200 OK` + objeto JSON |
| Crear | `POST` | `/api/entity-types` | `201 Created` + objeto creado |
| Modificar | `PUT` | `/api/entity-types/{id}` | `200 OK` + objeto actualizado |
| Eliminar | `DELETE` | `/api/entity-types/{id}` | `204 No Content` |

### Experiencia de usuario

- Lista de registros con búsqueda.
- Formulario reutilizado para alta y edición.
- Confirmación antes de crear y modificar.
- Confirmación antes de eliminar.
- Indicadores de carga.
- Mensajes claros cuando no existe conexión con el servidor.
- Estado específico para listas vacías.
- Reintento cuando una lectura falla.
- Botones deshabilitados durante operaciones de escritura para evitar duplicados.
- Navegación mediante Navigation Compose.

---

## 3. Arquitectura

El proyecto utiliza una arquitectura inspirada en **MVVM**, manteniendo cada responsabilidad separada.

```text
┌───────────────────────────────┐
│       Jetpack Compose         │
│        UI / Screens           │
└───────────────┬───────────────┘
                │ eventos / estado
                ▼
┌───────────────────────────────┐
│          ViewModel            │
│   lógica de presentación      │
│   StateFlow / UiState         │
└───────────────┬───────────────┘
                │
                ▼
┌───────────────────────────────┐
│          Repository           │
│ acceso y traducción de datos  │
└───────────────┬───────────────┘
                │
                ▼
┌───────────────────────────────┐
│       Retrofit + OkHttp       │
│         comunicación HTTP     │
└───────────────┬───────────────┘
                │
                ▼
┌───────────────────────────────┐
│       Spring Boot API         │
└───────────────┬───────────────┘
                │
                ▼
┌───────────────────────────────┐
│            MySQL              │
└───────────────────────────────┘
```

### Responsabilidades principales

**Compose / UI**

Presenta información, captura eventos del usuario y observa el estado expuesto por el `ViewModel`.

**ViewModel**

Centraliza la lógica de presentación, validaciones y estados de las operaciones. Las llamadas se realizan desde `viewModelScope` y la interfaz observa los cambios mediante `StateFlow`.

**Repository**

Actúa como punto de acceso a los datos remotos y transforma las respuestas de Retrofit en resultados que la capa de presentación puede manejar.

**Retrofit / OkHttp**

Construyen y ejecutan las solicitudes HTTP, serializan/deserializan JSON y gestionan la comunicación con el backend.

---

## 4. Estructura del proyecto

```text
reclamos-android/
└── app/
    └── src/
        └── main/
            └── java/
                └── com/
                    └── jairllalen/
                        └── reclamos_android/
                            ├── MainActivity.kt
                            │
                            ├── data/
                            │   ├── model/
                            │   │   ├── EntityType.kt
                            │   │   ├── EntityTypeCreateRequest.kt
                            │   │   ├── EntityTypeUpdateRequest.kt
                            │   │   └── ApiError.kt
                            │   │
                            │   ├── remote/
                            │   │   ├── ApiService.kt
                            │   │   ├── ApiClient.kt
                            │   │   └── NetworkResult.kt
                            │   │
                            │   └── repository/
                            │       └── EntityTypeRepository.kt
                            │
                            └── ui/
                                ├── navigation/
                                │   ├── Routes.kt
                                │   └── AppNavigation.kt
                                │
                                ├── entitytype/
                                │   ├── EntityTypeUiState.kt
                                │   ├── EntityTypeViewModel.kt
                                │   ├── EntityTypeViewModelFactory.kt
                                │   ├── EntityTypeListScreen.kt
                                │   ├── EntityTypeItem.kt
                                │   └── EntityTypeFormScreen.kt
                                │
                                └── theme/
                                    ├── Color.kt
                                    ├── Theme.kt
                                    └── Type.kt
```

---

## 5. Modelo de datos

### EntityType

La aplicación representa la entidad con los siguientes campos:

```text
idEntityType    : Int
nameTypeEntity  : String
entitySize      : String
sector          : String
idUserCreate    : Int
idUserUpdate    : Int?
dateCreate      : String?
dateUpdate      : String?
```

### Request de creación

```text
nameTypeEntity
entitySize
sector
idUserCreate
```

### Request de actualización

```text
nameTypeEntity
entitySize
sector
idUserUpdate
```

---

## 6. Estados de la aplicación

La UI utiliza un estado explícito para representar lo que está ocurriendo.

```text
                    ┌──────────────┐
                    │   Cargando   │
                    └──────┬───────┘
                           │
                 ┌─────────┴─────────┐
                 ▼                   ▼
          ┌─────────────┐      ┌─────────────┐
          │  Con datos  │      │    Error    │
          └─────────────┘      └─────────────┘
                 │
                 ▼
          ┌─────────────┐
          │ Lista vacía │
          └─────────────┘
```

También se diferencian las operaciones de escritura:

- `isSaving` para crear o modificar.
- `isDeleting` para eliminar.
- mensajes de error específicos para guardar, eliminar y cargar.
- indicador de éxito de la operación.

El resultado de las operaciones de red se modela mediante `NetworkResult`:

```text
Success
HttpError
NetworkError
UnknownError
```

Esto permite distinguir, entre otros casos, una respuesta HTTP de error de una falla donde directamente no existe conexión con el servidor.

---

## 7. Comunicación con la API

### URL base utilizada durante las pruebas

En el dispositivo físico utilizado durante el desarrollo, la API se consumió mediante:

```text
http://192.168.1.51:8080/api/
```

La IP pertenece al equipo donde se ejecuta Spring Boot. **Esta dirección es dependiente de la red local y puede cambiar**. Antes de ejecutar la aplicación en otro equipo o red debe actualizarse `ApiClient.kt` con la dirección correspondiente.

### Emulador vs. dispositivo físico

Para el emulador de Android Studio, la dirección habitual para acceder al host es:

```text
http://10.0.2.2:8080/
```

Para un dispositivo físico conectado a la misma red local que el servidor se utiliza la IP local del equipo que ejecuta Spring Boot.

---

## 8. Flujo de una operación

### Crear

```text
Usuario
  ↓
EntityTypeFormScreen
  ↓
EntityTypeViewModel
  ↓
EntityTypeRepository
  ↓
Retrofit / OkHttp
  ↓
POST /api/entity-types
  ↓
Spring Boot
  ↓
MySQL
  ↓
201 Created
  ↓
ViewModel actualiza estado
  ↓
Volver a la lista
```

### Modificar

```text
Editar registro
  ↓
GET /api/entity-types/{id}
  ↓
cargar valores actuales
  ↓
modificar campos
  ↓
PUT /api/entity-types/{id}
  ↓
200 OK
  ↓
volver a la lista
```

### Eliminar

```text
Eliminar
  ↓
Confirmación
  ↓
DELETE /api/entity-types/{id}
  ↓
204 No Content
  ↓
el registro deja de mostrarse
```

---

## 9. Validaciones

Antes de enviar información al servidor se validan los campos del formulario.

Reglas actualmente implementadas:

- `nameTypeEntity` obligatorio.
- `entitySize` obligatorio.
- `sector` obligatorio.
- Longitud máxima de cada campo: **45 caracteres**.

Ejemplo:

```text
Campo vacío
    ↓
Mostrar error
    ↓
NO enviar request
```

Esto evita hacer una llamada de red cuando el problema ya puede detectarse localmente.

---

## 10. Manejo de errores

La aplicación diferencia varios escenarios.

| Situación | Comportamiento |
|---|---|
| Sin conexión | Mensaje comprensible de conexión con el servidor |
| Spring Boot apagado | Mensaje de conexión / error de red |
| HTTP 400 | Se informa el error de la solicitud |
| HTTP 401 | Se informa la falta o invalidez de autorización cuando corresponde al backend |
| HTTP 403 | Se informa falta de permisos |
| HTTP 404 | Se informa que el recurso/ruta no fue encontrado |
| HTTP 409 | Se informa un conflicto de negocio |
| HTTP 500 | Se informa un error del servidor |
| Lista vacía | Se muestra un estado explícito de lista vacía |
| Validación local | Se muestra el error sin enviar request |

La interfaz no debe interpretar una respuesta HTTP de error como una operación exitosa.

---

## 11. Prevención de operaciones duplicadas

Para evitar registros duplicados por pulsaciones rápidas:

```text
Guardar
  ↓
isSaving = true
  ↓
Botón deshabilitado
  ↓
Request
  ↓
Fin de operación
  ↓
isSaving = false
```

El mismo criterio se aplica a la eliminación con `isDeleting`.

---

## 12. Requisitos del entorno

### Desarrollo

- Android Studio.
- Kotlin.
- Jetpack Compose.
- Material 3.
- Navigation Compose.
- ViewModel / StateFlow.
- Retrofit.
- Gson Converter.
- OkHttp.
- Spring Boot para el backend.
- MySQL para la persistencia del servidor.

### Dispositivo de pruebas

Durante el desarrollo y las pruebas se utilizó un teléfono Android físico **Redmi 15C**.

---

## 13. Configuración del backend

La aplicación depende de que el backend de Spring Boot se encuentre ejecutándose y sea accesible desde el dispositivo.

Condiciones mínimas:

1. Spring Boot iniciado.
2. API disponible en el puerto configurado (`8080` durante el desarrollo).
3. MySQL disponible para el backend.
4. El teléfono y el equipo servidor deben tener conectividad entre sí cuando se utiliza una red local.
5. `ApiClient.kt` debe apuntar a la IP correcta del equipo servidor.

> No incluir credenciales de base de datos, tokens JWT, contraseñas ni otros secretos dentro de este README ni dentro del repositorio.

---

## 14. Ejecución

### Paso 1 — iniciar el backend

Abrir el proyecto Spring Boot y verificar que la API levante correctamente.

### Paso 2 — verificar la base de datos

Comprobar que MySQL esté disponible y que existan registros útiles para la demostración.

### Paso 3 — verificar la URL de la API

Revisar `ApiClient.kt`:

```kotlin
private const val BASE_URL = "http://IP_DEL_SERVIDOR:8080/api/"
```

Ejemplo de desarrollo local utilizado:

```kotlin
private const val BASE_URL = "http://192.168.1.51:8080/api/"
```

### Paso 4 — ejecutar Android

1. Abrir `reclamos-android` en Android Studio.
2. Esperar a que Gradle sincronice correctamente.
3. Seleccionar el dispositivo físico o emulador.
4. Ejecutar la aplicación.

---

## 15. Pruebas funcionales realizadas

La aplicación fue probada contra la API real y se verificaron las operaciones principales del CRUD.

### Listar

```text
GET /api/entity-types
→ 200 OK
```

### Crear

```text
POST /api/entity-types
→ 201 Created
```

### Obtener por ID

```text
GET /api/entity-types/{id}
→ 200 OK
```

### Modificar

```text
PUT /api/entity-types/{id}
→ 200 OK
```

### Eliminar

```text
DELETE /api/entity-types/{id}
→ 204 No Content
```

También se verificaron escenarios de error y robustez:

- pérdida de conexión;
- servidor Spring Boot apagado;
- recurso inexistente (`404`);
- campos vacíos;
- campos que superan los 45 caracteres;
- doble pulsación de guardar;
- lista vacía;
- rotación del dispositivo;
- actualización de la lista al volver del formulario;
- respuesta de carga visible mientras se realizan operaciones remotas.

---

## 16. Evidencia de red y depuración

El desarrollo utiliza **Logcat** como herramienta principal para comprobar el recorrido de las solicitudes y respuestas.

Ejemplo conceptual:

```text
--> GET http://.../api/entity-types
<-- 200

--> POST http://.../api/entity-types
<-- 201

--> PUT http://.../api/entity-types/{id}
<-- 200

--> DELETE http://.../api/entity-types/{id}
<-- 204
```

Al presentar la aplicación es suficiente mostrar los elementos relevantes del request y response:

- método HTTP;
- URL;
- código de estado;
- cuerpo JSON cuando corresponda.

No deben exponerse tokens, contraseñas u otra información sensible en los logs que se compartan.

---

## 17. Ciclo de vida y concurrencia

Las llamadas a la API se ejecutan mediante funciones `suspend` de Retrofit y corrutinas administradas desde el `ViewModel`.

La intención de esta separación es:

```text
UI Thread
  → dibuja y responde a la interacción

Coroutine / red
  → ejecuta la operación remota

Resultado
  → vuelve al estado observado por Compose
```

La aplicación fue probada para comprobar que la interfaz continúe respondiendo durante una operación de red.

La rotación del dispositivo también fue verificada sin romper el flujo de la aplicación.

---

## 18. Decisiones de diseño

### Por qué ViewModel

Permite separar la lógica de presentación de los Composables y mantener el estado de la pantalla fuera de la UI puramente visual.

### Por qué Repository

Evita que las pantallas conozcan los detalles de Retrofit y centraliza el acceso remoto.

### Por qué DTOs de creación y actualización separados

La API no utiliza exactamente los mismos campos para crear y modificar. Por eso se modelan requests diferentes:

```text
EntityTypeCreateRequest
EntityTypeUpdateRequest
```

### Por qué NetworkResult

Permite expresar explícitamente diferentes tipos de resultado:

```text
éxito
error HTTP
error de red
error inesperado
```

---

## 19. Limitaciones y configuración dependiente del entorno

El proyecto actual está preparado para una demostración local contra un backend Spring Boot accesible desde el dispositivo.

La principal configuración dependiente del entorno es la URL base de la API. Una IP local como `192.168.1.51` no debe considerarse permanente.

Para publicar una aplicación real sería necesario, entre otras cosas, utilizar HTTPS y una estrategia de configuración de entorno más apropiada en lugar de depender de una IP local fija.

---

## 20. Alcance actual

El objetivo de esta versión es resolver el CRUD de `EntityType` dentro del cliente Android del proyecto académico.

No se incorpora almacenamiento local mediante Room/SQLite como parte de este alcance. La aplicación trabaja contra la API remota y comunica los errores de conectividad cuando no puede acceder al servidor.

---

## Licencia

Proyecto académico. El uso, distribución o publicación fuera del contexto educativo depende de las condiciones definidas por los autores y la institución.
