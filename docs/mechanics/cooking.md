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

## Crops y recetas por capitulo

Tablas completas de crops, workstations y recetas por capitulo: ver [progression.md → Distribucion](progression.md#distribucion-de-contenido-por-capitulo).

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
