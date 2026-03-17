# Progression System (ProgressiveStages)

> Fuente de verdad para stages, bloqueo, distribucion de contenido por capitulo.
> Relacionado: [Game Loop](game-loop.md), [Quest System](quests.md), [Space Elevator](space-elevator.md), [Bosses](bosses.md)
> Datos de Create extraidos de JARs: [create-ecosystem-content.md](../mod-data/create-ecosystem-content.md)

## Overview

Progresion gateada por **8 capitulos** usando ProgressiveStages. Cada capitulo se desbloquea al completar:
1. Derrotar al **boss del capitulo anterior**
2. Completar la **entrega al Space Elevator** del capitulo anterior

Dos sistemas separados por capitulo:
- **FTB Quests** = "aprendiste esta mecanica?" (tutorial, logros puntuales, one-time)
- **Space Elevator** = "puedes producir a escala?" (entregas de volumen, produccion)

## Stages

| Stage | Trigger | Desbloquea (resumen) |
|-------|---------|---------------------|
| servo_ch1 | Default (mundo nuevo) | Vanilla, FD basico, QoL, Spell Engine T0, Small Rune Pouch, Copper/Iron Ring |
| servo_ch2 | Boss Ch1 + Delivery Ch1 | Croptopia 12, B&C, Create basico (belt/funnel), Drawers, Rogue/Warrior, T1 melee, Gold/Citrine/Jade jewelry |
| servo_ch3 | Boss Ch2 + Delivery Ch2 | Frutas, Create andesite (press/fan/saw/mill), Slice&Dice, Tom's Storage, Wizard/Paladin/Priest, T1 magico, T2 melee, Llave Avanzada |
| servo_ch4 | Boss Ch3 + Delivery Ch3 | Especias, Create brass+steam+logistics (mixer/deployer/crafter/packager/stock), Create Stock system, Feasts, Skill Tree, T2 magico, servo_cooking (Wok, Baker's Oven) |
| servo_ch5 | Boss Ch4 + Delivery Ch4 | Exoticos, Create trains+routing (chain conveyor/frogport/postbox), Create C&A (electric motor/alternator/rolling mill), Enchantment Industry, Refined Storage, Llave Maestra, Netherite jewelry |
| servo_ch6 | Boss Ch5 + Delivery Ch5 | Avanzados, Create Deco, Dragons Plus (ending/freezing), enchant V, T3 Netherite upgrades |
| servo_ch7 | Boss Ch6 + Delivery Ch6 | Raros, Llave del Nucleo, Unique jewelry loot en dungeons |
| servo_ch8 | Boss Ch7 + Delivery Ch7 | Todo, T4 custom (Aeternium/Ruby), recetas endgame |

## Mecanismos de bloqueo (ProgressiveStages)

| Mecanismo | Config | Efecto |
|-----------|--------|--------|
| Inventory Scanner | `BLOCK_ITEM_INVENTORY = true` | Items bloqueados se CAEN al suelo |
| Block Pickup | `BLOCK_ITEM_PICKUP = true` | No puedes recoger items bloqueados |
| Block Use | `BLOCK_ITEM_USE = true` | Cancela click derecho/uso |
| Block Crafting | `BLOCK_CRAFTING = true` | No puedes craftear |
| Mask Names | `MASK_LOCKED_ITEM_NAMES = true` | Dice "Unknown Item" en rojo |
| Tooltip | `SHOW_TOOLTIP = true` | "Locked - Stage required: [nombre]" |
| JEI/EMI | `SHOW_LOCK_ICON = true` | Candado sobre items bloqueados |

## Feedback al jugador

1. Item dice "Unknown Item" en rojo con tooltip "Stage required: servo_ch4"
2. Mensaje en chat: "You haven't unlocked this item yet!" + sonido
3. JEI/EMI: candado visual + overlay de color
4. Opcion de ocultar items bloqueados completamente

## Gap conocido: Curios slots

ProgressiveStages NO escanea slots de Curios API. `BLOCK_ITEM_PICKUP` impide recoger el item, `BLOCK_ITEM_USE` impide equiparlo via click derecho. Backup: servo_core listener que escanee Curios slots.

## Filosofia

- **ProgressiveStages = unica fuente de verdad** para TODA la progresion (items, recetas Y dificultad)
- Per-player/team: cada jugador ve dificultad acorde a su stage personal
- Champions escalan por **stage del jugador mas cercano** (servo_core post-procesa via API publica)
- Dungeons escalan por **tier de llave usada** (independiente del player stage)
- Dungeons siempre mas dificiles que overworld al mismo nivel
- Cada capitulo tiene de todo: cocina + farming + dungeon + combate + decoracion
- Zero SavedData custom. Zero sincronizacion entre sistemas.
- **Nada queda obsoleto**: las entregas ACUMULAN items de capitulos anteriores en cantidades crecientes
- **Entregas = produccion, no equipo personal**: el Space Elevator pide materias primas y productos procesados, nunca herramientas/armas/armadura del jugador
- **FTB Quests = tutorial**: las quests ensenan mecanicas ("craftea tu primer sword", "construye un Water Wheel"). El Elevator testea produccion.

---

## Distribucion de contenido por capitulo

Cada capitulo desbloquea contenido de TODOS los pilares. No hay "capitulo de Create" ni "capitulo de dungeons".

### Crops (gradual, no dump)

| Cap | Crops nuevos | Total |
|-----|-------------|-------|
| 1 | 12: Vanilla (trigo, papa, zanahoria, beetroot, melon, pumpkin, cacao, sugar cane) + FD (tomate, cebolla, repollo, arroz) | 12 |
| 2 | +12 Croptopia basicos: lettuce, corn, strawberry, blueberry, grape, cucumber, bell pepper, garlic, ginger, spinach, peanut, soybeans | 24 |
| 3 | +10 frutas: banana, mango, lemon, orange, apple, pineapple, coconut, peach, cherry, lime | 34 |
| 4 | +8 hierbas/especias: basil, cinnamon, nutmeg, turmeric, vanilla, mustard, hops, tea leaves | 42 |
| 5 | +8 exoticos: dragon fruit, star fruit, avocado, kiwi, fig, date, pomegranate, cranberry | 50 |
| 6 | +8 avanzados: artichoke, asparagus, eggplant, leek, rhubarb, elderberry, coffee beans, olive | 58 |
| 7 | +6 raros: saguaro, kumquat, persimmon, nectarine, currant, tomatillo | 64 |
| 8 | Todos los restantes desbloqueados | 70+ |

### Cocina

| Cap | Workstations nuevas | Recetas nuevas aprox |
|-----|--------------------|-----------------------|
| 1 | Cutting Board, Cooking Pot, Stove, Skillet (FD) | ~25 |
| 2 | B&C Keg | +30 |
| 3 | Expanded Delight, servo_cooking (Prep Station, Licuadora) + Slice&Dice (Slicer, Sprinkler) | +25 |
| 4 | FD Feasts + servo_cooking (Wok, Baker's Oven) + recetas con especias | +20 |
| 5 | Recetas exoticas | +15 |
| 6 | Recetas avanzadas con ingredientes raros | +10 |
| 7 | Recetas con ingredientes exclusivos de dungeon | +10 |
| 8 | Recetas legendarias que combinan todo | +10 |

**Todas las workstations se usan SIEMPRE una vez desbloqueadas:**
```
Cooking Pot ──── Ch1→Ch8
Cutting Board ── Ch1→Ch8 (automatizado con Slicer en Ch3+)
Keg ─────────── Ch2→Ch8
Slicer (S&D) ── Ch3→Ch8
Prep Station ── Ch3→Ch8
Licuadora ───── Ch3→Ch8
Wok ─────────── Ch4→Ch8
Baker's Oven ── Ch4→Ch8
```

### Create

| Cap | Tier | Que se desbloquea |
|-----|------|-------------------|
| 1 | — | Nada |
| 2 | Andesite Basics | Water Wheel, Shaft, Cogwheel, Large Cogwheel, Belt, Depot, Andesite Funnel, Chute, Gearbox, Clutch, Gearshift, Encased Chain Drive |
| 3 | Andesite Processing | Mechanical Press, Encased Fan, Mechanical Saw, Drill, Harvester, Plough, Millstone, Basin, Spout, Item Drain, Fluid Pipe/Tank/Pump, Andesite Tunnel + Slice&Dice (Slicer, Sprinkler) |
| 4 | Brass + Logistics | Mixer, Deployer, Crafter, Arm, Crushing Wheel, Steam Engine, Brass Funnel/Tunnel, Smart Chute, Sequenced Assembly, Rotation Speed Controller + **Packager, Stock Link, Stock Ticker, Transmitter, Redstone Requester, Table Cloth** |
| 5 | Trains + Electric + Enchanting | Trains (Track, Station, Signal, Controls, Bogeys, Schedule, Train Door) + **Chain Conveyor, Frogport, Postbox** + Create C&A (Electric Motor, Alternator, Accumulator, Tesla Coil, Rolling Mill) + Enchantment Industry (Blaze Enchanter/Forger, Printer, Mechanical Grindstone) |
| 6+ | Todo | Create Deco (389 bloques decorativos), Dragons Plus (Ending/Freezing processing, dye fluids), Mechanical Roller, todo restante |

**Todas las maquinas se usan SIEMPRE una vez desbloqueadas:**
```
Belt/Funnel ──── Ch2→Ch8
Millstone ────── Ch3→Ch8 (flour siempre se pide)
Press ─────────── Ch3→Ch8 (sheets siempre se piden)
Fan ──────────── Ch3→Ch8 (washing siempre se pide)
Slicer (S&D) ── Ch3→Ch8 (cocina automatizada)
Mixer ─────────── Ch4→Ch8 (brass siempre se pide)
Deployer ──────── Ch4→Ch8
Crushing Wheel ─ Ch4→Ch8 (crushed ore siempre se pide)
Rolling Mill ──── Ch5→Ch8 (rods/wire siempre se piden)
Trains ─────────── Ch5→Ch8
```

### Energia (Create — se apilan, no se reemplazan)

| Cap | Fuente | SU/FE | Proposito |
|-----|--------|-------|-----------|
| 2 | Hand Crank | 32 SU | Probar un belt, demo |
| 3 | Water Wheel | 64-256 SU | Correr Press + Mill + Fan a la vez |
| 3 | Large Water Wheel | 128-512 SU | Mas potencia, mas maquinas |
| 4 | Steam Engine | 1024+ SU | Crushing Wheels, fabricas grandes (Water Wheel ya no alcanza) |
| 5 | Electric Motor (FE→SU) | Variable | Convertir energia de Refined Storage para alimentar Create remoto |
| 5 | Alternator (SU→FE) | Variable | Convertir Create para alimentar Refined Storage |
| 5 | Accumulator | Storage FE | Almacenar energia para contraptions moviles |

**Cada fuente de energia tiene su razon de ser:**
- Water Wheel sigue corriendo tu Millstone de Ch3 en Ch8
- Steam Engine corre los Crushing Wheels que necesitan mas SU
- Electric Motor/Alternator son el **bridge entre Create (SU) y Refined Storage (FE)** — sin Create C&A, son dos sistemas separados; con C&A, Create puede alimentar RS y viceversa

### Storage

| Cap | Sistema | Que resuelve |
|-----|---------|-------------|
| 1 | Vanilla chests | Almacenamiento basico |
| 2 | Storage Drawers basicos | Bulk storage visual (1 item por cajon) |
| 3 | Tom's Storage (terminal, connector) | Red de cofres conectados, busqueda desde un punto |
| 4 | Create Stock (Link, Ticker, Requester) + Drawers upgrades | Red logistica nativa de Create — buscar items, hacer pedidos, auto-restock |
| 5 | Refined Storage completo | Storage digital, autocrafting, wireless. Bridge con Create via C&A |
| 6+ | Todo disponible | Optimizacion |

**Tom's Storage y Create Stock coexisten** — Tom's es simple (conecta cofres), Stock es avanzado (busca en toda la red, hace pedidos, routing con addresses). No se eliminan mutuamente.

### RPG Clases

| Cap | Clases disponibles | Tier | Notas |
|-----|--------------------|------|-------|
| 1 | Ninguna (ataques genericos) | T0 | Spell Binding Table, Small Rune Pouch, Copper/Iron Ring |
| 2 | Rogue, Warrior (melee) | T1 melee | Arms Station. Melee primero (materiales accesibles) |
| 3 | + Wizard, Paladin, Priest | T1 magico + T2 melee | Diamond melee gear + Monk Workbench, Rune Crafting Altar, Medium Rune Pouch, Ruby/Sapphire jewelry |
| 4 | Todas + Skill Tree | T2 magico | Diamond staves/robes + Elegir especializacion. Tanzanite/Topaz/Diamond/Emerald jewelry |
| 5 | Todas | T2+ enchants | Enchants magicos, Pociones Spell Power, Soul/Lightning runes, Large Rune Pouch, Netherite jewelry |
| 6 | Todas | T3 | Netherite upgrades para todo equipo RPG. Tipped Arrows Spell Power |
| 7 | Todas | T3+ uniques | 24 unique jewelry farmeable en Dungeon del Nucleo |
| 8 | Todas (maxeadas) | T4 custom | Aeternium/Ruby via KubeJS (materiales de boss) |

### Dungeons

| Cap | Llaves disponibles | Lo que cambia |
|-----|-------------------|---------------|
| 1 | Basica (1ra gratis + crafteo barato) | 5-7 salas, champions 1 affix, loot basico |
| 2 | Basica | Mismo tier, mas variedad de loot |
| 3 | + Avanzada | 10-14 salas, champions 2 affix, Esencia de Dungeon, 5% unique jewelry |
| 4 | Basica + Avanzada | Loot actualizado a Ch4 |
| 5 | + Maestra | 15-20 salas, champions 3 affix, 15% unique jewelry |
| 6 | Basica + Avanzada + Maestra | Loot actualizado a Ch6 |
| 7 | + Del Nucleo | 20-25 salas + boss de dungeon, champions exclusivos, unique jewelry garantizada |
| 8 | Todas | Loot endgame, T4 materials |

### Decoracion

| Cap | Que se desbloquea |
|-----|-------------------|
| 1 | Macaw's Furniture basico |
| 2 | + Macaw's Furniture completo |
| 3 | + Refurbished Furniture basico (funcional: nevera, estufa, fregadero) |
| 4 | + Macaw's Windows completo |
| 5 | + Refurbished completo |
| 6+ | + Create Deco (389 bloques industriales), todo disponible |

### Enchantments (vanilla, gateados)

| Cap | Max nivel de enchant |
|-----|---------------------|
| 1-2 | Nivel I-II |
| 3-4 | Nivel III |
| 5 | Nivel IV + Enchantment Industry (auto-enchanting) |
| 6+ | Nivel V, todo |

---

## Entregas al Space Elevator por capitulo

### Filosofia de entregas

1. **Solo produccion, nunca equipo personal** — entregas materias primas y productos procesados, no tus herramientas/armas/armadura
2. **Acumulativo** — cada capitulo pide items de capitulos anteriores en cantidades crecientes. Tus maquinas viejas SIGUEN produciendo
3. **El volumen fuerza automatizacion** — las cantidades escalan para que Create sea practicamente necesario desde Ch3
4. **Boss drop = unica excepcion** — 1 item directo de boss por capitulo como prueba de combate
5. **FTB Quests son el tutorial** — las quests ensenan mecanicas ("craftea X", "construye Y"). El Elevator testea produccion a escala

### Capitulo 1: "Primeras Raices"

Manual puro. Cantidades pequenas.

| Item | Cant. | Prueba de que |
|------|-------|---------------|
| Vegetable Soup | 16 | Cooking Pot |
| Beef Stew | 16 | Cocina con carne |
| Comida variada (min 8 tipos) | 16 | Diversidad culinaria |
| Crops Ch1 (cualquiera) | 32 | Farming |
| Pescado cocinado | 8 | Exploracion |
| Iron Ingot | 32 | Mineria |
| Leather + String | 16+16 | Caza/exploracion |
| Raiz del Guardian | 1 | **Boss** |
| **Total** | **~153** | ~3 horas manual |

### Capitulo 2: "Engranajes"

Belts mueven cosas. Todo de Ch1 sigue pidiendo.

| Item | Cant. | Nuevo/Acumulado |
|------|-------|-----------------|
| Cooking Pot recipe (cualquiera) | 32 | Acumulado (x2) |
| Cutting Board output | 32 | Acumulado |
| Cerveza/Hidromiel (Keg) | 16 | **Nuevo** |
| Queso (B&C) | 16 | **Nuevo** |
| Crops Croptopia | 64 | **Nuevo** |
| Comida variada (min 14 tipos) | 32 | Acumulado |
| Andesite Alloy | 32 | **Nuevo** — primer Create |
| Mandibula de la Bestia | 1 | **Boss** |
| **Total** | **~225** | |

### Capitulo 3: "Automatizacion"

Maquinas procesan. Las cantidades ya piden Create.

| Item | Cant. | Nuevo/Acumulado |
|------|-------|-----------------|
| Cooking Pot recipe | 64 | Acumulado (x2) |
| Cutting Board output | 32 | Acumulado |
| Keg product | 32 | Acumulado (x2) |
| Wheat Flour | 128 | **Nuevo** — Millstone |
| Iron Sheet | 64 | **Nuevo** — Press |
| Copper Sheet | 64 | **Nuevo** — Press |
| Fan-washed output | 32 | **Nuevo** — Encased Fan |
| Slicer output | 64 | **Nuevo** — Slice&Dice |
| Jugo/smoothie (Expanded Delight) | 32 | **Nuevo** |
| Comida variada (min 22 tipos) | 32 | Acumulado |
| Blaze Rod | 16 | **Nuevo** — Nether |
| Engranaje del Coloso | 1 | **Boss** |
| **Total** | **~561** | |

128 flour + 128 sheets a mano es inviable → Millstone y Press obligatorios.

### Capitulo 4: "La Fabrica"

Fabrica completa. Steam Engine porque Water Wheel ya no alcanza.

| Item | Cant. | Nuevo/Acumulado |
|------|-------|-----------------|
| Cooking Pot recipe | 64 | Acumulado |
| Keg product | 32 | Acumulado |
| Wheat Flour | 128 | Acumulado |
| Iron/Copper Sheet | 128 | Acumulado (x2) |
| Slicer output | 64 | Acumulado |
| Brass Ingot | 128 | **Nuevo** — Mixer |
| Precision Mechanism | 8 | **Nuevo** — Sequenced Assembly |
| Crushed ore (cualquiera) | 128 | **Nuevo** — Crushing Wheel |
| Deployer recipe output | 32 | **Nuevo** — Deployer |
| Feast completo (FD) | 8 | **Nuevo** |
| Wok recipe (servo_cooking) | 32 | **Nuevo** |
| Baker's Oven recipe | 32 | **Nuevo** |
| Comida variada (min 30 tipos) | 32 | Acumulado |
| Senal Fantasma | 1 | **Boss** |
| **Total** | **~807** | |

128 crushed ore = Crushing Wheels chupan SU → Steam Engine necesario. El Packager y Stock system estan disponibles para automatizar entregas via belt → Delivery Port.

### Capitulo 5: "La Red"

Escala industrial. Trenes mueven entre ubicaciones. Logistica rutea dentro de la fabrica.

| Item | Cant. | Nuevo/Acumulado |
|------|-------|-----------------|
| Cooking Pot recipe | 64 | Acumulado |
| Keg product | 32 | Acumulado |
| Sheets (iron+copper+brass) | 256 | Acumulado (x2) |
| Flour | 128 | Acumulado |
| Crushed ore | 128 | Acumulado |
| Slicer output | 64 | Acumulado |
| Wok + Baker's recipes | 64 | Acumulado |
| Iron/Copper/Brass Rod | 64 | **Nuevo** — Rolling Mill |
| Wire (cualquier tipo) | 64 | **Nuevo** — Rolling Mill |
| Enchanted Book III+ | 16 | **Nuevo** — Enchantment Industry |
| Comida exotica (crops Ch5) | 64 | **Nuevo** |
| Comida variada (min 38 tipos) | 32 | Acumulado |
| Quartz | 64 | **Nuevo** — material para Refined Storage |
| Nucleo del Arquitecto | 1 | **Boss** |
| **Total** | **~1041** | |

256 sheets + 128 crushed + 64 rods + 64 wire = volumen que pide trenes y logistics.

### Capitulo 6: "Maestria"

Menos volumen, items dificiles. Procesamiento exotico.

| Item | Cant. | Nuevo/Acumulado |
|------|-------|-----------------|
| Sheets + Rods + Wire (variados) | 128 | Acumulado |
| Crushed ore | 64 | Acumulado |
| ALL workstations output (Cooking Pot+Keg+Wok+Baker) | 64 | Acumulado |
| Slicer output | 32 | Acumulado |
| Comida variada (min 42 tipos) | 32 | Acumulado |
| Netherite Scrap | 16 | **Nuevo** |
| Netherite Ingot | 4 | **Nuevo** |
| End Stone (Dragons Plus Ending) | 32 | **Nuevo** — Ending processing |
| Blue Ice (Dragons Plus Freezing) | 32 | **Nuevo** — Freezing processing |
| Multi-step recipe (3+ workstations) | 32 | **Nuevo** |
| Hoz del Senor | 1 | **Boss** |
| **Total** | **~437** | |

End Stone y Blue Ice en masa solo se obtienen via Dragons Plus processing → fuerza ese sistema.

### Capitulo 7: "Profundidades"

Dungeon farming. La fabrica corre sola, el foco es combate.

| Item | Cant. | Nuevo/Acumulado |
|------|-------|-----------------|
| Factory output variado (sheets+rods+crushed) | 128 | Acumulado |
| ALL workstations output | 64 | Acumulado |
| Comida variada (min 44 tipos) | 32 | Acumulado |
| Unique Jewelry | 3 | **Nuevo** — dungeon farming |
| Esencia de Dungeon | 16 | **Nuevo** — champions en dungeon |
| Loot del Nucleo | 8 | **Nuevo** — Llave del Nucleo |
| Cristal del Nucleo | 1 | **Boss** |
| **Total** | **~252** | |

### Capitulo 8: "El Legado"

Todo combinado. La prueba final.

| Item | Cant. | Nuevo/Acumulado |
|------|-------|-----------------|
| Comida variada (min 48 tipos) | 64 | Acumulado |
| Comida legendaria (endgame recipes) | 32 | **Nuevo** |
| Factory output (sheets+rods+wire+crushed) | 256 | Acumulado |
| ALL workstations output | 64 | Acumulado |
| Cristal del Nucleo | 8 | **Nuevo** — dungeon endgame farming |
| Trofeos de Boss (1 de cada) | 7 | **Nuevo** — todos los bosses |
| Fragmento del Devorador | 1 | **Boss** |
| **Total** | **~432** | |

### Resumen de escalado

| Cap | Total items | Foco de la entrega |
|-----|-------------|-------------------|
| 1 | ~153 | Manual: cocina + farming basico |
| 2 | ~225 | Belts: Keg, Create basico, Croptopia |
| 3 | ~561 | Maquinas: Press, Mill, Fan, Slicer obligatorios |
| 4 | ~807 | Fabrica: Mixer, Crusher, Deployer, Sequenced Assembly |
| 5 | ~1041 | Industrial: Rolling Mill, Enchanting, trains/logistics necesarios |
| 6 | ~437 | Maestria: items dificiles (Netherite, Dragons Plus processing) |
| 7 | ~252 | Dungeon: farming de jewelry, esencia, loot |
| 8 | ~432 | Final: todo combinado |

---

## Decisiones arquitecturales (v2 — 2026-03-17)

### Eliminados
- **servo_packaging**: ELIMINADO. Create 6.0 tiene packaging nativo (Packager, Cardboard Package, Chain Conveyor, Frogport). Space Elevator acepta items directos.
- **servo_create**: ELIMINADO. Su unica funcion era compat de packaging con Create. Sin servo_packaging, no tiene razon de existir.

### Absorbidos
- **PepeMart**: Absorbido en servo_core como 1 bloque (`servo_core:pepe_mart`). Tienda con catalogo fijo, precios en materiales, stage-gated. Los Table Cloth shops de Create son para comercio entre jugadores.

### Arquitectura resultante: 4 mods custom
| Mod | Funcion |
|-----|---------|
| servo_core | Glue: stages, tokens, gacha compat, PepeMart, accessories, champions processing |
| servo_delivery | Space Elevator: acepta items directos (sin packaging) |
| servo_cooking | 4 workstations custom (Prep Station, Licuadora, Wok, Baker's Oven) |
| servo_dungeons | Dungeons procedurales + bosses (altar, llaves, dimension, rooms) |

Ver [architecture.md](../architecture.md) para el grafo completo de dependencias.
