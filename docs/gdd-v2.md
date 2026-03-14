# Game Design Document v2 - Modpack Servo
## Minecraft 1.21.1 NeoForge - Modpack Progresivo

---

## Indice de mecanicas detalladas

Cada mecanica tiene su propio doc en `docs/mechanics/`. El GDD es el overview, los docs de mecanicas tienen el detalle completo.

| Mecanica | Doc |
|----------|-----|
| Progression (ProgressiveStages) | [mechanics/progression.md](mechanics/progression.md) |
| Space Elevator | [mechanics/space-elevator.md](mechanics/space-elevator.md) |
| Packaging (Cajas de Envio) | [mechanics/packaging.md](mechanics/packaging.md) |
| Cooking (Sistema de cocina) | [mechanics/cooking.md](mechanics/cooking.md) |
| Dungeons | [mechanics/dungeons.md](mechanics/dungeons.md) |
| Bosses | [mechanics/bosses.md](mechanics/bosses.md) |
| Champions | [mechanics/champions.md](mechanics/champions.md) |
| RPG Classes | [mechanics/rpg-classes.md](mechanics/rpg-classes.md) |
| Accessories (Curios) | [mechanics/accessories.md](mechanics/accessories.md) |
| Gacha | [mechanics/gacha.md](mechanics/gacha.md) |
| Tokens (Pepe Coins) | [mechanics/tokens.md](mechanics/tokens.md) |
| Create y Automatizacion | [mechanics/create-automation.md](mechanics/create-automation.md) |
| Storage Progression | [mechanics/storage.md](mechanics/storage.md) |
| ServoMart (IKEA) | [mechanics/servomart.md](mechanics/servomart.md) |
| Quest System | [mechanics/quests.md](mechanics/quests.md) |
| Death System | [mechanics/death-system.md](mechanics/death-system.md) |
| Enchantments | [mechanics/enchantments.md](mechanics/enchantments.md) |
| Jewelry | [mechanics/jewelry.md](mechanics/jewelry.md) |
| Runes | [mechanics/runes.md](mechanics/runes.md) |
| Game Loop | [mechanics/game-loop.md](mechanics/game-loop.md) |
| Nether | [mechanics/nether.md](mechanics/nether.md) |

## Capitulos detallados (con quests aproximadas)

| Capitulo | Doc |
|----------|-----|
| Ch1: Raices | [chapters/ch1-raices.md](chapters/ch1-raices.md) |
| Ch2: La Cocina | [chapters/ch2-cocina-melee.md](chapters/ch2-cocina-melee.md) |
| Ch3: Engranajes | [chapters/ch3-engranajes-magia.md](chapters/ch3-engranajes-magia.md) |
| Ch4: Horizontes | [chapters/ch4-horizontes.md](chapters/ch4-horizontes.md) |
| Ch5: La Red | [chapters/ch5-red-poder.md](chapters/ch5-red-poder.md) |
| Ch6: Maestria | [chapters/ch6-maestria.md](chapters/ch6-maestria.md) |
| Ch7: Profundidades | [chapters/ch7-profundidades.md](chapters/ch7-profundidades.md) |
| Ch8: Final | [chapters/ch8-final.md](chapters/ch8-final.md) |

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
- Capitulo introductorio que enseña mecanicas custom
- Variedad en cada capitulo: cocina + farming + dungeon + combate + construccion

---

# 2. MODS - LISTA FINAL

## 2.1 Mods de contenido (gameplay)

### Cocina y Farming
| Mod | Funcion | Justificacion |
|-----|---------|---------------|
| Farmer's Delight | Core de cocina: Cooking Pot, Cutting Board, Stove, Skillet. 92 items, 4 crops | La base de toda la cocina |
| Brewin' And Chewin' | Fermentacion, quesos, Keg. 36 items | Bebidas alcoholicas y quesos |
| Expanded Delight | Mas crops (asparagus, chili, cranberry, cinnamon), jugos. 60 items | Expande variedad de FD |
| Croptopia | 60+ crops crudos (RECETAS ELIMINADAS, solo ingredientes) | Variedad de cultivos |
| Aquaculture 2 | 30+ peces, neptunium, tackle box. Recetas usan tag generico #c:fish | Pesca expandida, cualquier pez sirve |

### Automatizacion
| Mod | Funcion | Justificacion |
|-----|---------|---------------|
| Create | Engranajes, belts, prensas, mixers, trenes. 204 items, 701 bloques | Core de automatizacion |
| Create Crafts & Additions | Motor electrico, alternador, cables (bridge FE) | Conecta Create con RS |
| Create Deco | 389 bloques decorativos industriales | Estetica de fabrica |
| Create Enchantment Industry | Auto-enchanting, XP liquido, printer | Automatizar enchants |
| Slice and Dice | Automatiza Cutting Board y Cooking Pot con Create | Puente Create + cocina |

### Storage (progresivo)
| Mod | Funcion | Capitulo |
|-----|---------|----------|
| Storage Drawers | Cajones bulk, 1 item por cajon, visual | Ch2 |
| Tom's Simple Storage | Terminal que conecta cofres cercanos, busqueda | Ch3 |
| Refined Storage | Storage digital, autocraft, wireless | Ch5 |

### RPG y Clases (RPG Series by Daedelus - 3er pilar)
| Mod | Items | Funcion |
|-----|-------|---------|
| Spell Engine | 3 | Framework de spells + Spell Binding Table |
| Spell Power | 76 | Pociones/flechas/enchants de 19 tipos de spell power |
| Wizards | 55 | Clases Wizard (Arcane/Fire/Frost): staffs, wands, robes, Wizard Merchant NPC |
| Rogues & Warriors | 60 | Clases Rogue (daggers, sickles) + Warrior (double axes, glaives), Arms Dealer NPC |
| Paladins & Priests | 71 | Clases Paladin (claymores, hammers, shields) + Priest (holy staffs/wands), Monk NPC |
| Skill Tree | 0 | UI del arbol de habilidades |
| MRPGC Skill Tree | 0 | 8 especializaciones: Berserker, Deadeye, War Archer, Tundra Hunter, Forcemaster, Air, Earth, Water |
| Runes | 10 | 6 tipos de runa consumible + 3 pouches + Rune Crafting Altar |
| Jewelry | 85 | 6 gemas custom (ores), rings/necklaces por gema, 24 unique loot-only, Jeweler NPC |
| Ranged Weapon API | 10 | Soporte para armas a distancia |

### Exploracion y Dungeons
| Mod | Funcion | Justificacion |
|-----|---------|---------------|
| Supplementaries | Rope, bombs, keys, quiver, slingshot, cages | Herramientas utiles, NECESARIAS en dungeons |
| Lootr | Loot individual por jugador en cofres | Multiplayer justo |

### Gacha y Progresion
| Mod | Funcion | Justificacion |
|-----|---------|---------------|
| Bloo's Gacha Machine | Maquina gacha con capsulas | Engagement hook |
| ProgressiveStages | Bloquea recetas/items por stage | Gating de capitulos |
| FTB Quests | Quest book | Guia de progresion |
| FTB Teams | Sync multiplayer | Progresion compartida |
| FTB Chunks | Claim de chunks | Proteccion de bases |

### Decoracion
| Mod | Funcion | Bloques |
|-----|---------|---------|
| Macaw's Furniture | Muebles decorativos | 652 |
| Macaw's Windows | Ventanas | 310 |
| MrCrayfish Refurbished | Muebles funcionales (nevera, estufa, TV, etc) | 448 |

### Quality of Life
| Mod | Funcion |
|-----|---------|
| Waystones | Teletransporte entre puntos marcados |
| Xaero's Minimap + World Map | Minimapa y mapa completo |
| Jade | Tooltip al mirar bloques/mobs (HP, tipo, etc) |
| Sophisticated Backpacks | Mochilas con upgrades para dungeon runs |
| Carry On | Cargar bloques/cofres con las manos |
| Curios API | Framework para accesorios equipables |

### Multimedia
| Mod | Funcion |
|-----|---------|
| WATERFrAMES | Pantallas de video: Frame, Projector, TV, Big TV. Reproduce YouTube, Twitch, Google Drive, OneDrive. Hasta 16x16 bloques. Multiplayer sync |
| WATERMeDIA | API multimedia (dependencia). Usa LibVLC, pre-empaquetado en Windows |

### Performance (16 mods)
| Mod | Funcion |
|-----|---------|
| Embeddium | Rendering optimizado (Sodium port) |
| Embeddium Extra | Opciones de video tipo OptiFine |
| FerriteCore | Reduccion de uso de RAM (7.0.1+) |
| ModernFix | Fixes de startup y memoria |
| Lithium | Tick/TPS: fisica, IA mobs, colisiones |
| ServerCore | Chunk I/O async, mob caps, lag spike smoothing |
| Clumps | Agrupa orbs de XP |
| EntityCulling | No renderiza entidades fuera de vista |
| ImmediatelyFast | Optimizacion de render adicional |
| Cull Leaves | Oculta caras internas de hojas |
| Krypton FNP | Optimiza networking para multiplayer |
| Packet Fixer | Previene errores de conexion en modpacks pesados |
| LazyDFU Reloaded | Startup 10-30s mas rapido |
| ThreadTweak Reforged | Distribuye carga CPU al inicio |
| NoisiumForked | Generacion de chunks 20-30% mas rapida |

### Shaders (opcional)
| Mod | Funcion |
|-----|---------|
| Iris + Monocle | Shaders con Embeddium (sin Sodium). Shader recomendado: Complementary Reimagined (compatible con WATERFrAMES) |

## 2.2 Mods ELIMINADOS
| Mod | Razon |
|-----|-------|
| Mystical Agriculture | Desbalanceado - cultivar diamantes rompe el loop de explorar/minar. Compite con Create |
| Botany Pots | Redundante con farming manual + Create harvester |
| Bosses of Mass Destruction | Port no oficial, bosses custom son mejores |
| Brutal Bosses | Riesgo de opacar bosses custom |
| Alex's Mobs + Citadel | Crash al cargar + no encaja en 3 pilares de contenido |
| Dimensional Dungeons | Licencia "All Rights Reserved", diseño incompatible. Sistema custom en servo_dungeons |
| Dungeon Crawl | Overworld limpio, dungeons custom en servo_dungeons |
| When Dungeons Arise | Idem |
| Silent Gear / Simply Swords / Better Combat | No mods de combate, vanilla + custom |
| Iron Chests | No existe para 1.21.1 |
| Mekanism / AE2 | No interesan |

## 2.3 Contenido custom (7 mods propios)

> Arquitectura multi-mod completa en `docs/architecture.md`. Los sistemas se distribuyen en 7 JARs.

| Sistema | Mod | Estado |
|---------|-----|--------|
| Sistema de empaque (Cajas de Envio) | servo_packaging | CODIGO COMPLETO |
| Create compat para empacadora | servo_create | pendiente |
| Space Elevator (Terminal de Entrega) | servo_delivery | in-progress (scaffold + GUI completo) |
| 4 workstations de cocina (Blender, Moldes, Drink Maker, Horno Avanzado) | servo_cooking | pendiente |
| ServoMart (IKEA) | servo_mart | pendiente |
| Bosses (8) + Dungeons (4 tiers) + llaves + dimension void | servo_dungeons | pendiente |
| Tokens (Pepe Coin), accesorios custom (belt/back/feet), gacha pity, progression manager | servo_core (glue) | scaffold |
| Champions post-processing | servo_core | pendiente (Fase 7) |
| Recetas Tier 4 RPG | KubeJS | pendiente |
| Rebalanceo Berserker armor | KubeJS | pendiente |

---

# 3. SISTEMAS CUSTOM

## 3.1 Space Elevator (Terminal de Entrega)

Inspirado en Satisfactory. Bloque multibloque 3x3 donde entregas items para avanzar de capitulo.

```
Vista frontal (3 ancho x 3 alto):
[Antena                      ]
[Puerto][Terminal/Screen][Puerto]
[Base  ][Base           ][Base  ]
```

- Screen muestra items requeridos y progreso
- Puertos aceptan: click manual, hopper, Create funnel/belt, RS exporter
- Items se consumen al insertarlos
- Al completar: animacion + FTB Quest auto-complete + stage unlock
- **Items de entrega son CAJAS EMPACADAS**, no items sueltos (ver 3.2)

## 3.2 Sistema de Empaque (Cajas de Envio)

**Implementado en servo_packaging (v0.3.0 — CODIGO COMPLETO). Ver [mechanics/packaging.md](mechanics/packaging.md) para detalle completo.**

**Problema resuelto**: cocinar es divertido a mano, pero las entregas al Space Elevator piden cantidades grandes. Create automatiza la ESCALA, no reemplaza la cocina.

**Como funciona (flujo real)**:
1. Craftear Carton Plano (4 Paper + 1 String = 4 unidades)
2. Doblar en la **Empacadora** (GUI con 2 slots, 40 ticks = 2s): Carton Plano → Caja Abierta
3. **Colocar Caja Abierta** en el piso como bloque (inmersivo, sin GUI)
4. Click derecho con items en mano para llenar (mismo tipo, max 16 items, tag packable)
5. Caja se sella automaticamente al llegar a 16 items → **Caja de Envio** (sellada, con icono visible)
6. Recoger la Caja de Envio y llevarla al Space Elevator

La Empacadora SOLO dobla carton. El empaque de items se hace en la caja colocada en el suelo.
Hoppers nativos automatizan ambos pasos. Create (servo_create) agrega funnels/deployers para pipeline completo.
El Space Elevator SOLO acepta Cajas de Envio, no items sueltos.

**Ejemplo**: el Space Elevator pide "16 Cajas de Tomato Soup". Puedes cocinar las sopas a mano y empacarlas una por una, O automatizar con Create: harvester → belt → Slice&Dice → Cooking Pot → hopper → Caja Abierta (auto-seal) → belt al Space Elevator.

**Por que esto funciona**:
- Jugador que ama cocinar: cocina a mano, empaca a mano. Disfruta el proceso.
- Jugador que ama Create: automatiza todo el pipeline. Disfruta la ingenieria.
- Ambos contribuyen al mismo objetivo (llenar el Space Elevator).
- Create NUNCA es obligatorio para progresar, solo hace las entregas mas eficientes.

## 3.3 Workstations de cocina

### Batidora/Blender (Ch2)
- 4 ingredientes + 1 fluido + 1 output
- 100 ticks (5 seg) proceso
- Sin calor requerido
- Recetas: smoothies, salsas, jugos, mezclas
- Compatible con: Hopper, Create Funnel, RS Exporter

### Moldes de Postres (Ch2)
- 1 masa + 1 relleno + 1 molde (no consume) + 1 output
- Moldes coleccionables (7 tipos, 5 rarezas)
- Calidad 1-5 estrellas por molde (afecta stats de comida)
- Formula: nutrition = base * (1 + 0.2 * (stars - 1))
- Moldes de: gacha, bosses, quests

### Drink Maker (Ch3)
- 1 base + 3 extras + 1 vaso + 1 output
- ORDEN DE EXTRAS IMPORTA (mecanica repetitiva satisfactoria)
- Bebidas dan efectos de pocion temporales
- Cafe=Speed, Te=Regen, Chocolate=Resistance, Smoothie=Saturation

### Horno Avanzado (Ch3)
- 3 ingredientes + 1 output
- 3 modos: Hornear, Rostizar, Gratinar
- TEMPERATURA slider 1-10 (correcta=perfecto, +3=quemado)
- Receta muestra "Media-Alta" no el numero exacto (descubrimiento)
- Requiere calor: Blaze Burner, lava, o fuego debajo

## 3.4 ServoMart (Sistema IKEA)

Bloque custom tipo tablet/computadora. Abres un catalogo de muebles:
- Muebles se desbloquean por capitulo
- "Compras" con tokens o materiales
- Te llega una Caja de Carton que colocas y al abrirla aparece el mueble
- Muebles de Macaw's y Refurbished Furniture se obtienen por aqui

**Muebles funcionales de Refurbished (afectan progresion)**:
- Nevera: conserva comida (no se pudre)
- Estufa: workstation alternativa para cocinar
- Fregadero: lava items (crafting ingredient)
- Ciertas recetas Ch4+ requieren cocina equipada con nevera+estufa+fregadero

**Gacha de ServoMart**: opcion de "caja misteriosa" con mueble random. Separado del gacha de artefactos.

## 3.5 Llaves de Dungeon (Sistema custom servo_dungeons)

**UN solo sistema de dungeons con 4 tiers de llave.** No hay dungeons "normales" vs "especiales" — el tier de la llave determina la dificultad, duracion, y loot.

### Reglas de llaves
- Cada entrada **consume 1 llave** (1 llave = 1 run)
- La Llave Basica siempre es crafteable barata → nunca te quedas sin acceso
- Llaves superiores cuestan mas → decision riesgo/recompensa
- Primera llave se obtiene gratis como quest reward en Ch1
- Muerte en dungeon = pierdes tu inventario (roguelike), pero la llave ya se consumio

### Crafteo de llaves

| Llave | Materiales | Costo aprox | Disponible |
|-------|-----------|-------------|------------|
| Basica | 4 Iron Ingot + 2 Gold Ingot | Barato, crafteable infinitamente | Ch1 |
| Avanzada | 2 Diamond + 4 Blaze Rod + 1 Ender Pearl + drop de boss Ch2 | Medio, requiere Nether | Ch3 |
| Maestra | 4 Diamond + 2 Netherite Scrap + 1 Nether Star fragment + 1 Esencia de Dungeon (loot de Avanzada) | Alto, requiere dungeon previa | Ch5 |
| Del Nucleo | 2 Netherite Ingot + 4 Esencia de Dungeon + 1 Cristal de Boss Ch6 | Muy alto, requiere dungeons + boss | Ch7 |

**Nota**: cada tier de llave necesita un drop del tier anterior o de boss, creando una cadena de progresion natural.

### Estructura de dungeon por tier

| Llave | Salas | Duracion | Champions | Boss de dungeon |
|-------|-------|----------|--------|----------------|
| Basica | 5-7 | ~15-20 min | Max 1 affix, 15% spawn | No |
| Avanzada | 10-14 | ~30-40 min | Max 2 affix, 25% spawn | No |
| Maestra | 15-20 | ~45-60 min | Max 3 affix, 30% spawn | No |
| Del Nucleo | 20-25 | ~60-75 min | Max 3 affix + exclusivos (Teleporter, Invisible, Shield), 35% spawn | Si (boss de dungeon al final) |

### Loot table por tier (items reales de nuestros mods)

**Basica (Ch1+)** — recursos para empezar:
| Drop | Fuente | Chance |
|------|--------|--------|
| Ores vanilla (iron, gold, diamond) | cofres | Comun |
| Pepe Coins (5-10) | cofres + champions | Garantizado |
| Runas basicas (Arcane, Frost, Fire) | cofres | 30% |
| Enchanted Books nivel I-II | cofres | 20% |
| Accesorio custom T1 (belt/back/feet) | cofres raros | 10% |
| Molde de postre (1-2 estrellas) | cofres raros | 5% |

**Avanzada (Ch3+)** — todo lo de Basica +:
| Drop | Fuente | Chance |
|------|--------|--------|
| Pepe Coins (10-20) | cofres + champions | Garantizado |
| Materiales Nether (Blaze Rod, Nether Wart, Quartz) | cofres | Comun |
| RPG gear T1-T2 (armas/armor pre-crafteadas) | cofres raros | 15% |
| Enchanted Books nivel II-III + Spell Power | cofres | 20% |
| Accesorio custom T2 | cofres raros | 10% |
| **Esencia de Dungeon** (custom, para craftear Llave Maestra) | champions | 15% por champion |
| Unique Jewelry (de las 24 loot-only) | cofre final de run | 5% |

**Maestra (Ch5+)** — todo lo de Avanzada +:
| Drop | Fuente | Chance |
|------|--------|--------|
| Pepe Coins (20-35) | cofres + champions | Garantizado |
| Materiales raros (Netherite Scrap, Nether Star fragments) | cofres | 15% |
| RPG gear T2-T3 | cofres raros | 15% |
| Enchanted Books nivel III-IV + Spell Power avanzado | cofres | 25% |
| Accesorio custom T3 | cofres raros | 12% |
| Esencia de Dungeon (x2-3) | champions | 25% por champion |
| Unique Jewelry | cofre final de run | 15% |
| Soul/Lightning runes | cofres | 20% |
| Moldes 4-5 estrellas | cofres raros | 8% |

**Del Nucleo (Ch7+)** — todo lo de Maestra +:
| Drop | Fuente | Chance |
|------|--------|--------|
| Pepe Coins (35-50) | cofres + champions | Garantizado |
| Fragmento de Cristal del Nucleo (custom, para T4 RPG) | boss de dungeon | Garantizado (1-2) |
| RPG gear T3 (pre-enchantado) | cofres raros | 20% |
| Accesorio custom T4 | boss de dungeon | 25% |
| Unique Jewelry | boss de dungeon | **Garantizado 1** |
| Unique Jewelry adicional | cofres raros | 20% |
| T4 crafting materials | cofres raros + boss | 15% |

### Items exclusivos de dungeon (no se obtienen de otra forma)

| Item | Tier minimo | Para que sirve |
|------|------------|----------------|
| **Esencia de Dungeon** | Avanzada | Craftear Llave Maestra y Llave del Nucleo |
| **Unique Jewelry** (24 items de Jewelry mod) | Avanzada (5%), mejor en tiers altos | Mejor equipo de rings/necklaces del juego |
| **Fragmento de Cristal del Nucleo** | Del Nucleo | Craftear equipo RPG Tier 4 (endgame) |
| **Accesorio custom T4-T5** (belt/back/feet) | Maestra/Nucleo | Mejores accesorios del juego |
| **Moldes 4-5 estrellas** | Maestra/Nucleo | Mejores moldes de postres |

### Por que el jugador NECESITA ir a dungeons superiores

El game loop obliga a subir de tier de dungeon porque:
1. **Llaves superiores requieren drops de dungeons previas** (Esencia de Dungeon solo cae en Avanzada+)
2. **Equipo RPG T4 requiere Fragmentos del Nucleo** (solo en dungeon Del Nucleo)
3. **Unique Jewelry no tiene crafteo** — solo cae en dungeons, y la tasa mejora con el tier
4. **Accesorios T4-T5 son exclusivos** de dungeons altas
5. **Quest de progresion** piden items especificos de dungeon (el Space Elevator puede pedir Esencia de Dungeon)

El jugador nunca va a una dungeon "por explorar" — siempre tiene un objetivo concreto: necesita un material, un accesorio, o una unique jewelry para su build.

### Muerte en dungeons (servo_dungeons custom)

**Sistema simple**: gear equipado se queda contigo, loot de la run se pierde.

**Items Soulbound** (NUNCA se pierden):
- Armor equipada (4 piezas)
- Arma principal equipada
- Rune Pouch equipada
- Accesorios equipados (Curios: rings, necklaces, belt, back, feet)
- Nota: Soulbound es una propiedad que se aplica automaticamente al gear T2+. Items T0-T1 NO son soulbound.

**Items que SÍ se pierden al morir en dungeon**:
- Loot recogido en esa run (ores, tokens, drops de cofres)
- Comida y pociones que llevabas
- Bloques y materiales sueltos
- Llaves extra en inventario

**Comportamiento por zona**:
| Zona | Al morir | Que pasa |
|------|----------|----------|
| Overworld | YIGD tumba normal | Nada se pierde (tumba con todo, compas para encontrarla) |
| Nether | YIGD tumba normal | Nada se pierde |
| Dungeon | **Roguelike** | Soulbound se queda, todo lo demas se pierde. Dungeon se limpia |

**Caso multiplayer**:
- Jugador A muere → respawnea en overworld con Soulbound items. Pierde loot de la run.
- Jugador B sigue en dungeon → puede completarla solo o morir tambien.
- Cuando 0 jugadores quedan en la dungeon → servo_dungeons marca chunks para limpieza.

### Loot individual (bosses y cofres)

**Cofres de dungeon**: Lootr genera loot INDIVIDUAL por jugador. Cada jugador abre el mismo cofre pero ve diferente loot. No hay pelearse por drops.

**Boss de dungeon/capitulo**: servo_dungeons implementa drops individuales por participante. Cada jugador que participo en el kill recibe su propio drop instanciado (como un MMO). Si 2 jugadores matan al boss del Nucleo, AMBOS reciben 1-2 Fragmentos de Cristal independientemente.

**Cuantos boss kills para T4 completo**: Una pieza T4 cuesta ~3 Fragmentos de Cristal del Nucleo. Un set completo (arma + 4 armor) = ~15 fragmentos. Boss da 1-2 por kill → ~8-10 runs del Nucleo. Jugando en pareja, ambos reciben drops → ~8-10 runs juntos equipan a los 2.

### Anti-skip de tiers de dungeon

Un jugador NO puede saltarse tiers farmeando dungeons basicas porque:
1. **Esencia de Dungeon NO dropea en Basica** — solo en Avanzada+
2. **Llave Avanzada requiere drop de boss Ch2** — no la puedes craftear sin matar al boss
3. **Llave Maestra requiere Esencia** — necesitas haber hecho Avanzadas
4. **Llave del Nucleo requiere Cristal de Boss Ch6** — imposible sin estar en Ch7

Farmear 100 Basicas te da muchos tokens y ores, pero jamas los materiales para subir de tier.

### Salas con puzzles de Supplementaries
- Salas con pozos → necesitas Rope para bajar
- Paredes agrietadas → necesitas Bomb para abrir paso secreto
- Puertas cerradas → necesitas Key de Supplementaries
- Esto hace que Supplementaries sea NECESARIO, no solo decorativo

## 3.6 Game Loop (ciclo de juego)

```
┌─────────────────────────────────────────────────┐
│                CICLO POR CAPITULO                │
│                                                  │
│  1. QUEST BOOK                                   │
│     └─ Ve lo que necesitas: item, comida,        │
│        material, entrega al Space Elevator       │
│                                                  │
│  2. OBTENER MATERIALES (segun lo que pida)       │
│     ├─ Farming/mining → overworld                │
│     ├─ Nether → ingredientes cocina/RPG          │
│     ├─ Dungeon → drops exclusivos (ver 3.5)      │
│     └─ Automatizar → Create (opcional, escala)   │
│                                                  │
│  3. PROCESAR                                     │
│     ├─ Cocinar → workstations (a mano = divertido│
│     ├─ Craftear → RPG gear, accesorios, llaves   │
│     └─ Empacar → Cajas de Envio para entregas    │
│                                                  │
│  4. ENTREGAR al Space Elevator                   │
│     ├─ Manual: caminar con cajas                 │
│     ├─ Tren: carga → transporta → descarga       │
│     └─ Create pipeline: auto (Ch5+)              │
│                                                  │
│  5. BOSS DE CAPITULO                             │
│     └─ Craftear Boss Key → pelear → drop          │
│                                                  │
│  6. COMPLETAR CAPITULO                           │
│     ├─ Delivery completa + Boss derrotado        │
│     ├─ ProgressiveStages desbloquea siguiente    │
│     └─ Nuevos mods, recetas, tiers, dungeons     │
│                                                  │
│  REPETIR con contenido mas dificil               │
└─────────────────────────────────────────────────┘
```

### Sub-loops que alimentan el ciclo principal

**Loop de Dungeon** (cuando necesitas drops exclusivos):
```
Necesito item exclusivo → crafteo llave del tier correcto
  → entro a dungeon (arriesgo inventario)
  → sobrevivo y saco loot / muero y pierdo items
  → uso el drop para craftear/quest/entregar
```

**Loop de Cocina** (constante, Perfil B ama esto):
```
Nuevo crop desbloqueado → descubro recetas → cocino variedad
  → FTB Quests "Recetario" da corazones bonus (quest rewards) → empaco para entregas
  → Create automatiza escala (Perfil A)
```

**Loop de RPG** (progresion de clase):
```
Desbloqueo tier de gear → crafteo armor/arma → mejoro build
  → entro a dungeon mas dificil → looteo unique jewelry
  → preparo boss → derroto boss → siguiente tier
```

**Loop de Automatizacion** (Perfil A ama esto):
```
Necesito producir en masa → diseño fabrica Create
  → optimizo pipeline → conecto a RS (Ch5+)
  → tren transporta materiales → Space Elevator recibe
```

## 3.7 Tren al Nether (Create cross-dimensional)

### Como funciona (built-in de Create)
- Tracks van directo al portal Nether → aparece track fantasma del otro lado
- Requisito unico: alguien cruzo el portal manualmente una vez
- Los trenes viajan por chunks NO cargados sin problema (se trackean en el grafo abstracto)
- Solo las ESTACIONES necesitan chunks cargados para cargar/descargar

### Modos de operacion (progresivos)

| Modo | Capitulo | Como | Chunks forzados |
|------|----------|------|-----------------|
| **Manual** | Ch4 | Jugador conduce el tren, cruza portal | 0 (el jugador carga chunks) |
| **Semi-auto** | Ch5 | Conductor (Blaze Burner) + Schedule. Jugador inicia | ~18 (FTB Chunks en estaciones) |
| **Full auto** | Ch6+ | Tren va y viene con schedule 24/7 | ~18 (FTB Chunks) |

**~18 chunks forzados (9 por estacion) es insignificante** para un server de 12GB. Un jugador explorando carga 100+.

### Que procesar en el Nether con Create

| Proceso | Input | Output | Para que |
|---------|-------|--------|----------|
| Lava Power | Hose Pulley en lago de lava | Energia masiva (Steam Engine) | Alimentar toda la fabrica Nether |
| Gold Processing | Nether Gold Ore + Crushing Wheels | Gold Ingots + Nether Quartz | Materiales, jewelry |
| Blaze Farm | Spawner + mechanical arms | Blaze Rods | Fuel, pociones, RPG recipes |
| Nether Wart Farm | Mechanical Bearing + Harvesters | Nether Wart | Cocina (Red Rum, Steel-Toe Stout, Withering Dross, Nether Wart Stew) |
| Basalt Generator | Lava + Soul Soil + Blue Ice | Basalt infinito | Construccion, decoracion |

### Recetas de cocina que necesitan Nether (bridge natural)

| Receta | Mod | Ingredientes Nether |
|--------|-----|---------------------|
| Red Rum | Brewin' And Chewin' | Crimson Fungus + Nether Wart + Shroomlight |
| Steel-Toe Stout | Brewin' And Chewin' | Crimson Fungus + Nether Wart |
| Withering Dross | Brewin' And Chewin' | Wither Rose + Nether Wart |
| Glittering Grenadine | Brewin' And Chewin' | Glowstone Dust |
| Nether Salad | Farmer's Delight | Crimson Fungus + Warped Fungus |
| Nether Wart Stew | Croptopia | Nether Wart + Crimson Fungus + Warped Fungus |
| Nether Star Cake | Croptopia | Nether Star (Wither kill) |
| Netherite Knife | Farmer's Delight | Netherite Ingot |

### Flujo de transporte Nether → Overworld
```
NETHER:
  Granja automatizada (Nether Wart, Blaze, Gold)
  → Create belts → procesamiento in-situ
  → carga en tren (Portable Storage Interface)

CRUCE:
  Tren atraviesa portal Nether (automatico o manual)

OVERWORLD:
  Tren llega a estacion base
  → descarga (Portable Storage Interface)
  → Create belts → fabrica principal / Space Elevator
```

**Entregas del Space Elevator que requieren Nether**: a partir de Ch3, las entregas incluyen items procesados del Nether (bebidas Nether, materiales de Blaze, Gold procesado). Esto obliga a establecer una ruta de transporte.

## 3.8 Bosses

8 bosses, 1 por capitulo. Clase base con scaling:
- HP: `base * (1 + (players-1) * 0.3)` → fijo al spawnear, 0 ticks
- Dmg: `base * (1 + (players-1) * 0.15)` → fijo al spawnear
- Boss Key crafteable, costo escala por capitulo
- Boss Altar: consume Boss Key, teleporta a arena

| Cap | Boss | HP Base | Tema |
|-----|------|---------|------|
| 1 | Guardian del Bosque | 200 | Humanoide de raices, melee + raices + minions |
| 2 | Bestia Glotona | 400 | Devora comida, vomita acido. Darle comida envenenada |
| 3 | Coloso Mecanico | 800 | Construccion de engranajes. TNT en puntos debiles |
| 4 | Locomotora Fantasma | 1,600 | Tren fantasma, pelear encima del tren |
| 5 | El Arquitecto | 3,200 | Entidad digital RS. Hackear nodos |
| 6 | Senor de las Cosechas | 6,400 | Raices que absorben crops. Cortar raices |
| 7 | Nucleo del Dungeon | 12,800 | Cristalino. Arena que cambia de forma |
| 8 | Devorador de Mundos | 25,600 | 4 fases: melee → Create → cocina buff → DPS race |

## 3.9 Champions (Champions Unofficial)

> Implementado via mod **Champions Unofficial** (no custom servo_core). Ver detalle en [mechanics/champions.md](mechanics/champions.md).

### Ofensivos
| Affix | Efecto | Visual | Dificultad |
|-------|--------|--------|------------|
| Veloz | Speed II, +30% dmg | Particulas azules | Media |
| Explosivo | Explota al morir (sin block dmg) | Particulas fuego | Media |
| Invocador | Spawna 2 copias cada 15s | Particulas ender | Alta |
| Berserker | +50% dmg y speed bajo 30% HP | Ojos rojos | Alta |

### Defensivos
| Affix | Efecto | Visual | Dificultad |
|-------|--------|--------|------------|
| Tanque | +150% HP, Resistance I | Tamano +20% | Media |
| Vampiro | Se cura 20% del dmg dado | Particulas rojas | Alta |
| Reflector | Devuelve 15% dmg recibido | Escudo visual | Alta |
| Regenerador | Regen II constante | Particulas verdes | Media |

### Control
| Affix | Efecto | Visual | Dificultad |
|-------|--------|--------|------------|
| Gelido | Slowness II al pegar | Escarcha | Media |
| Toxico | Nube de veneno al morir (5s) | Particulas verdes | Baja |
| Cegador | Blindness 2s al pegar | Particulas oscuras | Alta |
| Gravitante | Pull hacia el mob cada 10s | Particulas moradas | Media |

### Dungeon Exclusivos
| Affix | Efecto | Visual | Dificultad |
|-------|--------|--------|------------|
| Teleporter | Se teleporta detras del jugador | Flash ender | Muy Alta |
| Invisible | Invisible hasta que ataca | Shimmer | Muy Alta |
| Shield | Escudo que se rompe con 3 hits | Escudo giratorio | Alta |

**Spawn** (escala por stage del jugador — ver [champions.md](mechanics/champions.md) para tabla completa):
- Overworld: tier max segun stage del player mas cercano, max 1→2 affixes, pool crece por capitulo
- Nether: 15-18%, max 2→3 affixes
- Dungeons: 15-35% segun tier de llave, siempre mas dificil que overworld
- Del Nucleo: 35%, 3 affixes + exclusivos (Teleporter/Invisible/Shield)

## 3.10 Accesorios (Curios API)

**6 slots totales**: 3 de Jewelry mod (rings/necklaces) + 3 custom (servo_core).

### Slots del jugador
| Slot | Fuente | Cantidad | Items |
|------|--------|----------|-------|
| Ring | Jewelry mod | 2 | 85 items (gem rings, unique rings, netherite variants) |
| Necklace | Jewelry mod | 1 | gem necklaces, unique necklaces |
| Belt | servo_core | 1 | Cinturones custom con efectos |
| Back (Capa) | servo_core | 1 | Capas/cloaks custom |
| Feet (Tobillera) | servo_core | 1 | Tobilleras/accesorios de pie custom |

### Tiers de accesorios custom (belt/back/feet)
| Tier | Nombre | Boost | Obtencion |
|------|--------|-------|-----------|
| 1 | Comun (cobre) | +5% | Gacha, quest |
| 2 | Uncommon (hierro) | +12% | Gacha, champion drops |
| 3 | Raro (oro) | +22% | Boss drops, gacha |
| 4 | Epico (diamante) | +40% | Boss drops, dungeon |
| 5 | Legendario (netherite) | +70% | Boss final, dungeon boss |

### Familias de efecto por slot
- **Belt**: utilidad/defensa (velocidad, armor, knockback res, fire res, mining speed)
- **Back (Capa)**: ofensa/movilidad (ATK, crit, speed, jump, fall reduction)
- **Feet (Tobillera)**: soporte/exploration (regen, luck/loot, swim, XP, water breathing)

### Items custom (~50+)
5 tiers × 3 slots × 3-4 efectos por slot = ~45-60 items curados (no todas las combinaciones, solo las que hacen sentido).

### Ideas de modelos (Blockbench)
- **Cinturones**: hebilla con simbolo del efecto. T1=cuerda, T3=cuero con gema, T5=metal ornamentado
- **Capas**: T1=trapo, T2=capa simple, T3=capa bordada, T4=capa con capucha, T5=capa legendaria luminosa
- **Tobilleras**: T1=cuerda con cuenta, T3=cadena con gema, T5=brazalete arcano

### Equipo completo de un jugador
```
ARMOR (RPG Series):  Helmet + Chest + Legs + Boots (4 piezas de clase)
MANOS:               Arma + Offhand (shield/spell book)
CURIOS (Jewelry):    Ring ×2 + Necklace ×1
CURIOS (Custom):     Belt + Back + Feet
RUNES:               Rune Pouch (en Curios slot)
TOTAL:               ~13 piezas de equipo (build tipo MMO)
```

## 3.11 Gacha Machine

- NO crafteable. Se obtiene como recompensa de quest en Ch1.
- Unica por grupo. No se pueden hacer mas.
- Usa Pepe Coins (obtenidos de quests, champions, bosses, dungeons).

**Gacha de Artefactos (la principal)**:
| Rareza | Probabilidad | Contenido |
|--------|-------------|-----------|
| Comun | 60% | Materiales utiles del capitulo actual |
| Uncommon | 25% | Accesorio T1-T2, materiales del sig capitulo |
| Raro | 10% | Accesorio T2-T3, moldes de postres raros |
| Epico | 4% | Llave de Dungeon avanzada, accesorio T3 |
| Legendario | 1% | Accesorio T4, item unico |
| **Pity**: +2% epico por pull sin epico+, hard pity a 50 pulls |

**Gacha de ServoMart (muebles)**:
- Separado del principal
- Caja misteriosa con mueble random
- Usa Pepe Coins tambien pero menos (5 tokens vs 10 del principal)

## 3.12 Token System

Pepe Coin = moneda universal. Obtencion:
- Quests completadas: 3-10 tokens segun dificultad
- Champions: 1-3 tokens por kill
- Bosses de capitulo: 15-100+ tokens (escala por capitulo — ver [mechanics/tokens.md](mechanics/tokens.md))
- Dungeon runs: 5-15 tokens por run
- Comidas nuevas cocinadas (primera vez): 1 token

Uso:
- Gacha principal: 10 tokens por pull (~15 pulls/cap, pity a 50 pulls → primera pity Ch3-4)
- Gacha ServoMart: 5 tokens por pull (~6 muebles/cap)
- Crafteo de Llaves de Dungeon avanzadas
- ServoMart compras directas (muebles especificos, mas caro que gacha)

## 3.13 Sistema RPG (RPG Series)

### Clases y Subclases
| Clase | Mod | Arma principal | Armor base → Tier 2 | Spells |
|-------|-----|----------------|---------------------|--------|
| Wizard (Arcane) | wizards | Staff/Wand Arcane | Wizard Robes → Arcane Robes | Arcane Blast, Bolt, Beam, Missiles, Blink |
| Wizard (Fire) | wizards | Staff/Wand Fire | Wizard Robes → Fire Robes | Fireball, Pyroblast, Fire Breath, Meteor, Wall of Flames |
| Wizard (Frost) | wizards | Staff/Wand Frost | Wizard Robes → Frost Robes | Frost Shard, Frostbolt, Frost Nova, Frost Shield, Blizzard |
| Rogue | rogues | Dagger, Sickle | Rogue set → Assassin set | Shadowstep, Vanish, Shock Powder, Fan of Knives |
| Warrior | rogues | Double Axe, Glaive | Warrior set → Berserker set | Charge, Whirlwind, Demoralizing Shout, Shattering Throw |
| Paladin | paladins | Claymore, Hammer, Mace, Shield | Paladin set → Crusader set | Judgement, Divine Protection, Battle Banner, Barrier |
| Priest | paladins | Holy Staff, Holy Wand | Priest set → Prior set | Heal, Flash Heal, Holy Shock, Circle of Healing |

### Especializaciones del Skill Tree (Ch4+)
| Especializacion | Base | Estilo |
|-----------------|------|--------|
| Berserker | Warrior | Rage y sangrado melee |
| Deadeye | Rogue | Venenos y trampas |
| War Archer | Fire Wizard | Arquero de fuego |
| Tundra Hunter | Frost Wizard | Arquero de hielo |
| Forcemaster | Arcane Wizard | Punetazos arcanos melee |
| Air | Cualquiera | Viento, knockback, tornados |
| Earth | Cualquiera | Tierra, armadura, terremotos |
| Water | Cualquiera | Agua, healing, cleanse |

### Tier Progression
| Tier | Material principal | Crafteo | Capitulo |
|------|-------------------|---------|----------|
| 0 | Flint, Stone, Wood | Shaped basico | Ch1 |
| 1 | Iron, Gold, Leather, Wool | Shaped | Ch2 (melee), Ch3 (magico) |
| 2 | Diamond, Ender Pearl, Blaze Powder, Prismarine, Rabbit Hide, Ghast Tear | Shaped | Ch4 |
| 3 | Netherite Ingot + Smithing Template | Smithing Transform | Ch6 |
| 4 | Cristal del Nucleo / Rubi Infernal (custom boss drops) | KubeJS custom | Ch8 |

### NPC Merchants (spawn en villages)
| NPC | Mod | Vende |
|-----|-----|-------|
| Wizard Merchant | wizards | Scrolls, robes basicas |
| Arms Dealer | rogues | Armas melee, armor basica |
| Monk | paladins | Armas holy, paladin gear |
| Jeweler | jewelry | Gemas y jewelry basica |

### Workstations RPG
| Bloque | Mod | Funcion |
|--------|-----|---------|
| Spell Binding Table | spell_engine | Vincular spells a class books |
| Arms Station | rogues | Workstation para Arms Dealer NPC |
| Monk Workbench | paladins | Workstation para Monk NPC |
| Jeweler's Kit | jewelry | Workstation para Jeweler NPC |
| Rune Crafting Altar | runes | Crafteo eficiente de runas (x8/x16 vs x2/x4 a mano) |

### Unique Jewelry (24 items, LOOT-ONLY)
Solo dropean en dungeons/gacha/bosses. Sin receta de crafteo.
| Escuela | Necklace | Ring |
|---------|----------|------|
| Arcane | Amulet of Unfettered Magics | Ring of Subjugation |
| Fire | Pendant of Sunfire | Sunflare Signet |
| Frost | Glacial Shard | Permafrost Stone |
| Healing | Adria's Pendant | Anett's Embrace |
| Lightning | Amulet of Unrelenting Storms | Ring of Unstable Currents |
| Soul | Occult Necklace | Signet of Captured Souls |
| Spell | Pendant of Acumen | Ring of Omnipotence |
| Attack | Amulet of Torture | Ring of Fury |
| Crit | Necklace of Anguish | Ring of Suffering |
| Dex/Rogue | Chocker of Vile Intent | Angelista's Revenge |
| Tank | Sentinel's Amulet | Juggernaut Band |
| Archer | Darnassian Strider Amulet | Thalassian Ranger Band |

### Loop RPG + Dungeon
```
Progresar capitulo → Desbloquear tier de gear + llave de dungeon
  → Craftear gear → Entrar a dungeon con mejor equipo
  → Loot: unique jewelry, materiales, tokens
  → Unique jewelry mejora build → Preparar boss de capitulo
  → Derrotar boss → Siguiente capitulo
```

### Decisiones de rebalanceo (KubeJS)
- Berserker armor T2: reemplazar netherite_scrap por chain+iron (demasiado caro para T2)
- Tier 4: reemplazar Aeternium/Ruby (requieren BetterEnd/BetterNether) con materiales custom de boss
- Rune Pouches: configurar para que NO gasten runas (el gate es craftear la pouch, no farmear runas infinitamente)

---

# 4. PROGRESION POR CAPITULO

## 4.1 Filosofia de distribucion

**Cada capitulo tiene de todo**: cocina nueva, crops nuevos, dungeon accesible, combate, decoracion, y un objetivo de entrega al Space Elevator. No hay "el capitulo de Create" o "el capitulo de dungeons".

**Dificultad escala por stage del jugador** (per-player via ProgressiveStages):
- Cada jugador enfrenta dificultad acorde a su propio stage — Player A (Ch2) ve Ch2, Player B (Ch5) ve Ch5
- servo_core post-procesa champions via API publica y hace downgrade si exceden el tier del player mas cercano
- Overworld/Nether: tier basado en stage del player mas cercano
- Dungeons: tier basado en llave usada para crear la instancia (siempre mas dificil que overworld)
- Pool de affixes crece con cada capitulo completado

**ProgressiveStages = unica fuente de verdad** para toda la progresion: items, recetas Y dificultad. Zero SavedData custom.

## 4.2 Distribucion de contenido por capitulo

### Crops por capitulo (no dump, gradual)
| Cap | Crops nuevos | Total acumulado |
|-----|-------------|-----------------|
| 1 | 12: Vanilla (trigo, papa, zanahoria, beetroot, melon, pumpkin, cacao, sugar cane) + FD (tomate, cebolla, repollo, arroz) | 12 |
| 2 | +12 Croptopia basicos: lettuce, corn, strawberry, blueberry, grape, cucumber, bell pepper, garlic, ginger, spinach, peanut, soybeans | 24 |
| 3 | +10 frutas: banana, mango, lemon, orange, apple (croptopia), pineapple, coconut, peach, cherry, lime | 34 |
| 4 | +8 hierbas/especias: basil, cinnamon, nutmeg, turmeric, vanilla, mustard, hops, tea leaves | 42 |
| 5 | +8 exoticos: dragon fruit, star fruit, avocado, kiwi, fig, date, pomegranate, cranberry | 50 |
| 6 | +8 avanzados: artichoke, asparagus, eggplant, leek, rhubarb, elderberry, coffee beans, olive | 58 |
| 7 | +6 raros: saguaro, kumquat, persimmon, nectarine, currant, tomatillo | 64 |
| 8 | Todos los restantes desbloqueados | 70+ |

### Cocina por capitulo
| Cap | Workstations nuevas | Recetas nuevas aprox |
|-----|--------------------|-----------------------|
| 1 | Cutting Board, Cooking Pot, Stove, Skillet (FD) | ~25 |
| 2 | Blender, Moldes de Postres (custom) + B&C Keg | +30 |
| 3 | Drink Maker, Horno Avanzado (custom) + Expanded Delight | +25 |
| 4 | FD Feasts + recetas con especias | +20 |
| 5 | Slice&Dice (auto-cocina) + recetas exoticas | +15 |
| 6 | Recetas avanzadas con ingredientes raros | +10 |
| 7 | Recetas con ingredientes exclusivos de dungeon | +10 |
| 8 | Recetas legendarias que combinan todo | +10 |

### Create por capitulo
| Cap | Que se desbloquea |
|-----|-------------------|
| 1 | Nada |
| 2 | Basico: Water Wheel, Shaft, Cogwheel, Belt, Depot, Andesite Funnel, Chute |
| 3 | Andesite completo: Mechanical Press, Fan, Saw, Drill, Harvester, Millstone |
| 4 | Brass tier: Mixer, Deployer, Crafter, Arm, Brass Funnel + Steam Engine + **Trains basicos** (track, locomotora, schedules) |
| 5 | Trains avanzados (estaciones, signaling) + Create C&A (motor electrico, alternador) + Enchantment Industry |
| 6+ | Todo disponible |

### Storage por capitulo
| Cap | Que se desbloquea |
|-----|-------------------|
| 1 | Vanilla chests |
| 2 | Storage Drawers basicos |
| 3 | Tom's Storage (terminal, connector) |
| 4 | Storage Drawers upgrades avanzados |
| 5 | Refined Storage completo |
| 6+ | Todo disponible |

### RPG Clases por capitulo
| Cap | Clases disponibles | Tier | Armas/Armor | Notas |
|-----|--------------------|------|-------------|-------|
| 1 | Ninguna (solo ataques genericos) | T0 | Flint Dagger, Stone weapons, Novice Wand, Acolyte Wand | Spell Binding Table como quest. Small Rune Pouch |
| 2 | Rogue, Warrior (melee) | T1 melee | Iron Dagger/Sickle/Double Axe/Glaive, Rogue set, Warrior set | Arms Station. Melee primero (materiales accesibles) |
| 3 | + Wizard (Arcane/Fire/Frost), Paladin, Priest | T1 magico | Wizard Staff/Wands, Wizard Robes, Paladin set, Priest set, Iron Claymore/Hammer/Mace/Shield | Monk Workbench, Rune Crafting Altar, Medium Rune Pouch. Nether abre Fire Wizard |
| 4 | Todas + **Skill Tree** (especializacion) | T2 | Diamond weapons, Assassin/Berserker/Arcane-Fire-Frost Robes/Crusader/Prior armor, Crystal Arcane Staff | Elegir especializacion: Berserker, Deadeye, Forcemaster, etc. |
| 5 | Todas | T2+ enchants | + Enchantments magicos (Spell Power, Sunfire, etc.) + Pociones Spell Power + Netherite jewelry | Large Rune Pouch (Nether Star). Soul/Lightning runes |
| 6 | Todas | T3 | Netherite upgrades para TODAS las armas/armaduras RPG (smithing) | Tipped Arrows de Spell Power |
| 7 | Todas | T3+ uniques | Mismo + unique jewelry de dungeons como coleccionables | 24 unique jewelry farmeable en Dungeon del Nucleo |
| 8 | Todas (maxeadas) | T4 custom | Aeternium/Ruby items via KubeJS (mat. de dungeon/boss) | Armas legendarias endgame |

### Jewelry por capitulo
| Cap | Disponible | Gemas |
|-----|-----------|-------|
| 1 | Copper Ring, Iron Ring | Ninguna (metal basico) |
| 2 | Gold Ring + Citrine/Jade Ring y Necklace | Citrine, Jade |
| 3 | + Ruby/Sapphire Ring y Necklace, Jeweler's Kit | Ruby, Sapphire |
| 4 | + Tanzanite/Topaz Ring y Necklace, Diamond Ring/Necklace, Emerald Necklace | Tanzanite, Topaz |
| 5 | + Netherite Gem jewelry (todas las variantes netherite) | Netherite + cualquier gema |
| 6+ | Todo crafteable. Unique jewelry solo de dungeon loot | - |

### Runes por capitulo
| Cap | Runas | Pouch | Crafteo |
|-----|-------|-------|---------|
| 1 | Arcane, Frost | Small Rune Pouch | A mano (x2) |
| 2 | + Fire, Healing | Small | A mano (x2) |
| 3 | Todas basicas | + Medium Rune Pouch | + Rune Crafting Altar (x8/x16) |
| 4 | Todas | Medium | Altar |
| 5 | + Soul, Lightning | + Large Rune Pouch (Nether Star) | Altar |
| 6+ | Todas | Large | Altar |

### Dungeons por capitulo
| Cap | Llaves disponibles | Lo que cambia |
|-----|-------------------|---------------|
| 1 | Basica (1ra gratis + crafteo barato) | 5-7 salas, champions 1 affix, loot basico |
| 2 | Basica | Mismo tier, mas variedad de loot acorde a Ch2 |
| 3 | + **Avanzada** | 10-14 salas, champions 2 affix, Esencia de Dungeon empieza a dropear, 5% unique jewelry |
| 4 | Basica + Avanzada | Mismo, loot actualizado a Ch4 |
| 5 | + **Maestra** | 15-20 salas, champions 3 affix, 15% unique jewelry, moldes 4-5 estrellas |
| 6 | Basica + Avanzada + Maestra | Mismo, loot actualizado a Ch6 |
| 7 | + **Del Nucleo** | 20-25 salas + boss de dungeon, champions exclusivos, unique jewelry garantizada, Fragmentos de Cristal |
| 8 | Todas | Loot endgame, T4 materials |

### Decoracion por capitulo
| Cap | Que se desbloquea |
|-----|-------------------|
| 1 | Supplementaries basico (rope, jar, sign post, soap) |
| 2 | + Macaw's Furniture basico + Supplementaries completo |
| 3 | + Refurbished Furniture basico (funcional: nevera, estufa, fregadero) |
| 4 | + Macaw's Windows completo |
| 5 | + Create Deco + Refurbished completo |
| 6+ | Todo disponible |

### Enchantments por capitulo (vanilla, gateados con ProgressiveStages)
| Cap | Max nivel de enchant |
|-----|---------------------|
| 1-2 | Nivel I-II |
| 3-4 | Nivel III |
| 5 | Nivel IV + Enchantment Industry (auto-enchanting) |
| 6+ | Nivel V, todo |

## 4.3 Capitulos detallados

### Capitulo 1: Raices (Tutorial + Survival)

**Tema**: Capitulo introductorio. Aprender mecanicas vanilla + FD + mecanicas custom + descubrir sistema RPG.

**Mods activos**: Vanilla, FD basico, Supplementaries basico, Aquaculture, FTB Quests (Recetario), Lootr, Gacha Machine, Waystones, Xaero's, Jade, Backpacks, Carry On, Spell Engine (limitado), Runes (basico), Jewelry (basico)

**Contenido**:
- 12 crops, ~25 recetas de cocina
- Dungeon basica (primera llave gratis)
- Champions 1 affix (8% overworld)
- Boss: Guardian del Bosque (200 HP)
- Gacha machine como quest reward
- Tutorial de TODAS las mecanicas custom
- **RPG**: Spell Binding Table (quest reward), armas T0 (Flint Dagger, Stone weapons, Novice Wand, Acolyte Wand)
- **RPG**: Small Rune Pouch + Arcane/Frost runes. NO class books aun (solo ataques genericos)
- **Jewelry**: Copper/Iron Ring (introductorio, sin stats reales)

**Entregas**: Ver [mechanics/space-elevator.md](mechanics/space-elevator.md#41-capitulo-1-primeras-raices) | **Quests detallados**: Ver [chapters/ch1-raices.md](chapters/ch1-raices.md)

**Quests Ch1 (~50)**:
- Historia/tutorial (12): mecanicas custom, FD, gacha, dungeon, champions, Space Elevator, empaque, Spell Binding Table
- Cocina (8): recetas de FD, variedad para FTB Quests "Recetario"
- Farming (6): cada crop, granja, animales
- Dungeon (5): primera run, sobrevivir, loot (Spell Scrolls como teaser)
- Combate (5): champions, boss prep, boss fight, craftear arma T0
- Exploracion (5): biomas, Terralith, Waystones
- Construccion (4): casa, cocina, almacen
- Coleccion (5): comidas unicas, tokens, gacha pulls

### Capitulo 2: La Cocina + Clase Melee

**Tema**: Cocina expandida. Create basico. Primeros Drawers. Elegir clase melee.

**Mods nuevos**: Croptopia (12 crops), B&C, Blender, Moldes (custom), Storage Drawers, Create basico, Macaw's Furniture basico, Rogues & Warriors (class books), Jewelry (gemas)

**Contenido**:
- +12 crops (24 total), +30 recetas
- Blender y Moldes de Postres
- Create basico (waterwheels, belts, shafts)
- Quesos y fermentacion (B&C)
- Dungeon con loot mejorado
- **RPG**: Class books Rogue Handbook + Warriors' Codex. Elegir clase melee
- **RPG**: T1 armor (Rogue set leather, Warrior set iron) + T1 armas (Iron Dagger/Sickle/Double Axe/Glaive)
- **RPG**: Arms Station crafteable. Fire/Healing runes
- **Jewelry**: Gold Ring, Citrine/Jade Ring y Necklace (gemas del overworld)

**Por que melee primero?** Iron y leather son materiales accesibles en Ch2. Magia requiere materiales mas raros (Ender Pearl, Blaze Powder). El jugador aprende combate cuerpo a cuerpo antes de castear.

**Entregas**: Ver [mechanics/space-elevator.md](mechanics/space-elevator.md#42-capitulo-2-la-mesa-servida) | **Quests detallados**: Ver [chapters/ch2-cocina-melee.md](chapters/ch2-cocina-melee.md)

### Capitulo 3: Los Engranajes + La Magia Despierta

**Tema**: Create andesite completo. Drink Maker + Horno Avanzado. Nether accesible. Clases magicas/support se desbloquean.

**Mods nuevos**: Create andesite completo, Slice&Dice, Drink Maker (custom), Horno Avanzado (custom), Tom's Storage, Expanded Delight, Refurbished basico, Llave Avanzada de dungeon, Wizards (class books), Paladins & Priests (class books)

**Contenido**:
- +10 crops frutas (34 total), +25 recetas
- Bebidas con efectos (Drink Maker)
- Horno con mecanica de temperatura
- Nether abierto
- Create: Press, Fan, Saw, Harvester
- Dungeons medianas disponibles (Llave Avanzada)
- Cocina funcional requerida (Refurbished: nevera+estufa+fregadero)
- **RPG**: Tome of Arcane/Fire/Frost, Paladin Libram, Holy Book (TODAS las clases disponibles)
- **RPG**: T1 armor magica (Wizard Robes, Paladin set, Priest set) + T1 armas (Wizard Staff, wands, Holy Staff/Wand, Iron Claymore/Hammer/Mace/Shield)
- **RPG**: Monk Workbench, Jeweler's Kit, Rune Crafting Altar, Medium Rune Pouch
- **RPG**: NPC Merchants empiezan a spawnear en villages (Wizard, Arms Dealer, Monk, Jeweler)
- **Jewelry**: Ruby/Sapphire rings y necklaces
- **Nether** abre materiales para Fire Wizard (Blaze Powder) y Crusader/Prior (Ghast Tear en Ch4)

**Entregas**: Ver [mechanics/space-elevator.md](mechanics/space-elevator.md#43-capitulo-3-engranajes-y-magia) | **Quests detallados**: Ver [chapters/ch3-engranajes-magia.md](chapters/ch3-engranajes-magia.md)

### Capitulo 4: Horizontes + Especializacion

**Tema**: Create brass. Especias. Decoracion completa. Feasts. Tier 2 RPG y Skill Tree.

**Mods nuevos**: Create brass (Mixer, Deployer, Crafter), Macaw's completo, FD Feasts, Skill Tree (especializaciones)

**Contenido**:
- +8 crops especias (42 total), +20 recetas
- Feasts (platos grandes para compartir)
- Create Mixer para recetas avanzadas
- Deployer para automatizar acciones de jugador
- Toda la decoracion desbloqueada
- Steam Engine
- **RPG**: T2 completo: Diamond weapons, Assassin/Berserker/Arcane-Fire-Frost Robes/Crusader/Prior armor
- **RPG**: Crystal Arcane Staff, Diamond Holy Staff/Wand
- **RPG**: **Skill Tree se desbloquea** → elegir especializacion (Berserker, Deadeye, War Archer, Tundra Hunter, Forcemaster, Air, Earth, Water)
- **Jewelry**: Tanzanite/Topaz rings y necklaces, Diamond Ring/Necklace, Emerald Necklace
- **Dungeon Avanzada**: unique jewelry empieza a dropear

**Momento clave**: El jugador se define. Ya no es "un tipo con espada" sino un Berserker, un Frost Wizard, o un Priest Water.

**Entregas**: Ver [mechanics/space-elevator.md](mechanics/space-elevator.md#44-capitulo-4-horizontes) | **Quests detallados**: Ver [chapters/ch4-horizontes.md](chapters/ch4-horizontes.md)

### Capitulo 5: La Red + Poder Magico

**Tema**: Refined Storage. Trenes. Auto-enchanting. Escala industrial. Enchantments magicos y pociones.

**Mods nuevos**: Refined Storage, Create Trains, Create C&A, Create Enchantment Industry, Create Deco, Llave Maestra de dungeon, Spell Power enchants/potions

**Contenido**:
- +8 crops exoticos (50 total), +15 recetas
- RS: Controller, Disks, Grids, Autocraft
- Trenes para logistica interna
- Auto-enchanting con Enchantment Industry
- RS + Create = fabrica inteligente
- Dungeons dificiles disponibles (Llave Maestra)
- **RPG**: Enchantments magicos: Spell Power, Sunfire, Soulfrost, Energize, Spell Haste, Spell Volatility, Amplify Spell, Magic Protection
- **RPG**: Pociones de Spell Power (19 tipos via brewing) + Tipped Arrows
- **RPG**: Soul/Lightning runes + Large Rune Pouch (Nether Star → requiere Wither)
- **Jewelry**: Netherite variants de todas las gemas (netherite + gem)
- **Enchantment Industry** automatiza enchants magicos para todo el equipo RPG

**Entregas**: Ver [mechanics/space-elevator.md](mechanics/space-elevator.md#45-capitulo-5-la-red) | **Quests detallados**: Ver [chapters/ch5-red-poder.md](chapters/ch5-red-poder.md)

### Capitulo 6: La Maestria + Netherite RPG

**Tema**: Optimizacion total. Recetas avanzadas con ingredientes raros. Tier 3 Netherite para todo el equipo RPG.

**Contenido**:
- +8 crops avanzados (58 total), +10 recetas
- Recetas que requieren multiples workstations encadenadas
- Optimization de fabricas
- Enchantments nivel V
- **RPG**: T3 Netherite upgrades para TODAS las armas/armaduras RPG (smithing_transform)
- **RPG**: Tipped Arrows de Spell Power (19 tipos)
- **RPG**: Full build: Netherite gear + enchants magicos + potions + jewelry
- **Dungeon Maestra**: unique jewelry drop rate mas alto
- Boss Ch6 dropea "Rubi Infernal" (material para T4)

**Entregas**: Ver [mechanics/space-elevator.md](mechanics/space-elevator.md#46-capitulo-6-maestria) | **Quests detallados**: Ver [chapters/ch6-maestria.md](chapters/ch6-maestria.md)

### Capitulo 7: Las Profundidades + Coleccion Legendaria

**Tema**: Dungeon endgame. Llave del Nucleo. Boss de dungeon. Caza de unique jewelry.

**Mods nuevos**: Llave del Nucleo de dungeon

**Contenido**:
- +6 crops raros (64 total), +10 recetas con dungeon ingredients
- Piso 10 + boss de dungeon
- Champions dungeon exclusivos (Teleporter, Invisible, Shield)
- **RPG**: Las 24 unique jewelry como coleccionables de dungeon
- **RPG**: Boss de dungeon garantiza 1 unique jewelry
- **RPG**: Accesorios custom T4 (belt/charm/head) como boss drops
- Boss Ch7 dropea "Cristal del Nucleo" (material para T4)

**Entregas**: Ver [mechanics/space-elevator.md](mechanics/space-elevator.md#47-capitulo-7-profundidades) | **Quests detallados**: Ver [chapters/ch7-profundidades.md](chapters/ch7-profundidades.md)

### Capitulo 8: El Final + Armas Legendarias

**Tema**: Todo desbloqueado. Tier 4 legendario. Mega-entregas. Boss final epico.

**Contenido**:
- Todos los crops, todas las recetas
- Boss final: Devorador de Mundos (4 fases, 25,600 HP)
- Recetas legendarias que requieren TODO
- Mega Space Elevator delivery final
- **RPG T4**: Recetas custom KubeJS usando "Cristal del Nucleo" y "Rubi Infernal":
  - Aeternium weapons: Dagger, Double Axe, Glaive, Sickle, Claymore, Great Hammer, Kite Shield, Mace
  - Ruby weapons: Dagger, Double Axe, Glaive, Sickle, Claymore, Great Hammer, Kite Shield, Mace, Holy Staff
  - Ruby Fire Staff, Smaragdant Frost Staff, Valkyrie Magister Staff
- **RPG**: Unique jewelry legendarias como drops del boss final (Ring of Omnipotence, Pendant of Acumen, etc.)
- **RPG**: Todas las especializaciones maxeadas

**Entregas**: Ver [mechanics/space-elevator.md](mechanics/space-elevator.md#48-capitulo-8-final-el-legado) | **Quests detallados**: Ver [chapters/ch8-final.md](chapters/ch8-final.md)

---

# 5. ECONOMIA Y BALANCE

## 5.1 Formulas
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

## 5.2 FTB Quests "Recetario" — milestones de variedad de comidas

Los corazones bonus se otorgan via FTB Quests (quest rewards manuales). Spice of Life: Onion removido.
Tabla completa: ver [mechanics/cooking.md](mechanics/cooking.md#ftb-quests-capitulo-recetario-milestones-de-variedad).

## 5.3 ProgressiveStages

### Como bloquea items/equipo
ProgressiveStages tiene 7 mecanismos independientes. Nuestra config:

| Mecanismo | Config | Efecto |
|-----------|--------|--------|
| **Inventory Scanner** | `BLOCK_ITEM_INVENTORY = true` | Escanea inventario+armor+offhand cada tick. Items bloqueados se CAEN al suelo |
| **Block Pickup** | `BLOCK_ITEM_PICKUP = true` | No puedes recoger items bloqueados del suelo |
| **Block Use** | `BLOCK_ITEM_USE = true` | Cancela click derecho/uso de items bloqueados |
| **Block Crafting** | `BLOCK_CRAFTING = true` | No puedes craftear items bloqueados |
| **Mask Names** | `MASK_LOCKED_ITEM_NAMES = true` | Items bloqueados dicen "Unknown Item" en rojo |
| **Tooltip** | `SHOW_TOOLTIP = true` | Muestra "Locked - Stage required: [nombre]" |
| **JEI/EMI** | `SHOW_LOCK_ICON = true` | Icono de candado sobre items bloqueados en recipe viewer |

**Como se entera el jugador**:
1. Item dice "Unknown Item" en rojo con tooltip "Stage required: servo_ch4"
2. Mensaje en chat: "You haven't unlocked this item yet!" + sonido de lock
3. Items en JEI/EMI tienen candado visual y overlay de color
4. Opcion de ocultar items bloqueados completamente de JEI/EMI

**Gap: Curios slots** - ProgressiveStages NO escanea slots de Curios API (rings, necklaces). Solo escanea inventory+armor+offhand vanilla. Solucion: `BLOCK_ITEM_PICKUP = true` impide recoger el item, y `BLOCK_ITEM_USE = true` impide equiparlo via click derecho. Si aun asi llega a un Curios slot, servo_core agrega un listener que escanee Curios slots tambien.

| Stage | Trigger | Desbloquea |
|-------|---------|-----------|
| servo_ch1 | Default (mundo nuevo) | Vanilla, FD basico, Supplementaries basico, Aquaculture, QoL mods, **Spell Engine (T0 armas, Small Rune Pouch, Copper/Iron Ring)** |
| servo_ch2 | Boss Ch1 + Delivery Ch1 | Croptopia 12 crops, B&C, Blender, Moldes, Create basico, Drawers, Macaw's basico, **Rogue/Warrior class books, T1 melee armor+armas, Arms Station, Gold/Citrine/Jade jewelry, Fire/Healing runes** |
| servo_ch3 | Boss Ch2 + Delivery Ch2 | Croptopia frutas, Create andesite, Slice&Dice, Tom's, Drink Maker, Horno, Exp.Delight, Refurbished basico, Llave Avanzada, **Wizard/Paladin/Priest class books, T1 magico armor+armas, Monk Workbench, Jeweler's Kit, Rune Crafting Altar, Medium Rune Pouch, Ruby/Sapphire jewelry** |
| servo_ch4 | Boss Ch3 + Delivery Ch3 | Croptopia especias, Create brass+steam, Macaw's completo, FD Feasts, **Skill Tree (especializaciones), T2 armor+armas (Diamond, Assassin, Berserker, Elemental Robes, Crusader, Prior), Tanzanite/Topaz/Diamond/Emerald jewelry** |
| servo_ch5 | Boss Ch4 + Delivery Ch4 | Croptopia exoticos, RS, Create Trains+C&A+Enchant, Create Deco, Llave Maestra, **Enchantments magicos, Pociones Spell Power, Soul/Lightning runes, Large Rune Pouch, Netherite jewelry** |
| servo_ch6 | Boss Ch5 + Delivery Ch5 | Croptopia avanzados, enchant V, Refurbished completo, **T3 Netherite upgrades para todo equipo RPG, Tipped Arrows Spell Power** |
| servo_ch7 | Boss Ch6 + Delivery Ch6 | Croptopia raros, Llave del Nucleo, **Unique jewelry loot habilitado en dungeons** |
| servo_ch8 | Boss Ch7 + Delivery Ch7 | Todo, recetas endgame, **T4 custom (Aeternium/Ruby via KubeJS con materiales de boss)** |

---

# 6. PREGUNTAS ABIERTAS

### Resueltas
- [x] RPG Series integrado como tercer pilar (Sesion 7)
- [x] Jewelry vs Custom Curios: Jewelry maneja rings/necklaces, custom solo belt/back/feet
- [x] Capitulos: 8 confirmado
- [x] Trenes: Ch4 manual, Ch5 semi-auto, Ch6+ full auto. Uso: transporte Nether→Overworld
- [x] Trenes Nether: Create trains cross-dimensional built-in. ~18 chunks forzados con FTB Chunks. Viable para 12GB server
- [x] Dungeons: UN solo sistema con 4 tiers de llave (no dos sistemas paralelos)
- [x] Llaves: se CONSUMEN (1 llave = 1 run). Basica siempre barata
- [x] Llaves superiores requieren drops de tier anterior (cadena de progresion)
- [x] Items exclusivos de dungeon definidos: Esencia de Dungeon, Fragmentos de Cristal, unique jewelry, accesorios T4+
- [x] Game loop definido (ver 3.6)
- [x] Nether cooking bridge: 7+ recetas de cocina usan ingredientes del Nether (Brewin', FD, Croptopia)
- [x] Nether mod: YUNG's Better Nether Fortresses (ligero, solo mejora fortalezas)
- [x] NO quests de "explorar biomas por explorar"
- [x] Incendium descartado (demasiado complejo, NPCs no van con nuestro diseño)
- [x] ProgressiveStages bloquea equipo: inventory scanner + block pickup + block use + tooltips + JEI lock icons
- [x] Anti-skip tiers: Esencia NO dropea en Basica, llaves superiores requieren boss drops + materials anteriores
- [x] Boss loot individual: servo_dungeons implementa drops por participante (como MMO). ~8-10 runs Nucleo para T4 completo
- [x] Muerte en dungeon: YIGD (You're in Grave Danger) — tumba, compas, soulbound selectivo, timer anti-robo
- [x] Pilares separados: cocina NO en dungeon loot (pilares se conectan via Space Elevator, no mezclando rewards)
- [x] Servo Tokens renombrados a Pepe Coins
- [x] Dimensional Dungeons REMOVIDO: licencia impide fork. Sistema custom desde cero en servo_dungeons
- [x] Muerte en dungeon: YIGD tumba donde moriste, puedes volver a buscarla (Dark Souls). Soulbound en gear T2+, pierdes loot de la run si no recuperas la tumba. Boss Chamber: sin tumba YIGD, portal reabre 15s

### Por resolver
- [ ] ServoMart: diseño completo del GUI y sistema de pedidos
- [ ] Lore: historia minima visual sin texto. Que historia contamos?
- [ ] Recetas exactas: cuales recetas van en que workstation, cuales se redirigen de Croptopia
- [ ] Dungeon custom: arquitectura del sistema de generacion procedural (servo_dungeons)
- [ ] Dungeon custom: diseño de las 100+ salas con Structure Blocks
- [ ] Dungeon custom: como incluir puzzles de Supplementaries en salas
- [ ] Aquaculture: cuales peces son relevantes si todos usan #c:fish?
- [ ] Muebles funcionales: exactamente cuales afectan progresion y como
- [ ] Accesorios custom (belt/back/feet): cuales exactos, cuantos tiers, que efectos
- [ ] Unique jewelry: distribucion exacta por dungeon tier (cuales droppen donde)
- [ ] Better Combat: agregar para mejorar animaciones melee? (mismo dev, solo visual)
- [ ] Spell Power balance: DPS de spells vs HP de bosses por capitulo
- [ ] Rune consumption: confirmar config de pouches para no gastar runas
- [ ] End dimension: que mods y contenido custom (Nullscape + YUNG's End Island + servo_core?)
- [ ] Trenes al End: NO posible (Create solo soporta Nether portals). Transporte End = manual
