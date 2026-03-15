# Capitulo 2: La Cocina + Clase Melee — Quests Detallados

> Tema: Cocina expandida con Croptopia, Blender, Moldes y Keg. Create basico (Water Wheel, shafts, belts). Storage Drawers. Elegir clase melee (Rogue o Warrior). Gacha Rosa de muebles se desbloquea. PepeMart abre. El jugador enfrenta a la Bestia Glotona.

## Quests

### Historia/Tutorial (12)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 1 | Nuevos cultivos: Croptopia | Historia | `item:croptopia:lettuce` | 2 PC | - |
| 2 | La Batidora | Historia | `item:servo_cooking:blender` | 3 PC | #1 |
| 3 | Moldes de Postres | Historia | `item:servo_cooking:cake_mold_1star?` | 3 PC + Molde de Pastel 1 estrella | #2 |
| 4 | El Keg: fermentacion | Historia | `item:brewinandchewin:keg?` | 3 PC | #1 |
| 5 | Storage Drawers | Historia | `item:storagedrawers:oak_full_drawers_1` | 2 PC | - |
| 6 | Create: la Water Wheel | Historia | `item:create:water_wheel` | 3 PC | - |
| 7 | Create: belts y shafts | Historia | `item:create:mechanical_belt` | 2 PC | #6 |
| 8 | Elige tu clase melee | Historia | `custom:usar Class Book de Rogue o Warrior` | 3 PC | - |
| 9 | Arms Station | Historia | `item:rogues:arms_station?` | 2 PC | #8 |
| 10 | Gemas y Jewelry | Historia | `item:jewelry:citrine?` | 2 PC | - |
| 11 | Equipo Tier 1 | Historia | `custom:equipar arma T1 de clase` | 2 PC | #8 |
| 12 | Minas de Citrine y Jade | Historia | `item:jewelry:jade?` | 2 PC | #10 |

### Cocina (8)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 13 | Primer smoothie | Cocina | `custom:hacer un smoothie en el Blender` | 3 PC | #2 |
| 14 | Variedad de smoothies | Cocina | `custom:hacer 3 smoothies diferentes` | 3 PC | #13 |
| 15 | Postre con molde | Cocina | `custom:usar Molde de Postres para hacer un postre` | 3 PC | #3 |
| 16 | Queso del Keg | Cocina | `item:brewinandchewin:flaxen_cheese?` | 2 PC | #4 |
| 17 | Bebida fermentada | Cocina | `item:brewinandchewin:beer?` | 2 PC | #4 |
| 18 | Recetario: 15 comidas | Cocina | `custom:comer 15 comidas diferentes (hito Recetario)` | 5 PC + 2 corazones extra | #13 |
| 19 | Receta de Croptopia | Cocina | `item:croptopia:fruit_salad?` | 2 PC | #1 |
| 20 | Cajas de Queso | Cocina | `custom:empacar queso en Caja de Envio` | 3 PC | #16 |

### Farming (6)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 21 | 4 nuevos cultivos | Farming | `custom:plantar 4 crops Croptopia` | 2 PC | #1 |
| 22 | 8 cultivos plantados | Farming | `custom:plantar 8 crops Croptopia diferentes` | 3 PC | #21 |
| 23 | Los 12 de Croptopia | Farming | `custom:plantar los 12 crops nuevos de Croptopia` | 4 PC | #22 |
| 24 | Expansion agricola | Farming | `custom:expandir granja a 24+ crops plantados total` | 3 PC | #21 |
| 25 | Vinedo de uvas | Farming | `item:croptopia:grape x16` | 2 PC | #1 |
| 26 | Riego y cuidado | Farming | `custom:tener granja funcionando con agua adyacente` | 2 PC | #21 |

### Dungeon (5)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 27 | 3 runs basicas | Dungeon | `custom:completar 3 dungeon Basica runs` | 4 PC | - |
| 28 | Accesorio T1 de dungeon | Dungeon | `custom:obtener accesorio T1 de cofre de dungeon` | 3 PC | #27 |
| 29 | Runas de dungeon | Dungeon | `custom:obtener runas de cofre de dungeon` | 2 PC | #27 |
| 30 | 20 Pepe Coins de dungeons | Dungeon | `custom:acumular 20 PC solo de dungeon runs` | 3 PC | #27 |
| 31 | Molde en dungeon | Dungeon | `custom:encontrar molde de postre en dungeon Basica` | 3 PC | #27 |

### Combate/RPG (5)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 32 | Clase melee elegida | Combate | `custom:tener clase Rogue o Warrior activa` | 2 PC | #8 |
| 33 | Armadura T1 de clase | Combate | `custom:craftear set completo T1 de clase melee` | 4 PC | #32 |
| 34 | Arma T1 de clase | Combate | `custom:craftear arma T1 de clase (Iron Dagger/Glaive/etc)` | 3 PC | #32 |
| 35 | Runa de Fuego o Curacion | Combate | `item:runes:fire_rune?` | 2 PC | - |
| 36 | **GATE: Bestia Glotona** | Combate | `kill:servo_dungeons:bestia_glotona` | 10 PC + Mandibula de la Bestia | #33, #34 |

### Exploracion (5)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 37 | Minerales de Citrine | Exploracion | `custom:encontrar y minar ore de Citrine` | 2 PC | - |
| 38 | Minerales de Jade | Exploracion | `custom:encontrar y minar ore de Jade` | 2 PC | - |
| 39 | 3 biomas nuevos | Exploracion | `custom:visitar 3 biomas Terralith nuevos` | 2 PC | - |
| 40 | Red de Waystones | Exploracion | `custom:colocar 3 Waystones` | 3 PC | - |
| 41 | Mercader de armas | Exploracion | `custom:encontrar NPC Arms Dealer en village` | 3 PC | #39 |

### Construccion (4)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 42 | Sala de Drawers | Construccion | `custom:colocar 8+ Storage Drawers` | 3 PC | #5 |
| 43 | Water Wheel instalada | Construccion | `custom:colocar Water Wheel funcional generando SU` | 3 PC | #6 |
| 44 | Linea de belt | Construccion | `custom:conectar belt de Create funcional` | 2 PC | #7 |
| 45 | Cocina mejorada | Construccion | `custom:instalar Blender y Keg en tu cocina` | 2 PC | #2, #4 |

### Coleccion (5)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 46 | 5 tiradas de gacha | Coleccion | `custom:hacer 5 pulls en cualquier Gacha Machine` | 3 PC + Gacha Machine Rosa | - |
| 47 | 3 moldes coleccionados | Coleccion | `custom:tener 3 moldes de postres diferentes` | 3 PC | #3 |
| 48 | Primer anillo de gema | Coleccion | `custom:equipar ring de Citrine o Jade` | 2 PC | #10 |
| 49 | Recetario: 20 comidas | Coleccion | `custom:completar 20 comidas unicas en Recetario` | 3 PC | #18 |
| 50 | **GATE: Entrega al Space Elevator Ch2** | Coleccion | `custom:completar entrega Ch2 en Terminal y presionar LAUNCH` | 8 PC | #36 |

## Resumen

| Metrica | Valor |
|---------|-------|
| Total quests | 50 |
| Total PC | 145 |
| Quests GATE | #36 (Boss), #50 (Space Elevator) |

### Distribucion por categoria

| Categoria | Quests | PC |
|-----------|--------|-----|
| Historia/Tutorial | 12 | 29 |
| Cocina | 8 | 23 |
| Farming | 6 | 16 |
| Dungeon | 5 | 15 |
| Combate/RPG | 5 | 21 |
| Exploracion | 5 | 12 |
| Construccion | 4 | 10 |
| Coleccion | 5 | 19 |
| **Total** | **50** | **145** |

> Nota: PC total ajustado cerca de 150. El jugador tiene 2 maquinas gacha (Verde + Rosa) y PepeMart, distribuyendo tokens entre multiples sinks.
