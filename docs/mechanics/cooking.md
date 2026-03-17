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
| FTB Quests (Recetario) | Variedad = corazones permanentes via quest rewards | Siempre (capitulo propio en quest book) |
| Slice & Dice | Automatizar cocina con Create | Ch3 |

## servo_cooking (4 workstations custom)

Las 4 workstations de servo_cooking estan definidas. Cadenas completas de sub-ingredientes documentadas en [docs/design/cooking-recipe-chains.md](../design/cooking-recipe-chains.md).

| Workstation | Mecanica | Capitulo | Recetas | Create compat |
|-------------|----------|----------|---------|---------------|
| **Prep Station** | Ensamblaje frio (4 slots → 1 output) | Ch3 | ~57 | Deployer en secuencia |
| **Licuadora** | Mezcla ingredientes + liquido (3 seg) | Ch3 | ~23 | Basin + Mixer |
| **Wok** | Freidora con control activo de temperatura | Ch4 | ~21 | Deployer + Blaze Burner |
| **Baker's Oven** | Horno multislot (4 slots + modo) | Ch4 | ~46 | Mixer Heated |

**Congelados (helados, 7 recetas)**: van al **Freezer de MrCrayfish Refurbished** (bloque ya en el modpack). NO requieren 5ta workstation. Issue #71 para recetas KubeJS.

**Create como gateway**: flour, butter, olive_oil, chocolate, whipping_cream se producen en maquinas Create (Millstone, Press, Mixer), forzando el puente entre los pilares Cocina y Automatizacion a partir de Ch3-Ch4.

**Slice&Dice como bridge Create↔Cocina** (Ch3): Slice&Dice automatiza Cutting Board y Cooking Pot de Farmer's Delight con maquinas Create (Slicer → Sprinkler). Las workstations de servo_cooking deben seguir el mismo patron: runtime recipe injection permite que Deployer, Basin u otras maquinas Create lean y ejecuten recetas custom sin depender de Create directamente. Esto hace que servo_cooking sea standalone (0 deps) pero Create-compatible via injection en runtime.

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
