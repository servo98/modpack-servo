# Modpack Servo - Instrucciones para Claude

## Proyecto
Modpack progresivo MC 1.21.1 NeoForge con mod custom (servo_core).
8 capitulos, ~240 horas, 3 pilares: Cocina/Granja + Create/Automatizacion + RPG/Clases.
GDD completo en `docs/gdd-v2.md`. Tareas en `docs/TODO.md`.

## Comandos clave
- Build: `./gw build` (wrapper que setea JAVA_HOME, desde raiz)
- Run server: `./gw runServer`
- Run client: `./gw runClient`
- Run client (background, logs): `./gw runClient 2>&1 | tee run/client-latest.log`
- NOTA: usar `./gw` (no `./gradlew` — JAVA_HOME del sistema apunta a JDK incorrecto)
- Hot reload KubeJS: `/reload` en consola del server
- Hot reload texturas: `F3+T` en cliente
- Hot reload Java: JBR hotswap (save en IntelliJ)

## Logs y debugging
- **Logs del juego**: `run/logs/latest.log`
- **Logs KubeJS**: `run/logs/kubejs/server.log` y `client.log`
- **Crash reports**: `run/crash-reports/crash-*.txt`
- **Tip**: Crash al arrancar = leer crash report. Sin crash report = error en logs de Gradle (terminal).

## KubeJS comandos utiles
- `/kubejs hand` — info del item en mano (ID, NBT, tags)
- `/kubejs stages list <player>` / `add <player> <stage>` — ver/dar stages
- `/kubejs errors server` — ver errores de scripts
- `/kubejs reload server-scripts` — recarga scripts (para recetas/tags usar `/reload`)
- `/kubejs export debug` — exporta registros a `run/local/kubejs/export/`
- **Ver recetas**: EMI en el cliente (tecla R = receta, U = usos)

## Mapa de docs
| Tema | Ubicacion | Descripcion |
|------|-----------|-------------|
| Tareas | `docs/TODO.md` | Lista maestra con estado por tarea |
| GDD | `docs/gdd-v2.md` | Overview del modpack completo |
| Mods | `docs/design/mod-decisions.md` | Cada mod con su razon de ser |
| Balance | `docs/balance/*.md` | combat-scaling, gacha-rates, accessories, rpg-weapon-stats |
| RPG Series | `docs/mod-data/rpg-series-content.md` | Clases, spells, tiers (datos de JARs) |
| Blockers | `docs/blockers.md` | Problemas que bloquean progreso |
| Historial | `git log` | Commits = registro de sesiones (reemplaza session-log.md) |
| Mecanicas | `docs/mechanics/*.md` | 21 docs, una por mecanica |
| Capitulos | `docs/chapters/ch*.md` | 8 docs (ch1-ch8) con quests por capitulo |
| Ideas originales | `docs/reference/mods-ideas*.md` | Brainstorming del usuario (solo archivo) |

## Estructura de codigo
- `src/` — mod Java (servo_core)
- `modpack/kubejs/` — scripts KubeJS
- `modpack/config/` — configs de mods
- `modpack/mods/` — JARs de mods
- `scripts/` — utilidades (extract-mod-content.py, download-mods.py)

## Convenciones
- Java: PascalCase clases, camelCase metodos, SCREAMING_SNAKE constantes
- KubeJS: camelCase funciones, archivos con prefijo numerico para orden
- Mod ID: `servo_core` | Package: `com.servo.core`
- Recipe namespace: `servo_core:`
- Lang: siempre espanol (es_mx) e ingles (en_us)

## Agentes disponibles (.claude/agents/)
| Agente | Cuando usarlo |
|--------|--------------|
| `doc-keeper` | **OBLIGATORIO** al final de sesion. Al cambiar diseno, agregar/quitar mods o docs. |
| `kubejs-writer` | Al crear/editar scripts en `modpack/kubejs/`. Preferir sobre escribir KubeJS directo. |
| `balance-checker` | Al cambiar numeros de balance o cuando el usuario pregunte si algo esta balanceado. |
| `mod-researcher` | ANTES de sugerir cualquier mod nuevo. Verifica NeoForge 1.21.1. |

## Persistencia entre sesiones

### Al INICIO
1. Leer `docs/TODO.md` (tareas y estado)
2. Leer `docs/blockers.md` (problemas activos)
3. `git log --oneline -10` (contexto de sesiones recientes via commits)

### Al FINAL
1. Invocar `doc-keeper` (sync de docs, redundancias, consistencia)
2. Commitear cambios con mensaje descriptivo (git = registro de sesiones)
3. Si cambio diseno/estado → actualizar MEMORY.md
4. Si se resolvio un blocker → mover a "Resueltos" en blockers.md

### Reglas de archivos de estado
- **TODO.md** = fuente de verdad de tareas. Cada tarea tiene estado (pending/planned/needs-decision/done).
- **blockers.md** = fuente de verdad de problemas. Solo problemas que bloquean progreso real.
- **git log** = registro de sesiones. Cada commit documenta que se hizo. NO usar docs/session-log.md.
- NO repetir la misma info en multiples archivos.

## Flujo de testing
| Cambio | Como verificar |
|--------|---------------|
| Java (servo_core) | `./gw build` → `./gw runClient` → probar in-game |
| KubeJS scripts | `/reload` → verificar en EMI (R = receta, U = usos) |
| Items custom | `/give @s servo_core:item_name` en creative |
| Progression/stages | Mundo nuevo → `/kubejs stages add @s servo_ch2` → verificar |
| Configs de mods | Reiniciar cliente |
| Mods nuevos/eliminados | Reiniciar cliente → verificar en EMI |

## NeoForge Version
- MC: 1.21.1 | NeoForge: 21.1.219 | JDK: 21 (Foojay) | Gradle: 9.2.1
- Build: build.gradle (Groovy, NOT Kotlin DSL) | ModDevGradle: 2.0.140 | Parchment: 2024.11.17
