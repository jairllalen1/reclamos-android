FASE 13
Errores / loading / lista vacía
↓
FASE 14
Rotación / estado
↓
FASE 15
Probar en tu Redmi
↓
FASE 16
Corregir errores
↓
FASE 17
Room opcional
↓
FASE 18
Diseño visual final
↓
FASE 19
Checklist 0–100
↓
FASE 20
Preparación para la exposición




FASE 14
Prueba real contra Spring Boot

Vamos a levantar:

reclamos-api

y conectar:

reclamos-android
↓
IP de tu PC
↓
Spring Boot :8080

Ahí vamos a comprobar GET, POST, PUT y DELETE de verdad.

FASE 15
Prueba en tu Redmi

No emulador.

Tu teléfono real:

Redmi
↓
Wi-Fi / hotspot
↓
PC
↓
Spring Boot
FASE 16
Corrección de errores

Acá ya hacemos:

Logcat
Network Inspector
errores HTTP
errores Retrofit
errores Compose
errores de navegación

El material considera justamente la observación del recorrido de red como parte importante del aprendizaje y debugging.

FASE 17
Room / SQLite

Recién acá evaluamos si realmente nos conviene agregar:

Room
↓
SQLite

como respaldo/local.

No quiero meter Room antes de tener estable:

Android → API → MySQL
FASE 18
Interfaz visual definitiva

Acá mejoramos:

colores
iconos
espaciado
tipografía
cards
botones
dialog
loading
empty state
responsive

Y recién acá tendría sentido pasar el proyecto por la revisión visual que querés hacer con las otras IAs.

FASE 19
Auditoría final

Acá vamos a construir una checklist basada literalmente en lo que pide el PDF:

CRUD
ciclo de vida
segundo plano
errores
diseño
presentación
FASE 20
Preparación de exposición

Vamos a preparar el recorrido:

1. Listar
2. Crear
3. Modificar
4. Eliminar

y además cómo explicar:

Usuario
↓
Compose
↓
ViewModel
↓
Repository
↓
ApiService
↓
Retrofit
↓
Spring Boot
↓
Base de datos

Porque el material dice expresamente que vas a tener que poder explicar el camino completo del dato, capa por capa.















A. CRUD
✅ Listar
✅ Crear
✅ Modificar
✅ Eliminar

B. Arquitectura
✅ Model
✅ API
✅ Repository
✅ ViewModel
✅ UiState
✅ Navigation
✅ Compose

C. Robustez
✅ Loading
✅ Error
✅ Lista vacía
✅ Reintento
✅ Doble envío
✅ Confirmación
✅ Rotación preparada

D. Persistencia local
⬜ Room / SQLite

E. Interfaz definitiva
⬜ Diseño final
⬜ Componentes visuales
⬜ Estados visuales
⬜ Pulido UX

F. Pruebas
⬜ API real
⬜ Dispositivo real
⬜ Logcat
⬜ Network Inspector
⬜ errores HTTP
⬜ errores Retrofit
⬜ errores Compose
⬜ errores Navigation

G. Calidad
⬜ Tests
⬜ Correcciones
⬜ README.md
⬜ Checklist final

H. Presentación
⬜ Recorrido CRUD
⬜ Explicación HTTP
⬜ Explicación arquitectura
⬜ Camino capa por capa
⬜ Ensayo



Entonces el plan definitivo

Queda:

FASE 13
✅ Robustez y requisitos
↓
FASE 14
⬜ Integración real con Spring Boot
↓
FASE 15
⬜ Prueba en dispositivo real
↓
FASE 16
⬜ Debugging / Logcat / Network Inspector

Pero vos querés terminar código antes, entonces hacemos:

FASE 17
⬅ Room / SQLite

y después:

FASE 18
⬜ Interfaz visual definitiva

Después:

FASE 19
⬜ Tests + correcciones
FASE 20
⬜ README.md / documentación
FASE 21
⬜ Auditoría final
FASE 22
⬜ Preparación de exposición





CÓDIGO
↓
terminar funcionalidades
↓
SQLite / Room
↓
interfaz definitiva
↓
PRUEBAS
↓
corrección de errores
↓
tests
↓
README.md
↓
auditoría
↓
exposición

Y no hacemos todavía la prueba real contra Spring Boot ni la prueba completa del teléfono.