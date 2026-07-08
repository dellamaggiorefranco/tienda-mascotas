# Frontend — pendiente de generar

Esta carpeta se genera con el CLI oficial de Next.js, no a mano, para evitar versiones desactualizadas de dependencias.

Desde la raíz del repo, corré:

```bash
npx create-next-app@latest frontend --typescript --tailwind --app --eslint --src-dir --import-alias "@/*"
```

Elegí "No" cuando pregunte por Turbopack si preferís la config estable por ahora (cualquiera de las dos opciones sirve para el proyecto).

Una vez generado, borrá este archivo (`frontend/PENDIENTE.md`) — ya cumplió su función.
