# Sprints — Plataforma Inteligente para Mascotas

> Nota deliberada: solo se detalla en profundidad el sprint actual. El resto queda como backlog general en `ROADMAP.md`. Planificar sprints futuros en detalle antes de terminar el actual suele ser esfuerzo que se descarta cuando cambian las prioridades reales — ver principio de simplicidad en `PROJECT_BIBLE.md`.

## Cómo usar este archivo

- Al cerrar una tarea, marcarla `[x]` acá mismo.
- Al terminar un sprint, mover el resumen a "Sprints cerrados" (abajo) y definir el siguiente sprint recién en ese momento, no antes.
- Si surge una decisión técnica nueva durante el sprint, agregarla a `DECISIONS.md`, no dejarla solo en el chat con Claude Code.

## Sprint 1 — Auth + esqueleto del proyecto

**Objetivo del sprint:** tener el proyecto corriendo localmente con Docker, con registro/login funcionando de punta a punta (frontend ↔ backend ↔ base de datos), siguiendo la arquitectura definida en `ARCHITECTURE.md`.

**Duración estimada:** 2-4 semanas (part-time, un solo desarrollador — ver ADR-006 en `DECISIONS.md`).

**Tareas:**

- [x] Crear repositorio con la estructura definida en `CODING_STANDARDS.md` (`backend/`, `frontend/`, `docs/`).
- [x] Backend: proyecto Spring Boot inicial (`pom.xml`, `application.properties`, conexión a PostgreSQL vía Docker Compose).
- [x] Backend: primera migración Flyway — tabla `users` con los campos comunes (`id`, `createdAt`, `updatedAt`, `createdBy`, `updatedBy`, `tenantId`) más `email`, `passwordHash`, `role`.
- [x] Backend: módulo `auth` — registro (`POST /api/v1/auth/register`) y login (`POST /api/v1/auth/login`) con JWT, siguiendo la estructura de módulo de `ARCHITECTURE.md` (controller/service/repository/entity/dto/mapper/validator/exception).
- [x] Backend: Spring Security configurado, contraseñas con BCrypt, validación de entrada en ambos endpoints.
- [x] Backend: Swagger habilitado y documentando los dos endpoints.
- [x] Backend: tests unitarios de `AuthService` (JUnit + Mockito).
- [ ] Frontend: proyecto Next.js inicial con TypeScript y Tailwind.
- [ ] Frontend: pantallas de registro y login, consumiendo la API vía Axios/React Query.
- [ ] Docker Compose levantando backend + frontend + PostgreSQL con un solo comando.
- [ ] Documentar cualquier decisión técnica tomada durante el sprint en `DECISIONS.md`.
- [ ] Revisión cruzada (PR + code review) antes de mergear a `develop`.

**Definition of Done de este sprint:** ver `CHECKLIST.md` — se aplica tarea por tarea, no solo al final del sprint.

## Backlog general (sin fecha, sin detalle todavía)

Referencia rápida — el detalle vive en `ROADMAP.md`:

- `POST /api/v1/auth/refresh` — tarea chica, inmediata siguiente a "módulo auth (registro + login)" de Sprint 1. Reutiliza `JwtService`; ver ADR-010 (refresh token JWT stateless, sin tabla ni revocación).
- Rate limiting en `/login` y `/checkout` — exigido como regla obligatoria en `DOMAIN.md`, diferido explícitamente del alcance de la tarea de `auth` de Sprint 1 por tamaño (requiere evaluar una librería nueva, ej. Bucket4j, no agregada todavía al proyecto).
- `AuthIntegrationTest` (Testcontainers) no corre en la máquina de desarrollo de Franco: el Docker Desktop instalado (motor con API 1.55, arquitectura Moby v2) todavía no es compatible con la librería Testcontainers Java (probado 1.19.8 y 1.20.4), incluso exponiendo el daemon por TCP. El test está escrito y compila; falta poder ejecutarlo (en esta máquina cuando Testcontainers lo soporte, o en CI con una versión de Docker más estándar).
- Pendientes menores detectados en la revisión cruzada del módulo `auth` (no bloquean el merge):
  - `JwtAuthenticationFilter` queda registrado dos veces (dentro de la cadena de Spring Security y como filtro de servlet genérico de Spring Boot, por ser `@Component`). Hoy es inofensivo porque `OncePerRequestFilter` evita ejecutar la lógica dos veces, pero conviene declararlo explícitamente sin el auto-registro genérico (ej. `FilterRegistrationBean` con `setEnabled(false)`).
  - No se confirmó si todas las rutas de assets de Swagger (ej. `/webjars/**`) están exceptuadas de pedir login en `SecurityConfig` — falta probarlo a mano contra la app corriendo.
  - `JwtService.parseToken()` reconstruye el `JwtParser` en cada llamada en vez de cachearlo como ya se hace con la `SecretKey` — mejora de prolijidad, no de corrección.
  - El login iguala el mensaje de error para "email no existe" y "password incorrecta", pero no el tiempo de respuesta (no corre BCrypt si el email no existe) — un atacante podría inferir igual qué emails están registrados midiendo latencia. Evaluar correr `passwordEncoder.matches` contra un hash dummy también cuando el email no existe.
- Catálogo de productos + categorías + búsqueda.
- Carrito de compras.
- Flujo de pedidos.
- Panel de administración básico.
- Despliegue del MVP en un hosting real (Railway/Render).

## Sprints cerrados

_(vacío por ahora — se completa a medida que se cierran sprints)_
