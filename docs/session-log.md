# Session Log - Modpack Servo

> Append-only. Cada sesion registra QUE se hizo y lecciones aprendidas.
> Estado de tareas → `docs/TODO.md` | Problemas → `docs/blockers.md`

## Sesion 1: 2026-03-09
- Investigacion mod loaders → NeoForge elegido
- Investigacion mods disponibles y matematicas de balanceo
- Plan maestro creado
- Decisiones: JEI (luego reemplazado por EMI/TMRV), Croptopia, RS en Ch5, 8 capitulos

## Sesion 2: 2026-03-09
- FASE 0 COMPLETA: Git init, NeoForge MDK, build.gradle, ServoCore scaffold
- Gradle wrapper 9.2.1, neoforge.mods.toml, CLAUDE.md, KubeJS placeholders
- BUILD SUCCESSFUL
- Leccion: Kotlin DSL NO funciona con ModDevGradle → Groovy siempre
- Leccion: JAVA_HOME del sistema incorrecto → wrappers gw/gw.bat

## Sesion 3: 2026-03-09
- Revision completa de diseno del modpack
- GDD v1 escrito (~600 lineas)
- Balance sheets creados (combat-scaling, gacha-rates, accessories)
- Decisiones: NO mods combate, NO mods bosses externos, solo Dimensional Dungeons, 54 accesorios custom, ~400 quests

## Sesion 4: 2026-03-09
- FASE 1 COMPLETA: ~60 JARs instalados, juego arranca
- Script download-mods.py (Modrinth API), FTB desde maven.ftb.dev
- Spice of Life: Carrot → Onion (fork NeoForge)
- Leccion: Modrinth API puede matchear mods incorrectos
- Leccion: ProgressiveStages requiere EMI (no solo JEI)

## Sesion 5: 2026-03-10
- GDD v2 reescrito (gdd-v2.md), GDD v1 eliminado
- Extraccion contenido de mods desde JARs
- Mystical Agriculture, Botany Pots, Cucumber removidos
- Nuevas mecanicas: Space Elevator pre-colocado, trenes, Cajas de Envio, ServoMart

## Sesion 6: 2026-03-10
- RPG Series agregado como tercer pilar (11 mods, 367 items, ~164 spells)
- Analisis completo de recetas y tiers RPG → docs/mod-data/rpg-series-content.md
- Hallazgo: Tier 4 requiere BetterEnd/BetterNether → KubeJS custom necesario
- Hallazgo: Unique Jewelry (~20 items) son LOOT-ONLY → perfectos para dungeons/gacha
- Hallazgo: Berserker armor usa netherite_scrap en Tier 2 → rebalancear

## Sesion 7: 2026-03-10
- Reorganizacion docs como wiki: balance/, mod-data/, reference/, design/
- CLAUDE.md reescrito con mapa de docs tipo tabla
- MEMORY.md adelgazado (90→73 lineas)
- 4 agentes custom creados (doc-keeper, kubejs-writer, balance-checker, mod-researcher)
- 8 capitulos confirmado

## Sesion 8: 2026-03-10
- YUNG's Better Nether Fortresses aprobado, Incendium y Eternal Nether descartados
- GDD v2 actualizado: llaves dungeon, game loop, tren al Nether
- Decisiones: NO quests de "explorar por explorar", trenes Ch4→Ch6 progresion

## Sesion 9: 2026-03-10
- Modpack lanza con ~80 mods (79 JARs)
- Alex's Mobs + Citadel removidos (crash + no encaja)
- Dimensional Dungeons removido (licencia ARR) → dungeons 100% custom servo_core
- 15 dependencias resueltas
- ProgressiveStages investigado: 7 mecanismos de enforcement
- docs/TODO.md creado

## Sesion 10: 2026-03-10
- Mod review completo: 52 mods evaluados con usuario
- Champions Unofficial instalado (16 affixes, elite mobs)
- Chipped + Athena removidos
- Decisiones finales: JEI→TMRV, JER removido, Jade+Soph Backpacks+Carry On SI, Waystones+Xaero's NO
- WATERFrAMES+Iris aprobados, Macaw's Bridges/Roofs/Trapdoors por remover

## Sesion 12: 2026-03-11
- Auditoria completa de documentacion y organizacion
- CLAUDE.md reescrito: ~100 lineas (antes ~200), fix JEI→EMI, quitar mod list, reglas de archivos de estado
- TODO.md reescrito con status tracking (pending/planned/needs-decision/done)
- session-log.md limpiado: append-only, sin duplicar blockers ni pendientes
- blockers.md limpiado: B015 resuelto, B011 actualizado
- MEMORY.md actualizado con nuevas reglas
- memory/mod-ecosystem.md eliminado (referencia historica sin valor)
