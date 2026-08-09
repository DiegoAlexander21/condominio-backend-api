# Condominio API REST

API REST robusta y escalable desarrollada para la gestión integral y administrativa de condominios residenciales. Permite centralizar la información de residentes, incidencias, paquetería, finanzas, mantenimientos y áreas comunes, optimizando la convivencia y administración del recinto.

## Stack Tecnológico

- **Lenguaje:** Java 21
- **Framework Core:** Spring Boot 3.3.13
- **Base de Datos:** PostgreSQL
- **Autenticación:** Spring Security & JWT (JSON Web Tokens)
- **ORM / Persistencia:** Spring Data JPA / Hibernate
- **Inteligencia Artificial:** Spring AI (Integración con OpenAI / Gemini)
- **Herramientas de Desarrollo:** Lombok, Maven, Thymeleaf, WebSocket

## Características Principales (Key Features)

- **Autenticación Segura:** Sistema de login y registro protegido con tokens JWT y control de acceso basado en roles.
- **Gestión de Unidades y Ocupantes:** Registro completo de unidades (departamentos), torres, inquilinos y propietarios.
- **Control de Incidencias:** Reporte, seguimiento de estados y carga de evidencias para problemas dentro del condominio.
- **Gestión de Visitas y Paquetería:** Registro de ingresos/salidas de visitantes y control de recepción/entrega de paquetes para los residentes.
- **Mantenimiento y Salud Ambiental:** Programación de tareas de mantenimiento, gestión de insumos críticos y evaluaciones (checklists) de áreas comunes.
- **Dashboard y Reportes:** Generación de métricas y estadísticas clave del condominio para la junta administrativa.
- **Asistencia Inteligente:** Integración de funcionalidades impulsadas por IA a través de Spring AI.

## Prerrequisitos y Variables de Entorno

Antes de ejecutar el proyecto, asegúrate de tener instalado:
- **Java 21** o superior.
- **Maven** (o usa el wrapper incluido `mvnw`).
- **PostgreSQL** corriendo en tu máquina local o servidor.

Configura tus variables de entorno. Puedes crear un archivo `.env` o configurarlas en tu IDE/sistema basándote en la siguiente estructura (`.env.example`):

```env
# Configuración de Base de Datos
DB_URL=jdbc:postgresql://localhost:5432/condominio_db
DB_USERNAME=postgres
DB_PASSWORD=tu_password

# Autenticación y Seguridad
JWT_SECRETO=tu_secreto_super_seguro_y_largo_de_al_menos_256_bits

# Correo (SMTP)
MAIL_USERNAME=tu_correo@gmail.com
MAIL_PASSWORD=tu_app_password

# Inteligencia Artificial
AI_API_KEY=tu_api_key_de_gemini_u_openai
```

## Tabla de Endpoints API (Resumen Clave)

A continuación, se listan algunos de los endpoints principales expuestos por el sistema:

| Método | Ruta | Descripción | Autenticación |
|---|---|---|---|
| `POST` | `/api/auth/login` | Autentica a un usuario y retorna un token JWT | No |
| `POST` | `/api/auth/registro` | Registra un nuevo residente o conserje | No |
| `GET`  | `/api/usuarios/me` | Obtiene el perfil completo del usuario autenticado | Sí |
| `GET`  | `/api/unidades` | Lista todas las unidades habitacionales disponibles | Sí |
| `POST` | `/api/incidencias/unidad` | Reporta una nueva incidencia ligada a una unidad | Sí |
| `POST` | `/api/visitas/ingreso` | Registra oficialmente la entrada de una visita | Sí |
| `GET`  | `/api/paqueteria` | Lista los paquetes pendientes o entregados del usuario | Sí |
| `GET`  | `/api/reportes/dashboard` | Obtiene las métricas generales y reportes del condominio | Sí |

## Instrucciones de Instalación y Ejecución Local

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/TuUsuario/condominio-backend-api.git
   cd condominio-backend-api/backend-api
   ```

2. **Configurar la base de datos:**
   Asegúrate de crear una base de datos llamada `condominio_db` en tu instancia local de PostgreSQL. Spring Data JPA se encargará de crear e inicializar las tablas automáticamente al iniciar.

3. **Instalar dependencias:**
   ```bash
   ./mvnw clean install
   ```
   *(Si usas Windows CMD, usa `mvnw.cmd clean install`)*

4. **Configurar variables de entorno:**
   Establece las variables mencionadas en la sección de *Prerrequisitos* en tu entorno de desarrollo local.

5. **Ejecutar el proyecto:**
   ```bash
   ./mvnw spring-boot:run
   ```
   La API REST estará escuchando peticiones en `http://localhost:8080`.

## Arquitectura y Flujo de Datos

El proyecto sigue una arquitectura orientada a dominios (Domain-Driven Design) para mantener un alto nivel de cohesión y bajo acoplamiento. Cada dominio (ej. `seguridad`, `unidades`, `incidencias`, `visitas`) es independiente y maneja su propia estructura de:
- **Controllers:** Punto de entrada para peticiones HTTP.
- **Services:** Lógica de negocio y reglas del sistema.
- **Repositories:** Persistencia e interacción directa con PostgreSQL.
- **Entities y DTOs:** Modelado de los datos y transferencia segura de información entre el cliente y el servidor.

## Contacto y Autor

- **Desarrollador:** Diego Alexander García (Stardust)
- **LinkedIn:** [Diego Alexander García Espinoza](https://www.linkedin.com/in/diego-alexander-garcia-espinoza/)
- **Correo:** [stardust.axl25@gmail.com](mailto:stardust.axl25@gmail.com)
