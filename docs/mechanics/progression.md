# Progression System (ProgressiveStages)

> Fuente de verdad para stages, bloqueo, distribucion de contenido por capitulo.
> Relacionado: [Game Loop](game-loop.md), [Quest System](quests.md), [Space Elevator](space-elevator.md), [Bosses](bosses.md)

## Overview

Progresion gateada por **8 capitulos** usando ProgressiveStages. Cada capitulo se desbloquea al completar:
1. Derrotar al **boss del capitulo anterior**
2. Completar la **entrega al Space Elevator** del capitulo anterior

## Stages

| Stage | Trigger | Desbloquea (resumen) |
|-------|---------|---------------------|
| servo_ch1 | Default (mundo nuevo) | Vanilla, FD basico, QoL, Spell Engine T0, Small Rune Pouch, Copper/Iron Ring |
| servo_ch2 | Boss Ch1 + Delivery Ch1 | Croptopia 12, B&C, Create basico, Drawers, Rogue/Warrior, T1 melee, Gold/Citrine/Jade jewelry |
| servo_ch3 | Boss Ch2 + Delivery Ch2 | Frutas, Create andesite, Slice&Dice, Tom's, Wizard/Paladin/Priest, T1 magico, **T2 melee** (Diamond melee gear), Llave Avanzada |
| servo_ch4 | Boss Ch3 + Delivery Ch3 | Especias, Create brass+steam, Feasts, Skill Tree, **T2 magico** (Diamond staves/robes), Tanzanite/Topaz/Diamond/Emerald jewelry |
| servo_ch5 | Boss Ch4 + Delivery Ch4 | Exoticos, RS, Trains, Enchantment Industry, Enchants magicos, Pociones Spell Power, Llave Maestra, Netherite jewelry |
| servo_ch6 | Boss Ch5 + Delivery Ch5 | Avanzados, enchant V, T3 Netherite upgrades, Tipped Arrows Spell Power |
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
| 3 | Expanded Delight | +25 |
| 4 | FD Feasts + recetas con especias | +20 |
| 5 | Recetas exoticas | +15 |
| 6 | Recetas avanzadas con ingredientes raros | +10 |
| 7 | Recetas con ingredientes exclusivos de dungeon | +10 |
| 8 | Recetas legendarias que combinan todo | +10 |

### Create

| Cap | Que se desbloquea |
|-----|-------------------|
| 1 | Nada |
| 2 | Basico: Water Wheel, Shaft, Cogwheel, Belt, Depot, Andesite Funnel, Chute |
| 3 | Andesite completo: Mechanical Press, Fan, Saw, Drill, Harvester, Millstone + Slice&Dice |
| 4 | Brass tier: Mixer, Deployer, Crafter, Arm, Brass Funnel + Steam Engine + Trains basicos |
| 5 | Trains avanzados + Create C&A (motor electrico, alternador) + Enchantment Industry |
| 6+ | Todo disponible |

### Storage

| Cap | Que se desbloquea |
|-----|-------------------|
| 1 | Vanilla chests |
| 2 | Storage Drawers basicos |
| 3 | Tom's Storage (terminal, connector) |
| 4 | Storage Drawers upgrades avanzados |
| 5 | Refined Storage completo |
| 6+ | Todo disponible |

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
| 5 | + Create Deco + Refurbished completo |
| 6+ | Todo disponible |

### Enchantments (vanilla, gateados)

| Cap | Max nivel de enchant |
|-----|---------------------|
| 1-2 | Nivel I-II |
| 3-4 | Nivel III |
| 5 | Nivel IV + Enchantment Industry (auto-enchanting) |
| 6+ | Nivel V, todo |
