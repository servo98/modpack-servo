# Sistema de Dungeons

> Fuente: GDD v2, seccion 3.5
> Relacionado: [Bosses](bosses.md), [Champions](champions.md), [Death System](death-system.md), [RPG Classes](rpg-classes.md), [Accessories](accessories.md), [Tokens](tokens.md)

## Overview

Sistema 100% custom en servo_core. Dimension void, generacion procedural de salas, 4 tiers de llave, multiplayer, roguelike (muerte = pierde loot de la run). **Dimensional Dungeons REMOVIDO** (licencia ARR).

## Llaves de Dungeon

### Reglas
- Cada entrada **consume 1 llave** (1 llave = 1 run)
- Llave Basica siempre crafteable barata → nunca te quedas sin acceso
- Primera llave gratis como quest reward en Ch1
- Llaves superiores: decision riesgo/recompensa

### Crafteo

| Llave | Materiales | Costo | Disponible |
|-------|-----------|-------|------------|
| Basica | 4 Iron + 2 Gold | Barato, infinito | Ch1 |
| Avanzada | 2 Diamond + 4 Blaze Rod + 1 Ender Pearl + drop boss Ch2 | Medio, requiere Nether | Ch3 |
| Maestra | 4 Diamond + 2 Netherite Scrap + 1 Nether Star frag + 1 Esencia de Dungeon | Alto, requiere dungeon previa | Ch5 |
| Del Nucleo | 2 Netherite Ingot + 4 Esencia de Dungeon + 1 Cristal de Boss Ch6 | Muy alto | Ch7 |

Cada tier necesita drop del tier anterior → **cadena de progresion natural**.

## Estructura por tier

| Llave | Salas | Duracion | Champions | Boss dungeon |
|-------|-------|----------|-----------|--------------|
| Basica | 5-7 | ~15-20 min | Max 1 affix, 15% spawn | No |
| Avanzada | 10-14 | ~30-40 min | Max 2 affix, 25% spawn | No |
| Maestra | 15-20 | ~45-60 min | Max 3 affix, 30% spawn | No |
| Del Nucleo | 20-25 | ~60-75 min | Max 3 affix + exclusivos, 35% spawn | Si |

## Loot por tier

### Basica (Ch1+)
| Drop | Fuente | Chance |
|------|--------|--------|
| Ores vanilla | cofres | Comun |
| Pepe Coins (5-10) | cofres + champions | Garantizado |
| Runas basicas | cofres | 30% |
| Enchanted Books I-II | cofres | 20% |
| Accesorio custom T1 | cofres raros | 10% |
| Molde postre 1-2 estrellas | cofres raros | 5% |

### Avanzada (Ch3+) — todo lo de Basica +
| Drop | Fuente | Chance |
|------|--------|--------|
| Pepe Coins (10-20) | cofres + champions | Garantizado |
| Materiales Nether | cofres | Comun |
| RPG gear T1-T2 | cofres raros | 15% |
| **Esencia de Dungeon** | champions | 15% por champion |
| Unique Jewelry | cofre final | 5% |

### Maestra (Ch5+) — todo lo de Avanzada +
| Drop | Fuente | Chance |
|------|--------|--------|
| Pepe Coins (20-35) | cofres + champions | Garantizado |
| Materiales raros (Netherite Scrap, Nether Star) | cofres | 15% |
| Accesorio custom T3 | cofres raros | 12% |
| Esencia de Dungeon (x2-3) | champions | 25% |
| Unique Jewelry | cofre final | 15% |
| Boss Key fragments | cofres raros | 8% |

### Del Nucleo (Ch7+) — todo lo de Maestra +
| Drop | Fuente | Chance |
|------|--------|--------|
| Pepe Coins (35-50) | cofres + champions | Garantizado |
| **Fragmento de Cristal del Nucleo** | boss dungeon | Garantizado (1-2) |
| Accesorio custom T4 | boss dungeon | 25% |
| **Unique Jewelry** | boss dungeon | **Garantizado 1** |

## Items exclusivos de dungeon

| Item | Tier minimo | Para que sirve |
|------|------------|----------------|
| Esencia de Dungeon | Avanzada | Craftear Llave Maestra y del Nucleo |
| Unique Jewelry (24 items) | Avanzada (5%) | Mejor equipo rings/necklaces |
| Fragmento de Cristal del Nucleo | Del Nucleo | RPG Tier 4 (endgame) |
| Accesorio custom T4-T5 | Maestra/Nucleo | Mejores accesorios |
| Boss Key fragments | Maestra | Reducir costo Boss Key |
| Moldes 4-5 estrellas | Maestra/Nucleo | Mejores moldes postres |

## Anti-skip de tiers

1. Esencia de Dungeon NO dropea en Basica
2. Llave Avanzada requiere drop de boss Ch2
3. Llave Maestra requiere Esencia
4. Llave del Nucleo requiere Cristal de Boss Ch6

## Puzzles con Supplementaries

- Pozos → necesitas Rope para bajar
- Paredes agrietadas → Bomb para abrir paso secreto
- Puertas cerradas → Key de Supplementaries
- Esto hace Supplementaries NECESARIO en dungeons

## Multiplayer

- Portal de Dungeon: 1 llave = todo el grupo entra
- Loot individual: Lootr genera loot por jugador
- Boss drops individuales por participante (como MMO)
- Si 2 jugadores matan boss, AMBOS reciben 1-2 Fragmentos independientemente

## Cuantos runs para T4 completo

- 1 pieza T4 = ~3 Fragmentos de Cristal del Nucleo
- Set completo (arma + 4 armor) = ~15 fragmentos
- Boss da 1-2 por kill → **~8-10 runs Del Nucleo**

## Salas

- 100+ templates .nbt
- 7 tipos: entrada, pasillo, esquina, T, cruz, dead-end, boss
- Generacion procedural en dimension void
- Limpieza de chunks cuando 0 jugadores quedan
