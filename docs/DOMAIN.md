# Domain Model — Plataforma Inteligente para Mascotas

> Modelo de dominio y base de datos. Ver `ARCHITECTURE.md` para las reglas generales del backend.

## 1. Reglas generales de datos

- Ninguna tabla se modifica manualmente en producción: toda modificación pasa por una migración Flyway versionada junto al código.
- Soft Delete en entidades de negocio: nunca se eliminan registros importantes (pedidos, usuarios, pagos).
- Todas las entidades incluyen, sin excepción: `id`, `createdAt`, `updatedAt`, `createdBy`, `updatedBy`, `tenantId` (ver `ARCHITECTURE.md`, sección 7).

## 2. Entidades principales (MVP)

| Entidad | Responsabilidad |
|---|---|
| `User` | Datos de cuenta, credenciales, rol |
| `Pet` | Mascotas asociadas a un usuario (especie, raza, edad, peso) |
| `Category` | Categorías y subcategorías de productos |
| `Product` | Catálogo: nombre, descripción, precio, proveedor, stock disponible |
| `Cart` / `CartItem` | Carrito activo de un usuario |
| `Order` / `OrderItem` | Pedido confirmado y sus líneas |
| `Payment` | Estado y datos de la transacción de pago |
| `Shipment` | Seguimiento del envío asociado a un pedido |

## 3. Relaciones clave (MVP)

```
User 1---N Pet
User 1---1 Cart
Cart 1---N CartItem N---1 Product
User 1---N Order
Order 1---N OrderItem N---1 Product
Order 1---1 Payment
Order 1---1 Shipment
Product N---1 Category
```

## 4. Roles y seguridad

| Rol | Alcance | Fase |
|---|---|---|
| `ADMIN` | Gestión total: productos, pedidos, usuarios, precios | MVP |
| `CUSTOMER` | Compra, gestión de su cuenta y sus mascotas | MVP |
| `EMPLOYEE` | Operación diaria: pedidos, atención al cliente | V2 |
| `SUPPLIER` | Actualización de stock y precios propios | V2/V3 |

Reglas obligatorias: JWT con expiración corta + refresh token, contraseñas con BCrypt, validación en todos los endpoints, `@PreAuthorize` por rol, rate limiting en login y checkout.

## 5. Convenciones de API REST

- Recursos en plural: `/api/v1/products`, `/api/v1/orders`.
- Versionado explícito desde el día uno.
- Verbos HTTP semánticos (GET, POST, PUT/PATCH, DELETE).
- Paginación estándar: `page`, `size`, `sort` como query params.
- Formato único de error:

```json
{
  "timestamp": "2026-07-08T14:22:00Z",
  "status": 404,
  "error": "PRODUCT_NOT_FOUND",
  "message": "El producto solicitado no existe."
}
```

## 6. Evolución futura del dominio (V3, no MVP)

Pensado para más adelante, sin construirlo ahora: `Pet` se enriquece con alimentación actual, frecuencia estimada de recompra y compatibilidades; se agregan `Reminder` (recordatorio de recompra) y `Recommendation` (sugerencias basadas en el perfil de la mascota). No se modela todavía para no anticipar estructura que puede cambiar antes de tener datos reales de uso.
