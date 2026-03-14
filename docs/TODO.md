# TODO - Modpack Servo

> Last updated: 2026-03-12 (Session 15)
> Estados: `pending` (listo para hacer) | `planned` (disenado, falta codigo) | `needs-decision` (bloqueado por decision) | `done`

## Fase actual: servo_delivery en progreso (Fase 2 de arquitectura multi-mod)

### 1. Mod management (plan de sesion 10)
| # | Tarea | Estado |
|---|-------|--------|
| 1.1 | Borrar Macaw's Bridges, Roofs, Trapdoors JARs | done |
| 1.2 | Borrar JEI + JER JARs | done |
| 1.3 | Verificar librerias huerfanas post-borrado | done |
| 1.4 | Descargar TMRV — innecesario, EMI instalado directo | done |
| 1.5 | Descargar RS-EMI integration (v1.0.0) | done |
| 1.6 | Descargar Jade + Jade Addons | done |
| 1.7 | Descargar Sophisticated Backpacks | done |
| 1.8 | Descargar Carry On (v2.2.4.4) | done |
| 1.9 | Descargar WATERFrAMES + WATERMeDIA + WaterVision | done |
| 1.10 | Descargar Iris (v1.8.12) + Monocle (v0.2.2) | done |
| 1.11 | Configurar Supplementaries (desactivar overlap servo_core) | planned — postponed hasta que servo_core tenga contenido |
| 1.12 | Implementar post-procesamiento de Champions por ProgressiveStages en servo_core (Fase 7) | planned |
| 1.13 | Verificar juego lanza con todos los cambios | done |

### 2. KubeJS cleanup
| # | Tarea | Estado |
|---|-------|--------|
| 2.1 | Eliminar TODAS las recetas de Croptopia | planned |
| 2.2 | Redirigir ingredientes Croptopia a FD workstations | planned |
| 2.3 | Identificar y unificar recetas duplicadas entre mods cocina | planned |
| 2.4 | Ocultar items internos/disabled de EMI | planned |

### 3. Progression system
| # | Tarea | Estado |
|---|-------|--------|
| 3.1 | ProgressiveStages: configs TOML (servo_ch1 a servo_ch8) | planned |
| 3.2 | RPG Tier 4: recetas KubeJS (reemplazar Aeternium/Ruby) | planned |
| 3.3 | Rebalancear Berserker armor (quitar netherite_scrap de Tier 2) | planned |

### 4. Custom items (KubeJS)
| # | Tarea | Estado |
|---|-------|--------|
| 4.1 | Pepe Coins (token de quest/gacha) | planned |
| 4.2 | Esencia de Dungeon (drop Advanced+) | planned |
| 4.3 | Fragmentos de Cristal del Nucleo (drop Core boss) | planned |
| 4.4 | Llaves de Dungeon (4 tiers) | planned |
| 4.5 | Cajas de Envio (empacado para Space Elevator) | done — implementado en servo_packaging |

### 5. Diseno pendiente
| # | Tarea | Estado |
|---|-------|--------|
| 5.1 | Quest design (~400 quests, ~50/capitulo via FTB Quests) | planned |
| 5.2 | End dimension (Nullscape + YUNG's Better End Island?) | needs-decision |
| 5.3 | Runas: consumibles o infinitas en pouches? | needs-decision |
| 5.4 | ServoMart: disenar tienda automatizada estilo IKEA | planned |
| 5.5 | Slots custom Curios: definir nombres finales (`belt/back/feet` vs `belt/charm/head`) — 15+ docs afectados | needs-decision |
| 5.6 | MrCrayfish Refurbished: desbloqueo en Ch3 o Ch4? (mod-decisions dice Ch4, resto dice Ch3) | needs-decision |
| 5.7 | T2 RPG (Diamond/Gold): desbloqueo en Ch4 o Ch3-4? (rpg-weapon-stats dice Ch3-4, resto dice Ch4) | needs-decision |
| 5.8 | Macaw's Bridges/Roofs/Trapdoors: confirmar remocion y limpiar de GDD/gacha/servomart/ch4 | done |
| 5.9 | Create Dragons Plus: se queda o se quita? (solo en mod-decisions, sin contenido asignado) | needs-decision |
| 5.10 | Spice of Life: quitar JAR fisico + crear capitulo "Recetario" en FTB Quests + verificar AttributeResetFix NeoForge | pending |
| 5.11 | Lore: historia minima visual sin texto. Que historia contamos? | needs-decision |
| 5.12 | Better Combat: agregar para mejorar animaciones melee? (mismo dev que RPG Series, solo visual) | needs-decision |
| 5.13 | Unique jewelry: distribucion exacta por dungeon tier (cuales de las 24 droppen donde) | planned |
| 5.14 | Muebles funcionales Refurbished: exactamente cuales afectan progresion y como | planned |
| 5.15 | Aquaculture: cuales peces son relevantes si todos usan tag generico #c:fish? | needs-decision |
| 5.16 | Spell Power balance: DPS de spells vs HP de bosses por capitulo | planned |
| 5.17 | Recetas exactas: cuales recetas van en que workstation, cuales se redirigen de Croptopia | planned |
| 5.18 | Dungeon: diseño de las 100+ salas con Structure Blocks | planned |
| 5.19 | Dungeon: como incluir puzzles de Supplementaries en salas | planned |

### 6. Arquitectura y sistemas custom (servo_core Java)
| # | Tarea | Estado |
|---|-------|--------|
| 6.1 | Crear `docs/architecture.md` — plano tecnico de sistemas servo_core | done |
| 6.2 | Crear agente `quest-builder` (.claude/agents/) — genera SNBT de FTB Quests | planned |
| 6.3 | Sistema de dungeons (dimension void, generacion procedural, portales, llaves consumibles) | planned |
| 6.4 | 8 bosses de capitulo (entities, AI, drops, scaling) | planned |
| 6.5 | 4 workstations de cocina (Blender, Moldes, Drink Maker, Horno Avanzado) | planned |
| 6.6 | Accesorios custom Curios (belt, charm, head) | planned |
| 6.7 | Terminal de Entrega — multibloque 3x3, GUI, puertos automation, progresion por capitulo | in-progress — ver sección 8 |
| 6.8 | Sistema de Cajas de Carton — Empacadora inmersiva, Carton Plano, Caja Abierta/Cerrada, Create compat | done (codigo) — assets pendientes para artista |

### 7. servo_packaging — COMPLETO
> Estado detallado en `docs/status/servo-packaging.md`

| # | Tarea | Estado |
|---|-------|--------|
| 7.1 | Recetas JSON: Carton Plano (4 Paper + 1 String -> 4x) | done |
| 7.2 | Recetas JSON: doblar batch (shapeless 1:1 crafting table) | done |
| 7.3 | Receta JSON: Empacadora (4 Iron Ingot + 4 Plank + 1 Hopper) | done |
| 7.4 | Tag JSONs: `#packable`, `#pack_size_*`, `#category/*` | done |
| 7.5 | Activar check de packable tag en `isPackable()` | done |
| 7.6 | Traducciones: `en_us.json` y `es_mx.json` | done |
| 7.7 | Assets: modelos 3D + texturas (generadas por convert.py + gen scripts) | done |
| 7.8 | Color de categoria en tooltip | done |
| 7.9 | Feedback al jugador: ActionBar messages | done |
| 7.10 | GUI Packing Station: textura custom, 2 slots, progreso | done |
| 7.11 | BER: items visibles dentro de caja abierta (bobbing, layout 1-4) | done |
| 7.12 | BER: estampa del contenido en cara frontal de caja sellada | done |
| 7.13 | ShippingBox: placeable como bloque sellado (click derecho en superficie) | done |
| 7.14 | ShippingBox: overlay icono contenido en inventario (IItemDecorator) | done |
| 7.15 | Open Box: 4 direcciones facing (N/S/E/W) | done |
| 7.16 | Open Box: insercion stack completo sin shift | done |
| 7.17 | Carry On blacklist (neoforge:immovable tag) | done |
| 7.18 | convert.py: bake rotations >45° en geometria | done |
| 7.19 | Function test_kit para pruebas in-game | done |
| 7.20 | Animacion de cierre (~0.5s en vez de instantaneo) | planned (nice-to-have) |
| 7.21 | Cantidades variables por tipo (usar tags pack_size_*) | planned (nice-to-have) |
| 7.22 | Sonidos custom (reemplazar placeholders vanilla) | pending — assets de artista |

### 8. servo_delivery — EN PROGRESO
> Estado detallado en `docs/status/servo-delivery.md`

| # | Tarea | Estado |
|---|-------|--------|
| 8.1 | Scaffold: build.gradle, gradle.properties, neoforge.mods.toml | done |
| 8.2 | 4 bloques: Terminal, Puerto, Base, Antena | done |
| 8.3 | Multibloque 3x3: validacion de estructura, patron master/slave | done |
| 8.4 | GeckoLib BlockEntity: renderer + overlay texto en bloque + rotacion FACING | done — animaciones reales pendientes (registerControllers vacio) |
| 8.5 | Sistema data-driven: JSONs de entregas por capitulo | done |
| 8.6 | DeliveryCompleteEvent para integracion con servo_core | done |
| 8.7 | Progreso global via DeliverySavedData (sobrevive destruccion del terminal) | done |
| 8.8 | Lang files: en_us + es_mx | done |
| 8.9 | Blockstates y modelos placeholder | done |
| 8.10 | GUI / Menu: DeliveryTerminalMenu + DeliveryTerminalScreen + boton LAUNCH | done |
| 8.11 | BoxContents matching: logica completa de validacion categoria/tipo | pending — ver 8.22 |
| 8.12 | Create automation: hopper/funnel extraction en puertos | pending |
| 8.13 | Efectos: particulas + sonidos + beacon beam durante celebracion | pending |
| 8.14 | Loot tables para los 4 bloques | pending |
| 8.15 | Recetas de crafteo (JSON) | pending |
| 8.16 | JSONs de entregas Ch2-Ch8 (solo Ch1 placeholder existe) | pending |
| 8.17 | Funcion de test: `/function servo_delivery:test_kit` | pending |
| 8.18 | Modelo GeckoLib final (Blockbench): geo.json + animation.json + textura | pending — assets de artista |
| 8.19 | Texturas finales para los 4 bloques | pending — assets de artista |
| 8.20 | Chunk forzado del terminal: implementar force-loading al armar multibloque, o quitar del diseno? | needs-decision |
| 8.21 | Buffer en puertos de entrega: 1 slot con senal redstone al rechazar, o dejar como passthrough? | needs-decision |
| 8.22 | Validacion de BoxContents: verificar que ShippingBox tenga el tipo de contenido correcto segun el requisito? | needs-decision |

## Completado
- [x] Phase 0: Build compiles, proyecto scaffold
- [x] Phase 1: ~80 mods instalados, juego arranca
- [x] Phase 1.5: Mod review completo (52 mods evaluados, decisiones tomadas)
- [x] GDD v2 completo con 21 mecanicas y 8 capitulos
- [x] RPG Series integrado (11 mods, carga sin errores)
- [x] Champions Unofficial instalado (16 affixes, mobs con affixes tipo Diablo)
- [x] Mods removidos: Chipped, Athena, Alex's Mobs, Citadel, Dimensional Dungeons, Mystical Ag, Botany Pots
- [x] Docs reorganizados como wiki (mechanics/, chapters/, balance/)
- [x] 4 agentes custom creados (doc-keeper, kubejs-writer, balance-checker, mod-researcher)
- [x] Dungeon system disenado (custom servo_core, 4 tiers llaves)
- [x] ProgressiveStages investigado (7 mecanismos de enforcement)
- [x] Limpieza docs y archivos de estado (session 12)
- [x] Arquitectura multi-mod disenada (7 JARs: packaging/create/delivery/cooking/dungeons/mart/core)
- [x] docs/architecture.md creado con grafo de dependencias y orden de desarrollo
- [x] servo_packaging: 100% completo (15 Java files, BERs, GUI, decorators, models, textures, blockstates, lang, tags, recipes, test function)
