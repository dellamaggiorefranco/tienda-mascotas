# Decisions (ADR Log) — Plataforma Inteligente para Mascotas

> Registro vivo de decisiones de arquitectura y producto. Cada decisión relevante se agrega acá con contexto, alternativas consideradas y consecuencias. Nunca se borran entradas viejas, aunque queden reemplazadas — se marca su estado.

**Plantilla para nuevas entradas:**

```
## ADR-XXX: <Título>
Estado: Propuesta | Aceptada | Reemplazada por ADR-YYY
Fecha: AAAA-MM-DD
Contexto: ¿Qué problema motiva la decisión?
Decisión: ¿Qué se decidió hacer?
Alternativas consideradas: ¿Qué otras opciones se evaluaron y por qué se descartaron?
Consecuencias: ¿Qué se gana y qué se sacrifica?
```

---

## ADR-001: Arquitectura modular monolítica en lugar de microservicios
Estado: Aceptada
Fecha: 2026-07-03
Contexto: El equipo es de un solo desarrollador (ver ADR-006) en etapa de aprendizaje y validación de negocio.
Decisión: Se construye un monolito modular con límites claros entre módulos, en lugar de microservicios desde el inicio.
Alternativas consideradas: Microservicios desde el día uno, descartado por la complejidad operativa que no se justifica con el tamaño actual del equipo ni del negocio.
Consecuencias: Menor complejidad operativa inicial. Los módulos están diseñados con bajo acoplamiento, lo que facilita extraerlos como servicios independientes en el futuro si el negocio lo justifica.

---

## ADR-002: Notificaciones vía WhatsApp/email en lugar de push nativo o PWA
Estado: Aceptada
Fecha: 2026-07-08
Contexto: Se evaluó usar una PWA con notificaciones push para recordatorios de recompra. En iOS la instalación es manual (Safari → Compartir → Agregar a inicio) y sin aviso automático, por lo que la adopción real es muy baja.
Decisión: El canal principal de notificaciones y recordatorios es WhatsApp, con email como respaldo. El manifest de PWA queda habilitado en el código como beneficio de bajo costo, sin que ninguna funcionalidad dependa de que el usuario la instale.
Alternativas consideradas: Notificaciones push vía PWA (descartada por baja adopción en iOS); app nativa solo para push (descartada por desproporcionada en esta etapa).
Consecuencias: Mayor alcance real desde el día uno, sin pedirle ninguna acción extra al usuario. Requiere integrar un proveedor de WhatsApp Business API (directo o vía intermediario como Twilio), a definir en un ADR posterior.

---

## ADR-003: Descartar plataforma SaaS multi-tenant internacional como primera versión
Estado: Aceptada
Fecha: 2026-07-08
Contexto: Se evaluó construir, en lugar de una tienda propia, una plataforma tipo plantilla donde cualquier tienda de mascotas pudiera crear la suya (multi-tenant, white-label, internacional). Se confirmó que soluciones similares ya existen en el mercado (ej. Spree Commerce).
Decisión: Se construye primero una tienda single-tenant real (el negocio del hermano de Franco), no una plataforma para terceros.
Alternativas consideradas: Plataforma SaaS multi-tenant desde el inicio, descartada porque (a) compite directo con soluciones ya maduras, (b) multiplica la complejidad (aislamiento de datos, panel de super-admin, facturación a terceros, onboarding self-service, soporte a N negocios), y (c) el negocio de base todavía no está validado con una sola venta real.
Consecuencias: Se reduce drásticamente el alcance del MVP. El modelo de datos se diseña con una columna `tenant_id` desde el día uno (ver `ARCHITECTURE.md`, sección 7) para no cerrar la puerta a una futura evolución a multi-tenant, sin construir ninguna funcionalidad de eso ahora.

---

## ADR-006: Cambio de equipo — desarrollo en solitario
Estado: Aceptada
Fecha: 2026-07-08
Contexto: El plan original consideraba a Franco y Gastón desarrollando el proyecto juntos, full-stack, ambos. Se definió que Gastón finalmente no participa del desarrollo.
Decisión: Franco desarrolla todo el proyecto en solitario. El referente de negocio (hermano) no cambia — sigue a cargo de la operación comercial.
Alternativas consideradas: Buscar reemplazo para el segundo puesto de desarrollo, descartado por ahora para no bloquear el arranque del proyecto; se reevalúa más adelante si hace falta.
Consecuencias: Se pierde el segundo par de ojos para revisión de código humana — se compensa pidiéndole a Claude Code que cumpla ese rol antes de cada merge (ver `CODING_STANDARDS.md`, sección 7). El ritmo de desarrollo es más lento por sprint (ver estimaciones ajustadas en `SPRINTS.md`). No cambia ninguna decisión de arquitectura previa (ADR-001 a 005) — de hecho refuerza la elección de monolito modular sobre microservicios.

---

## ADR-007 (pendiente): Proveedor de pagos
Estado: Propuesta
Contexto: Se necesita procesar pagos online desde la V1 del roadmap.
Decisión: A definir — candidato principal Mercado Pago por ser el más usado en Argentina.
Alternativas consideradas: Pendiente de comparar con Stripe/PayPal para la fase internacional futura.
Consecuencias: Pendiente.

---

## ADR-008 (pendiente): Proveedor de WhatsApp Business API
Estado: Propuesta
Contexto: Se necesita enviar recordatorios de recompra por WhatsApp (ver ADR-002).
Decisión: A definir — evaluar WhatsApp Business API directa vs. intermediario (ej. Twilio).
Alternativas consideradas: Pendiente.
Consecuencias: Pendiente.

---

## ADR-009: Límite entre los módulos `users` y `auth`
Estado: Aceptada
Fecha: 2026-07-08
Contexto: `ARCHITECTURE.md` y `DOMAIN.md` no aclaraban en qué módulo vive la entidad `User`, y el repo tiene carpetas paralelas `modules/auth/` y `modules/users/` con la misma subestructura (controller/service/repository/entity/dto/mapper/validator/exception). Sin definir esto antes de escribir código, era fácil terminar con la entidad `User` duplicada o con `auth` accediendo directo a un repository ajeno, violando la regla de `ARCHITECTURE.md` de nunca cruzar repositories entre módulos.
Decisión: El módulo `users` es dueño de la entidad `User`, su `UserRepository` y su `UserService` (crear, buscar por email, etc.). El módulo `auth` no tiene entidad propia: su `AuthService` orquesta registro y login llamando al `UserService` de `users` (nunca a su repository), y se encarga del hash de contraseñas y de la emisión/validación de JWT.
Alternativas consideradas: Poner la entidad `User` dentro de `auth`, descartada porque otros módulos futuros (`pets`, `orders`, `reviews`) necesitan referenciar al usuario dueño de un recurso, y esa relación pertenece naturalmente a `users`, no a `auth`.
Consecuencias: `auth` queda como módulo delgado, enfocado solo en autenticación (credenciales, tokens). Cualquier lógica de cuenta de usuario que no sea puramente de login/registro (por ejemplo, cambiar el propio email) va en `users`, no en `auth`.
