# Coding Standards — Plataforma Inteligente para Mascotas

## 1. Convenciones de nombres

| Patrón | Uso |
|---|---|
| `XxxRequest` | DTO de entrada para crear/actualizar un recurso |
| `XxxResponse` | DTO de salida devuelto por la API |
| `XxxService` | Lógica de negocio de un módulo |
| `XxxRepository` | Acceso a datos de una entidad |
| `XxxMapper` | Conversión entre Entity y DTO (MapStruct) |
| `XxxException` | Excepción específica de dominio |

Evitar nombres ambiguos como `UserDTO`; preferir `CreateUserRequest`, `UpdateUserRequest`, `UserResponse`.

## 2. Reglas de código

- Sin lógica de negocio en los controllers.
- Sin acceso a Repository desde otro módulo (siempre vía Service).
- Sin retorno de Entities desde la API (siempre DTOs).
- Validación repetida en backend, sin excepción, aunque el frontend ya valide.
- Métodos pequeños y con nombres explícitos, preferidos sobre comentarios extensos.
- Si se copia y pega código, probablemente falte una abstracción.
- Fallar rápido y explícito: validar entradas al principio de cada operación.

## 3. Estructura del repositorio

```
/
├── backend/                # Proyecto Spring Boot
├── frontend/                # Proyecto Next.js
├── docs/                     # Documentación viva (este directorio)
│   ├── PROJECT_BIBLE.md
│   ├── ARCHITECTURE.md
│   ├── DOMAIN.md
│   ├── CODING_STANDARDS.md
│   ├── DECISIONS.md
│   ├── ROADMAP.md
│   ├── SPRINTS.md
│   └── CHECKLIST.md
├── CLAUDE.md                 # Contexto para Claude Code
├── docker-compose.yml
└── README.md
```

## 4. Git Flow

Nunca se trabaja directamente sobre `main`.

```
main         → producción, siempre estable y desplegable
develop      → integración de features, base del próximo release
feature/...  → una funcionalidad puntual, sale de develop
hotfix/...   → corrección urgente, sale de main
release/...  → estabilización previa a un release
```

## 5. Pull Requests

- Toda funcionalidad se integra mediante Pull Request, sin excepción.
- Ningún PR se mergea sin autorevisión completa del diff, y sin pedirle a Claude Code que lo revise como segundo par de ojos antes de aceptar (ver `CLAUDE.md`).
- El PR describe qué cambia, por qué, y cómo se probó.
- PRs pequeños y enfocados, preferibles sobre PRs enormes.

## 6. Convención de commits

Conventional Commits, para historial legible y changelog automático:

```
feat: agregar endpoint de creación de pedidos
fix: corregir cálculo de total del carrito
docs: actualizar handbook con reglas de API
refactor: extraer lógica de precio a servicio dedicado
test: agregar tests unitarios de OrderService
```

## 7. Desarrollo en solitario — cómo compensar no tener un segundo par de ojos

Franco desarrolla todo el sistema (full-stack) en solitario. La principal desventaja de no trabajar en equipo es no tener a alguien que revise el código antes de mergear, así que esto se compensa así:

- Antes de mergear cualquier PR, pedirle explícitamente a Claude Code que lo revise como si fuera un segundo desarrollador (ver rol en `CLAUDE.md`), no solo que lo escriba.
- Dejar pasar al menos unas horas entre terminar una funcionalidad y hacer la autorevisión final — revisar el propio código en caliente hace que se pasen por alto los mismos errores que al escribirlo.
- Documentar en `DECISIONS.md` cualquier decisión de arquitectura no trivial, para no depender de la memoria propia meses después.
