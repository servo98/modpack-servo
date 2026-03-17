# Capitulo 3: Automatizacion

> Stage: `servo_ch3` (requiere: Boss Ch2 + Delivery Ch2)
> Trigger para Ch4: Derrotar al Coloso Mecanico + Completar entrega Ch3
> Mecanicas: [Create Automation](../mechanics/create-automation.md), [Cooking](../mechanics/cooking.md), [RPG Classes](../mechanics/rpg-classes.md), [Dungeons](../mechanics/dungeons.md), [Storage](../mechanics/storage.md), [Nether](../mechanics/nether.md)

## Tema

Create pasa de "mover cosas" a "procesar materiales". Press, Fan, Millstone y Saw transforman recursos. Nether se abre (ingredientes raros). Clases magicas se desbloquean (Wizard, Paladin, Priest). Slice&Dice conecta Create con cocina. Tom's Storage organiza todo. Las cantidades del Elevator empiezan a forzar automatizacion — 128 flour a mano es inviable.

## Mods nuevos

Create andesite processing completo, Slice&Dice (Slicer, Sprinkler), Tom's Simple Storage, Expanded Delight, servo_cooking (Prep Station, Licuadora), Refurbished Furniture basico, Llave Avanzada de dungeon, Wizards, Paladins & Priests

## Contenido

| Area | Detalle |
|------|---------|
| Crops | +10 frutas (34 total): banana, mango, lemon, orange, apple, pineapple, coconut, peach, cherry, lime |
| Recetas | +25 (jugos, smoothies, recetas con frutas, Expanded Delight) |
| Workstations | + Expanded Delight + servo_cooking (**Prep Station**, **Licuadora**) + Slice&Dice (Slicer, Sprinkler) |
| Create | **Andesite Processing**: Mechanical Press, Encased Fan, Mechanical Saw, Drill, Harvester, Plough, Millstone, Basin, Spout, Item Drain, Fluid Pipe/Tank/Pump, Andesite Tunnel |
| Energia | Water Wheel (64-256 SU), Large Water Wheel (128-512 SU) — suficiente para Press+Mill+Fan |
| Storage | **Tom's Storage** (terminal, connector) — red de cofres con busqueda |
| Nether | Abierto. Blaze Rod, Nether Wart, ingredientes raros |
| RPG | + Wizard, Paladin, Priest (T1 magico). T2 melee. Monk Workbench, Rune Crafting Altar, Medium Rune Pouch |
| Jewelry | Ruby/Sapphire rings y necklaces. Jeweler's Kit |
| Dungeon | **Llave Avanzada**. 10-14 salas, 2 affixes, Esencia de Dungeon empieza a dropear, 5% unique jewelry |
| Champions | 10% overworld, 1-2 affixes |
| Boss | Coloso Mecanico (1,600 HP) |
| Decoracion | Refurbished Furniture basico (nevera, estufa, fregadero — funcional) |

### servo_cooking workstations nuevas

| Workstation | Mecanica | Create compat |
|-------------|----------|---------------|
| **Prep Station** | Ensamblaje frio — 4 slots de ingredientes, combina en 1 output | Deployer en secuencia |
| **Licuadora** | Mezcla ingredientes + liquido (3 seg timer) | Basin + Mixer |

### Pipeline tipico Ch3
```
Harvester → Belt → Millstone → Wheat Flour
              → Press → Iron/Copper Sheet
              → Fan → Washed ore
Slicer → automatiza Cutting Board
Sprinkler → automatiza Cooking Pot
```

## Entrega al Space Elevator — Ch3: "Automatizacion"

> Detalle completo: [progression.md → Entregas](../mechanics/progression.md#capitulo-3-automatizacion)

Maquinas procesan. Las cantidades fuerzan Create (~561 items total).

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

128 flour + 128 sheets a mano es inviable → Millstone y Press obligatorios.

## Quests (~48)

### Historia/Tutorial (10)
1. Tutorial: Mechanical Press — hacer sheets
2. Tutorial: Encased Fan — washing, smelting, haunting
3. Tutorial: Millstone — wheat flour y crushed ore
4. Tutorial: Slice&Dice — Slicer automatiza Cutting Board
5. Tutorial: Slice&Dice — Sprinkler automatiza Cooking Pot
6. Tutorial: Tom's Storage — terminal y connector
7. Tutorial: Nether — acceso y peligros
8. Tutorial: Class books magicos — Wizard, Paladin, Priest
9. Tutorial: Rune Crafting Altar — craftear runas
10. Tutorial: Llave Avanzada de dungeon

### Cocina (6)
1. Automatizar receta con Slice&Dice (Slicer o Sprinkler)
2. Usar la Prep Station (servo_cooking) — ensamblar primera receta fria
3. Usar la Licuadora (servo_cooking) — primer jugo/smoothie custom
4. Preparar jugo con Expanded Delight
5. Cocinar con ingrediente del Nether
6. Comer 30 comidas diferentes (Recetario: +3 corazones)

### Farming (6)
1. Plantar 4 frutas nuevas (Croptopia)
2. Plantar todas las 10 frutas
3. Granja de banana o mango
4. Granja de Nether Wart
5. Automatizar harvest con Create Harvester
6. Conectar Harvester → Belt → procesamiento

### Dungeon (5)
1. Craftear primera Llave Avanzada
2. Completar dungeon Avanzada (10-14 salas)
3. Obtener primera Esencia de Dungeon
4. Obtener unique jewelry de dungeon (5% chance)
5. Hacer 3 runs Avanzadas

### Combate/RPG (5)
1. Elegir clase magica (Wizard, Paladin, o Priest)
2. Craftear armor T1 magica (Monk Workbench)
3. Craftear Medium Rune Pouch
4. Craftear runas en el Rune Crafting Altar
5. Derrotar al Coloso Mecanico

### Exploracion (5)
1. Entrar al Nether
2. Encontrar fortaleza Nether (YUNG's Better Nether Fortresses)
3. Obtener Blaze Rods (16+ para entrega)
4. Encontrar ores Ruby/Sapphire
5. Explorar 3 biomas nuevos

### Construccion (5)
1. Instalar Tom's Storage terminal conectado a cofres
2. Construir primera linea Create (belt con procesamiento)
3. Setup de Millstone + Press funcional
4. Instalar cocina funcional Refurbished (nevera+estufa+fregadero)
5. Instalar Prep Station y Licuadora en la cocina

### Coleccion (5)
1. Obtener Ruby y Sapphire (gemas)
2. Equipar ring y necklace de gema
3. Producir 64+ Iron Sheets con Press
4. Acumular 50 Pepe Coins
5. Completar entrega al Space Elevator Ch3
