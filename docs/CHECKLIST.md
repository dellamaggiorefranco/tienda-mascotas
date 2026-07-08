# Checklist de Calidad — Plataforma Inteligente para Mascotas

## 1. Definition of Done

Una tarea se considera terminada únicamente cuando:

1. Funciona correctamente en el escenario esperado.
2. Está cubierta por tests relevantes.
3. Está documentada (código y, si aplica, Swagger).
4. Pasó code review y fue aprobada.
5. Está mergeada a la rama correspondiente.
6. No rompe funcionalidades existentes (regresión).

## 2. Checklist antes de hacer merge

- [ ] Compila sin errores.
- [ ] Los tests existentes y los nuevos pasan.
- [ ] Swagger / documentación de la API está actualizada.
- [ ] No hay warnings importantes sin justificar.
- [ ] El código fue autorevisado (dejando pasar algo de tiempo) y revisado por Claude Code como segundo par de ojos.

## 3. Checklist para una nueva funcionalidad (antes de empezar a codear)

- [ ] ¿Responde a una necesidad real de negocio o de usuario? (ver `PROJECT_BIBLE.md`)
- [ ] ¿Está definido el modelo de datos necesario y su migración Flyway?
- [ ] ¿Los DTOs de entrada y salida están claramente nombrados? (ver `CODING_STANDARDS.md`)
- [ ] ¿La lógica de negocio vive en el Service y no en el Controller?
- [ ] ¿Está validada la entrada tanto en frontend como en backend?
- [ ] ¿Tiene tests unitarios y, si corresponde, de integración?
- [ ] ¿Está documentada en Swagger?
- [ ] ¿Introduce un patrón nuevo que debería reflejarse en `ARCHITECTURE.md` o `CODING_STANDARDS.md`?
- [ ] ¿Es una decisión de arquitectura no trivial? Si sí, agregar ADR en `DECISIONS.md`.

## 4. Estrategia de testing

| Tipo | Herramienta | Qué cubre |
|---|---|---|
| Unitario | JUnit + Mockito | Lógica de servicios de forma aislada |
| Integración | Spring Boot Test + Testcontainers | Repositorios y flujo contra PostgreSQL real |
| API / Contrato | MockMvc / RestAssured | Comportamiento de los endpoints |
| Frontend | Jest + Testing Library | Componentes y hooks críticos |
| End-to-end (más adelante) | Playwright | Flujos completos de compra |

## 5. Estrategia de despliegue (resumen — detalle en `ARCHITECTURE.md`)

- Local: Docker Compose, un comando levanta todo.
- Staging: despliegue automático al mergear a `develop`.
- Producción: despliegue al mergear a `main`, con aprobación manual mientras el equipo sea chico.
- Todo evento relevante (login, pedidos, pagos, errores, cambios de precio) queda registrado en logs desde el MVP.
