# Progression System (ProgressiveStages)

> Fuente: GDD v2, seccion 4 + 5.3
> Relacionado: [Game Loop](game-loop.md), [Quest System](quests.md), [Space Elevator](space-elevator.md), [Bosses](bosses.md)

## Overview

Progresion gateada por **8 capitulos** usando ProgressiveStages. Cada capitulo se desbloquea al completar:
1. Derrotar al **boss del capitulo anterior**
2. Completar la **entrega al Space Elevator** del capitulo anterior

## Stages

| Stage | Trigger | Desbloquea (resumen) |
|-------|---------|---------------------|
| servo_ch1 | Default (mundo nuevo) | Vanilla, FD basico, Supplementaries basico, Aquaculture, QoL, Spell Engine T0, Small Rune Pouch, Copper/Iron Ring |
| servo_ch2 | Boss Ch1 + Delivery Ch1 | Croptopia 12, B&C, Blender, Moldes, Create basico, Drawers, Rogue/Warrior, T1 melee, Gold/Citrine/Jade jewelry |
| servo_ch3 | Boss Ch2 + Delivery Ch2 | Frutas, Create andesite, Slice&Dice, Tom's, Drink Maker, Horno, Wizard/Paladin/Priest, T1 magico, Llave Avanzada |
| servo_ch4 | Boss Ch3 + Delivery Ch3 | Especias, Create brass+steam, Feasts, Skill Tree, T2 armor+armas, Tanzanite/Topaz/Diamond/Emerald jewelry |
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
