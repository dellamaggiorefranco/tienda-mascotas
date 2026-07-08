# Plataforma Inteligente para Mascotas

Tienda online de productos para mascotas, con venta por pedido (sin stock propio) y módulos pensados para conocer a cada mascota del cliente, no solo vender productos.

## Documentación

Toda la documentación viva del proyecto está en [`/docs`](./docs):

| Archivo | Contenido |
|---|---|
| [`docs/PROJECT_BIBLE.md`](./docs/PROJECT_BIBLE.md) | Visión, objetivos, propuesta de valor, público, riesgos |
| [`docs/ARCHITECTURE.md`](./docs/ARCHITECTURE.md) | Arquitectura, stack tecnológico, despliegue |
| [`docs/DOMAIN.md`](./docs/DOMAIN.md) | Modelo de datos y entidades |
| [`docs/CODING_STANDARDS.md`](./docs/CODING_STANDARDS.md) | Convenciones de código, estructura del repo, Git Flow |
| [`docs/DECISIONS.md`](./docs/DECISIONS.md) | Registro de decisiones técnicas (ADRs) |
| [`docs/ROADMAP.md`](./docs/ROADMAP.md) | Roadmap por versiones |
| [`docs/SPRINTS.md`](./docs/SPRINTS.md) | Sprint actual y backlog |
| [`docs/CHECKLIST.md`](./docs/CHECKLIST.md) | Definition of Done, checklists, estrategia de testing |

Si estás usando **Claude Code**, el archivo [`CLAUDE.md`](./CLAUDE.md) en la raíz define su rol (mentor, no generador de código) y le indica dónde leer cada cosa.

## Cómo arrancar (una vez creado el código)

```bash
docker compose up -d
```

Levanta backend (Spring Boot), frontend (Next.js) y PostgreSQL en un solo comando.

## Estado actual

Ver [`docs/SPRINTS.md`](./docs/SPRINTS.md) para saber en qué sprint estamos y cuál es la próxima tarea.
