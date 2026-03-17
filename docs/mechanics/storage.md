# Storage Progression

> Fuente: GDD v2, seccion 4.2
> Mods: Storage Drawers, Tom's Simple Storage, Refined Storage, Create (Stock system)
> Relacionado: [Create Automation](create-automation.md), [Progression](progression.md)

## Overview

Storage progresivo en 4 tiers. Cada uno resuelve un problema diferente y coexisten — no se reemplazan.

## Progresion

| Cap | Sistema | Que resuelve |
|-----|---------|-------------|
| 1 | Vanilla chests | Almacenamiento basico |
| 2 | Storage Drawers basicos | Bulk storage visual (1 item por cajon) |
| 3 | Tom's Storage (terminal, connector) | Red de cofres conectados, busqueda desde un punto |
| 4 | Create Stock (Link, Ticker, Requester) + Drawers upgrades | Red logistica nativa de Create — monitorear stock, auto-restock, on-demand routing |
| 5 | Refined Storage completo | Storage digital, autocrafting, wireless. Bridge con Create via C&A |
| 6+ | Todo disponible | Optimizacion |

## Detalles por tier

### Tier 1: Storage Drawers (Ch2)
- Cajones de 1 item, visual
- Perfecto para bulk materials (cobblestone, iron, etc)
- Compacting drawers para compactar automaticamente

### Tier 2: Tom's Simple Storage (Ch3)
- Terminal: busca items en todos los cofres conectados
- Connector: une cofres existentes en red
- NO reemplaza Drawers, complementa

### Tier 3: Create Stock system (Ch4)
Sistema de logistica nativo de Create 6.0. No requiere Refined Storage.
- **Stock Link**: conecta a un inventario y reporta su contenido a la red
- **Stock Ticker**: muestra resumen del stock de toda la red Create
- **Transmitter**: propaga la red de Stock a distancia
- **Redstone Requester**: emite señal redstone cuando el stock de un item cae bajo un threshold (activa maquinas on-demand)

**Tom's Storage y Create Stock coexisten**: Tom's es simple (conecta cofres, busqueda manual), Stock es avanzado (monitoreo automatico, routing reactivo). No se eliminan mutuamente.

### Tier 4: Refined Storage (Ch5)
- Controller, Disk Drive, Grid
- Autocraft con patterns
- Importer/Exporter para conectar con Create belts
- Wireless Grid para acceso remoto
- Bridge con Create via Create Crafts & Additions (Electric Motor/Alternator)
