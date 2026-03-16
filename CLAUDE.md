# Modpack Servo - Instrucciones para Claude

## Proyecto
Modpack progresivo MC 1.21.1 NeoForge con 7 mods custom (arquitectura multi-mod).
8 capitulos, ~240 horas, 3 pilares: Cocina/Granja + Create/Automatizacion + RPG/Clases.
GDD completo en `docs/gdd-v2.md`. Arquitectura en `docs/architecture.md`. Tareas en [GitHub Issues](https://github.com/servo98/modpack-servo/issues).

## Comandos clave
- Build: `./gw build` (wrapper que setea JAVA_HOME, desde raiz)
- Run server: `./gw runServer` (auto-copia server.properties + eula.txt a run/)
- Run client: `./gw runClient`
- Run client (background, logs): `./gw runClient 2>&1 | tee run/client-latest.log`
- Run server (background, logs): `./scripts/start-server.sh --background`
- RCON interactive: `python scripts/rcon.py -i` (password: servo-dev, port: 25575)
- RCON single cmd: `python scripts/rcon.py "function servo_core:test_all_kits"`
- Run tests: `python scripts/run-tests.py` (smoke: `--smoke`, por mod: `--mod packaging`)
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
| Tareas | [GitHub Issues](https://github.com/servo98/modpack-servo/issues) | Issues con labels por mod y tipo. Consultar con `gh issue list` |
| GDD | `docs/gdd-v2.md` | Overview del modpack completo |
| Arquitectura | `docs/architecture.md` | 7 mods custom, dependencias, orden de desarrollo |
| Mods | `docs/design/mod-decisions.md` | Cada mod con su razon de ser |
| Balance | `docs/balance/*.md` | combat-scaling, gacha-rates, accessories, rpg-weapon-stats |
| RPG Series | `docs/mod-data/rpg-series-content.md` | Clases, spells, tiers (datos de JARs) |
| Historial | `git log` | Commits = registro de sesiones (reemplaza session-log.md) |
| Assets | `assets/` | Drop folder para artista. Subcarpetas por mod. Ver `assets/README.md` |
| Mecanicas | `docs/mechanics/*.md` | 21 docs, una por mecanica |
| Capitulos | `docs/chapters/ch*.md` | 8 docs (ch1-ch8) con quests por capitulo |

## Estructura de codigo
- `servo-packaging/` — mod Java standalone (COMPLETO v0.3.0)
- `servo-delivery/` — mod Java in-progress (scaffold+GUI completo)
- `servo-core/` — mod Java glue (scaffold)
- `servo-create/` — mod Java (scaffold completo, requiere testing)
- `servo-dungeons/` — mod Java (scaffold parcial — altar, keys, dimension, room gen)
- Futuros: `servo-cooking/`, `servo-mart/`
- `modpack/kubejs/` — scripts KubeJS
- `modpack/config/` — configs de mods
- `modpack/mods/` — JARs de mods
- `scripts/` — utilidades (extract-mod-content.py, download-mods.py, rcon.py, run-tests.py)
- `server/` — configs de server dev (server.properties, eula.txt, ops.json)

## Convenciones
- Java: PascalCase clases, camelCase metodos, SCREAMING_SNAKE constantes
- KubeJS: camelCase funciones, archivos con prefijo numerico para orden
- Mod IDs: `servo_packaging`, `servo_delivery`, `servo_core`, `servo_cooking`, `servo_create`, `servo_dungeons`, `servo_mart`
- Packages: `com.servo.packaging`, `com.servo.delivery`, `com.servo.core`, etc.
- Recipe namespaces: `servo_packaging:`, `servo_delivery:`, `servo_core:`, etc.
- Lang: siempre espanol (es_mx) e ingles (en_us)

## Agentes disponibles (.claude/agents/)
| Agente | Cuando usarlo |
|--------|--------------|
| `doc-keeper` | **OBLIGATORIO** al final de sesion. Al cambiar diseno, agregar/quitar mods o docs. |
| `kubejs-writer` | Al crear/editar scripts en `modpack/kubejs/`. Preferir sobre escribir KubeJS directo. |
| `balance-checker` | Al cambiar numeros de balance o cuando el usuario pregunte si algo esta balanceado. |
| `mod-researcher` | ANTES de sugerir cualquier mod nuevo. Verifica NeoForge 1.21.1. |
| `issue-manager` | Al crear/actualizar issues. Detecta duplicados, mantiene formato estandar. |
| `accessory-modeler` | Al crear modelos 3D de accesorios (gorros, cinturones, anillos, mochilas) via Blockbench MCP. |
| `quest-builder` | Al generar archivos SNBT de FTB Quests a partir de docs/chapters/. |
| `server-tester` | Testea mods via RCON en server. Items, stages, mcfunctions, logs. Sin necesidad de cliente. |

## Comandos disponibles (.claude/commands/)
| Comando | Cuando usarlo |
|---------|--------------|
| `/todo [idea]` | Capturar tareas futuras rapido. Invoca `issue-manager` con `priority:low`. |
| `/session-start` | Inicio de sesion. Muestra issues y estado de mods. |

## Persistencia entre sesiones

### Al INICIO
Usar `/session-start` o ejecutar manualmente:
1. `gh issue list --repo servo98/modpack-servo --state open --limit 20` (tareas pendientes)

### Al FINAL
1. Invocar `doc-keeper` (sync de docs, redundancias, consistencia)
2. Commitear cambios con mensaje descriptivo (git = registro de sesiones)
3. Si cambio diseno/estado → actualizar MEMORY.md

### Reglas de archivos de estado
- **GitHub Issues** = fuente de verdad de tareas y blockers. Filtrar por label: `gh issue list --label "mod:delivery"`. **NO cerrar manualmente** — se cierran solos al pushear commits con `Fixes #X` en el mensaje.
- **git log** = registro de sesiones. Cada commit documenta que se hizo.
- NO repetir la misma info en multiples archivos.

## Flujo de testing
| Cambio | Como verificar |
|--------|---------------|
| Java (cualquier mod) | `./gw build` → `./gw runServer` → `python scripts/run-tests.py --smoke` |
| KubeJS scripts | RCON: `reload` → verificar con give/function |
| Items custom (cualquier mod) | RCON: `function servo_core:test_all_kits` o `give @a <mod>:<item> 1` |
| Progression/stages | RCON: `function servo_core:stage_unlock` / `stage_reset` |
| Flujo completo por mod | `python scripts/run-tests.py --mod <nombre>` |
| Test suite completo | `python scripts/run-tests.py --all` |
| Verificacion visual (texturas, GUI, EMI) | `./gw runClient` → probar in-game |
| Configs de mods | Reiniciar server |
| Mods nuevos/eliminados | Reiniciar server → `--smoke` |

### Server de desarrollo
- Config: `server/server.properties` (RCON habilitado, port 25575, password: servo-dev)
- Start: `./gw runServer` (auto-copia configs a run/) o `./scripts/start-server.sh`
- RCON: `python scripts/rcon.py -i` (interactivo) o `python scripts/rcon.py "comando"`

### mcfunctions de testing
| Funcion | Que hace |
|---------|----------|
| `servo_core:test_all_kits` | Da TODOS los test kits de todos los mods |
| `servo_core:stage_unlock` | Desbloquea ch1-ch8 |
| `servo_core:stage_reset` | Resetea a ch1 |
| `servo_core:test_stages` | Info de stages + comandos utiles |
| `servo_packaging:test_kit` | Items de packaging |
| `servo_delivery:test_kit` | Bloques multibloque + shipping boxes |
| `servo_core:test_kit` | Coins, keys, esencias, cristales |
| `servo_create:test_kit` | Materiales deployer/basin + Create components |
| `servo_dungeons:test_kit` | Dungeon essence |
| `servo_mart:test_kit` | Bloque PepeMart |

## NeoForge Version
- MC: 1.21.1 | NeoForge: 21.1.219 | JDK: 21 (Foojay) | Gradle: 9.2.1
- Build: build.gradle (Groovy, NOT Kotlin DSL) | ModDevGradle: 2.0.140 | Parchment: 2024.11.17
