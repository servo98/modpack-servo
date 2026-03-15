# Blockers - Modpack Servo

> Solo problemas que bloquean progreso real. Tareas normales van en [GitHub Issues](https://github.com/servo98/modpack-servo/issues).

## Activos

### B003: ProgressiveStages configuracion completa
- **Impacto**: Alto. Sin configs, no hay gating por capitulo.
- **Estado**: Investigado (7 mecanismos). Falta crear configs TOML y asignar items por stage.
- **Gap**: NO escanea Curios API slots → necesita fix en servo_core.

### B014: Tier 4 RPG sin recetas crafteables
- **Impacto**: Medio. Items Aeternium/Ruby requieren BetterEnd/BetterNether (no instalados).
- **Solucion**: Recetas KubeJS custom usando materiales de dungeon/boss drops.

### B016: End dimension sin decidir
- **Impacto**: Medio. Capitulos 7-8 necesitan contenido del End.
- **Opciones**: Nullscape + YUNG's Better End Island + servo_core custom.
- **Restriccion**: Trenes al End NO posibles (Create solo soporta portales Nether).

## Resueltos

| ID | Problema | Resolucion |
|----|----------|------------|
| B000 | JDK 21 | Foojay resolver auto-descarga |
| B001 | JAVA_HOME | gw/gw.bat wrappers |
| B002 | Maven de mods | Modrinth API + maven.ftb.dev + CurseForge manual |
| B004 | GameStages no existe 1.21.1 | ProgressiveStages |
| B005 | Iron Chests no existe 1.21.1 | Excluido. Vanilla + Storage Drawers suficiente |
| B006 | Alex's Mobs crash | Removido (crash Citadel + no encaja en pilares) |
| B007 | GDD pendiente | GDD v2 completo |
| B008 | Mods de combate | NO agregar. RPG Series + Champions Unofficial |
| B009 | Mods de bosses | NO agregar. Solo custom servo_dungeons |
| B010 | Dungeons mod | Dim Dungeons removido (ARR). 100% custom servo_dungeons |
| B011 | Mods sin descargar | Todos descargados |
| B012 | 6 vs 8 capitulos | 8 capitulos confirmado |
| B013 | RPG Series sin testear | Carga correctamente con 15 dependencias |
| B015 | Jewelry vs Custom Curios overlap | Jewelry = rings/necklaces, servo_core = belt/back/feet/head |
