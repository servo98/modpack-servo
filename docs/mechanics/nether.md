# Nether

> Fuente: GDD v2, seccion 3.7
> Mods: Create (trenes cross-dimensional), YUNG's Better Nether Fortresses
> Relacionado: [Create Automation](create-automation.md), [Cooking](cooking.md), [RPG Classes](rpg-classes.md), [Champions](champions.md)

## Overview

Nether accesible desde Ch3. Bridge natural entre cocina (ingredientes Nether) y automatizacion (trenes). Champions spawn 15%, max 2 affixes.

## Trenes cross-dimensional (Create built-in)

- Tracks van directo al portal → track fantasma del otro lado
- Requisito: alguien cruzo el portal manualmente una vez
- Trenes viajan por chunks NO cargados (grafo abstracto)
- Solo estaciones necesitan chunks cargados (FTB Chunks)

## Procesamiento en Nether con Create

| Proceso | Input | Output | Para que |
|---------|-------|--------|----------|
| Lava Power | Hose Pulley en lago | Energia masiva (Steam Engine) | Alimentar fabrica Nether |
| Gold Processing | Nether Gold + Crushing Wheels | Gold + Quartz | Materiales, jewelry |
| Blaze Farm | Spawner + mechanical arms | Blaze Rods | Fuel, pociones, RPG |
| Nether Wart Farm | Mechanical Bearing + Harvesters | Nether Wart | Cocina, pociones |
| Basalt Generator | Lava + Soul Soil + Blue Ice | Basalt infinito | Construccion |

## Recetas de cocina con ingredientes Nether

| Receta | Mod | Ingredientes Nether |
|--------|-----|---------------------|
| Red Rum | Brewin' And Chewin' | Crimson Fungus + Nether Wart + Shroomlight |
| Steel-Toe Stout | Brewin' And Chewin' | Crimson Fungus + Nether Wart |
| Withering Dross | Brewin' And Chewin' | Wither Rose + Nether Wart |
| Glittering Grenadine | Brewin' And Chewin' | Glowstone Dust |
| Nether Salad | Farmer's Delight | Crimson/Warped Fungus |
| Nether Wart Stew | Croptopia | Nether Wart + Fungus |
| Nether Star Cake | Croptopia | Nether Star (Wither kill) |
| Netherite Knife | Farmer's Delight | Netherite Ingot |

## Flujo de transporte

```
NETHER:
  Granja automatizada → Create belts → procesamiento
  → carga en tren (Portable Storage Interface)

CRUCE:
  Tren atraviesa portal Nether (automatico o manual)

OVERWORLD:
  Tren llega a estacion → descarga
  → Create belts → fabrica principal / Space Elevator
```

## Entregas del Space Elevator con items Nether

A partir de Ch3, entregas incluyen items procesados del Nether → obliga a establecer ruta de transporte.

## YUNG's Better Nether Fortresses

Mod ligero: solo mejora fortalezas del Nether. No agrega NPCs ni contenido excesivo. Compatible con Create.
