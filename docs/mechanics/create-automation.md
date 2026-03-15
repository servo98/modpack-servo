# Create y Automatizacion

> Fuente: GDD v2, secciones 3.7, 4.2 (Create por capitulo)
> Mods: Create, Create Crafts & Additions, Create Deco, Create Enchantment Industry, Slice & Dice
> Relacionado: [Packaging](packaging.md), [Space Elevator](space-elevator.md), [Cooking](cooking.md), [Storage](storage.md), [Nether](nether.md)

## Overview

Segundo pilar del modpack. Create tiene PROPOSITO: empacar y entregar items al Space Elevator. Nunca obligatorio, pero hace entregas mas eficientes.

## Progresion por capitulo

| Cap | Que se desbloquea |
|-----|-------------------|
| 1 | Nada |
| 2 | Basico: Water Wheel, Shaft, Cogwheel, Belt, Depot, Andesite Funnel, Chute |
| 3 | Andesite completo: Mechanical Press, Fan, Saw, Drill, Harvester, Millstone |
| 4 | Brass tier: Mixer, Deployer, Crafter, Arm, Brass Funnel + Steam Engine + **Trenes basicos** |
| 5 | Trenes avanzados + Create C&A (motor electrico) + Enchantment Industry |
| 6+ | Todo disponible |

## Trenes (progresivos)

| Modo | Capitulo | Como | Chunks forzados |
|------|----------|------|-----------------|
| Manual | Ch4 | Jugador conduce | 0 |
| Semi-auto | Ch5 | Conductor (Blaze Burner) + Schedule | ~18 (FTB Chunks) |
| Full auto | Ch6+ | Schedule 24/7 | ~18 (FTB Chunks) |

~18 chunks forzados es insignificante para server 12GB.

## Mods de Create

| Mod | Funcion | Capitulo |
|-----|---------|----------|
| Create | Core: 204 items, 701 bloques | Ch2+ |
| Create Crafts & Additions | Motor electrico, bridge FE con RS | Ch5 |
| Create Deco | 389 bloques decorativos industriales | Ch5 |
| Create Enchantment Industry | Auto-enchanting, XP liquido | Ch5 |
| Slice & Dice | Automatizar Cutting Board y Cooking Pot | Ch3 |

## Pipeline de empaque automatizado (Ch4+ — servo_create)

servo_create agrega dos integraciones con Create:

1. **Deployer Folding**: Carton Plano en belt → Deployer (Use) → Caja Abierta
2. **Basin Compacting**: Basin (Caja Abierta + items packable) + Press → Caja de Envio con BoxContents

```
Harvester → Belt → Press (Paper) → Crafter (Carton)
  → Deployer (fold) → Basin+Press (compact) → Belt → Delivery Port
```

Ver [Packaging](packaging.md#4-automatizacion-con-create-servo_create-addon) para pipeline completo.

## Pipeline tipico (Ch5+)

```
Harvester → Belt → Slice&Dice → Cooking Pot
  → Basin+Press (compact) → Belt → Delivery Port
```

Con RS:
```
RS Exporter → Create Belt → Procesamiento
  → RS Importer → Autocraft → RS Exporter → Delivery Port
```

## Bridge con Nether

Ver [Nether](nether.md) para detalles del transporte cross-dimensional con trenes.
