# Sistema de Cocina

> Fuente: GDD v2, secciones 3.3, 4.2 (Cocina por capitulo, Crops por capitulo)
> Relacionado: [Packaging](packaging.md), [Space Elevator](space-elevator.md), [Create Automation](create-automation.md), [Nether](nether.md)

## Overview

Pilar central del modpack. Cocinar a mano es **divertido** (Perfil B), Create escala para entregas. Incentivo: capitulo "Recetario" en FTB Quests (variedad de comidas = corazones extras como rewards de quest).

## Mods de cocina

| Mod | Funcion | Capitulo |
|-----|---------|----------|
| Farmer's Delight | Core: Cooking Pot, Cutting Board, Stove, Skillet. 92 items, 4 crops | Ch1 (basico), Ch2 (completo) |
| Brewin' And Chewin' | Keg: fermentacion, quesos, bebidas alcoholicas. 36 items | Ch2 |
| Expanded Delight | Juicer, mas crops y recetas FD. 60 items | Ch3 |
| Croptopia | ~60 crops (recetas ELIMINADAS, solo ingredientes) | Ch2+ (gradual) |
| Aquaculture 2 | 30+ peces, tag generico #c:fish | Siempre |
| FTB Quests (Recetario) | Variedad = corazones permanentes via quest rewards | Siempre (capitulo propio en quest book) |
| Slice & Dice | Automatizar cocina con Create | Ch3 |

## 4 Workstations custom (servo_cooking)

### Batidora/Blender (Ch2)
- 4 ingredientes + 1 fluido → 1 output
- 100 ticks (5 seg), sin calor
- Recetas: smoothies, salsas, jugos, mezclas
- Compatible: Hopper, Create Funnel, RS Exporter

### Moldes de Postres (Ch2)
- 1 masa + 1 relleno + 1 molde (no consume) → 1 output
- Moldes coleccionables: 7 tipos, 5 rarezas
- Calidad 1-5 estrellas (afecta stats): `nutrition = base * (1 + 0.2 * (stars - 1))`
- Obtencion moldes: gacha, bosses, quests

### Drink Maker (Ch3)
- 1 base + 3 extras + 1 vaso → 1 output
- **Orden de extras IMPORTA** (mecanica repetitiva satisfactoria)
- Efectos: Cafe=Speed, Te=Regen, Chocolate=Resistance, Smoothie=Saturation

### Horno Avanzado (Ch3)
- 3 ingredientes → 1 output
- 3 modos: Hornear, Rostizar, Gratinar
- **Temperatura slider 1-10** (correcta=perfecto, +3=quemado)
- Receta muestra "Media-Alta" (descubrimiento, no numero exacto)
- Requiere calor: Blaze Burner, lava, o fuego debajo

## Crops por capitulo

| Cap | Crops nuevos | Total | Detalle |
|-----|-------------|-------|---------|
| 1 | 12 | 12 | Vanilla (trigo, papa, zanahoria, etc) + FD (tomate, cebolla, repollo, arroz) |
| 2 | +12 | 24 | Croptopia basicos: lettuce, corn, strawberry, blueberry, grape, etc |
| 3 | +10 | 34 | Frutas: banana, mango, lemon, orange, pineapple, etc |
| 4 | +8 | 42 | Hierbas/especias: basil, cinnamon, nutmeg, turmeric, vanilla, etc |
| 5 | +8 | 50 | Exoticos: dragon fruit, star fruit, avocado, kiwi, fig, etc |
| 6 | +8 | 58 | Avanzados: artichoke, asparagus, eggplant, leek, coffee beans, etc |
| 7 | +6 | 64 | Raros: saguaro, kumquat, persimmon, nectarine, etc |
| 8 | Todos | 70+ | Todos los restantes desbloqueados |

## Recetas por capitulo

| Cap | Workstations nuevas | Recetas aprox |
|-----|--------------------|-|
| 1 | Cutting Board, Cooking Pot, Stove, Skillet (FD) | ~25 |
| 2 | Blender, Moldes de Postres + B&C Keg | +30 |
| 3 | Drink Maker, Horno Avanzado + Expanded Delight | +25 |
| 4 | FD Feasts + recetas con especias | +20 |
| 5 | Slice&Dice (auto-cocina) + recetas exoticas | +15 |
| 6 | Recetas avanzadas con ingredientes raros | +10 |
| 7 | Recetas con ingredientes de dungeon | +10 |
| 8 | Recetas legendarias que combinan todo | +10 |

## FTB Quests: capitulo "Recetario" (milestones de variedad)

> Reemplaza Spice of Life: Onion. FTB Quests trackea las comidas cocinadas via quest triggers.
> Los corazones bonus se otorgan como quest rewards (no automaticamente por el mod).

| Comidas unicas | Corazones bonus | Capitulo esperado |
|----------------|----------------|-------------------|
| 5 | +1 | Ch1 |
| 15 | +2 | Ch1-2 |
| 30 | +3 | Ch2-3 |
| 50 | +4 | Ch3-4 |
| 70 | +5 | Ch5 |
| 85 | +6 | Ch6 |
| 100 | +8 | Ch7 |
| 130 | +10 (max 20 hearts) | Ch8 |

## Bridge con Nether

Ver [Nether](nether.md) — 7+ recetas de cocina usan ingredientes del Nether (Brewin', FD, Croptopia).
