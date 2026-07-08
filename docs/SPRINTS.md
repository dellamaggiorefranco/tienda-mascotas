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

- [ ] Crear repositorio con la estructura definida en `CODING_STANDARDS.md` (`backend/`, `frontend/`, `docs/`).
- [ ] Backend: proyecto Spring Boot inicial (`pom.xml`, `application.properties`, conexión a PostgreSQL vía Docker Compose).
- [ ] Backend: primera migración Flyway — tabla `users` con los campos comunes (`id`, `createdAt`, `updatedAt`, `createdBy`, `updatedBy`, `tenantId`) más `email`, `passwordHash`, `role`.
- [ ] Backend: módulo `auth` — registro (`POST /api/v1/auth/register`) y login (`POST /api/v1/auth/login`) con JWT, siguiendo la estructura de módulo de `ARCHITECTURE.md` (controller/service/repository/entity/dto/mapper/validator/exception).
- [ ] Backend: Spring Security configurado, contraseñas con BCrypt, validación de entrada en ambos endpoints.
- [ ] Backend: Swagger habilitado y documentando los dos endpoints.
- [ ] Backend: tests unitarios de `AuthService` (JUnit + Mockito).
- [ ] Frontend: proyecto Next.js inicial con TypeScript y Tailwind.
- [ ] Frontend: pantallas de registro y login, consumiendo la API vía Axios/React Query.
- [ ] Docker Compose levantando backend + frontend + PostgreSQL con un solo comando.
- [ ] Documentar cualquier decisión técnica tomada durante el sprint en `DECISIONS.md`.
- [ ] Revisión cruzada (PR + code review) antes de mergear a `develop`.

**Definition of Done de este sprint:** ver `CHECKLIST.md` — se aplica tarea por tarea, no solo al final del sprint.

## Backlog general (sin fecha, sin detalle todavía)

Referencia rápida — el detalle vive en `ROADMAP.md`:

- Catálogo de productos + categorías + búsqueda.
- Carrito de compras.
- Flujo de pedidos.
- Panel de administración básico.
- Despliegue del MVP en un hosting real (Railway/Render).

## Sprints cerrados

_(vacío por ahora — se completa a medida que se cierran sprints)_
