## PLAN COMPLETO DEL PROYECTO

### BLOQUE C — Persistencia local

Acá entra lo que vos querés agregar.

**Fase C1 — Room / SQLite**

Vamos a incorporar:

```text
Room
 ↓
SQLite
```

con:

```text
EntityTypeEntity
EntityTypeDao
AppDatabase
```

---

**Fase C2 — Caché local**

Cuando hay Internet:

```text
API → Room → UI
```

Guardamos localmente los datos descargados.

Cuando no hay Internet:

```text
Room → UI
```

El usuario puede seguir viendo los últimos datos disponibles.

Esto es una **mejora nuestra**; el PDF no exige CRUD offline completo.

---

**Fase C3 — Crear offline**

Si no hay conexión:

```text
Crear
 ↓
SQLite
 ↓
Pendiente de sincronización
```

No intentamos fingir que el servidor confirmó la operación.

---

**Fase C4 — Modificar offline**

```text
Editar
 ↓
SQLite
 ↓
Pendiente de sincronización
```

---

**Fase C5 — Eliminar offline**

```text
Eliminar
 ↓
SQLite
 ↓
Pendiente de sincronización
```

---

**Fase C6 — Cola de sincronización**

Cuando vuelva Internet:

```text
Internet vuelve
      ↓
Pendientes
      ↓
POST / PUT / DELETE
      ↓
Servidor
      ↓
marcar sincronizado
```

Acá tendremos que definir cómo manejar IDs, errores y posibles conflictos. Es la parte más compleja de la extensión offline, por lo que la vamos a hacer después de estabilizar Room.

---

### BLOQUE H — Verificación real

Recién cuando todo el código esté terminado.

**Fase H1 — GET**

```text
Android → Spring Boot → MySQL
```

**Fase H2 — POST**

**Fase H3 — PUT**

**Fase H4 — DELETE**

**Fase H5 — Room**

**Fase H6 — Offline**

**Fase H7 — Sincronización**

---

### BLOQUE I — Pruebas de fallos

Vamos a probar deliberadamente:

```text
sin Internet
servidor apagado
404
400
500
lista vacía
rotación
doble toque
cancelación
timeout
salir de pantalla
```

---

### BLOQUE J — Tests

Después del código funcional:

```text
Unit Tests
ViewModel
Validators
Repository
```

Y los tests que tengan sentido para la lógica offline.

---

### BLOQUE K — Logcat / Network Inspector

Vamos a preparar:

```text
GET
POST
PUT
DELETE
```

para mostrar:

```text
request
headers
body
status code
response
```

El PDF pide explícitamente que puedas mostrar el request en Logcat.

---

### BLOQUE L — README.md

Después de que todo funcione.

El README tendrá:

```text
1. Descripción
2. Tecnologías
3. Arquitectura
4. Estructura del proyecto
5. Configuración
6. Cómo levantar Spring Boot
7. Cómo ejecutar Android
8. BASE_URL
9. Endpoints
10. GET
11. POST
12. PUT
13. DELETE
14. Room/SQLite
15. Offline
16. Sincronización
17. Errores
18. Pruebas
```

---

### BLOQUE M — Auditoría final

Vamos a tomar los **17 puntos oficiales** del PDF y marcar:

```text
✅ Verificado
🟡 Implementado, falta probar
🔴 Falló
⬜ Pendiente
```

Además revisaremos los seis criterios de evaluación: funcionalidad, ciclo de vida, segundo plano, errores, diseño del código y presentación oral.

---

### BLOQUE N — Exposición

Finalmente:

```text
1. Listar
2. Crear
3. Modificar
4. Eliminar
```

y explicar:

```text
Botón
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
Service
 ↓
DAO
 ↓
MySQL
```

También prepararemos las respuestas de las 12 preguntas del PDF.

---

# El orden que vamos a seguir desde ahora

Queda así:

```text
AHORA
│
├─ 1. Doble toque
├─ 2. Confirmar Crear
├─ 3. Confirmar Modificar
├─ 4. Revisar Confirmar Eliminar
├─ 5. Estados y errores
├─ 6. Room / SQLite
├─ 7. Caché local
├─ 8. CRUD offline
├─ 9. Sincronización
├─ 10. Ciclo de vida
├─ 11. Navegación
├─ 12. Robustez
│
└─ TERMINAR TODO EL CÓDIGO
          ↓
    Interfaz definitiva
          ↓
        Pruebas
          ↓
        Tests
          ↓
       README
          ↓
       Auditoría
          ↓
      Exposición
```

Y una decisión importante: **no vamos a saltar todavía a la interfaz bonita ni al README**. Primero vamos a dejar **todo el comportamiento terminado y funcionando**.

En el próximo mensaje arrancamos con **1. Doble toque + confirmaciones de Crear/Modificar/Eliminar**, y ahí sí te voy a dar los archivos completos que haya que reemplazar, listos para copiar y pegar.
