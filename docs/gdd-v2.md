# Game Design Document v2 - Modpack Servo
## Minecraft 1.21.1 NeoForge - Modpack Progresivo

---

## Documentacion detallada

Este GDD es el **overview**. El detalle vive en docs especificos:

| Tema | Doc |
|------|-----|
| Mecanicas (21 docs) | `docs/mechanics/*.md` |
| Capitulos (8 docs) | `docs/chapters/ch*.md` |
| Balance | `docs/balance/*.md` |
| Mods y justificaciones | [design/mod-decisions.md](design/mod-decisions.md) |
| Arquitectura multi-mod | [architecture.md](architecture.md) |
| Distribucion por capitulo | [mechanics/progression.md](mechanics/progression.md#distribucion-de-contenido-por-capitulo) |
| RPG Series datos | [mod-data/rpg-series-content.md](mod-data/rpg-series-content.md) |
| Estado de mods custom | `docs/status/*.md` |
| Tareas pendientes | [TODO.md](TODO.md) |

---

# 1. VISION

Modpack progresivo para hasta 8 jugadores (~240 horas) con 8 capitulos. Combina cocina, farming, automatizacion con Create, dungeons roguelike, bosses custom, y sistema gacha. Inspirado en Satisfactory (Space Elevator = entregas escalonadas).

**Jugadores target**: Hasta 8. Funciona desde 1 jugador.
**Perfil A**: Ama Create, automatizacion, combate, optimizacion de sistemas.
**Perfil B**: Ama cocina, farming, animales, tareas repetitivas satisfactorias.

**Principios**:
- Cada mod justifica su presencia - si no sirve para progresion, no entra (excepcion: cosmeticos)
- Progresion gateada por capitulos (ProgressiveStages)
- Combate: RPG Series (7 clases, spells, skill trees) + Champions Unofficial (16 affixes) + accesorios Jewelry/custom
- Cocina como sistema central - cocinar a mano es divertido, Create escala para entregas
- Automatizacion tiene PROPOSITO: empacar y entregar items al Space Elevator
- Gacha como engagement hook, no pay-to-win
- Dungeons accesibles desde Ch1, dificultad escala con tier de llave
- Dificultad escala por **stage del jugador mas cercano** (per-player via ProgressiveStages)
- Variedad en cada capitulo: cocina + farming + dungeon + combate + construccion

---

# 2. MODS

~80 mods en ~10 categorias + 7 mods custom. Lista completa con justificaciones: [mod-decisions.md](design/mod-decisions.md).

## Mods de contenido (resumen)

| Categoria | Mods clave |
|-----------|-----------|
| Cocina/Farming | Farmer's Delight, Brewin' & Chewin', Expanded Delight, Croptopia (solo ingredientes), Aquaculture |
| Automatizacion | Create + C&A + Deco + Enchantment Industry, Slice & Dice |
| Storage | Storage Drawers → Tom's Storage → Refined Storage |
| RPG/Clases | Spell Engine, Spell Power, Wizards, Rogues, Paladins, Skill Tree, Runes, Jewelry (11 JARs) |
| Combate | Champions Unofficial (16 affixes) |
| Gacha | Bloo's Gacha Machine |
| Decoracion | Macaw's Furniture/Windows, Refurbished Furniture |
| QoL | Waystones, Xaero's Maps, Jade, Backpacks, Carry On, YIGD |
| Multimedia | WATERFrAMES (video screens) |
| Progression | ProgressiveStages, FTB Quests/Teams/Chunks |

## Mods custom (7 JARs)

Arquitectura completa: [architecture.md](architecture.md). Estado por mod: `docs/status/`.

| Mod | Funcion | Estado |
|-----|---------|--------|
| servo_packaging | Cajas de carton, Empacadora (standalone) | CODIGO COMPLETO |
| servo_delivery | Terminal de Entrega / Space Elevator (deps: packaging) | in-progress |
| servo_cooking | 4 workstations: Blender, Moldes, Drink Maker, Horno Avanzado | pendiente |
| servo_create | Compat Create ↔ packaging (funnels, deployers) | pendiente |
| servo_mart | Tienda catalogo dinamico tipo IKEA (deps: packaging) | pendiente |
| servo_dungeons | Bosses (8), dungeons (4 tiers), llaves, dimension void (deps: GeckoLib) | pendiente |
| servo_core | Glue: Pepe Coins, accesorios custom, gacha pity, champions post-processing | scaffold |

---

# 3. SISTEMAS CUSTOM

Cada sistema tiene su doc detallado en `docs/mechanics/`. Aqui el resumen ejecutivo.

## 3.1 Space Elevator (Terminal de Entrega)

Multibloque 3x3 inspirado en Satisfactory. Entregas items empacados para avanzar de capitulo. Screen muestra progreso, puertos aceptan automatizacion (hopper, Create, RS). Items de entrega son CAJAS EMPACADAS, no items sueltos.

Detalle: [mechanics/space-elevator.md](mechanics/space-elevator.md) | Implementacion: [status/servo-delivery.md](status/servo-delivery.md)

## 3.2 Sistema de Empaque (Cajas de Envio)

Carton Plano → doblar en Empacadora → colocar Caja Abierta → llenar con items → se sella auto a 16 items → llevar al Space Elevator. Hoppers automatizan ambos pasos. Create compat via servo_create.

**Por que funciona**: Perfil B cocina a mano y empaca a mano. Perfil A automatiza con Create. Ambos contribuyen al Space Elevator.

Detalle: [mechanics/packaging.md](mechanics/packaging.md) | Implementacion: [status/servo-packaging.md](status/servo-packaging.md)

## 3.3 Workstations de cocina (4 custom)

| Workstation | Cap | Mecanica clave |
|-------------|-----|---------------|
| Blender | Ch2 | 4 ingredientes + 1 fluido. Smoothies, salsas, jugos |
| Moldes de Postres | Ch2 | Moldes coleccionables (7 tipos, 5 rarezas). Calidad 1-5 estrellas |
| Drink Maker | Ch3 | 1 base + 3 extras + vaso. ORDEN importa. Bebidas con efectos |
| Horno Avanzado | Ch3 | 3 ingredientes, 3 modos, TEMPERATURA slider 1-10 (descubrimiento) |

Detalle: [mechanics/cooking.md](mechanics/cooking.md)

## 3.4 ServoMart (IKEA)

Tablet/catalogo donde "compras" muebles con tokens o materiales. Te llega una Caja de Carton que al abrirla aparece el mueble. Opcion de caja misteriosa (gacha de muebles separado).

Detalle: [mechanics/servomart.md](mechanics/servomart.md)

## 3.5 Dungeons y Llaves

UN solo sistema con 4 tiers de llave (Basica → Avanzada → Maestra → Del Nucleo). Cada llave se consume (1 llave = 1 run). Basica siempre crafteable barata. Llaves superiores requieren drops del tier anterior (cadena de progresion anti-skip). Dimension void, generacion procedural, multiplayer. Muerte = pierdes loot de la run, gear equipado T2+ es soulbound.

Detalle: [mechanics/dungeons.md](mechanics/dungeons.md) | Muerte: [mechanics/death-system.md](mechanics/death-system.md)

## 3.6 Bosses (8, uno por capitulo)

Cada boss tiene tematica unica, mecanicas especiales, y HP que dobla por capitulo (200 → 25,600). Scaling multiplayer: HP * (1 + (players-1)*0.3). Boss Key crafteable consume al usar. Drops individuales por participante (como MMO).

Detalle: [mechanics/bosses.md](mechanics/bosses.md)

## 3.7 Champions

Mobs con affixes tipo Diablo (16 affixes en 4 categorias: ofensivos, defensivos, control, dungeon-exclusivos). Spawn rate y max affixes escalan por stage del jugador mas cercano. servo_core post-procesa via API publica.

Detalle: [mechanics/champions.md](mechanics/champions.md)

## 3.8 RPG (RPG Series)

7 clases con 8 especializaciones. 4 tiers de gear (T0 stone → T4 custom boss drops). Melee primero en Ch2 (materiales accesibles), magia en Ch3 (requiere Nether). Skill Tree en Ch4.

Detalle: [mechanics/rpg-classes.md](mechanics/rpg-classes.md) | Datos: [mod-data/rpg-series-content.md](mod-data/rpg-series-content.md)

## 3.9 Accesorios (Curios API)

6 slots: Ring x2 + Necklace (Jewelry mod) + Belt + Back + Feet (servo_core custom). Accesorios custom en 5 tiers con boosts de 5% a 70%.

Detalle: [mechanics/accessories.md](mechanics/accessories.md) | Jewelry: [mechanics/jewelry.md](mechanics/jewelry.md)

## 3.10 Gacha y Tokens

Gacha Machine unica (quest reward Ch1). Usa Pepe Coins (150/capitulo). Pull principal = 10 tokens, pity a 50 pulls. Gacha de muebles separado (5 tokens). Tokens de: quests, champions, bosses, dungeons, primera cocina.

Detalle: [mechanics/gacha.md](mechanics/gacha.md) | Tokens: [mechanics/tokens.md](mechanics/tokens.md)

## 3.11 Game Loop

```
Quest Book → Obtener materiales (farm/mine/dungeon)
  → Procesar (cocinar/craftear/empacar)
  → Entregar al Space Elevator
  → Boss de capitulo → Siguiente capitulo
```

Sub-loops: Dungeon (craftear llave → run → loot), Cocina (nuevo crop → descubrir recetas → Recetario), RPG (nuevo tier → craftear → dungeon → unique jewelry), Automatizacion (diseñar fabrica → optimizar → tren).

Detalle: [mechanics/game-loop.md](mechanics/game-loop.md)

## 3.12 Tren al Nether

Create trains cross-dimensional (built-in). Manual Ch4 → Semi-auto Ch5 → Full auto Ch6+. ~18 chunks forzados con FTB Chunks. Bridge natural: 7+ recetas de cocina usan ingredientes Nether.

Detalle: [mechanics/nether.md](mechanics/nether.md)

---

# 4. PROGRESION

8 capitulos gateados por ProgressiveStages. Cada capitulo requiere: Boss derrotado + Entrega al Space Elevator completada.

## Stages y mecanismos de bloqueo

Detalle completo (stages, mecanismos, gaps, filosofia): [mechanics/progression.md](mechanics/progression.md)

## Distribucion de contenido por capitulo

Tablas de que se desbloquea en cada capitulo (crops, cocina, Create, storage, RPG, dungeons, decoracion, enchantments): [mechanics/progression.md → Distribucion](mechanics/progression.md#distribucion-de-contenido-por-capitulo)

## Capitulos

Quests detallados por capitulo en `docs/chapters/`:

| Cap | Tema | Boss | HP | Contenido clave |
|-----|------|------|----|----------------|
| 1 | Tutorial + Survival | Guardian del Bosque | 200 | 12 crops, FD basico, dungeon basica, gacha machine, RPG T0 |
| 2 | Cocina + Clase Melee | Bestia Glotona | 400 | +12 crops, Blender, Moldes, Create basico, Rogue/Warrior, B&C |
| 3 | Engranajes + Magia | Coloso Mecanico | 800 | +10 frutas, Create andesite, Drink Maker, Horno, Wizard/Paladin/Priest, Nether |
| 4 | Horizontes + Especializacion | Locomotora Fantasma | 1,600 | +8 especias, Create brass+trains, Skill Tree, T2, Feasts |
| 5 | La Red + Poder Magico | El Arquitecto | 3,200 | +8 exoticos, RS, Trains avanzados, Enchants magicos, Llave Maestra |
| 6 | Maestria + Netherite RPG | Senor de las Cosechas | 6,400 | +8 avanzados, T3 Netherite, enchant V, optimizacion |
| 7 | Profundidades + Coleccion | Nucleo del Dungeon | 12,800 | +6 raros, Llave del Nucleo, unique jewelry, T4 materials |
| 8 | Final + Legendarias | Devorador de Mundos | 25,600 | Todo, T4 custom, mega-entregas, boss 4 fases |

---

# 5. ECONOMIA Y BALANCE

Detalle completo en `docs/balance/`: [combat-scaling.md](balance/combat-scaling.md), [gacha-rates.md](balance/gacha-rates.md), [accessories.md](balance/accessories.md), [rpg-weapon-stats.md](balance/rpg-weapon-stats.md).

## Formulas clave

```
Boss HP base:    HP(ch) = 200 * 2^(ch-1)
Boss HP spawn:   HP * (1 + (players-1)*0.3)  [fijo al spawn]
Boss dmg base:   dmg(ch) = 5 * 1.35^(ch-1)
Boss dmg spawn:  dmg * (1 + (players-1)*0.15) [fijo al spawn]
Gacha pity:      rate = base + (increment * failures), hard pity 50
Champion HP:     mob_hp * 2.5 (1 affix), * 3.75 (2), * 5 (3)
Accesorio boost: 5%, 12%, 22%, 40%, 70% por tier
Nutrition bonus: base * (1 + 0.2*(mold_stars-1))
```
