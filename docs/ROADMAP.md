# Roadmap — Plataforma Inteligente para Mascotas

> Roadmap por versiones. El detalle sprint a sprint del trabajo en curso vive en `SPRINTS.md`; acá vive la dirección de mediano plazo.

## MVP — Fundación

- Registro y login de usuarios con JWT.
- Catálogo de productos con categorías y búsqueda básica.
- Carrito de compras.
- Flujo de pedidos (creación y consulta de estado).
- Panel de administración básico (alta/baja/modificación de productos).
- Entorno Dockerizado y desplegado en Internet, accesible públicamente.

## Versión 1 — Operación real

- Integración de pagos (Mercado Pago como prioridad para Argentina — ver ADR-007 en `DECISIONS.md`).
- Notificaciones y recordatorios de recompra vía WhatsApp (canal principal) y email (respaldo) — ver ADR-002.
- Panel de administración completo (métricas básicas, gestión de stock).
- Búsquedas y filtros avanzados en el catálogo.
- Gestión de imágenes de producto.

## Versión 2 — Fidelización

- Favoritos y listas de deseos.
- Reseñas y calificaciones de productos.
- Historial de compras y direcciones guardadas.
- Sistema de notificaciones más elaborado (seguimiento post-compra).

## Versión 3 — Plataforma inteligente

- Perfiles de mascota enriquecidos (alimentación, edad, peso, compatibilidades).
- Recordatorios automáticos de recompra según consumo estimado.
- Recomendaciones de productos compatibles con cada mascota.
- Comparador de alimentos y productos.
- Dashboard avanzado de negocio (productos más vendidos, cohortes).
- Cupones y estrategias de descuento.
- Exploración de internacionalización (idiomas, monedas, impuestos) — recién acá, no antes.

> Cada nueva versión debe aportar valor real y verificable, no funcionalidades por completitud.

## App nativa — largo plazo, no priorizada

No se prioriza el desarrollo de una app nativa (React Native/Flutter) en el MVP ni en V1/V2. Se reevalúa como fase futura únicamente si: el negocio ya está validado, la API REST ya existe y madura, y aparece una necesidad concreta que la web no resuelva bien (cámara, notificaciones push confiables en ambos sistemas operativos).

## Fuera de alcance (decidido explícitamente, no es un olvido)

- Plataforma SaaS multi-tenant para terceros — descartada como primera versión, ver ADR-003.
- Notificaciones push vía PWA como estrategia central — descartada, ver ADR-002.
