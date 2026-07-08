# Architecture — Plataforma Inteligente para Mascotas

> Ver `PROJECT_BIBLE.md` para el porqué. Acá vive el cómo: arquitectura, stack, despliegue.

## 1. Principios de arquitectura

- **Modularidad:** cada módulo tiene una única responsabilidad.
- **Bajo acoplamiento:** los módulos no dependen innecesariamente entre sí.
- **Alta cohesión:** cada clase tiene una única responsabilidad.
- **Clean Code:** el código se entiende sin necesitar explicaciones adicionales.
- **SOLID:** se aplica siempre que agregue valor real, nunca como imposición formal.

## 2. Capas del sistema

Cuatro capas, cada una consciente únicamente de la inmediatamente inferior. Esta regla nunca se rompe.

```
Frontend (Next.js)
       ↓
REST API (Controllers)
       ↓
Business Layer (Services)
       ↓
Persistence Layer (Repositories)
       ↓
PostgreSQL
```

## 3. Organización del backend

Arquitectura modular monolítica (no microservicios — ver ADR-001 en `DECISIONS.md`), organizada por módulos de negocio:

```
backend/
├── config/              # Configuración global (seguridad, CORS, Swagger)
├── common/              # Utilidades, excepciones y respuestas compartidas
└── modules/
    ├── auth/
    ├── users/
    ├── pets/
    ├── products/
    ├── categories/
    ├── cart/
    ├── orders/
    ├── payments/
    ├── shipping/
    ├── notifications/
    ├── reviews/
    └── admin/
```

Cada módulo respeta internamente la misma estructura, sin excepciones:

```
modules/products/
├── controller/
├── service/
├── repository/
├── entity/
├── dto/
├── mapper/
├── validator/
└── exception/
```

## 4. Reglas de diseño no negociables

- No usar nombres ambiguos (`CreateUserRequest`, `UpdateUserRequest`, `UserResponse`, no "UserDTO" genérico).
- No escribir lógica de negocio en los controllers: solo reciben la solicitud y devuelven la respuesta.
- Nunca acceder a un Repository desde otro módulo distinto: siempre se pasa por el Service.
- Nunca devolver Entities directamente desde la API: siempre DTOs.
- Nunca confiar en el frontend: toda validación se repite en el backend.
- Integraciones externas (pagos, WhatsApp) van detrás de una interfaz propia del dominio, nunca acopladas directamente al SDK del proveedor (ver riesgo de dependencia en `PROJECT_BIBLE.md`).

## 5. Stack tecnológico

### Backend
| Tecnología | Uso |
|---|---|
| Java 21 | Lenguaje principal |
| Spring Boot | Framework de aplicación |
| Spring Security + JWT | Autenticación y autorización |
| Spring Data JPA + Hibernate | Persistencia |
| PostgreSQL | Base de datos relacional |
| Flyway | Migraciones versionadas |
| MapStruct | Mapeo Entity ↔ DTO |
| Lombok | Reducción de boilerplate |
| Bean Validation | Validación declarativa |
| Swagger / OpenAPI | Documentación viva de la API |
| JUnit + Mockito | Testing |

### Frontend
| Tecnología | Uso |
|---|---|
| Next.js | SSR/SSG, clave para SEO |
| TypeScript | Tipado estático |
| Tailwind CSS | Estilos utilitarios |
| React Query | Estado de datos remotos y caché |
| Axios | Cliente HTTP |
| React Hook Form + Zod | Formularios y validación |
| Web App Manifest (PWA) | Habilitado como plus técnico de bajo costo, no como estrategia (ver ADR-002) |

### Infraestructura
| Herramienta | Uso |
|---|---|
| Docker + Docker Compose | Entornos reproducibles |
| GitHub + GitHub Actions | Repositorio y CI/CD |
| Railway / Render (inicial) → AWS (escala) | Hosting |

## 6. Justificación de decisiones clave

- **Java + Spring Boot:** ecosistema maduro para arquitecturas mantenibles a largo plazo, y stack que el equipo está aprendiendo en profundidad.
- **PostgreSQL sobre NoSQL:** el dominio (usuarios, pedidos, pagos, stock) es fuertemente relacional y transaccional.
- **Next.js sobre React puro:** el SEO es crítico para una tienda que depende de tráfico orgánico.
- **Monolito modular, no microservicios:** con un solo desarrollador, microservicios agregarían complejidad operativa sin ningún beneficio real — habría que mantener múltiples despliegues, redes y contratos entre servicios sin un equipo para repartir ese trabajo. Los módulos se diseñan igual con bajo acoplamiento, para permitir una futura extracción si el negocio y el equipo crecen.
- **WhatsApp como canal de notificaciones, no push/PWA:** ver ADR-002.

## 7. Preparado para escalar sin sobre-construir ahora

Aunque no se construye multi-tenant en esta etapa (ver ADR-003), el modelo de datos incluye desde el día uno una columna `tenant_id` en las tablas de negocio clave, con valor único fijo por ahora. Esto no agrega complejidad operativa hoy, pero evita una migración de datos dolorosa si en el futuro se decide abrir la plataforma a terceros.

## 8. Estrategia de despliegue

- **Entornos:** local (Docker Compose) → staging (rama `develop`) → producción (rama `main`).
- **CI/CD:** GitHub Actions ejecuta build, tests y linter en cada PR; build de imágenes Docker y despliegue automático a staging al mergear a `develop`; despliegue a producción al mergear a `main` (con aprobación manual mientras el equipo sea chico).
- **Observabilidad:** logs estructurados para login, pedidos, pagos, errores y cambios de precio desde el MVP. Métricas (Spring Actuator) se suman cuando el tráfico lo justifique.
