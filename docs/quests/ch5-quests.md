# Capitulo 5: La Red + Poder Magico — Quests Detallados

> Tema: Refined Storage completo (Controller, Disks, Grids, Autocraft, Wireless). Trenes avanzados con scheduling. Create C&A (motor electrico). Create Enchantment Industry. Llave Maestra de dungeon. Enchants magicos y pociones Spell Power. Gacha Superior (Purpura). Netherite jewelry. El jugador enfrenta a El Arquitecto.

## Quests

### Historia/Tutorial (12)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 1 | Refined Storage: Controller | Historia | `item:refinedstorage:controller` | 3 PC | - |
| 2 | RS: Disk Drive y Grid | Historia | `item:refinedstorage:disk_drive` | 2 PC | #1 |
| 3 | RS: Autocraft (Pattern + Crafter) | Historia | `item:refinedstorage:crafter` | 3 PC | #2 |
| 4 | RS: Wireless Grid | Historia | `item:refinedstorage:wireless_grid` | 2 PC | #2 |
| 5 | Create: Enchantment Industry | Historia | `item:create_enchantment_industry:disenchanter?` | 2 PC | - |
| 6 | Create C&A: Motor electrico | Historia | `item:createaddition:electric_motor?` | 2 PC | - |
| 7 | Trenes automaticos | Historia | `custom:configurar schedule de tren automatico` | 2 PC | - |
| 8 | Llave Maestra de dungeon | Historia | `item:servo_dungeons:master_key?` | 3 PC | - |
| 9 | Enchants magicos | Historia | `custom:aplicar enchant de Spell Power a un item` | 2 PC | - |
| 10 | Pociones de Spell Power | Historia | `custom:brewear pocion de Spell Power` | 2 PC | - |
| 11 | Soul y Lightning runes | Historia | `item:runes:soul_rune?` | 2 PC + Gacha Machine Purpura | - |
| 12 | Large Rune Pouch | Historia | `item:runes:large_rune_pouch?` | 2 PC | #11 |

### Cocina (8)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 13 | Cocinar con exoticos | Cocina | `custom:cocinar receta con ingrediente exotico Ch5` | 3 PC | - |
| 14 | 3 recetas exoticas | Cocina | `custom:preparar 3 recetas con ingredientes exoticos` | 3 PC | #13 |
| 15 | Automatizar con RS + Create | Cocina | `custom:automatizar receta usando RS + Create juntos` | 4 PC | #1 |
| 16 | Produccion masa: Slice&Dice + RS | Cocina | `custom:produccion en masa con Slice&Dice alimentado por RS` | 3 PC | #15 |
| 17 | Recetario: 70 comidas | Cocina | `custom:comer 70 comidas diferentes (hito Recetario)` | 5 PC + 5 corazones extra | - |
| 18 | Empaque automatizado | Cocina | `custom:automatizar empaque completo (RS -> Create -> Packing Station)` | 3 PC | #15 |
| 19 | Pipeline: RS a Space Elevator | Cocina | `custom:pipeline RS -> Create -> Puerto de Entrega funcionando` | 4 PC | #18 |
| 20 | Tipped Arrows de cocina | Cocina | `custom:crear Tipped Arrows con efecto de comida (si aplica)` | 2 PC | - |

### Farming (6)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 21 | 4 crops exoticos | Farming | `custom:plantar 4 crops exoticos de Croptopia Ch5` | 2 PC | - |
| 22 | Todos los exoticos | Farming | `custom:plantar los 8 crops exoticos` | 4 PC | #21 |
| 23 | Granja full auto con RS | Farming | `custom:granja automatizada con RS imports` | 3 PC | #1 |
| 24 | Conectar granjas a RS | Farming | `custom:conectar granjas a red RS con exporters/importers` | 3 PC | #23 |
| 25 | Matar al Wither | Farming | `kill:minecraft:wither` | 5 PC | - |
| 26 | Optimizar produccion | Farming | `custom:optimizar produccion de crops (64+/min de algun crop)` | 2 PC | #23 |

### Dungeon (5)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 27 | Primera Llave Maestra | Dungeon | `item:servo_dungeons:master_key?` | 3 PC | #8 |
| 28 | Dungeon Maestra completa | Dungeon | `custom:completar dungeon Maestra` | 5 PC | #27 |
| 29 | Accesorio T3 de dungeon | Dungeon | `custom:obtener accesorio custom T3 de dungeon Maestra` | 3 PC | #28 |
| 30 | Unique jewelry en Maestra | Dungeon | `custom:obtener unique jewelry en dungeon Maestra (15%)` | 4 PC | #28 |
| 31 | Molde 4-5 estrellas | Dungeon | `custom:obtener molde de postre 4-5 estrellas en Maestra` | 3 PC | #28 |

### Combate/RPG (5)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 32 | Enchant magico en arma | Combate | `custom:aplicar enchant magico (Spell Power) a arma RPG` | 3 PC | #9 |
| 33 | Pocion de Spell Power | Combate | `custom:beber pocion de Spell Power` | 2 PC | #10 |
| 34 | Auto-enchantar | Combate | `custom:auto-enchantar pieza de equipo con Enchantment Industry` | 3 PC | #5 |
| 35 | Netherite jewelry | Combate | `custom:equipar Netherite jewelry` | 2 PC | - |
| 36 | **GATE: El Arquitecto** | Combate | `kill:servo_dungeons:el_arquitecto` | 10 PC + Nucleo del Arquitecto | #32 |

### Exploracion (5)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 37 | Ruta de tren automatica | Exploracion | `custom:tren automatico circulando con schedule` | 3 PC | #7 |
| 38 | Tren Nether-Overworld | Exploracion | `custom:establecer ruta de tren semi-auto Nether-Overworld` | 3 PC | #37 |
| 39 | Fabrica en Nether | Exploracion | `custom:tener fabrica funcionando en el Nether` | 2 PC | - |
| 40 | Conectar RS entre areas | Exploracion | `custom:conectar RS entre dos areas separadas` | 2 PC | #1 |
| 41 | Explorar con Wireless Grid | Exploracion | `custom:usar Wireless Grid lejos de la base` | 2 PC | #4 |

### Construccion (4)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 42 | Sala RS con Controller | Construccion | `custom:construir sala dedicada RS con Controller + Disk Drive` | 3 PC | #1 |
| 43 | Setup Enchantment Industry | Construccion | `custom:instalar setup de Enchantment Industry funcional` | 2 PC | #5 |
| 44 | Linea RS -> Create | Construccion | `custom:construir linea de produccion RS -> Create integrada` | 3 PC | #1 |
| 45 | Fabrica integrada | Construccion | `custom:fabrica integrada completa (RS + Create + empaque)` | 3 PC | #44 |

### Coleccion (5)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 46 | 3 piezas auto-enchantadas | Coleccion | `custom:auto-enchantar 3 piezas de equipo` | 2 PC | #5 |
| 47 | Set con enchants magicos | Coleccion | `custom:tener set completo con enchants magicos` | 3 PC | #9 |
| 48 | 3 unique jewelry | Coleccion | `custom:coleccion de 3 unique jewelry` | 3 PC | - |
| 49 | Pipeline automatizado | Coleccion | `custom:tener pipeline RS -> Create -> empaque -> Terminal funcionando` | 2 PC | #19 |
| 50 | **GATE: Entrega al Space Elevator Ch5** | Coleccion | `custom:completar entrega Ch5 en Terminal y presionar LAUNCH` | 8 PC | #36 |

## Resumen

| Metrica | Valor |
|---------|-------|
| Total quests | 50 |
| Total PC | 152 |
| Quests GATE | #36 (Boss), #50 (Space Elevator) |

### Distribucion por categoria

| Categoria | Quests | PC |
|-----------|--------|-----|
| Historia/Tutorial | 12 | 27 |
| Cocina | 8 | 27 |
| Farming | 6 | 19 |
| Dungeon | 5 | 18 |
| Combate/RPG | 5 | 20 |
| Exploracion | 5 | 12 |
| Construccion | 4 | 11 |
| Coleccion | 5 | 18 |
| **Total** | **50** | **152** |

> Nota: PC ligeramente sobre 150 dado el alto sink de Gacha Superior (15 PC/pull). Capitulo de industrializacion y escala.
