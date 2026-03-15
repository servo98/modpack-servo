# Capitulo 1: Raices — Quests Detallados

> Tema: Capitulo introductorio. Aprender mecanicas vanilla + Farmer's Delight + mecanicas custom. Primer contacto con dungeons, champions y el sistema RPG. El jugador construye su base, cocina sus primeras comidas y enfrenta al Guardian del Bosque.

## Quests

### Historia/Tutorial (12)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 1 | Bienvenido al mundo | Historia | `observation:primer login` | 2 PC | - |
| 2 | Un techo sobre tu cabeza | Historia | `item:minecraft:crafting_table` | 2 PC | #1 |
| 3 | Herramientas basicas | Historia | `item:minecraft:wooden_pickaxe` | 2 PC | #1 |
| 4 | Conoce el Cooking Pot | Historia | `item:farmersdelight:cooking_pot` | 3 PC | #2 |
| 5 | Conoce la Cutting Board | Historia | `item:farmersdelight:cutting_board` | 2 PC | #2 |
| 6 | Conoce el Stove | Historia | `item:farmersdelight:stove` | 2 PC | #4 |
| 7 | El Space Elevator: tu mision | Historia | `custom:recibir bloques del Terminal como quest reward` | 3 PC + 9 bloques Terminal | #4 |
| 8 | Empacando para el futuro | Historia | `item:servo_packaging:packing_station` | 3 PC | #7 |
| 9 | La Gacha Machine | Historia | `custom:recibir Gacha Machine Verde como quest reward` | 3 PC + Gacha Machine Verde | #8 |
| 10 | Mazmorras y llaves | Historia | `item:servo_dungeons:dungeon_pedestal` | 3 PC + 1 Pedestal + 4 Runas + 1 Llave Basica | #3 |
| 11 | La mesa de hechizos | Historia | `custom:recibir Spell Binding Table como quest reward` | 2 PC + Spell Binding Table | #10 |
| 12 | Bolsa de runas | Historia | `item:runes:small_rune_pouch` | 2 PC | #11 |

### Cocina (8)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 13 | Primera comida cocinada | Cocina | `item:farmersdelight:mixed_salad` | 3 PC | #4 |
| 14 | Variedad es sabor | Cocina | `custom:cocinar 3 recetas FD diferentes` | 3 PC | #13 |
| 15 | El arte de cortar | Cocina | `custom:usar Cutting Board para cortar un ingrediente` | 2 PC | #5 |
| 16 | Fuego controlado | Cocina | `custom:cocinar algo en el Stove` | 2 PC | #6 |
| 17 | Maestro del Skillet | Cocina | `item:farmersdelight:fried_egg` | 2 PC | #6 |
| 18 | Recetario: 5 comidas | Cocina | `custom:comer 5 comidas diferentes (hito Recetario)` | 5 PC + 1 corazon extra | #14 |
| 19 | Pesca del dia | Cocina | `item:aquaculture:bluegill?` | 2 PC | #1 |
| 20 | Del mar a la mesa | Cocina | `item:farmersdelight:grilled_salmon?` | 3 PC | #19 |

### Farming (6)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 21 | Primer tomate | Farming | `item:farmersdelight:tomato` | 2 PC | #1 |
| 22 | Cebollas para llorar | Farming | `item:farmersdelight:onion` | 2 PC | #1 |
| 23 | Repollo del huerto | Farming | `item:farmersdelight:cabbage` | 2 PC | #1 |
| 24 | Arroz de la tierra | Farming | `item:farmersdelight:rice` | 2 PC | #1 |
| 25 | Granja diversa | Farming | `custom:tener granja con 4+ tipos de crops plantados` | 4 PC | #21, #22, #23, #24 |
| 26 | Amigos del corral | Farming | `custom:domesticar 2 tipos de animales` | 3 PC | #1 |

### Dungeon (5)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 27 | Forjando tu primera llave | Dungeon | `item:servo_dungeons:basic_key` | 2 PC | #10 |
| 28 | Primera incursion | Dungeon | `custom:entrar a primera dungeon via beam` | 3 PC | #27 |
| 29 | Sobreviviente | Dungeon | `custom:completar dungeon Basica (salir por Exit Portal)` | 5 PC | #28 |
| 30 | Cofre de dungeon | Dungeon | `custom:abrir cofre Lootr en dungeon` | 2 PC | #28 |
| 31 | Champion cazador | Dungeon | `custom:derrotar un champion en dungeon` | 4 PC | #28 |

### Combate/RPG (5)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 32 | Primer arma | Combate | `item:rogues:flint_dagger?` | 2 PC | #3 |
| 33 | Champion del overworld | Combate | `custom:derrotar un champion en el overworld` | 4 PC | #32 |
| 34 | Equipar Rune Pouch | Combate | `custom:equipar Small Rune Pouch en Curios slot` | 2 PC | #12 |
| 35 | La Llave del Bosque | Combate | `item:servo_dungeons:boss_key_ch1` | 3 PC | #29, #33 |
| 36 | **GATE: Guardian del Bosque** | Combate | `kill:servo_dungeons:guardian_del_bosque` | 10 PC + Raiz del Guardian | #35 |

### Exploracion (4)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 37 | Tres biomas nuevos | Exploracion | `custom:visitar 3 biomas Terralith diferentes` | 3 PC | #1 |
| 38 | Piedra de regreso | Exploracion | `item:waystones:waystone` | 2 PC | #1 |
| 39 | Aldea encontrada | Exploracion | `location:minecraft:overworld:village?` | 3 PC | #37 |
| 40 | Construir tu Altar | Exploracion | `custom:colocar Pedestal + 4 Runas en patron de cruz` | 3 PC | #10 |

### Construccion (4)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 41 | Hogar dulce hogar | Construccion | `custom:construir casa con cama, puerta y techo` | 3 PC | #2 |
| 42 | La cocina | Construccion | `custom:colocar Cooking Pot + Cutting Board + Stove en tu base` | 3 PC | #4, #5, #6 |
| 43 | Almacen organizado | Construccion | `custom:colocar 8+ cofres` | 2 PC | #2 |
| 44 | La granja cercada | Construccion | `custom:construir granja con cercas y puerta` | 2 PC | #25 |

### Coleccion (6)

| # | Quest | Categoria | Deteccion | Recompensa | Depende de |
|---|-------|-----------|-----------|------------|------------|
| 45 | Primeras monedas | Coleccion | `item:servo_core:pepe_coin x5` | 2 PC | #1 |
| 46 | Primera tirada | Coleccion | `custom:hacer primer pull en Gacha Verde` | 3 PC | #9, #45 |
| 47 | Recetario: primeros pasos | Coleccion | `custom:cocinar 5 comidas unicas (Recetario)` | 2 PC | #14 |
| 48 | Pescador novato | Coleccion | `custom:pescar 3 tipos de pez con Aquaculture` | 3 PC | #19 |
| 49 | Primera Caja de Envio | Coleccion | `item:servo_packaging:shipping_box` | 2 PC | #8 |
| 50 | **GATE: Entrega al Space Elevator Ch1** | Coleccion | `custom:completar entrega Ch1 en Terminal y presionar LAUNCH` | 8 PC | #36, #49 |

## Resumen

| Metrica | Valor |
|---------|-------|
| Total quests | 50 |
| Total PC | 144 |
| Quests GATE | #36 (Boss), #50 (Space Elevator) |

### Distribucion por categoria

| Categoria | Quests | PC |
|-----------|--------|-----|
| Historia/Tutorial | 12 | 29 |
| Cocina | 8 | 22 |
| Farming | 6 | 15 |
| Dungeon | 5 | 16 |
| Combate/RPG | 5 | 21 |
| Exploracion | 4 | 11 |
| Construccion | 4 | 10 |
| Coleccion | 6 | 20 |
| **Total** | **50** | **144** |

> Nota: Ligero deficit de PC compensa que es el capitulo tutorial con rewards de quest no-PC (Terminal, Gacha Machine, Spell Binding Table, Altar+Runas+Llave gratis).
