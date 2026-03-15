Inicia una sesion de trabajo mostrando el estado actual del proyecto.

## Instrucciones

Ejecuta estos pasos en paralelo y presenta un resumen consolidado:

1. **Issues abiertos**: `gh issue list --repo servo98/modpack-servo --state open --limit 30 --json number,title,labels`
   - Agrupa por prioridad: primero `priority:high`, luego el resto
   - Muestra count total y lista los high priority con numero y titulo

2. **Blockers activos**: Lee `docs/blockers.md`
   - Lista solo los blockers activos (no resueltos)
   - Si no hay blockers, indicalo

## Formato de salida

Presenta el resumen asi:

```
## Estado del proyecto

**Issues abiertos**: N total (X high priority)
[lista de high priority con #numero y titulo]

**Blockers**: N activos
[lista corta de cada blocker]
```

Al final pregunta: "En que trabajamos hoy?"

## Reglas

- NO incluir git log (no agrega valor al inicio)
- Ser conciso — esto es un dashboard, no un reporte
- Si un paso falla (ej: gh no disponible), reportar el error y continuar con los demas
