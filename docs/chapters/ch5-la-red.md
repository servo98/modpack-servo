# Capitulo 5: La Red

> Stage: `servo_ch5` (requiere: Boss Ch4 + Delivery Ch4)
> Trigger para Ch6: Derrotar a El Arquitecto + Completar entrega Ch5
> Mecanicas: [Create Automation](../mechanics/create-automation.md), [Storage](../mechanics/storage.md), [Enchantments](../mechanics/enchantments.md), [RPG Classes](../mechanics/rpg-classes.md), [Dungeons](../mechanics/dungeons.md)

## Tema

Escala industrial. Trenes conectan ubicaciones remotas. Chain Conveyor/Frogport/Postbox rutean dentro de la fabrica a alta velocidad. Create C&A hace bridge entre Create (SU) y Refined Storage (FE). Enchantment Industry automatiza enchanting. RS desbloquea storage digital y autocrafting. Llave Maestra abre dungeons de 15-20 salas. El jugador pasa de "una fabrica" a "una red logistica".

## Mods nuevos

Create Trains (Track, Station, Signal, Controls, Bogeys, Schedule, Train Door), Create 6.0 routing (Chain Conveyor, Frogport, Postbox), Create Crafts & Additions (Electric Motor, Alternator, Accumulator, Tesla Coil, Rolling Mill), Create Enchantment Industry (Blaze Enchanter/Forger, Printer, Mechanical Grindstone), Refined Storage completo, Llave Maestra de dungeon, Spell Power enchants/potions

## Contenido

| Area | Detalle |
|------|---------|
| Crops | +8 exoticos (50 total): dragon fruit, star fruit, avocado, kiwi, fig, date, pomegranate, cranberry |
| Recetas | +15 (recetas exoticas, cocina avanzada) |
| Create | **Trains**: Track, Station, Signal, Controls, Bogeys, Schedule, Train Door. **Routing**: Chain Conveyor, Frogport, Postbox. **C&A**: Electric Motor, Alternator, Accumulator, Tesla Coil, Rolling Mill |
| Enchanting | **Enchantment Industry**: Blaze Enchanter, Blaze Forger, Printer, Mechanical Grindstone. Enchant nivel IV |
| Energia | Electric Motor (FE→SU — bridge con RS), Alternator (SU→FE), Accumulator (storage FE) |
| Storage | **Refined Storage completo**: Controller, Disk Drive, Grid, Autocraft, Importer/Exporter, Wireless Grid |
| RPG | Enchants magicos de Spell Power, Pociones Spell Power, Soul/Lightning runes, Large Rune Pouch (Nether Star) |
| Jewelry | Netherite variants de todas las gemas |
| Dungeon | **Llave Maestra**. 15-20 salas, 3 affixes, 15% unique jewelry |
| Champions | 15% overworld, 2-3 affixes |
| Boss | El Arquitecto (3,400 HP) |
| Decoracion | Nada nuevo (Create Deco en Ch6) |

### Create routing (nuevo en Ch5)

| Bloque | Funcion |
|--------|---------|
| **Chain Conveyor** | Belt de alta velocidad para distancias largas dentro de la fabrica |
| **Frogport** | Router de items — separa por tipo/destino en junctions de belts |
| **Postbox** | Envia items a una direccion especifica (como correo automatico) |

Juntos forman el backbone de routing intra-fabrica. Complementan (no reemplazan) el Stock system de Ch4.

### Bridge Create ↔ Refined Storage
```
Create Alternator → FE → RS Controller (Create alimenta RS)
RS Exporter → items → Create Belt → procesamiento
RS Importer → productos terminados → RS storage
Electric Motor → FE → SU (RS alimenta Create remoto)
```

### Trenes (progresivos)

| Modo | Este capitulo | Como |
|------|---------------|------|
| Manual | Si | Jugador conduce con controles |
| Semi-auto | Si | Conductor (Blaze Burner) + Schedule basico |
| Full auto | Ch6+ | Schedule 24/7 con rutas complejas |

### Pipeline tipico Ch5
```
Tren: Nether mine → Estacion A → Overworld → Estacion B → Fabrica
Frogport: items del tren → separa por tipo → lineas de procesamiento
Chain Conveyor: mover rapido entre areas distantes de la fabrica
Rolling Mill: Iron/Copper/Brass → Rods y Wire
RS Controller → Autocraft patterns → Exporter → Create Belt
Enchantment Industry: Blaze Enchanter → enchanted books automaticos
```

## Entrega al Space Elevator — Ch5: "La Red"

> Detalle completo: [progression.md → Entregas](../mechanics/progression.md#capitulo-5-la-red)

Escala industrial. Trenes y logistics necesarios (~1041 items total — pico de volumen).

| Item | Cant. | Nuevo/Acumulado |
|------|-------|-----------------|
| Cooking Pot recipe | 64 | Acumulado |
| Keg product | 32 | Acumulado |
| Sheets (iron+copper+brass) | 256 | Acumulado (x2) |
| Flour | 128 | Acumulado |
| Crushed ore | 128 | Acumulado |
| Slicer output | 64 | Acumulado |
| Wok + Baker's recipes | 64 | Acumulado |
| Iron/Copper/Brass Rod | 64 | **Nuevo** — Rolling Mill |
| Wire (cualquier tipo) | 64 | **Nuevo** — Rolling Mill |
| Enchanted Book III+ | 16 | **Nuevo** — Enchantment Industry |
| Comida exotica (crops Ch5) | 64 | **Nuevo** |
| Comida variada (min 38 tipos) | 32 | Acumulado |
| Quartz | 64 | **Nuevo** — material para RS |
| Nucleo del Arquitecto | 1 | **Boss** |

256 sheets + 128 crushed + 64 rods + 64 wire = volumen que pide trenes y routing avanzado.

## Quests (~52)

### Historia/Tutorial (12)
1. Tutorial: Trenes — construir Track y Station
2. Tutorial: Trenes — montar y conducir manualmente
3. Tutorial: Schedule — conductor automatico (Blaze Burner)
4. Tutorial: Chain Conveyor — belt de alta velocidad
5. Tutorial: Frogport — rutear items por tipo
6. Tutorial: Postbox — enviar items a direccion especifica
7. Tutorial: Rolling Mill (C&A) — rods y wire
8. Tutorial: Electric Motor / Alternator — bridge FE↔SU
9. Tutorial: Refined Storage — Controller, Disk, Grid
10. Tutorial: RS Autocraft — Pattern + Crafter
11. Tutorial: Enchantment Industry — Blaze Enchanter
12. Tutorial: Llave Maestra de dungeon

### Cocina (6)
1. Cocinar con ingredientes exoticos (dragon fruit, star fruit, etc.)
2. Preparar 3 recetas exoticas
3. Automatizar receta con RS Exporter → Create Belt → procesamiento
4. Produccion en masa con Slice&Dice + RS Importer
5. Comer 70 comidas diferentes (Recetario: +5 corazones)
6. Pipeline completo: RS → Create → workstation → RS

### Farming (5)
1. Plantar 4 crops exoticos (Croptopia)
2. Plantar todos los 8 exoticos
3. Granja full auto con RS Importer conectada
4. Matar al Wither (Nether Star para Large Rune Pouch)
5. Optimizar produccion de crops con Create Harvester + RS

### Dungeon (4)
1. Craftear primera Llave Maestra
2. Completar dungeon Maestra (15-20 salas)
3. Obtener unique jewelry en Maestra (15% chance)
4. Sobrevivir dungeon con 3 affixes champions

### Combate/RPG (5)
1. Aplicar enchant magico a arma (Spell Power)
2. Preparar Pocion de Spell Power
3. Auto-enchantar con Enchantment Industry
4. Equipar Netherite jewelry
5. Derrotar a El Arquitecto

### Exploracion (5)
1. Construir primera ruta de tren funcional
2. Tren semi-auto con Schedule (Blaze Burner conduce)
3. Conectar base con Nether via tren
4. Setup RS Wireless Grid para acceso remoto
5. Explorar con tren a biomas lejanos

### Construccion (5)
1. Sala de RS: Controller + Disk Drive + Grid
2. Setup Enchantment Industry funcional
3. Estacion de tren con carga/descarga automatica
4. Setup Chain Conveyor + Frogport en fabrica
5. Fabrica integrada: Create + RS bridge funcionando

### Coleccion (5)
1. Auto-enchantar 3 piezas de equipo con Enchantment Industry
2. Tener set completo con enchants Spell Power
3. Coleccion de 3 unique jewelry de dungeons
4. 64 Quartz entregados (prueba de mining)
5. Completar entrega al Space Elevator Ch5
