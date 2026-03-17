# Create Ecosystem — Contenido Completo (extraido de JARs)

> Extraido de los 5 JARs de Create en `modpack/mods/` (2026-03-16)
> Usar como referencia para: quests, progresion por capitulo, decision servo_packaging (issue #105)
> Relacionado: [progression.md](../mechanics/progression.md), [create-automation.md](../mechanics/create-automation.md)

---

## Resumen General

| Mod | Version | Items | Blocks | Entities | Recipes |
|-----|---------|-------|--------|----------|---------|
| **Create** (base) | 6.0.9 | 204 | 701 | 9 | 1,821 |
| **Crafts & Additions** | 1.5.10 | 34 | 21 | 0 | 152 |
| **Deco** | 2.1.2 | 20 | 389 | 0 | 1,721 |
| **Enchantment Industry** | 2.3.0 | 7 | 9 | 0 | 27 |
| **Dragons Plus** | 1.8.7 | 22 | 18 | 0 | 47 |
| **TOTAL** | — | **287** | **1,138** | **9** | **3,768** |

---

# 1. CREATE BASE (6.0.9)

## 1.1 Sistema de Packaging/Logistica (NUEVO en 6.0)

> Relevante para issue #105: evaluacion de servo_packaging

### Bloques de logistica

| Bloque | Funcion | Requiere rotacion | Requiere redstone |
|--------|---------|-------------------|-------------------|
| `packager` | Empaca items de inventario adyacente en Cardboard Package | No | Si |
| `repackager` | Consolida multiples packages de una misma orden en uno | No | Si |
| `package_frogport` | Router de packages por address. 18 slots. Deposita en inventario debajo | No | No (pasivo) |
| `chain_conveyor` | Transporta packages (solo packages, no items). Conecta con cadenas | Si | No |
| `stock_link` | Monitorea inventario. Sin limite de distancia. Cross-dimension | No | No |
| `stock_ticker` | Pantalla central de la red logistica. Buscar items, hacer pedidos | No | No |
| `redstone_requester` | Hace pedidos automaticos con senal redstone. Hasta 256 de 9 tipos | No | Si |
| `postbox` (x16 colores) | Como Frogport pero para red de trenes. Se vincula a Train Station | No | No |

### Items de logistica

| Item | Funcion |
|------|---------|
| `create:package` | Cardboard Package — 9 slots, no apilable, entidad lanzable |
| `create:rare_package` | Rare Package — variante cosmetica (10 diseños) |
| `create:package_filter` | Filtra packages por address en Brass Tunnel/Smart Chute |
| `create:cardboard` | Material base. Pulp → pressing → Cardboard |
| `create:pulp` | Pulpa. Mixing de bamboo/sugar_cane/saplings + agua |
| `create:shopping_list` | Lista de compras para Stock Ticker |
| `create:transmitter` | Conecta Stock Link a la red |

### Variantes de Package (tag `create:packages` — 14 total)

- 4 tamaños de cardboard: 10x8, 10x12, 12x10, 12x12 (NOTA: son variantes visuales, todos tienen 9 slots)
- 10 rare packages (cosmeticos): creeper, darcy, evan, jinx, kryppers, simi, starlotte, thunder, up, vector

### Cadena de produccion de Cardboard

```
[Bamboo / Sugar Cane / Saplings] (tag: create:pulpifiable)
    ↓ Mechanical Mixer + Basin (x3 items + agua)
[Pulp]
    ↓ Mechanical Press
[Cardboard]
    ↓ (opcional: 4x Cardboard = Cardboard Block crafting)
[Cardboard Block]
    ↓ + String (item_application)
[Bound Cardboard Block]
```

### Cardboard Equipment

| Item | Tipo |
|------|------|
| `create:cardboard_helmet` | Armadura (novelty) |
| `create:cardboard_chestplate` | Armadura (novelty) |
| `create:cardboard_leggings` | Armadura (novelty) |
| `create:cardboard_boots` | Armadura (novelty) |
| `create:cardboard_sword` | Arma (novelty) |

Soporta 10 armor trim variants.

### Interaccion manual con Packages

| Accion | Resultado |
|--------|-----------|
| Click derecho superficie | Coloca como entidad fisica |
| Mantener click derecho | Lanza (mas tiempo = mas distancia) |
| Click derecho en package en suelo | Recoge al inventario |
| Click izquierdo en package en suelo | Desempaca: destruye caja, dropea items |
| Shift + click derecho (inventario) | Desempaca: destruye caja, items al inventario |
| Contacto con agua/lava/cactus | Desempaca (destruye) |

**NO se puede llenar un package a mano.** Solo el Packager los crea.

---

## 1.2 Maquinas y Kinetica (core)

### Generadores de rotacion

| Bloque | Tier | SU generados | Notas |
|--------|------|-------------|-------|
| `hand_crank` | Basico | 32 SU | Manual |
| `water_wheel` | Andesite | 64-256 SU | Depende de flujo |
| `large_water_wheel` | Andesite | 128-512 SU | Version grande |
| `windmill_bearing` | Andesite | Variable | Depende de velas |
| `steam_engine` | Brass | 1024+ SU | Requiere calor |
| `creative_motor` | Creative | Infinito | Solo creativo |

### Maquinas de procesamiento

| Bloque | Funcion | Tier | Recetas en JAR |
|--------|---------|------|---------------|
| `mechanical_press` | Prensar (sheets, compacting, cardboard) | Andesite | 39 pressing + 7 compacting |
| `mechanical_mixer` | Mezclar (alloys, dough, pulp) | Brass | 14 mixing |
| `mechanical_saw` | Cortar (madera, piedra) | Andesite | 30 cutting |
| `mechanical_drill` | Minar bloques | Andesite | — |
| `mechanical_harvester` | Cosechar crops | Andesite | — |
| `mechanical_plough` | Arar tierra | Andesite | — |
| `mechanical_roller` | Aplanar terreno | Brass | — |
| `mechanical_crafter` | Crafting automatizado (grids) | Brass | 4 mechanical_crafting |
| `mechanical_arm` | Brazo robotico para mover items | Brass | — |
| `deployer` | Interactua con items/bloques | Brass | 167 deploying |
| `millstone` | Moler (harina, ores) | Andesite | 231 milling |
| `crushing_wheel` | Triturar (ores, bloques) | Brass | 201 crushing |
| `encased_fan` | Fan processing (washing, smoking, haunting, blasting) | Andesite | 52 splashing + 22 haunting |
| `basin` | Contenedor para mixer/press | Andesite | (usado por mixing/compacting) |
| `spout` | Verter fluidos en items | Andesite | 19 filling |
| `item_drain` | Extraer fluidos de items | Andesite | 5 emptying |

### Sequenced Assembly (3 recetas)

| Output | Pasos |
|--------|-------|
| Precision Mechanism | Golden Sheet → 5 pasos (deploying + pressing) |
| Sturdy Sheet | Powdered Obsidian → pasos multiples |
| Train Track | Sleepers → pasos multiples |

### Mechanical Crafting (4 recetas)

| Output | Nota |
|--------|------|
| Extendo Grip | Grid grande |
| Crushing Wheel | Grid grande |
| Wand of Symmetry | Grid grande |
| Potato Cannon | Grid grande |

---

## 1.3 Transporte y Logistica basica

### Belts y Funnels

| Bloque | Tier | Funcion |
|--------|------|---------|
| `belt` | Andesite | Transporta items |
| `andesite_funnel` | Andesite | Inserta/extrae items (basico) |
| `brass_funnel` | Brass | Inserta/extrae con filtro |
| `andesite_tunnel` | Andesite | Divide items en belt |
| `brass_tunnel` | Brass | Divide con filtro inteligente |
| `chute` | Andesite | Transporta items vertical |
| `smart_chute` | Brass | Chute con filtro |
| `depot` | Andesite | Almacen temporal en belt |
| `weighted_ejector` | Brass | Lanza items a distancia |

### Contraptions

| Bloque | Funcion |
|--------|---------|
| `mechanical_piston` / `sticky_mechanical_piston` | Extension lineal |
| `mechanical_bearing` | Rotacion |
| `clockwork_bearing` | Rotacion con hora |
| `rope_pulley` | Sube/baja contraptions |
| `elevator_pulley` / `elevator_contact` | Elevador de pasajeros |
| `gantry_shaft` / `gantry_carriage` | Movimiento lineal en riel |
| `cart_assembler` | Convierte contraption en minecart |
| `linear_chassis` / `radial_chassis` | Estructura de contraptions |
| `sticker` | Pega bloques a contraptions |
| `contraption_controls` | Controles manuales de contraption |
| `portable_storage_interface` | Transferir items a/de contraption |
| `portable_fluid_interface` | Transferir fluidos a/de contraption |

### Trenes

| Bloque | Funcion |
|--------|---------|
| `track` | Via de tren |
| `track_station` | Estacion (parada de tren) |
| `track_signal` | Semaforo |
| `track_observer` | Detector de paso de tren |
| `controls` | Controles de tren (montar y conducir) |
| `train_door` / `train_trapdoor` | Puertas de vagon |
| `small_bogey` / `large_bogey` | Ruedas de tren |
| `schedule` (item) | Programar rutas de tren |

---

## 1.4 Fluidos

| Bloque | Funcion |
|--------|---------|
| `fluid_pipe` / `glass_fluid_pipe` / `encased_fluid_pipe` | Transportar fluidos |
| `smart_fluid_pipe` | Pipe con filtro |
| `fluid_tank` / `creative_fluid_tank` | Almacenar fluidos |
| `mechanical_pump` | Mover fluidos |
| `fluid_valve` | Controlar flujo |
| `hose_pulley` | Bombear fluidos del mundo |

Fluidos nativos: `water`, `lava`, `chocolate`, `honey`

---

## 1.5 Redstone

| Bloque | Funcion |
|--------|---------|
| `redstone_link` | Redstone wireless |
| `redstone_contact` | Detector de contacto |
| `analog_lever` | Lever con nivel ajustable |
| `content_observer` (Smart Observer) | Observa contenido de inventario |
| `stockpile_switch` (Threshold Switch) | Activa redstone segun nivel de inventario |
| `powered_latch` / `powered_toggle_latch` | Flip-flops |
| `pulse_repeater` / `pulse_extender` / `pulse_timer` | Timing de redstone |
| `display_link` / `display_board` | Mostrar info en pantallas |
| `nixie_tube` (x16 colores) | Display numerico |

---

## 1.6 Materiales y Procesamiento

### Materiales de Create

| Material | Obtencion | Uso principal |
|----------|-----------|---------------|
| `raw_zinc` / `zinc_ingot` / `zinc_nugget` | Minado (zinc_ore, deepslate_zinc_ore) | Base para brass |
| `andesite_alloy` | Mixing (andesite + zinc/iron) | Tier Andesite |
| `brass_ingot` / `brass_nugget` | Mixing (copper + zinc) | Tier Brass |
| `iron_sheet` / `copper_sheet` / `brass_sheet` / `golden_sheet` | Press (ingot) | Crafting de maquinas |
| `sturdy_sheet` | Sequenced Assembly (obsidian dust) | Railway casing |
| `rose_quartz` / `polished_rose_quartz` | Minado + sandpaper | Electron tubes |
| `electron_tube` | Crafting (polished rose quartz + iron + redstone) | Tier Brass |
| `precision_mechanism` | Sequenced Assembly (golden sheet) | Tier avanzado |
| `chromatic_compound` | Crafting especial | Refined Radiance / Shadow Steel |
| `refined_radiance` / `shadow_steel` | Chromatic compound + luz/oscuridad | Endgame |
| `pulp` | Mixing (pulpifiable + agua) | Cardboard |
| `cardboard` | Press (pulp) | Packages, armor, bloques |
| `wheat_flour` / `dough` | Milling wheat / Mixing flour+water | Comida |
| `cinder_flour` | Haunting wheat flour | Crafting |
| `powdered_obsidian` | Crushing obsidian | Sturdy Sheet |
| `experience_nugget` | Crushing ores (bonus drop) | XP |

### Crushed Raw Ores (13 tipos)

aluminum, copper, gold, iron, lead, nickel, osmium, platinum, quicksilver, silver, tin, uranium, zinc

(Solo copper, gold, iron, zinc son vanilla/Create. El resto es compat con otros mods)

---

## 1.7 Equipamiento y Wearables

| Item | Funcion |
|------|---------|
| `goggles` | Engineer's Goggles — ver stats de rotacion. Curios: slot `head` |
| `copper_backtank` | Backtank de cobre — aire comprimido para herramientas |
| `netherite_backtank` | Backtank de netherite — version mejorada |
| `copper_diving_helmet` / `copper_diving_boots` | Equipo de buceo (cobre) |
| `netherite_diving_helmet` / `netherite_diving_boots` | Equipo de buceo (netherite) |
| `extendo_grip` | Extender alcance del jugador |
| `potato_cannon` | Arma que dispara papas |
| `linked_controller` | Control remoto para redstone links |
| `wrench` | Herramienta multiuso de Create |
| `super_glue` | Pegar bloques en contraptions |

---

## 1.8 Comida de Create

| Item | Obtencion |
|------|-----------|
| `dough` | Mixing (wheat_flour + water) |
| `sweet_roll` | Smoking dough |
| `bar_of_chocolate` | Compacting (chocolate) |
| `chocolate_glazed_berries` | Filling (berries + chocolate) |
| `honeyed_apple` | Filling (apple + honey) |
| `builders_tea` | Mixing (water + sugar + milk + dried kelp) |
| `blaze_cake` | Filling blaze_cake_base + lava |
| `blaze_cake_base` | Compacting (sugar + egg + cocoa + blaze powder) |

---

## 1.9 Decoracion

### Asientos (16 colores)
Todos los colores de MC: white, orange, magenta, light_blue, yellow, lime, pink, gray, light_gray, cyan, purple, blue, brown, green, red, black

### Table Cloths (19)
16 colores + andesite, brass, copper

### Toolboxes (16 colores)
Almacenamiento portatil con 8 compartimentos

### Sails (17)
16 colores + sail_frame (para windmill)

### Valve Handles (17)
Copper + 16 colores

### Ventanas
oak, spruce, birch, jungle, acacia, dark_oak, cherry, bamboo, mangrove, crimson, warped + framed_glass + horizontal/vertical framed (con variantes de panel)

### Puertas y Escaleras
andesite/brass/copper door + ladder + scaffolding + bars

### Campanas y Relojes
peculiar_bell, haunted_bell, cuckoo_clock, mysterious_cuckoo_clock

### Otros
`placard` (mostrar items), `clipboard` (lista de tareas), `copycat_panel/step/bars/base` (copian textura), `item_hatch` (depositar items rapido), `desk_bell`, `steam_whistle`

### Bloques de piedra (14 familias × ~18 variantes c/u = ~252 bloques)
Familias: andesite, asurine, calcite, crimsite, deepslate, diorite, dripstone, granite, limestone, ochrum, scorchia, scoria, tuff, veridium

Cada familia tiene: cut/polished_cut/cut_bricks/small_bricks + slab/stairs/wall de cada + layered + pillar

### Cobre decorativo
shingles/tiles + slab/stairs por cada estado de oxidacion (4) × waxed/unwaxed (2) = 24+ variantes

### Rose Quartz decorativo
rose_quartz_block, rose_quartz_lamp, rose_quartz_tiles, small_rose_quartz_tiles

---

## 1.10 Tipos de Receta de Create

| Tipo | Maquina | Cantidad |
|------|---------|----------|
| `create:milling` | Millstone | 231 |
| `create:crushing` | Crushing Wheel | 201 |
| `create:deploying` | Deployer | 167 |
| `create:splashing` | Fan + Water | 52 |
| `create:pressing` | Mechanical Press | 39 |
| `create:cutting` | Mechanical Saw | 30 |
| `create:haunting` | Fan + Soul Fire | 22 |
| `create:filling` | Spout | 19 |
| `create:mixing` | Mixer + Basin | 14 |
| `create:item_application` | Manual (click derecho) | 8 |
| `create:compacting` | Press + Basin | 7 |
| `create:emptying` | Item Drain | 5 |
| `create:mechanical_crafting` | Mechanical Crafter | 4 |
| `create:sequenced_assembly` | Multi-paso | 3 |
| `create:sandpaper_polishing` | Sandpaper | 1 |

---

# 2. CREATE CRAFTS & ADDITIONS (1.5.10)

## 2.1 Sistema Electrico

| Bloque | Funcion |
|--------|---------|
| `electric_motor` | Genera rotacion desde energia electrica (FE) |
| `alternator` | Genera FE desde rotacion |
| `accumulator` / `modular_accumulator` | Almacena FE (modular = expandible) |
| `connector` / `large_connector` / `small_light_connector` | Conectores electricos |
| `tesla_coil` | Daño electrico + enchanting |
| `portable_energy_interface` | Transferir FE a/de contraptions |
| `redstone_relay` | Relay de redstone controlado por FE |
| `digital_adapter` | Adapter para CC:Tweaked (computadoras) |
| `rolling_mill` | Convierte ingots → rods, rods → wire |

## 2.2 Materiales

| Material | Obtencion |
|----------|-----------|
| `electrum_ingot/nugget/sheet` | Mixing copper + gold, o Rolling electrum |
| `iron_rod/wire` | Rolling Mill |
| `copper_rod/wire/spool` | Rolling Mill |
| `brass_rod` | Rolling Mill |
| `gold_rod/wire/spool` | Rolling Mill |
| `electrum_rod/wire/spool` | Rolling Mill |
| `zinc_sheet` | Pressing |
| `diamond_grit` | Crushing diamond |
| `diamond_grit_sandpaper` | Crafting con grit |
| `capacitor` | Crafting |
| `biomass` | Mixing (organic + water) |
| `biomass_pellet` | Compacting biomass |
| `straw` | Item para liquid blaze burner |

## 2.3 Items Especiales

| Item | Funcion |
|------|---------|
| `copper_goblet` / `gold_goblet` | Decorativos |
| `electrum_amulet` | Pale Gold Amulet (accesorio) |
| `festive_spool` | Decorativo (luces) |

## 2.4 Comida

| Bloque | Tipo |
|--------|------|
| `chocolate_cake` | Pastel de chocolate (filling) |
| `honey_cake` | Pastel de miel (filling) |

## 2.5 Fluidos

| Fluido | Uso |
|--------|-----|
| `bioethanol` (Biofuel) | Combustible liquido |
| `seed_oil` | Compacting de semillas |

## 2.6 Tipos de Receta

| Tipo | Cantidad | Descripcion |
|------|----------|-------------|
| `createaddition:charging` | 53 | Tesla coil: deoxidar cobre, electrum, enchant books |
| `createaddition:rolling` | 16 | Rolling mill: ingots→rods, rods→wire |
| `createaddition:liquid_burning` | 14 | Combustibles liquidos para generacion |
| `create:mixing` | 19 | Biomass, alloys, bioethanol |
| Otros (crafting, crushing, etc.) | 50 | Varios |

---

# 3. CREATE DECO (2.1.2)

## 3.1 Infraestructura Metalica (por metal)

Metales disponibles: **Andesite, Brass, Copper, Iron, Industrial Iron, Zinc**

Por cada metal:
- Bars, Bars Overlay, Catwalk, Catwalk Railing, Catwalk Stairs
- Door, Locked Door, Trapdoor
- Mesh Fence, Sheet Metal, Support, Support Wedge
- Train Hull, Window, Window Pane
- (algunos tienen Ladder)

## 3.2 Ladrillos de Colores (8 colores × 7 variantes × 4 formas = 224 bloques)

**Colores:** Blue, Dean, Dusk, Pearl, Red, Scarlet, Umber, Verdant

**Variantes por color:** base, corner, cracked, long, mossy, short, tiled

**Formas por variante:** bricks, slab, stairs, wall

## 3.3 Decoracion

| Tipo | Cantidad | Notas |
|------|----------|-------|
| Shipping Containers | 16 | Todos los colores de MC |
| Cage Lamps | 24 | 4 colores × 6 metales |
| Placards | 16 | Todos los colores |
| Decals | 20 | Flechas, señales, warnings |
| Coin Stacks | 7 | Bloques decorativos de monedas apiladas |

## 3.4 Items

| Item | Tipo |
|------|------|
| Coins (x7 metales) | Brass, Copper, Gold, Industrial Iron, Iron, Netherite, Zinc |
| Coin Stacks (x7) | Versiones apiladas |
| Sheets (andesite, industrial_iron, zinc) | Material de crafting |
| `industrial_iron_ingot/nugget` | Material |
| `netherite_nugget` | Material |

---

# 4. CREATE ENCHANTMENT INDUSTRY (2.3.0)

## 4.1 Bloques

| Bloque | Funcion |
|--------|---------|
| `blaze_enchanter` | Enchant items con liquido XP + template |
| `blaze_forger` | Enchant herramientas/armor con niveles altos |
| `printer` | Copia enchanted books |
| `mechanical_grindstone` | Desenchantar items → liquid XP |
| `grindstone_drain` | Extraer XP de grindstone |
| `experience_hatch` | Depositar/extraer liquid XP |
| `experience_lantern` | Lampara que consume liquid XP |
| `super_experience_block` | Bloque de Super XP (almacen denso) |

## 4.2 Items

| Item | Funcion |
|------|---------|
| `enchanting_template` | Template para Blaze Enchanter |
| `super_enchanting_template` | Template para enchants altos |
| `experience_cake` / `experience_cake_base` / `experience_cake_slice` | Pastel de XP |
| `super_experience_nugget` | Nugget de Super XP |

## 4.3 Fluido

`experience` — Liquid Experience (usado por todas las maquinas del mod)

---

# 5. CREATE DRAGONS PLUS (1.8.7)

## 5.1 Fluidos (17)

- **16 Dye fluids:** Todos los colores de MC (250mB por item de dye, bidireccional)
- **Dragon's Breath** (fluido): Obtenido de dragon breath bottles via emptying

## 5.2 Procesamiento Especial

### Ending (Dragon's Breath processing — 7 recetas)
Fan + Dragon's Breath transforma:
- Cobblestone → End Stone
- Stone Bricks → End Stone Bricks (+ variantes)
- Apple → Chorus Fruit
- Leather → Phantom Membrane

### Freezing (4 recetas)
Fan + Powder Snow:
- Ice → Packed Ice
- Packed Ice → Blue Ice
- Magma Cream → Slime Ball
- Blaze Rod → Breeze Rod

## 5.3 Bloques

| Bloque | Funcion |
|--------|---------|
| `fluid_hatch` | Hatch para fluidos (depositar/extraer) |

## 5.4 Items

| Item | Funcion |
|------|---------|
| `blaze_upgrade_smithing_template` | Template para upgrades con Blaze |
| 16 Dye buckets | Buckets de cada fluido de dye |
| `dragon_breath_bucket` | Bucket de Dragon's Breath |

---

# 6. MAPA DE TIERS PARA PROGRESION

> Para cuadrar con la progresion por capitulos de docs/mechanics/progression.md

## Tier Andesite (Ch2-Ch3)

**Materiales:** Andesite Alloy (andesite + zinc/iron)

**Maquinas:**
- Water Wheel, Shaft, Cogwheel, Belt, Depot
- Andesite Funnel/Tunnel
- Mechanical Press, Fan, Saw, Drill, Harvester, Millstone
- Basin, Spout, Item Drain
- Chute, Fluid Pipe, Fluid Tank, Mechanical Pump

**Procesamiento disponible:**
- Pressing (sheets)
- Milling (231 recetas — harina, ores)
- Fan processing: splashing (52), haunting (22)
- Cutting (30 — madera, piedra)
- Filling/Emptying (24 — fluidos)

## Tier Brass (Ch4)

**Materiales:** Brass (copper + zinc mixing), Electron Tube (rose quartz + iron + redstone)

**Maquinas nuevas:**
- Mechanical Mixer, Deployer, Mechanical Crafter, Mechanical Arm
- Brass Funnel/Tunnel, Smart Chute
- Crushing Wheel, Rotation Speed Controller
- Sequenced Assembly (Precision Mechanism, Sturdy Sheet, Track)
- Steam Engine (alta potencia)

**Procesamiento nuevo:**
- Mixing (14 recetas — alloys, dough, pulp, chocolate)
- Compacting (7 — blaze cake base, chocolate bar)
- Deploying (167 — aplicar items)
- Crushing (201 — ores con bonus)
- Mechanical Crafting (4 — items grandes)
- Sequenced Assembly (3 — items complejos)

## Tier Logistics (Ch4-Ch5)

**Maquinas:**
- Packager, Repackager (redstone)
- Chain Conveyor (rotacion)
- Package Frogport, Postbox
- Stock Link, Stock Ticker, Redstone Requester

**Procesamiento:** Cardboard (pulp → pressing → cardboard)

## Tier Trains (Ch5)

**Materiales:** Precision Mechanism, Sturdy Sheet (sequenced assembly), Railway Casing

**Bloques:** Track, Track Station, Track Signal, Track Observer, Controls, Train Door, Bogeys, Schedule

## Tier Additions Electrico (Ch5)

**Maquinas:** Electric Motor, Alternator, Accumulator, Tesla Coil, Rolling Mill

**Procesamiento nuevo:**
- Rolling (16 — ingots→rods→wire)
- Charging (53 — deoxidacion, enchanting)

## Tier Enchantment Industry (Ch5)

**Maquinas:** Blaze Enchanter, Blaze Forger, Printer, Mechanical Grindstone

**Procesamiento:** Grinding (7 — items→liquid XP), enchanting automatizado

## Tier Dragons Plus (Ch6+)

**Procesamiento:**
- Ending (7 — transmutacion con Dragon's Breath)
- Freezing (4 — con Powder Snow)
- Dye fluids (32 — conversion bidireccional)

---

# 7. PREGUNTAS ABIERTAS PARA PROGRESION

1. **Packaging**: ¿servo_packaging es necesario si Create tiene Packager nativo? (Issue #105)
2. **Logistics tier**: ¿En que capitulo desbloquear Chain Conveyor, Frogport, Stock system? ¿Ch4 con brass o Ch5?
3. **Enchantment Industry**: Actualmente asignado a Ch5. ¿Correcto?
4. **Dragons Plus**: Ending/Freezing son procesamiento avanzado. ¿Ch6 o antes?
5. **Rolling Mill (C&A)**: ¿Mismo tier que brass (Ch4) o electrico (Ch5)?
6. **Deco**: ¿Bloquear bloques decorativos por stage o dejar libres? Actualmente Create Deco asignado a Ch5.
7. **Table Cloths + Stock Ticker shops**: Create 6.0 permite shops via Table Cloth. ¿Conflicto con PepeMart?
8. **Trains**: Progression.md dice Ch4 basicos + Ch5 avanzados. ¿Incluir Postbox en Ch5?
9. **Cardboard armor**: ¿Bloquear o dejar como novelty? Es mas debil que leather.
10. **Create rare packages**: 10 diseños cosmeticos. ¿Integrar con gacha/rewards o ignorar?
