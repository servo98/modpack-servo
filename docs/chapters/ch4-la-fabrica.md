# Capitulo 4: La Fabrica

> Stage: `servo_ch4` (requiere: Boss Ch3 + Delivery Ch3)
> Trigger para Ch5: Derrotar a la Locomotora Fantasma + Completar entrega Ch4
> Mecanicas: [Create Automation](../mechanics/create-automation.md), [Cooking](../mechanics/cooking.md), [RPG Classes](../mechanics/rpg-classes.md), [Storage](../mechanics/storage.md), [Jewelry](../mechanics/jewelry.md)

## Tema

La fabrica cobra vida. Brass tier desbloquea Mixer, Deployer, Crafter y Crushing Wheels. Steam Engine porque Water Wheel ya no alcanza. **Create 6.0 Logistics**: Packager empaca en belt, Stock Link/Ticker monitorean inventarios, Redstone Requester activa maquinas on-demand. Skill Tree permite especializarse. Wok y Baker's Oven completan la bateria de cocina. **Momento clave**: el jugador pasa de lineas simples a una fabrica interconectada.

## Mods nuevos

Create brass tier completo, Create 6.0 Logistics (Packager, Stock Link, Stock Ticker, Transmitter, Redstone Requester, Table Cloth), Steam Engine, servo_cooking (Wok, Baker's Oven), FD Feasts, Skill Tree (especializaciones), Macaw's Windows completo

## Contenido

| Area | Detalle |
|------|---------|
| Crops | +8 especias (42 total): basil, cinnamon, nutmeg, turmeric, vanilla, mustard, hops, tea leaves |
| Recetas | +20 (feasts, recetas con especias, wok stir-fries, baked goods) |
| Workstations | + FD Feasts + servo_cooking (**Wok**, **Baker's Oven**) |
| Create | **Brass + Logistics**: Mixer, Deployer, Mechanical Crafter, Mechanical Arm, Crushing Wheel, Brass Funnel/Tunnel, Smart Chute, Sequenced Assembly, Rotation Speed Controller + **Packager, Stock Link, Stock Ticker, Transmitter, Redstone Requester, Table Cloth** |
| Energia | **Steam Engine** (1024+ SU) — Crushing Wheels lo exigen. Water Wheel sigue corriendo las lineas de Ch3 |
| Storage | **Create Stock system**: Stock Link (reporta inventarios), Stock Ticker (resumen de red), Redstone Requester (auto-restock). + Storage Drawers upgrades |
| RPG | T2 completo. **Skill Tree**: elegir especializacion (Berserker, Deadeye, War Archer, Tundra Hunter, Forcemaster, Air, Earth, Water) |
| Jewelry | Tanzanite/Topaz/Diamond/Emerald rings y necklaces |
| Dungeon | Llave Basica + Avanzada. Loot actualizado a Ch4 |
| Champions | 12% overworld, 1-2 affixes |
| Boss | Locomotora Fantasma (2,400 HP) |
| Decoracion | Macaw's Windows completo |

### servo_cooking workstations nuevas

| Workstation | Mecanica | Create compat |
|-------------|----------|---------------|
| **Wok** | Freidora con control activo de temperatura (overheat mechanic) | Deployer + Blaze Burner |
| **Baker's Oven** | Horno multislot — 4 slots + selector de modo (hornear/tostar/gratinar) | Mixer Heated |

### Create 6.0 Logistics (nuevo en Ch4)

| Bloque | Funcion |
|--------|---------|
| **Packager** | Toma items de belt → empaca en Cardboard Package (bulk container nativo) |
| **Stock Link** | Conecta a inventario (chest/barrel/drawer) y reporta contenido a la red |
| **Stock Ticker** | Muestra resumen del stock de toda la red Create |
| **Transmitter** | Propaga la red de Stock a distancia |
| **Redstone Requester** | Emite redstone cuando stock de un item cae bajo threshold → activa maquinas on-demand |
| **Table Cloth** | Shop de comercio entre jugadores (colocar items con precios) |

### Pipeline tipico Ch4
```
Harvester → Belt → Slice&Dice → Cooking Pot output
Steam Engine → Crushing Wheels → Crushed ore
Mixer → Brass Ingot
Sequenced Assembly → Precision Mechanism
Stock Link → inventarios reportados → Redstone Requester → activar maquinas on-demand
Packager → Cardboard Package → Belt → Delivery Port
```

## Entrega al Space Elevator — Ch4: "La Fabrica"

> Detalle completo: [progression.md → Entregas](../mechanics/progression.md#capitulo-4-la-fabrica)

Fabrica completa. Steam Engine necesario (~807 items total).

| Item | Cant. | Nuevo/Acumulado |
|------|-------|-----------------|
| Cooking Pot recipe | 64 | Acumulado |
| Keg product | 32 | Acumulado |
| Wheat Flour | 128 | Acumulado |
| Iron/Copper Sheet | 128 | Acumulado (x2) |
| Slicer output | 64 | Acumulado |
| Brass Ingot | 128 | **Nuevo** — Mixer |
| Precision Mechanism | 8 | **Nuevo** — Sequenced Assembly |
| Crushed ore (cualquiera) | 128 | **Nuevo** — Crushing Wheel |
| Deployer recipe output | 32 | **Nuevo** — Deployer |
| Feast completo (FD) | 8 | **Nuevo** |
| Wok recipe (servo_cooking) | 32 | **Nuevo** |
| Baker's Oven recipe | 32 | **Nuevo** |
| Comida variada (min 30 tipos) | 32 | Acumulado |
| Senal Fantasma | 1 | **Boss** |

128 crushed ore = Crushing Wheels consumen mucho SU → Steam Engine necesario. Packager y Stock system disponibles para automatizar entregas via belt → Delivery Port.

## Quests (~52)

### Historia/Tutorial (12)
1. Tutorial: Create Mixer — hacer Brass Ingot
2. Tutorial: Deployer — aplicar items automaticamente
3. Tutorial: Mechanical Crafter — recetas avanzadas en grilla
4. Tutorial: Crushing Wheels — crushed ore a gran escala
5. Tutorial: Steam Engine — energia industrial
6. Tutorial: Sequenced Assembly — Precision Mechanism
7. Tutorial: Skill Tree — elegir especializacion
8. Tutorial: Packager — empacar items en Cardboard Package automaticamente
9. Tutorial: Stock Link + Stock Ticker — monitorear inventarios
10. Tutorial: Redstone Requester — auto-restock on-demand
11. Tutorial: Wok (servo_cooking) — stir-fry con control de temperatura
12. Tutorial: Baker's Oven (servo_cooking) — hornear pan, pasteles, galletas

### Cocina (8)
1. Cocinar primera receta en el Wok
2. Hornear primera receta en el Baker's Oven
3. Preparar un Feast (FD — plato grande para compartir)
4. Preparar 3 Feasts diferentes
5. Cocinar receta con especias (basil, cinnamon, etc.)
6. Compartir un Feast (quest multiplayer opcional)
7. Usar Deployer para automatizar procesamiento de comida
8. Comer 50 comidas diferentes (Recetario: +4 corazones)

### Farming (6)
1. Plantar 4 especias nuevas (Croptopia)
2. Plantar todas las 8 especias
3. Automatizar granja con Create Harvester + belts completos
4. Granja de vanilla o cinnamon
5. Producir en masa un crop con pipeline Create
6. Conectar granja a Stock Link (monitorear produccion)

### Dungeon (5)
1. Hacer 5 dungeon runs (cualquier tier)
2. Obtener unique jewelry de dungeon Avanzada
3. Farmear Esencia de Dungeon (3+)
4. Completar dungeon con gear T2
5. Mejorar build RPG antes de dungeon

### Combate/RPG (5)
1. Elegir especializacion en Skill Tree
2. Craftear set T2 completo (arma + 4 armor)
3. Usar spells de especializacion en combate
4. Equipar Tanzanite o Topaz jewelry
5. Derrotar a la Locomotora Fantasma

### Exploracion (4)
1. Encontrar ores Tanzanite/Topaz
2. Explorar Nether para Blaze Burner fuel
3. Visitar 3 villages con NPCs
4. Establecer Waystones en puntos clave

### Construccion (6)
1. Construir fabrica con Mechanical Crafter funcional
2. Instalar Steam Engine alimentando Crushing Wheels
3. Setup Stock Link en inventarios principales
4. Instalar Stock Ticker en centro de control
5. Setup Packager → Belt → Delivery Port
6. Instalar Wok y Baker's Oven en la cocina

### Coleccion (5)
1. Equipar Diamond ring/necklace
2. Coleccionar 5 especias diferentes
3. Producir 8 Precision Mechanisms
4. Hacer 10 pulls de gacha
5. Completar entrega al Space Elevator Ch4
