# Quest Preview — Modpack Servo

> Indice de quests detallados por capitulo. ~400 quests totales que guian al jugador a traves de los 8 capitulos del modpack.

## Capitulos

| Cap | Nombre | Archivo | Quests | PC Total | Tema principal |
|-----|--------|---------|--------|----------|----------------|
| 1 | Raices | [ch1-quests.md](ch1-quests.md) | 50 | 144 | Tutorial, Farmer's Delight, primer dungeon, Guardian del Bosque |
| 2 | La Cocina + Clase Melee | [ch2-quests.md](ch2-quests.md) | 50 | 145 | Croptopia, Blender, Keg, Create basico, Rogue/Warrior, Bestia Glotona |
| 3 | Engranajes + Magia | [ch3-quests.md](ch3-quests.md) | 50 | 149 | Create andesite, Drink Maker, Horno Avanzado, Nether, Wizard/Paladin/Priest, Coloso Mecanico |
| 4 | Horizontes + Especializacion | [ch4-quests.md](ch4-quests.md) | 50 | 149 | Create brass, trenes, Skill Tree, Feasts, Locomotora Fantasma |
| 5 | La Red + Poder Magico | [ch5-quests.md](ch5-quests.md) | 50 | 152 | Refined Storage, Enchantment Industry, Llave Maestra, El Arquitecto |
| 6 | Maestria + Netherite RPG | [ch6-quests.md](ch6-quests.md) | 50 | 152 | T3 Netherite, enchant V, recetas multi-step, Senor de las Cosechas |
| 7 | Profundidades + Coleccion | [ch7-quests.md](ch7-quests.md) | 50 | 152 | Llave del Nucleo, boss de dungeon, unique jewelry, Nucleo del Dungeon |
| 8 | El Final + Legendarios | [ch8-quests.md](ch8-quests.md) | 50 | 159 | T4 legendario, completismo, mega-entregas, Devorador de Mundos |

**Total global: 400 quests, ~1,202 PC de quests**

## Principios de diseno

Fuente: [mechanics/quests.md](../mechanics/quests.md)

1. **Cada quest tiene un objetivo concreto** — nada de "explora biomas por explorar"
2. **Rewards incluyen Pepe Coins** (2-3 PC facil, 4-5 PC medio, 8-10 PC dificil/boss)
3. **Quest book es la guia principal** del jugador
4. **FTB Teams**: progresion compartida en multiplayer
5. **~150 PC base por capitulo** con variacion segun sinks disponibles (mas maquinas gacha = mas PC)

## Categorias

Cada capitulo contiene quests de las mismas 8 categorias:

| Categoria | Quests/Cap | Descripcion |
|-----------|-----------|-------------|
| Historia/Tutorial | ~10-12 | Mecanicas nuevas del capitulo, tutoriales guiados |
| Cocina | ~8 | Recetas, workstations, variedad de comidas |
| Farming | ~6 | Crops nuevos, granjas, automatizacion agricola |
| Dungeon | ~5 | Runs, loot, llaves, supervivencia |
| Combate/RPG | ~5 | Clases, gear, spells, boss del capitulo |
| Exploracion | ~4-5 | Biomas, Waystones, NPCs, transporte |
| Construccion | ~4 | Base, cocina, fabrica, decoracion |
| Coleccion | ~5-7 | Gacha, unique jewelry, Recetario, Space Elevator |

## Quests GATE (obligatorios para avanzar)

Cada capitulo tiene exactamente 2 quests GATE que deben completarse para desbloquear el capitulo siguiente:

| Cap | GATE 1: Boss | GATE 2: Space Elevator |
|-----|-------------|----------------------|
| 1 | Guardian del Bosque (800 HP) | Entrega Ch1 (25 items) |
| 2 | Bestia Glotona (1,200 HP) | Entrega Ch2 (35 items) |
| 3 | Coloso Mecanico (1,600 HP) | Entrega Ch3 (41 items) |
| 4 | Locomotora Fantasma (2,400 HP) | Entrega Ch4 (40 items) |
| 5 | El Arquitecto (3,400 HP) | Entrega Ch5 (51 items) |
| 6 | Senor de las Cosechas (5,000 HP) | Entrega Ch6 (60 items) |
| 7 | Nucleo del Dungeon (7,200 HP) | Entrega Ch7 (66 items) |
| 8 | Devorador de Mundos (10,400 HP) | Entrega final Ch8 (90 items) |

Las condiciones pueden cumplirse en cualquier orden. Ambas son necesarias.

## Tokens: economia por capitulo

| Cap | PC de quests | PC de boss | PC de dungeon | PC de champions | Total estimado |
|-----|-------------|-----------|---------------|----------------|---------------|
| 1 | ~144 | 15-25 | 5-10/run | 1-3/kill | ~190+ |
| 2 | ~145 | 25-35 | 5-10/run | 1-3/kill | ~210+ |
| 3 | ~149 | 30-45 | 10-20/run | 1-3/kill | ~250+ |
| 4 | ~149 | 35-50 | 10-20/run | 1-3/kill | ~270+ |
| 5 | ~152 | 40-60 | 20-35/run | 1-3/kill | ~310+ |
| 6 | ~152 | 50-70 | 20-35/run | 1-3/kill | ~330+ |
| 7 | ~152 | 60-80 | 35-50/run | 1-3/kill | ~370+ |
| 8 | ~159 | 100+ | 35-50/run | 1-3/kill | ~380+ |

> Los PC de dungeon y champions son variables y dependen de cuantas runs hace el jugador. Los totales estimados asumen un jugador activo que hace 3-5 runs de dungeon por capitulo.

## Convenciones de deteccion

| Formato | Significado | Ejemplo |
|---------|-------------|---------|
| `item:mod:item_name` | Jugador tiene/craftea item | `item:farmersdelight:cooking_pot` |
| `item:mod:item_name xN` | Jugador tiene N de item | `item:minecraft:blaze_rod x4` |
| `kill:mod:entity_name` | Matar entidad | `kill:servo_dungeons:guardian_del_bosque` |
| `location:dimension:coords` | Visitar lugar | `location:minecraft:overworld:village?` |
| `stage:servo_chN` | Tener stage de progresion | `stage:servo_ch2` |
| `custom:descripcion` | Trigger custom via KubeJS/FTB Quests | `custom:completar dungeon Basica` |
| `observation:descripcion` | Quest manual/checkbox | `observation:entender mecanica` |

> Los IDs marcados con `?` son estimaciones que deben verificarse contra los registros reales del mod en cuestion.

## Recetario (FTB Quests — capitulo especial)

El Recetario es un capitulo aparte en el Quest Book que trackea comidas unicas. Los hitos otorgan corazones bonus como quest rewards:

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
