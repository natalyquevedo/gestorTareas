# Sistema de Gestión de Semilleros de Investigación

Proyecto académico para la gestión integral de semilleros de investigación, tutores, semilleristas y actividades.

## Estructura del Proyecto

- `backend/`: API REST desarrollada con Java 21, Spring Boot y Gradle.
- `frontend/`: Aplicación cliente desarrollada con React, Vite y TypeScript (repositorio complementario).

## Cómo Ejecutar el Backend

```bash
cd backend
./gradlew bootRun
```

Servidor disponible en: `http://localhost:8080`

## Colección Postman

En la carpeta `backend/` se encuentran los archivos para importar en Postman:
- `POSTMAN_COLLECTION.json`: Colección completa de pruebas de endpoints.
- `POSTMAN_ENVIRONMENT.json`: Variables de entorno para pruebas locales.
