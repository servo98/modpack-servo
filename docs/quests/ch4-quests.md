# Capitulo 4: Horizontes + Especializacion — Quests Detallados

> Tema: Create brass completo (Mixer, Deployer, Crafter, Steam Engine, Trenes basicos). Especias nuevas. Feasts de Farmer's Delight. Macaw's completo. Skill Tree: el jugador elige su especializacion (Berserker, Deadeye, Forcemaster, etc.). Tier 2 RPG. El jugador enfrenta a la Locomotora Fantasma.

## Quests

### Historia/Tutorial (12)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 1 | Create: el Mixer | Historia | `item:create:mechanical_mixer` | 3 PC | - |
| 2 | Create: el Deployer | Historia | `item:create:deployer` | 3 PC | - |
| 3 | Mechanical Crafter | Historia | `item:create:mechanical_crafter` | 2 PC | - |
| 4 | Steam Engine | Historia | `item:create:steam_engine?` | 3 PC | - |
| 5 | Trenes: vias y locomotora | Historia | `item:create:track` | 2 PC | #4 |
| 6 | Skill Tree: especializate | Historia | `custom:elegir especializacion en Skill Tree` | 3 PC | - |
| 7 | Equipo Tier 2 | Historia | `custom:craftear primera pieza T2 (Diamond tier)` | 3 PC | #6 |
| 8 | Farmer's Delight: Feasts | Historia | `item:farmersdelight:roast_chicken_block?` | 2 PC | - |
| 9 | Macaw's: decoracion completa | Historia | `item:mcwfurnitures:oak_chair?` | 2 PC | - |
| 10 | Minas de Tanzanite y Topaz | Historia | `custom:encontrar ore de Tanzanite o Topaz` | 2 PC | - |
| 11 | Soulbound: protege tu equipo | Historia | `custom:tener item T2+ con Soulbound` | 2 PC | #7 |
| 12 | Primer viaje en tren | Historia | `custom:viajar en tren de Create` | 2 PC | #5 |

### Cocina (8)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 13 | Primer Feast | Cocina | `item:farmersdelight:roast_chicken_block?` | 3 PC | #8 |
| 14 | 3 Feasts diferentes | Cocina | `custom:preparar 3 Feasts de FD diferentes` | 4 PC | #13 |
| 15 | Cocinar con especias | Cocina | `custom:usar especia en receta de cocina` | 2 PC | - |
| 16 | Feast compartido | Cocina | `custom:compartir un Feast con otro jugador (multiplayer)` | 3 PC | #13 |
| 17 | Mixer para cocina | Cocina | `custom:usar Mixer de Create para receta de cocina` | 3 PC | #1 |
| 18 | Recetario: 50 comidas | Cocina | `custom:comer 50 comidas diferentes (hito Recetario)` | 5 PC + 4 corazones extra | - |
| 19 | Deployer: empaque auto | Cocina | `custom:usar Deployer para automatizar empaque` | 3 PC | #2 |
| 20 | Receta de 3+ especias | Cocina | `custom:preparar receta usando 3 o mas especias` | 3 PC | #15 |

### Farming (6)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 21 | 4 especias nuevas | Farming | `custom:plantar 4 especias de Croptopia` | 2 PC | - |
| 22 | Todas las especias | Farming | `custom:plantar las 8 especias nuevas` | 4 PC | #21 |
| 23 | Granja auto con Create | Farming | `custom:automatizar granja con Create Harvester + belts` | 4 PC | - |
| 24 | Plantacion de vainilla | Farming | `item:croptopia:vanilla x16` | 2 PC | #21 |
| 25 | Produccion en masa | Farming | `custom:producir 64+ de un solo crop en batch automatico` | 3 PC | #23 |
| 26 | Granja al Deployer | Farming | `custom:conectar granja a Deployer para empaque` | 2 PC | #2, #23 |

### Dungeon (5)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 27 | 5 runs de dungeon | Dungeon | `custom:completar 5 dungeon runs (cualquier tier)` | 3 PC | - |
| 28 | Unique jewelry Avanzada | Dungeon | `custom:obtener unique jewelry de dungeon Avanzada` | 5 PC | - |
| 29 | Farmear Esencia x3 | Dungeon | `item:servo_dungeons:dungeon_essence? x3` | 3 PC | - |
| 30 | Preparar gear para dungeon | Dungeon | `custom:entrar a dungeon con gear T2 completo` | 2 PC | #7 |
| 31 | Dungeon con T2 | Dungeon | `custom:completar dungeon Avanzada con gear T2` | 4 PC | #30 |

### Combate/RPG (5)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 32 | Especializacion elegida | Combate | `custom:elegir especializacion en Skill Tree` | 2 PC | #6 |
| 33 | Set T2 completo | Combate | `custom:craftear set T2 completo (arma + 4 armor)` | 5 PC | #7 |
| 34 | Spells de especializacion | Combate | `custom:usar spell de especializacion (Skill Tree)` | 2 PC | #32 |
| 35 | Tanzanite o Topaz jewelry | Combate | `custom:equipar Tanzanite o Topaz ring/necklace` | 2 PC | #10 |
| 36 | **GATE: Locomotora Fantasma** | Combate | `kill:servo_dungeons:locomotora_fantasma` | 10 PC + Senal Fantasma | #33 |

### Exploracion (5)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 37 | Conducir tren manual | Exploracion | `custom:conducir tren manualmente por 200+ bloques` | 2 PC | #12 |
| 38 | Ruta de tren | Exploracion | `custom:establecer ruta de tren con schedule` | 3 PC | #37 |
| 39 | Explorar con tren | Exploracion | `custom:explorar bioma nuevo usando tren` | 2 PC | #38 |
| 40 | Ores de Tanzanite/Topaz | Exploracion | `item:jewelry:tanzanite? x2` | 2 PC | #10 |
| 41 | 3 villages con NPCs | Exploracion | `custom:visitar 3 villages diferentes con NPCs` | 2 PC | - |

### Construccion (4)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 42 | Estacion de tren | Construccion | `item:create:track_station?` | 3 PC | #5 |
| 43 | Steam Engine instalado | Construccion | `custom:instalar Steam Engine funcional` | 2 PC | #4 |
| 44 | Decorar con Macaw's | Construccion | `custom:colocar 10+ bloques de Macaw's Furniture` | 2 PC | #9 |
| 45 | Fabrica con Crafter | Construccion | `custom:construir linea con Mechanical Crafter funcional` | 3 PC | #3 |

### Coleccion (5)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 46 | Diamond ring/necklace | Coleccion | `custom:equipar Diamond ring o necklace` | 2 PC | - |
| 47 | 5 especias coleccionadas | Coleccion | `custom:tener 5 especias diferentes en inventario` | 3 PC | #21 |
| 48 | Primer accesorio T2 | Coleccion | `custom:obtener accesorio custom T2` | 3 PC | - |
| 49 | 10 pulls de gacha | Coleccion | `custom:hacer 10 pulls totales en cualquier maquina` | 2 PC | - |
| 50 | **GATE: Entrega al Space Elevator Ch4** | Coleccion | `custom:completar entrega Ch4 en Terminal y presionar LAUNCH` | 8 PC | #36 |

## Resumen

| Metrica | Valor |
|---------|-------|
| Total quests | 50 |
| Total PC | 149 |
| Quests GATE | #36 (Boss), #50 (Space Elevator) |

### Distribucion por categoria

| Categoria | Quests | PC |
|-----------|--------|-----|
| Historia/Tutorial | 12 | 29 |
| Cocina | 8 | 26 |
| Farming | 6 | 17 |
| Dungeon | 5 | 17 |
| Combate/RPG | 5 | 21 |
| Exploracion | 5 | 11 |
| Construccion | 4 | 10 |
| Coleccion | 5 | 18 |
| **Total** | **50** | **149** |

> Nota: Momento clave de especializacion (Skill Tree). PC dentro de rango ~150. El boss (Locomotora Fantasma) es el primer boss de 3+ fases.
