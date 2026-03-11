# Mod Decisions - Modpack Servo

## Principio: cada mod debe tener un proposito claro. No bloat.

## Core
| Mod | Proposito | Capitulo | Notas |
|-----|-----------|----------|-------|
| NeoForge 21.1.219 | Mod loader | - | Dominante para modpacks, FTB ecosystem |
| servo_core (custom) | Cocina custom (4 workstations), bosses (8), dungeons custom, accesorios (belt/back/feet), progresion, delivery (Space Elevator + Cajas) | Todos | Nuestro mod principal. Elite mobs manejados por Champions Unofficial |

## Quests & Progression
| Mod | Proposito | Notas |
|-----|-----------|-------|
| FTB Quests | Sistema de quests por capitulo (~400 quests) | Quest book principal |
| FTB Teams | Multiplayer quest sync | Necesario para 2 jugadores |
| FTB Chunks | Claim de chunks | Proteccion de base |
| ProgressiveStages | Gating por etapa + auto-hide items | Reemplaza GameStages (no existe para 1.21.1) |

## Cocina & Granja
| Mod | Proposito | Notas |
|-----|-----------|-------|
| Farmer's Delight | Workstations de cocina (cutting board, cooking pot, stove) | Base del sistema de cocina |
| Brewin' And Chewin' | Fermentacion, quesos, bebidas | Extiende FD |
| Expanded Delight | Mas recetas FD | Extiende FD |
| Croptopia | Ingredientes crudos SOLAMENTE | Eliminamos TODAS sus recetas via KubeJS |
| Spice of Life: Onion | Incentivo a comer variado (bonus hearts) | Fork NeoForge de Carrot |
| Create: Slice & Dice | Automatizar cocina con Create | Bridge cocina-automatizacion |
| Aquaculture 2 | Pesca expandida | Mas ingredientes de pescado |

## RPG & Clases
| Mod | Proposito | Notas |
|-----|-----------|-------|
| Spell Engine | Framework de spells y spell binding | Core del sistema RPG |
| Spell Power | Atributos magicos, pociones, enchantments | Stats de poder magico |
| Wizards | Clases: Arcane/Fire/Frost Wizard | Staffs, wands, robes, 15 spells |
| Rogues & Warriors | Clases: Rogue, Warrior | Daggers, axes, glaives, 10 skills |
| Paladins & Priests | Clases: Paladin, Priest | Claymores, hammers, shields, holy staves, 9 spells |
| Skill Tree | UI de arbol de habilidades | Framework visual |
| More RPG Classes Skill Tree | 8 especializaciones | Berserker, Deadeye, War Archer, Tundra Hunter, Forcemaster, Air, Earth, Water |
| Runes | Sistema de runas consumibles para spells | 6 tipos de runa + pouches |
| Jewelry | Gemas, anillos, collares, ores custom | 85 items, 6 gemas, ~20 unique loot-only |
| Ranged Weapon API | Framework para armas a distancia | Pociones/flechas de ranged buffs |

## Accesorios
| Mod | Proposito | Notas |
|-----|-----------|-------|
| Curios API | Framework de slots de accesorios | Jewelry maneja rings/necklaces, custom para belt/charm/head |
| Jewelry | Rings y necklaces con gemas | Integrado con Curios, tiers vanilla→netherite |

## Automatizacion
| Mod | Proposito | Notas |
|-----|-----------|-------|
| Create | Automatizacion mecanica central | Ch3+ |
| Create Crafts & Additions | Electricidad Create | Extiende Create |
| Create Deco | Decoracion industrial | Extiende Create |
| Create Enchantment Industry | Auto-enchanting | Extiende Create |

## Storage (progresivo)
| Mod | Proposito | Capitulo |
|-----|-----------|----------|
| Storage Drawers | Almacenamiento bulk | Ch2 |
| Tom's Simple Storage | Terminal basico | Ch3 |
| Refined Storage | Storage digital completo | Ch5 |

## Combate / Elite Mobs
| Mod | Proposito | Notas |
|-----|-----------|-------|
| Champions Unofficial | Elite mobs con 16 affixes en 4 categorias | Reemplaza sistema custom de servo_core. Configurable via JSON/KubeJS |

## Exploracion
| Mod | Proposito | Notas |
|-----|-----------|-------|
| ~~Alex's Mobs~~ | ~~Fauna diversa~~ | **REMOVIDO**: crash con Citadel + no encaja en 3 pilares de contenido |
| Supplementaries | Items utiles y decorativos | QoL |
| ~~Dimensional Dungeons~~ | ~~Dungeons roguelike~~ | **REMOVIDO**: licencia "All Rights Reserved", no se puede forkear. Sistema de dungeons custom en servo_core |
| Lootr | Loot unico por jugador | Fairness multiplayer |
| YUNG's Better Nether Fortresses | Mejora fortalezas del Nether | Ligero, solo mejora estructuras, sin NPCs, compatible con Create |
| YUNG's API | Dependencia de YUNG's Better Nether Fortresses | Requerida |

## Gacha
| Mod | Proposito | Notas |
|-----|-----------|-------|
| Bloo's Gacha Machine | Maquina gacha con tokens | Engagement hook, 150 tokens/cap |

## QoL (Quality of Life)
| Mod | Proposito | Notas |
|-----|-----------|-------|
| Waystones | Teletransporte entre puntos descubiertos | Fast travel, reduce tedium de exploration |
| Xaero's Minimap | Minimapa en HUD | Navegacion, waypoints |
| Jade | Tooltip de bloques/entidades (HWYLA) | QoL visual, que estoy mirando |
| Sophisticated Backpacks | Mochilas con upgrades | Storage portatil progresivo |
| Carry On | Cargar bloques/entidades en manos | Mover cofres, spawners, mobs |
| You're in Grave Danger (YIGD) | Tumba al morir con items, compas de muerte | Per-slot soulbound, timer anti-robo, NeoForge 1.21.1 |

## Multimedia
| Mod | Proposito | Notas |
|-----|-----------|-------|
| WATERFrAMES | Pantallas de video (Frame, Projector, TV, Big TV) | Reproduce YouTube, Twitch, Google Drive, etc. Hasta 16x16 bloques. Multiplayer sync |
| WATERMeDIA | API multimedia (dependencia de WATERFrAMES) | Usa LibVLC, pre-empaquetado en Windows |

## Recipe Viewer
| Mod | Proposito | Notas |
|-----|-----------|-------|
| JEI | Ver recetas | Standard, mejor soporte NeoForge |

## Decoracion
| Mod | Proposito | Notas |
|-----|-----------|-------|
| Macaw's (Bridges/Roofs/Furniture/Windows/Trapdoors) | Building blocks | Decoracion de base, Ch4+ |
| Chipped | Variantes de bloques vanilla | Mas opciones esteticas, Ch4+ |
| MrCrayfish Refurbished | Muebles funcionales | Decoracion funcional, Ch4+ |

## Worldgen
| Mod | Proposito | Notas |
|-----|-----------|-------|
| Terralith | ~95 biomas overworld, ~20 estructuras, solo bloques vanilla | Mundo hermoso sin afectar gameplay ni progresion |
| Chunk-Pregenerator | Pregenerar chunks antes de jugar | Elimina lag de worldgen. Multi-threaded, puede correr de fondo |

## Performance - Rendering
| Mod | Proposito | Notas |
|-----|-----------|-------|
| Embeddium | Rendering optimizado (Sodium port) | Base de todo el stack visual |
| Embeddium Extra | Opciones de video tipo OptiFine sobre Embeddium | Toggles de animaciones, niebla, etc. |
| EntityCulling | No renderizar entidades ocultas | Reduce lag |
| ImmediatelyFast | Optimizaciones de rendering | Reduce lag |
| Cull Leaves | Oculta caras internas de hojas | Hasta 30% FPS en bosques |

## Performance - Memoria y Startup
| Mod | Proposito | Notas |
|-----|-----------|-------|
| FerriteCore | Reducir uso de RAM | Usar version 7.0.1+ (compat Lithium) |
| ModernFix | Fixes de rendimiento y memoria | Startup + runtime |
| LazyDFU Reloaded | DFU lazy loading | Reduce 10-30s del startup |
| ThreadTweak Reforged | Distribuir carga CPU al inicio | Fork de Smooth Boot |

## Performance - Tick/TPS (Server)
| Mod | Proposito | Notas |
|-----|-----------|-------|
| Lithium | Optimiza fisica, IA mobs, colisiones, ticking | 20-50% menos MSPT. Esencial para 8 jugadores |
| ServerCore | Chunk I/O async, mob caps, suaviza lag spikes | Complementa Lithium |
| Clumps | Agrupar XP orbs | Reduce lag de entidades |
| NoisiumForked | Generacion de chunks 20-30% mas rapida | Solo server. Util para exploracion y dungeons |

## Performance - Network
| Mod | Proposito | Notas |
|-----|-----------|-------|
| Krypton FNP | Optimiza stack de networking Netty | Critico para 8 jugadores |
| Packet Fixer | Previene errores de conexion en modpacks pesados | Login timeouts, payload too large |

## Shaders (opcional)
| Mod | Proposito | Notas |
|-----|-----------|-------|
| Iris | Shader loader (soporte nativo NeoForge desde 1.8.0) | Requiere Monocle para usar con Embeddium |
| Monocle | Bridge Iris + Embeddium | Sin Monocle, Iris requiere Sodium |
| Shader pack recomendado: Complementary Reimagined | Compatible con WATERFrAMES | Otros packs pueden causar pantalla negra en WATERFrAMES |

## Excluidos (con razon)
| Mod | Razon |
|-----|-------|
| Mekanism | Usuario no interesado |
| AE2 | Usuario no interesado |
| Cooking for Blockheads | Incompatible con Farmer's Delight |
| GameStages | No existe para 1.21.1, reemplazado por ProgressiveStages |
| Alex's Mobs + Citadel | Eliminado: crash al cargar + no encaja en 3 pilares de contenido (cocina, Create, RPG) |
| Bosses of Mass Destruction | Eliminado: solo bosses custom servo_core, evitar que "relleno" sea mas interesante que progresion |
| Brutal Bosses | Eliminado: misma razon que BoMD |
| Dimensional Dungeons | Eliminado: licencia "All Rights Reserved" impide fork. Sistema custom en servo_core da control total (llaves, multiplayer, limpieza chunks, muerte roguelike) |
| Dungeon Crawl | Eliminado: dungeons custom en servo_core |
| When Dungeons Arise | Eliminado: estructuras overworld confundirian la progresion |
| Silent Gear | Eliminado: no combat mods, vanilla progression |
| Simply Swords | Eliminado: no combat mods |
| Better Combat | Eliminado: no combat mods |
| Epic Fight | Eliminado: no combat mods |
| Iron's Spells | Eliminado: RPG Series elegido en su lugar (mejor integracion clase/spell) |
| Pufferfish Skills | Evaluado: framework puro sin contenido, requiere crear todo desde cero. RPG Series preferido |
| Iron Chests | No hay build para 1.21.1. Vanilla chests + Storage Drawers cubren la necesidad |
| Mystical Agriculture | Eliminado: demasiado desbalanceado, trivializa farming de recursos. Reemplazado por sistema de empaque custom |
| Botany Pots | Eliminado: redundante con sistema de granja Croptopia + Create automation |
| Cucumber Library | Eliminado: era dependencia de Mystical Agriculture (ya removido) |
| Incendium | Descartado: demasiado complejo, tiene NPCs que no van con nuestro diseño de narrativa visual |
| Eternal Nether | Descartado: tambien tiene NPCs rescatables, mismo problema que Incendium |
| Tectonic | Descartado: terreno dramatico pero performance problematica en NeoForge (issue abierto en GitHub). Terralith solo es suficiente |
| C2ME | Descartado: version alpha, server tiene pocos hilos (2-3), riesgo de crashes esporadicos. Chunk-Pregenerator es mas seguro |
