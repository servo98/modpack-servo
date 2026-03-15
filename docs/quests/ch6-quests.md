# Capitulo 6: Maestria + Netherite RPG — Quests Detallados

> Tema: Optimizacion total. Recetas avanzadas multi-step con ingredientes raros. Tier 3 Netherite para todo el equipo RPG (smithing_transform). Enchant nivel V. Tipped Arrows de Spell Power. Trenes full auto. No hay mods nuevos: todo se desbloquea y se mejora. El jugador enfrenta al Senor de las Cosechas.

## Quests

### Historia/Tutorial (10)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 1 | Upgrade Netherite: RPG | Historia | `custom:aplicar Netherite Upgrade Smithing Template a arma RPG` | 3 PC | - |
| 2 | Enchant nivel V | Historia | `custom:aplicar enchant de nivel V a una pieza de equipo` | 3 PC | - |
| 3 | Tipped Arrows de Spell Power | Historia | `custom:craftear Tipped Arrows de Spell Power` | 2 PC | - |
| 4 | Trenes full auto | Historia | `custom:configurar tren full automatico con schedule completo` | 3 PC | - |
| 5 | Recetas multi-step | Historia | `custom:completar receta que requiere 3+ workstations` | 3 PC | - |
| 6 | Cristal de Boss Ch6 | Historia | `observation:entender que el Cristal de Boss Ch6 desbloquea Llave del Nucleo` | 2 PC | - |
| 7 | Preparar Llave del Nucleo | Historia | `observation:entender requisitos de la Llave del Nucleo (proximo capitulo)` | 2 PC | #6 |
| 8 | Optimizar fabrica | Historia | `custom:optimizar throughput de fabrica existente` | 2 PC | - |
| 9 | Full build RPG | Historia | `custom:tener build RPG completo (arma + armor + rings + runes)` | 3 PC | - |
| 10 | Receta encadenada | Historia | `custom:completar receta que encadena 3+ pasos de procesamiento` | 2 PC | - |

### Cocina (8)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 11 | Receta multi-step | Cocina | `custom:completar receta multi-step (3+ workstations)` | 3 PC | #5 |
| 12 | Ingredientes raros | Cocina | `custom:cocinar con ingrediente raro (crops avanzados)` | 3 PC | - |
| 13 | Cafe preparado | Cocina | `custom:preparar cafe con coffee beans en Drink Maker` | 2 PC | - |
| 14 | Aceite de oliva | Cocina | `custom:preparar plato con olive oil` | 2 PC | - |
| 15 | Recetario: 85 comidas | Cocina | `custom:comer 85 comidas diferentes (hito Recetario)` | 5 PC + 6 corazones extra | - |
| 16 | 3 workstations en 1 receta | Cocina | `custom:usar 3 workstations en una sola receta` | 3 PC | #5 |
| 17 | Automatizar receta multi-step | Cocina | `custom:automatizar receta multi-step con RS + Create` | 4 PC | #16 |
| 18 | Pipeline completo | Cocina | `custom:produccion masiva con pipeline completo funcionando` | 3 PC | #17 |

### Farming (6)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 19 | 4 crops avanzados | Farming | `custom:plantar 4 crops avanzados` | 2 PC | - |
| 20 | Todos los avanzados | Farming | `custom:plantar los 8 crops avanzados` | 4 PC | #19 |
| 21 | Granja de coffee beans | Farming | `item:croptopia:coffee_beans? x32` | 2 PC | #19 |
| 22 | Full auto farm pipeline | Farming | `custom:farm -> RS -> Create -> empaque funcionando` | 4 PC | - |
| 23 | Rendimiento optimizado | Farming | `custom:optimizar rendimiento de granja (128+/cosecha)` | 3 PC | #22 |
| 24 | Ingredientes raros en masa | Farming | `custom:producir ingredientes raros en masa (32+)` | 2 PC | #20 |

### Dungeon (5)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 25 | 5 runs Maestra | Dungeon | `custom:completar 5 dungeon Maestra runs` | 4 PC | - |
| 26 | Farmear unique jewelry (15%) | Dungeon | `custom:obtener unique jewelry de dungeon Maestra` | 4 PC | #25 |
| 27 | Esencia x6 | Dungeon | `item:servo_dungeons:dungeon_essence? x6` | 3 PC | #25 |
| 28 | Accesorio T3+ | Dungeon | `custom:obtener accesorio custom T3 o superior de dungeon` | 3 PC | #25 |
| 29 | Materiales Llave del Nucleo | Dungeon | `custom:tener materiales para craftear Llave del Nucleo` | 4 PC | #27 |

### Combate/RPG (5)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 30 | Arma Netherite (T3) | Combate | `custom:upgrade arma RPG a Netherite` | 3 PC | #1 |
| 31 | Set Netherite completo | Combate | `custom:upgrade set completo RPG a Netherite (5 piezas)` | 4 PC | #30 |
| 32 | Enchant nivel V | Combate | `custom:encantar pieza con nivel V` | 3 PC | #2 |
| 33 | Tipped Arrows Spell Power | Combate | `item:minecraft:tipped_arrow? x16` | 2 PC | #3 |
| 34 | **GATE: Senor de las Cosechas** | Combate | `kill:servo_dungeons:senor_de_las_cosechas` | 10 PC + Hoz del Senor + Cristal de Boss Ch6 | #31 |

### Exploracion (5)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 35 | Tren full auto Nether | Exploracion | `custom:tren full automatico circulando en Nether` | 3 PC | #4 |
| 36 | Fabrica Nether optimizada | Exploracion | `custom:fabrica Nether con produccion optimizada` | 2 PC | - |
| 37 | Logistica automatizada | Exploracion | `custom:logistica automatizada completa (overworld + Nether)` | 3 PC | #35 |
| 38 | Biomas restantes | Exploracion | `custom:explorar biomas restantes de Terralith` | 2 PC | - |
| 39 | Red de Waystones completa | Exploracion | `custom:red de Waystones cubriendo todas las areas principales` | 2 PC | - |

### Construccion (4)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 40 | Layout de fabrica optimizado | Construccion | `custom:optimizar layout de fabrica (menos belts, mas throughput)` | 3 PC | - |
| 41 | Trenes full auto setup | Construccion | `custom:setup de trenes full auto con multiples estaciones` | 2 PC | #4 |
| 42 | Linea de items complejos | Construccion | `custom:linea de produccion para items complejos (multi-step)` | 3 PC | - |
| 43 | Base completa | Construccion | `custom:base con todas las areas funcionales (cocina, fabrica, almacen, decoracion)` | 3 PC | - |

### Coleccion (7)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 44 | Set T3 Netherite completo | Coleccion | `custom:tener set completo T3 Netherite RPG` | 2 PC | #31 |
| 45 | Full enchant nivel V | Coleccion | `custom:tener set completo enchantado con nivel V` | 3 PC | #32 |
| 46 | 5+ unique jewelry | Coleccion | `custom:coleccion de 5 o mas unique jewelry` | 3 PC | - |
| 47 | 85+ comidas unicas | Coleccion | `custom:85+ comidas en Recetario` | 2 PC | #15 |
| 48 | Acumular Esencia | Coleccion | `item:servo_dungeons:dungeon_essence? x10` | 2 PC | - |
| 49 | Cristal de Boss guardado | Coleccion | `item:servo_dungeons:boss_crystal_ch6?` | 2 PC | #34 |
| 50 | **GATE: Entrega al Space Elevator Ch6** | Coleccion | `custom:completar entrega Ch6 en Terminal y presionar LAUNCH` | 8 PC | #34 |

## Resumen

| Metrica | Valor |
|---------|-------|
| Total quests | 50 |
| Total PC | 152 |
| Quests GATE | #34 (Boss), #50 (Space Elevator) |

### Distribucion por categoria

| Categoria | Quests | PC |
|-----------|--------|-----|
| Historia/Tutorial | 10 | 25 |
| Cocina | 8 | 25 |
| Farming | 6 | 17 |
| Dungeon | 5 | 18 |
| Combate/RPG | 5 | 22 |
| Exploracion | 5 | 12 |
| Construccion | 4 | 11 |
| Coleccion | 7 | 22 |
| **Total** | **50** | **152** |

> Nota: PC exactamente en rango ~150. Capitulo de optimizacion sin mods nuevos — recompensa dominio de sistemas existentes.
