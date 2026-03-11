# TODO - Modpack Servo

> Last updated: 2026-03-11 (Session 12)
> Estados: `pending` (listo para hacer) | `planned` (disenado, falta codigo) | `needs-decision` (bloqueado por decision) | `done`

## Fase actual: Mod cleanup → KubeJS

### 1. Mod management (plan de sesion 10)
| # | Tarea | Estado |
|---|-------|--------|
| 1.1 | Borrar Macaw's Bridges, Roofs, Trapdoors JARs | pending |
| 1.2 | Borrar JEI + JER JARs | pending |
| 1.3 | Verificar librerias huerfanas post-borrado | pending |
| 1.4 | Descargar TMRV (v0.6.3+mc.21.1) — reemplazo JEI | pending |
| 1.5 | Descargar RS-EMI integration | pending |
| 1.6 | Descargar Jade + Jade Addons | pending |
| 1.7 | Descargar Sophisticated Backpacks | pending |
| 1.8 | Descargar Carry On | pending |
| 1.9 | Descargar WATERFrAMES (v2.1.22) + WATERMeDIA (v2.1.34) | pending |
| 1.10 | Descargar Iris (v1.8.12) + Monocle (v0.2.3) | pending |
| 1.11 | Configurar Supplementaries (desactivar overlap servo_core) | pending |
| 1.12 | Configurar Champions tiers por zona | pending |
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

### 6. Arquitectura y sistemas custom (servo_core Java)
| # | Tarea | Estado |
|---|-------|--------|
| 6.1 | Crear `docs/architecture.md` — plano tecnico de sistemas servo_core | planned |
| 6.2 | Crear agente `quest-builder` (.claude/agents/) — genera SNBT de FTB Quests | planned |
| 6.3 | Sistema de dungeons (dimension void, generacion procedural, portales) | planned |
| 6.4 | 8 bosses de capitulo (entities, AI, drops, scaling) | planned |
| 6.5 | 4 workstations de cocina (Blender, Moldes, Drink Maker, Horno Avanzado) | planned |
| 6.6 | Accesorios custom Curios (belt, charm, head) | planned |
| 6.7 | Space Elevator multibloque + Cajas de Envio | planned |
| 6.8 | Portal de dungeon + sistema de llaves consumibles | planned |

## Completado
- [x] Phase 0: Build compiles, proyecto scaffold
- [x] Phase 1: ~80 mods instalados, juego arranca
- [x] Phase 1.5: Mod review completo (52 mods evaluados, decisiones tomadas)
- [x] GDD v2 completo con 21 mecanicas y 8 capitulos
- [x] RPG Series integrado (11 mods, carga sin errores)
- [x] Champions Unofficial instalado (16 affixes, elite mobs)
- [x] Mods removidos: Chipped, Athena, Alex's Mobs, Citadel, Dimensional Dungeons, Mystical Ag, Botany Pots
- [x] Docs reorganizados como wiki (mechanics/, chapters/, balance/)
- [x] 4 agentes custom creados (doc-keeper, kubejs-writer, balance-checker, mod-researcher)
- [x] Dungeon system disenado (custom servo_core, 4 tiers llaves)
- [x] ProgressiveStages investigado (7 mecanismos de enforcement)
- [x] Limpieza docs y archivos de estado (session 12)
