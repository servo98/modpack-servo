# Mod Decisions - Modpack Servo

> Lista definitiva de mods. Cada mod tiene proposito claro. No bloat.
> Performance mods y librerias no aparecen aqui (son todos necesarios y se manejan automaticamente).

## Core
| Mod | Proposito | Notas |
|-----|-----------|-------|
| NeoForge 21.1.219 | Mod loader | Dominante para modpacks, FTB ecosystem |
| servo_packaging (custom) | Cajas de carton, Empacadora. Standalone. | CODIGO COMPLETO v0.3.0. Ver architecture.md |
| servo_delivery (custom) | Terminal de Entrega (Space Elevator), multibloque 3x3. Deps: servo_packaging. | in-progress (scaffold+GUI). Ver architecture.md |
| servo_cooking (custom) | 4 workstations: Blender, Moldes, Drink Maker, Horno Avanzado. Standalone. | pendiente |
| servo_create (custom) | Compat Create <-> packaging (funnels, deployers). Deps: packaging+Create. | pendiente |
| servo_dungeons (custom) | Bosses (8), dungeons, llaves, Altar Unificado (Pedestal + 4 Runas), Boss Chamber, dimension void. Deps: GeckoLib. | pendiente |
| servo_mart (custom) | Tienda catalogo dinamico. Deps: servo_packaging. | pendiente |
| servo_core (custom, glue) | Tokens (Pepe Coin), accesorios custom (belt/back/feet/head), gacha pity, champions post-processing, progression manager. No standalone. | scaffold |

## Quests & Progression
| Mod | Proposito | Notas |
|-----|-----------|-------|
| FTB Quests | Sistema de quests por capitulo (~400 quests) | Quest book principal |
| FTB Teams | Multiplayer quest sync | Necesario para multiplayer |
| FTB Chunks | Claim de chunks | Proteccion de base + force-load para trenes |
| ProgressiveStages | Gating por etapa + auto-hide items | Requiere EMI |

## Cocina & Granja
| Mod | Proposito | Notas |
|-----|-----------|-------|
| Farmer's Delight | Workstations de cocina (cutting board, cooking pot, stove) | Base del sistema de cocina |
| Brewin' And Chewin' | Fermentacion, quesos, bebidas | Extiende FD |
| Expanded Delight | Mas recetas FD | Extiende FD |
| Croptopia | Ingredientes crudos SOLAMENTE | Eliminamos TODAS sus recetas via KubeJS |
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
| Curios API | Framework de slots de accesorios | Jewelry maneja rings/necklaces, custom para belt/back/feet/head |

## Automatizacion
| Mod | Proposito | Notas |
|-----|-----------|-------|
| Create | Automatizacion mecanica central | Ch3+ |
| Create Crafts & Additions | Electricidad Create | Extiende Create |
| Create Deco | Decoracion industrial | Extiende Create |
| Create Enchantment Industry | Auto-enchanting | Extiende Create |
| Create Dragons Plus | Mas contraptions y herramientas | Extiende Create |

## Storage (progresivo)
| Mod | Proposito | Capitulo |
|-----|-----------|----------|
| Storage Drawers | Almacenamiento bulk | Ch2 |
| Tom's Simple Storage | Terminal basico, red de cofres | Ch3 |
| Refined Storage | Storage digital completo, autocrafting | Ch5 |

## Combate
| Mod | Proposito | Notas |
|-----|-----------|-------|
| Champions Unofficial | Mobs con 16 affixes en 3 categorias tipo Diablo | Pool crece por capitulo (4→16). Ver [champions.md](../mechanics/champions.md) |

## Exploracion
| Mod | Proposito | Notas |
|-----|-----------|-------|
| Supplementaries | Items utiles y decorativos | Config para desactivar overlap con servo_core |
| Lootr | Loot unico por jugador | Fairness multiplayer |
| YUNG's Better Nether Fortresses | Mejora fortalezas del Nether | Ligero, solo mejora estructuras, sin NPCs |
| Terralith | ~95 biomas overworld, solo bloques vanilla | Mundo hermoso sin afectar gameplay |

## Gacha
| Mod | Proposito | Notas |
|-----|-----------|-------|
| Bloo's Gacha Machine | Maquina gacha con tokens | Engagement hook, 150 tokens/cap |

## QoL
| Mod | Proposito | Notas |
|-----|-----------|-------|
| Jade + Jade Addons | Tooltip de bloques/entidades | Instalado |
| Sophisticated Backpacks | Mochilas con upgrades | Instalado |
| You're in Grave Danger (YIGD) | Tumba al morir con items | Per-slot soulbound, timer anti-robo |

## Multimedia
| Mod | Proposito | Notas |
|-----|-----------|-------|
| WATERFrAMES | Pantallas de video (YouTube, Twitch, etc.) | Instalado. Hasta 16x16 bloques |
| WATERMeDIA | API multimedia (dependencia de WATERFrAMES) | Instalado |

## Recipe Viewer
| Mod | Proposito | Notas |
|-----|-----------|-------|
| EMI | Ver recetas, requerido por ProgressiveStages | Unico recipe viewer |

## Decoracion
| Mod | Proposito | Notas |
|-----|-----------|-------|
| Macaw's Furniture | Muebles decorativos (~650 muebles) | Ch4+ |
| Macaw's Windows | Ventanas decorativas (~310 variantes) | Ch4+ |
| MrCrayfish Refurbished | Muebles funcionales (nevera, estufa, TV, fregadero) | Ch3 (basico), Ch5 (completo) |

## Worldgen
| Mod | Proposito | Notas |
|-----|-----------|-------|
| Chunk-Pregenerator | Pregenerar chunks antes de jugar | Elimina lag de worldgen |

## Shaders (opcional)
| Mod | Proposito | Notas |
|-----|-----------|-------|
| Iris + Monocle | Shader loader + bridge Embeddium | Instalado. Complementary Reimagined recomendado |

