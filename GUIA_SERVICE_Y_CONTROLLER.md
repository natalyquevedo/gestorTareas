# Guía de Implementación: Capa Service y Controller (Gestor de Tareas)

Esta guía explica de forma clara, didáctica y estructurada cada archivo que conforma las capas **`service`** y **`controller`** en tu proyecto Spring Boot, integrando las **reglas de negocio del PDF** y las **buenas prácticas de desarrollo REST**.

---

## 1. Conceptos Fundamentales de la Arquitectura

```
Cliente (Postman / Frontend / Navegador)
                │  HTTP Request (JSON)
                ▼
        [ CONTROLLER ]   ➔ Recibe peticiones HTTP, valida sintaxis y responde códigos (200, 201, 404).
                │  Inyecta Service
                ▼
         [ SERVICE ]     ➔ Contiene la lógica del negocio, reglas y validaciones.
                │  Inyecta Repository
                ▼
        [ REPOSITORY ]   ➔ Ejecuta las consultas a la base de datos PostgreSQL.
                │
                ▼
          [ MODEL ]      ➔ Las tablas/entidades de la BD.
```

### Reglas de Negocio del PDF a Aplicar en la Capa Service:
1. **Regla 1**: Un docente puede registrar un máximo de 4 tareas por semana.
2. **Regla 2**: Un docente puede tener asignadas como máximo 4 asignaturas.
3. **Regla 3**: Una asignatura puede tener matriculados como máximo 25 estudiantes.
4. **Regla 4**: Un estudiante puede tener matriculadas como máximo 8 asignaturas.
5. **Regla 5**: Un estudiante no puede superar un máximo de 50 tareas (`MAX_TAREAS = 50`).
6. **Regla 6**: Prioridad ALTA para tareas que vencen en menos de 24 horas.
7. **Regla 7**: Escala de calificación de tareas entre `0.0` y `5.0`.

---

## 2. Mapa de Archivos por Carpeta

```text
src/main/java/com/example/crud/
├── service/
│   ├── TareaAcademicaService.java
│   ├── EstudianteService.java
│   ├── ProfesorService.java
│   ├── AsignaturaService.java
│   └── CoordinadorService.java
│
└── controller/
    ├── TareaAcademicaController.java
    ├── EstudianteController.java
    ├── ProfesorController.java
    ├── AsignaturaController.java
    └── CoordinadorController.java
```

---

## 3. Detalle de Archivos: Capa `service`

Los servicios llevan la anotación `@Service` y manejan transacciones con `@Transactional`. Inyectan sus respectivos repositorios mediante `@RequiredArgsConstructor` (inyección por constructor de Lombok).

---

### 3.1. `TareaAcademicaService.java`
**Propósito:** Gestionar el ciclo de vida de las tareas (creación, entrega, cálculo de urgencia y calificación).

* **Inyecciones necesarias:**
  - `TareaAcademicaRepository tareaRepository`
  - `EstudianteRepository estudianteRepository`
  - `ProfesorRepository profesorRepository`
  - `AsignaturaRepository asignaturaRepository`

* **Métodos principales:**
  1. `listarTodas()`: Retorna `List<TareaAcademica>`.
  2. `obtenerPorId(Long id)`: Busca por ID o lanza excepción si no existe.
  3. `crearTarea(TareaAcademica tarea, Long idAsignatura, Long idProfesor, Long idEstudiante)`:
     - Asocia la materia, el profesor que asigna y el estudiante.
     - Valida la fecha de entrega.
     - Estado inicial por defecto: `"PENDIENTE"`.
  4. `marcarComoEntregada(Long idTarea)`:
     - Cambia el estado de `"PENDIENTE"` a `"ENTREGADA"`.
  5. `calificarTarea(Long idTarea, Double nota, Long idProfesor)`:
     - Valida que la nota esté entre `0.0` y `5.0`.
     - Cambia el estado a `"CALIFICADA"`.
  6. `listarTareasUrgentes()`:
     - Filtra tareas pendientes cuya fecha de entrega sea menor a 24 horas.
  7. `listarPorEstado(String estado)`:
     - Filtra por `"PENDIENTE"`, `"ENTREGADA"` o `"CALIFICADA"`.
  8. `eliminarTarea(Long id)`: Elimina la tarea de la BD.

---

### 3.2. `EstudianteService.java`
**Propósito:** Administrar los alumnos, controlar su límite de tareas y materias.

* **Inyecciones necesarias:**
  - `EstudianteRepository estudianteRepository`
  - `AsignaturaRepository asignaturaRepository`

* **Métodos principales:**
  1. `listarTodos()`: Retorna todos los estudiantes.
  2. `obtenerPorId(Long id)`: Busca un estudiante o lanza excepción.
  3. `guardar(Estudiante estudiante)`: Guarda o actualiza un estudiante.
  4. `inscribirAsignatura(Long idEstudiante, Long idAsignatura)`:
     - Valida que el estudiante no tenga más de 8 asignaturas (Regla 4).
     - Valida que la asignatura no tenga más de 25 alumnos (Regla 3).
  5. `validarLimiteTareas(Long idEstudiante)`:
     - Verifica que no supere las 50 tareas permitidas antes de asignarle una nueva (Regla 5).
  6. `eliminar(Long id)`: Elimina el estudiante si existe.

---

### 3.3. `ProfesorService.java`
**Propósito:** Gestionar los datos de los profesores y sus asignaturas a cargo.

* **Inyecciones necesarias:**
  - `ProfesorRepository profesorRepository`
  - `AsignaturaRepository asignaturaRepository`

* **Métodos principales:**
  1. `listarTodos()`: Retorna la lista de docentes.
  2. `obtenerPorId(Long id)`: Retorna un profesor por ID.
  3. `guardar(Profesor profesor)`: Guarda nuevo profesor (con `rol = "PROFESOR"`).
  4. `asignarAsignatura(Long idProfesor, Long idAsignatura)`:
     - Valida que el profesor no tenga más de 4 asignaturas asignadas (Regla 2).
  5. `eliminar(Long id)`: Elimina el profesor.

---

### 3.4. `AsignaturaService.java`
**Propósito:** Gestionar el catálogo de materias académicas.

* **Inyecciones necesarias:**
  - `AsignaturaRepository asignaturaRepository`

* **Métodos principales:**
  1. `listarTodas()`: Lista de todas las materias.
  2. `obtenerPorId(Long id)`: Busca por ID.
  3. `guardar(Asignatura asignatura)`: Registra nueva asignatura.
  4. `obtenerEstudiantesDeAsignatura(Long idAsignatura)`: Devuelve los alumnos inscritos.
  5. `eliminar(Long id)`: Elimina la materia.

---

### 3.5. `CoordinadorService.java`
**Propósito:** Gestión administrativa general y autenticación.

* **Inyecciones necesarias:**
  - `CoordinadorRepository coordinadorRepository`

* **Métodos principales:**
  1. `listarTodos()`: Lista de coordinadores.
  2. `obtenerPorId(Long id)`: Busca coordinador por ID.
  3. `guardar(Coordinador coordinador)`: Guarda con `rol = "COORDINADOR"`.
  4. `eliminar(Long id)`: Elimina coordinador.

---

## 4. Detalle de Archivos: Capa `controller`

Los controladores exponen las rutas HTTP bajo `@RestController`, definen una ruta base con `@RequestMapping("/api/...")` y utilizan `ResponseEntity` con los códigos de estado HTTP apropiados:
- `200 OK`: Operación de consulta o actualización exitosa.
- `201 CREATED`: Creación de un nuevo recurso.
- `204 NO CONTENT`: Eliminación exitosa sin contenido de retorno.
- `400 BAD REQUEST`: Datos inválidos o regla de negocio no cumplida.
- `404 NOT FOUND`: Recurso no encontrado por ID.

---

### 4.1. `TareaAcademicaController.java`
**Ruta Base:** `/api/tareas`

| Método HTTP | Ruta | Acción | Código Éxito |
|---|---|---|---|
| `GET` | `/api/tareas` | Listar todas las tareas | `200 OK` |
| `GET` | `/api/tareas/{id}` | Obtener una tarea por su ID | `200 OK` |
| `GET` | `/api/tareas/urgentes` | Listar tareas con entrega < 24h | `200 OK` |
| `GET` | `/api/tareas/estado/{estado}` | Filtrar por estado (`PENDIENTE`, `ENTREGADA`, etc.) | `200 OK` |
| `POST` | `/api/tareas?asignaturaId=1&profesorId=2&estudianteId=3` | Crear nueva tarea vinculada | `201 CREATED` |
| `PUT` | `/api/tareas/{id}/entregar` | Marcar tarea como entregada por el alumno | `200 OK` |
| `PUT` | `/api/tareas/{id}/calificar?nota=4.5&profesorId=2` | Calificar la tarea | `200 OK` |
| `DELETE` | `/api/tareas/{id}` | Eliminar la tarea | `204 NO CONTENT` |

---

### 4.2. `EstudianteController.java`
**Ruta Base:** `/api/estudiantes`

| Método HTTP | Ruta | Acción | Código Éxito |
|---|---|---|---|
| `GET` | `/api/estudiantes` | Listar todos los estudiantes | `200 OK` |
| `GET` | `/api/estudiantes/{id}` | Obtener estudiante por ID | `200 OK` |
| `POST` | `/api/estudiantes` | Registrar nuevo estudiante | `201 CREATED` |
| `PUT` | `/api/estudiantes/{id}` | Actualizar datos del estudiante | `200 OK` |
| `POST` | `/api/estudiantes/{id}/inscribir/{idAsignatura}` | Inscribir materia al estudiante | `200 OK` |
| `DELETE` | `/api/estudiantes/{id}` | Eliminar estudiante | `204 NO CONTENT` |

---

### 4.3. `ProfesorController.java`
**Ruta Base:** `/api/profesores`

| Método HTTP | Ruta | Acción | Código Éxito |
|---|---|---|---|
| `GET` | `/api/profesores` | Listar todos los profesores | `200 OK` |
| `GET` | `/api/profesores/{id}` | Obtener profesor por ID | `200 OK` |
| `POST` | `/api/profesores` | Registrar nuevo profesor | `201 CREATED` |
| `PUT` | `/api/profesores/{id}` | Actualizar datos del profesor | `200 OK` |
| `POST` | `/api/profesores/{id}/asignar-materia/{idAsignatura}` | Asignar materia al docente | `200 OK` |
| `DELETE` | `/api/profesores/{id}` | Eliminar profesor | `204 NO CONTENT` |

---

### 4.4. `AsignaturaController.java`
**Ruta Base:** `/api/asignaturas`

| Método HTTP | Ruta | Acción | Código Éxito |
|---|---|---|---|
| `GET` | `/api/asignaturas` | Listar todas las asignaturas | `200 OK` |
| `GET` | `/api/asignaturas/{id}` | Obtener materia por ID | `200 OK` |
| `POST` | `/api/asignaturas` | Registrar nueva materia | `201 CREATED` |
| `PUT` | `/api/asignaturas/{id}` | Actualizar materia | `200 OK` |
| `DELETE` | `/api/asignaturas/{id}` | Eliminar materia | `204 NO CONTENT` |

---

### 4.5. `CoordinadorController.java`
**Ruta Base:** `/api/coordinadores`

| Método HTTP | Ruta | Acción | Código Éxito |
|---|---|---|---|
| `GET` | `/api/coordinadores` | Listar coordinadores | `200 OK` |
| `GET` | `/api/coordinadores/{id}` | Obtener coordinador por ID | `200 OK` |
| `POST` | `/api/coordinadores` | Registrar coordinador | `201 CREATED` |
| `PUT` | `/api/coordinadores/{id}` | Actualizar coordinador | `200 OK` |
| `DELETE` | `/api/coordinadores/{id}` | Eliminar coordinador | `204 NO CONTENT` |

---

## 5. Buenas Prácticas al Escribir el Código

1. **Inyección de Dependencias Limpia:**
   - Usar siempre `@RequiredArgsConstructor` de Lombok sobre la clase y declarar los atributos como `private final Repositorio repo;`. Esto evita usar el viejo `@Autowired` en cada campo.
2. **Uso de `ResponseEntity`:**
   - Permite devolver tanto el objeto de respuesta como el código HTTP exacto (`HttpStatus.CREATED`, `HttpStatus.OK`, `HttpStatus.NOT_FOUND`).
3. **Validación de Entradas:**
   - Usar la anotación `@Valid` en los parámetros de los métodos POST/PUT con `@RequestBody` para que Spring active las validaciones que configuramos en los modelos (`@NotBlank`, `@Size`, `@Min`, `@Max`).
4. **Manejo de Errores con `RuntimeException`:**
   - En el `service`, si algo no existe o viola una regla (por ejemplo: "El estudiante ya tiene 50 tareas"), se lanza un `throw new RuntimeException("Mensaje claro del error")`. Más adelante, la carpeta `exception` se encargará de atraparlo y mostrarlo bonito.

---

## 6. Siguiente Paso Sugerido
¿Con cuál entidad te gustaría comenzar a programar su `Service` y `Controller`?
- La más importante y central del proyecto es **`TareaAcademica`**, seguida de **`Estudiante`** y **`Profesor`**.
