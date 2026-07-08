# Project Bible — Plataforma Inteligente para Mascotas

> Este documento es la fuente de verdad del **por qué** del proyecto. Arquitectura, stack y convenciones viven en otros archivos de `/docs`; acá vive la visión, los objetivos y los principios que no cambian sprint a sprint.

## 1. Visión

Construir la mejor plataforma digital para la compra de productos para mascotas en Argentina, ofreciendo una experiencia moderna, rápida y personalizada. No competimos por tener más catálogo. Competimos por ofrecer la mejor experiencia posible al dueño de una mascota.

**Visión técnica:** desarrollar un producto con estándares profesionales, preparado para escalar desde el primer día, con una arquitectura que permita seguir creciendo durante años sin reescribirse.

## 2. El problema que resolvemos

Hoy montar un ecommerce es relativamente fácil (Shopify, Tiendanube, WooCommerce). Si construimos "otro ecommerce", no tenemos ninguna ventaja competitiva real. Por eso el objetivo no es una tienda genérica, sino una **plataforma especializada en el mundo de las mascotas**, donde vender productos es solo una parte de la experiencia.

## 3. Propuesta de valor

Con el tiempo, el sistema debe poder responder preguntas sobre cada mascota del usuario, no solo sobre el usuario:

- ¿Qué mascota tiene, de qué especie, raza y edad?
- ¿Qué alimento consume habitualmente?
- ¿Cada cuánto necesita volver a comprar?
- ¿Qué productos son compatibles entre sí?

> La tienda es el punto de partida. No es el objetivo final. Vendemos soluciones para el cuidado de una mascota, no solamente productos.

## 4. Público objetivo

- **Primario:** dueños de mascotas (perros y gatos, en una primera etapa) en Argentina, que ya compran productos de forma recurrente (alimento, accesorios) y valoran no tener que estar pendientes de cuándo reponer.
- **Secundario (fase de negocio):** el proveedor/operador del negocio (rol de "Referente de negocio"), responsable de stock bajo pedido, precios y logística.
- **No objetivo por ahora:** otras tiendas de mascotas como clientes de una plataforma SaaS (evaluado y descartado explícitamente — ver `DECISIONS.md`, ADR-003).

## 5. Objetivos del proyecto

**De negocio:**
- Vender productos para mascotas bajo modelo de venta por pedido (sin stock propio).
- Validar el modelo comercial con datos reales.
- Automatizar procesos para minimizar trabajo manual.
- Generar ingresos sostenibles.

**Técnicos:**
- Aprender y practicar desarrollo de software de nivel profesional.
- Dominar Spring Boot y arquitectura backend mantenible.
- Aprender integración con servicios externos (pagos, WhatsApp, envíos).
- Practicar procesos de equipo reales (Git Flow, PRs, code review).

## 6. Filosofía del proyecto

1. **El negocio manda.** Toda decisión técnica responde a una necesidad real. Nunca se agrega tecnología porque está de moda.
2. **Simplicidad primero.** La solución más simple que resuelva correctamente el problema es la elegida. Se evita la sobreingeniería — esto aplica también a la planificación, no solo al código.
3. **Calidad antes que velocidad.** Preferimos una funcionalidad una semana después si eso la hace mantenible durante años.
4. **Escalable desde el diseño.** Se construye pensando en miles de usuarios, aunque hoy solo haya uno.
5. **El código es un producto.** Limpio, legible, consistente, documentado y testeable.

## 7. Cómo tomamos decisiones técnicas

- Toda decisión relevante se fundamenta, nunca se impone por autoridad ("la mejor idea gana").
- Si hay desacuerdo y no se resuelve en una charla corta (~15 min), se prueba la opción más simple y se revisa después con datos reales, en vez de bloquear el avance.
- Toda decisión de arquitectura no trivial se documenta como ADR en `DECISIONS.md`, con contexto, alternativas consideradas y consecuencias.
- El rol de Tech Lead/mentor (humano o Claude Code) propone estándares y detecta riesgos; no impone sin justificación.

## 8. Riesgos conocidos

| Riesgo | Impacto | Mitigación |
|---|---|---|
| El proveedor no tiene stock actualizado al momento del pedido | Cliente compra algo no disponible | Confirmar disponibilidad antes de facturar; comunicación clara de tiempos de entrega |
| Desarrollo en solitario, sin segundo par de ojos humano | Errores o malas decisiones no detectadas a tiempo | Autorevisión con distancia temporal + Claude Code como revisor de cada PR (ver `CODING_STANDARDS.md`) |
| Todo el conocimiento del proyecto en una sola persona | Si Franco se frena, el proyecto se frena entero | Documentación viva en `docs/` siempre actualizada, para que retomar después de una pausa no dependa de la memoria |
| Ambición de alcance (ej. multi-tenant, app nativa) antes de validar el negocio base | Nunca se lanza / se sobre-construye | Roadmap estricto por versiones (ver `ROADMAP.md`); cualquier ampliación de alcance requiere ADR explícito |
| Dependencia de un solo proveedor de pagos o notificaciones | Interrupción del servicio si falla el proveedor | Diseñar la integración desacoplada (interfaz propia + adaptador), no acoplar el dominio al SDK externo |
| Dos desarrolladores part-time (estudiantes) | Ritmo de desarrollo variable | Sprints cortos y realistas, priorizando siempre lo que agrega valor validable |

## 9. Decisiones estratégicas ya tomadas (resumen)

- Se descartó construir una plataforma SaaS multi-tenant internacional como primera versión — ver ADR-003 en `DECISIONS.md`. Se valida primero un negocio real (single-tenant) antes de evaluar abrir la plataforma a terceros.
- Se descartó apoyar la estrategia de notificaciones en PWA/push nativo — ver ADR-002. El canal principal es WhatsApp + email.
- No se prioriza una app nativa en el MVP ni en V1/V2 — ver `ROADMAP.md`, sección de app nativa.
- El proyecto lo desarrolla Franco en solitario, no un equipo de dos — ver ADR-006. Esto no cambia el negocio ni la arquitectura, sí el ritmo de trabajo y el proceso de revisión de código.
