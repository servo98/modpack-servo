# TODO - Modpack Servo

> Last updated: 2026-03-12 (Session 14)
> Estados: `pending` (listo para hacer) | `planned` (disenado, falta codigo) | `needs-decision` (bloqueado por decision) | `done`

## Fase actual: servo_delivery siguiente (Fase 2 de arquitectura multi-mod)

### 1. Mod management (plan de sesion 10)
| # | Tarea | Estado |
|---|-------|--------|
| 1.1 | Borrar Macaw's Bridges, Roofs, Trapdoors JARs | done |
| 1.2 | Borrar JEI + JER JARs | done |
| 1.3 | Verificar librerias huerfanas post-borrado | done |
| 1.4 | ~~Descargar TMRV~~ — innecesario, EMI instalado directo | done |
| 1.5 | Descargar RS-EMI integration (v1.0.0) | done |
| 1.6 | Descargar Jade + Jade Addons | done |
| 1.7 | Descargar Sophisticated Backpacks | done |
| 1.8 | Descargar Carry On (v2.2.4.4) | done |
| 1.9 | Descargar WATERFrAMES + WATERMeDIA + WaterVision | done |
| 1.10 | Descargar Iris (v1.8.12) + Monocle (v0.2.2) | done |
| 1.11 | Configurar Supplementaries (desactivar overlap servo_core) | pending |
| 1.12 | Configurar Champions tiers por world stage (servo_core + config) | pending |
| 1.13 | Verificar juego lanza con todos los cambios | pending |

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
| 4.5 | Cajas de Envio (empacado para Space Elevator) | planned |

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
| 5.8 | Macaw's Bridges/Roofs/Trapdoors: confirmar remocion y limpiar de GDD/gacha/servomart/ch4 | needs-decision |
| 5.9 | Create Dragons Plus: se queda o se quita? (solo en mod-decisions, sin contenido asignado) | needs-decision |

### 6. Arquitectura y sistemas custom (servo_core Java)
| # | Tarea | Estado |
|---|-------|--------|
| 6.1 | Crear `docs/architecture.md` — plano tecnico de sistemas servo_core | done |
| 6.2 | Crear agente `quest-builder` (.claude/agents/) — genera SNBT de FTB Quests | planned |
| 6.3 | Sistema de dungeons (dimension void, generacion procedural, portales, llaves consumibles) | planned |
| 6.4 | 8 bosses de capitulo (entities, AI, drops, scaling) | planned |
| 6.5 | 4 workstations de cocina (Blender, Moldes, Drink Maker, Horno Avanzado) | planned |
| 6.6 | Accesorios custom Curios (belt, charm, head) | planned |
| 6.7 | Terminal de Entrega — multibloque 3x3, GUI, puertos automation, progresion por capitulo | planned |
| 6.8 | Sistema de Cajas de Carton — Empacadora inmersiva, Carton Plano, Caja Abierta/Cerrada, Create compat | done (codigo) — assets pendientes para artista |

### 7. servo_packaging — COMPLETO (solo faltan assets de artista)
> Estado detallado en `docs/status/servo-packaging.md`

| # | Tarea | Estado |
|---|-------|--------|
| 7.1 | Recetas JSON: Carton Plano (4 Paper + 1 String -> 4x) | done |
| 7.2 | Recetas JSON: doblar batch (shapeless 1:1 crafting table) | done |
| 7.3 | Receta JSON: Empacadora (4 Iron Ingot + 4 Plank + 1 Hopper) | done |
| 7.4 | Tag JSONs: `#packable`, `#pack_size_*`, `#category/*` con items reales del modpack | done |
| 7.5 | Activar check de packable tag en `isPackable()` | done |
| 7.6 | Traducciones: `en_us.json` y `es_mx.json` para items, bloque y tooltip | done |
| 7.7 | Assets: modelos 3D + texturas placeholder generadas | done (placeholder) |
| 7.8 | Conectar color de categoria en tooltip | done |
| 7.9 | Feedback al jugador: ActionBar (no empacable, tipo incorrecto, progreso, sellado, cancelado) | done |
| 7.10 | Contador de progreso "Empacando: 2/4" via ActionBar | done |
| 7.11 | BlockEntityRenderer: items visibles dentro de la caja abierta | planned (nice-to-have) |
| 7.12 | Animacion de cierre (~0.5s en vez de instantaneo) | planned (nice-to-have) |
| 7.13 | Assets reales de artista (5 texturas, 1 modelo 3D, 4 sonidos) | pending — ver `docs/status/servo-packaging.md` seccion "Assets pendientes" |

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
- [x] servo_packaging: codigo 100% completo (state machine, recetas, tags, lang, modelos, texturas placeholder, feedback ActionBar)
