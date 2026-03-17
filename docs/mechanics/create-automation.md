# Create y Automatizacion

> Fuente: GDD v2, secciones 3.7, 4.2 (Create por capitulo). Actualizado sesion 17 (2026-03-17) para Create 6.0.
> Mods: Create, Create Crafts & Additions, Create Deco, Create Enchantment Industry, Slice & Dice
> Relacionado: [Space Elevator](space-elevator.md), [Cooking](cooking.md), [Storage](storage.md), [Nether](nether.md)

## Overview

Segundo pilar del modpack. Create tiene PROPOSITO: producir y entregar items al Space Elevator. Nunca obligatorio en Ch1-Ch2, pero hace entregas masivas de Ch3+ practicamente necesario.

Create 6.0 incluye packaging nativo (Packager, Cardboard Package) y logistica avanzada (Stock Link/Ticker, Chain Conveyor, Frogport, Postbox). No se requieren mods custom de packaging.

## Progresion por capitulo

| Cap | Tier | Que se desbloquea |
|-----|------|-------------------|
| 1 | — | Nada |
| 2 | Andesite Basics | Water Wheel, Shaft, Cogwheel, Large Cogwheel, Belt, Depot, Andesite Funnel, Chute, Gearbox, Clutch, Gearshift, Encased Chain Drive |
| 3 | Andesite Processing | Mechanical Press, Encased Fan, Mechanical Saw, Drill, Harvester, Plough, Millstone, Basin, Spout, Item Drain, Fluid Pipe/Tank/Pump, Andesite Tunnel + Slice&Dice (Slicer, Sprinkler) |
| 4 | Brass + Logistics | Mixer, Deployer, Crafter, Arm, Crushing Wheel, Steam Engine, Brass Funnel/Tunnel, Smart Chute, Sequenced Assembly, Rotation Speed Controller + **Packager, Stock Link, Stock Ticker, Transmitter, Redstone Requester, Table Cloth** |
| 5 | Trains + Electric + Enchanting | Trains (Track, Station, Signal, Controls, Bogeys, Schedule, Train Door) + **Chain Conveyor, Frogport, Postbox** + Create C&A (Electric Motor, Alternator, Accumulator, Tesla Coil, Rolling Mill) + Enchantment Industry (Blaze Enchanter/Forger, Printer, Mechanical Grindstone) |
| 6+ | Todo | Create Deco (389 bloques decorativos), Dragons Plus (Ending/Freezing processing, dye fluids), Mechanical Roller, todo restante |

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
| Create | Core: incluye Packager, Stock system, Chain Conveyor, Frogport, Postbox | Ch2+ |
| Create Crafts & Additions | Motor electrico, bridge FE con RS | Ch5 |
| Create Deco | 389 bloques decorativos industriales | Ch6+ |
| Create Enchantment Industry | Auto-enchanting, XP liquido | Ch5 |
| Slice & Dice | Automatizar Cutting Board y Cooking Pot | Ch3 |

## Sistema de logistica nativo Create 6.0 (Ch4+)

Create 6.0 incluye un sistema completo de logistica sin mods externos:

### Packager
Toma items de un belt y los empaqueta en Cardboard Packages (bulk container nativo de Create).
Reemplaza la funcion de servo_packaging/servo_create del diseno anterior.

```
Belt con items → Packager → Cardboard Package → Belt → Delivery Port
```

### Create Stock system (Ch4)
- **Stock Link**: conecta a un inventario (chest, barrel, drawer) y reporta su contenido a la red
- **Stock Ticker**: muestra resumen del stock de toda la red
- **Transmitter**: propaga la red a distancia
- **Redstone Requester**: emite señal redstone cuando el stock de un item cae por debajo de un threshold
Util para: "pide mas brass cuando quede menos de 64", "activa Press cuando haya flour disponible"

### Chain Conveyor / Frogport / Postbox (Ch5)
- **Chain Conveyor**: belt de alta velocidad para distancias largas dentro de la fabrica
- **Frogport**: router de items — separa por tipo/destino en un junction de belts
- **Postbox**: send items a una direccion especifica (como hoglin mail pero automatico)
Juntos forman el backbone de routing intra-fabrica antes de Refined Storage.

## Energia (se apilan, no se reemplazan)

| Cap | Fuente | SU/FE | Proposito |
|-----|--------|-------|-----------|
| 2 | Hand Crank | 32 SU | Probar un belt, demo |
| 3 | Water Wheel | 64-256 SU | Correr Press + Mill + Fan a la vez |
| 3 | Large Water Wheel | 128-512 SU | Mas potencia, mas maquinas |
| 4 | Steam Engine | 1024+ SU | Crushing Wheels, fabricas grandes (Water Wheel ya no alcanza) |
| 5 | Electric Motor (FE→SU) | Variable | Convertir energia de Refined Storage para alimentar Create remoto |
| 5 | Alternator (SU→FE) | Variable | Convertir Create para alimentar Refined Storage |
| 5 | Accumulator | Storage FE | Almacenar energia para contraptions moviles |

**Cada fuente tiene su razon de ser:**
- Water Wheel sigue corriendo tu Millstone de Ch3 en Ch8
- Steam Engine corre los Crushing Wheels que necesitan mas SU
- Electric Motor/Alternator son el **bridge entre Create (SU) y Refined Storage (FE)**

## Pipeline tipico por capitulo

### Ch3: Primeras maquinas
```
Harvester → Belt → Millstone → Flour
                → Press → Iron/Copper Sheet
                → Fan → Washed ore
```

### Ch4: Fabrica completa
```
Harvester → Belt → Slice&Dice → Cooking Pot
Steam Engine → Crushing Wheels → Crushed ore
Mixer → Brass Ingot
Sequenced Assembly → Precision Mechanism
Packager → Cardboard Package → Belt → Delivery Port
```

### Ch5: Red logistica
```
Stock Link → Stock Ticker (ver inventarios)
Redstone Requester → activar maquinas on-demand
Chain Conveyor → mover items rapido entre areas
Frogport → rutear por tipo de item

Tren: Fabrica A → Estacion → Tren → Estacion → Delivery Terminal
```

### Ch5: Bridge Create ↔ Refined Storage
```
Create Alternator → FE → RS Controller
RS Exporter → items → Create Belt → procesamiento
RS Importer → productos terminados → RS
```

## Cocina automatizada con Slice & Dice

Slice & Dice (Ch3) automatiza Farmer's Delight y mods de cocina compatibles:
```
Slicer → automatiza Cutting Board (cortar ingredientes)
Sprinkler → automatiza Cooking Pot con temperatura controlada
```

Para servo_cooking (Prep Station, Licuadora, Wok, Baker's Oven), el mismo patron aplica:
runtime recipe injection (ver [cooking.md](cooking.md)) permite que Deployer+Basin u otras maquinas Create automaticen las workstations custom.

## Bridge con Nether

Ver [Nether](nether.md) para detalles del transporte cross-dimensional con trenes.
