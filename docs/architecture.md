# Arquitectura de Mods — Modpack Servo

> Fuente: Sesion 14 (2026-03-12). Actualizado sesion 17 (2026-03-17): 7 mods → 4 mods.
> Relacionado: [GDD](gdd-v2.md), [GitHub Issues](https://github.com/servo98/modpack-servo/issues)

## Vision

4 mods separados (JARs independientes). 3 standalone reutilizables + 1 glue del modpack.
Los mods standalone son configurables via JSON/datapacks y no dependen de servo_core.

## Mods

| # | Mod ID | JAR | Standalone | Descripcion |
|---|--------|-----|-----------|-------------|
| 1 | `servo_delivery` | servo-delivery | Si | Terminal de Entrega (Space Elevator), multibloque. Acepta items directos. |
| 2 | `servo_cooking` | servo-cooking | Si | Workstations custom de cocina (Prep Station, Licuadora, Wok, Baker's Oven) |
| 3 | `servo_dungeons` | servo-dungeons | Si | Dungeons + Bosses + void instances + ritual altars |
| 4 | `servo_core` | servo-core | No (glue) | Tokens, accesorios, gacha, PepeMart, progression, champions |

### Mods eliminados

| Mod ID | Razon |
|--------|-------|
| `servo_packaging` | ELIMINADO. Create 6.0 tiene packaging nativo (Packager, Cardboard Package). Space Elevator acepta items directos. |
| `servo_create` | ELIMINADO. Su unica funcion era compat packaging con Create. Sin servo_packaging, no tiene razon de existir. |
| `servo_mart` | ABSORBIDO en servo_core. PepeMart es ahora 1 bloque (`servo_core:pepe_mart`) dentro del mod glue. |

## Grafo de dependencias

```
                    +---------------+
                    |  servo_core   | (tokens, accessories,
                    |               |  gacha, PepeMart, progression)
                    +--+--+--+------+
           soft deps   |  |  |
        +--------------+  |  +------+
        v                 v         v
+---------------+ +----------------+ +-----------------+
|servo_dungeons | |servo_delivery  | | servo_cooking   |
|dungeons+bosses| |space elevator  | | workstations    |
|GeckoLib       | |acepta items    | |                 |
+---------------+ |directos (nativo| +-----------------+
                  |Create 6.0)     |
                  +----------------+
```

## Contenido por mod

### servo_delivery
- **Bloques**: Terminal de Entrega, Puerto de Entrega, Base, Antena
- **GUI**: Pantalla de progreso con lista de entregas por capitulo
- **Insercion**: items directos por click derecho o por automation (hopper/funnel/belt)
- **Config**: Entregas por capitulo via JSON datapack
- **Deps**: ninguna (aceptaba Cajas antes; ahora acepta raw items directamente)

### servo_cooking
- **Bloques**: Prep Station, Licuadora, Wok, Baker's Oven
- **Recipe Types**: uno por workstation (KubeJS o custom recipe type)
- **Create compat**: runtime recipe injection (Slice&Dice pattern). Deployer+Basin para workstations que lo soporten.
- **Deps**: ninguna

### servo_dungeons
- **Sistema**: Void dimension manager, instance lifecycle, multiples instancias via offsets
- **Bloques**: Pedestal Unificado (dungeon + boss), Runa de Dungeon, Exit Portal
- **Items**: 4 Dungeon Keys, 8 Boss Keys, Esencia de Dungeon, Fragmento de Cristal, 8 Boss Drops
- **Entities**: 8 bosses GeckoLib con AI, fases, scaling
- **Entrada**: Beam vertical sobre altar (tocar = entrar). Sin restriccion de teams.
- **Proc-gen**: Templates .nbt, 7 tipos de sala, spawning programatico de champions por tier
- **Deps**: GeckoLib (hard), Champions Unofficial API (soft)

### servo_core (glue)
- **Items**: Pepe Coin
- **Bloques**: PepeMart (`servo_core:pepe_mart`) — tienda con catalogo fijo, precios en materiales, stage-gated
- **Curios**: 4 slots (belt/back/feet/head), ~65 accesorios custom
- **Gacha**: Pity tracker (AttachedData por player)
- **Progression**: Conecta Terminal + Boss kill -> grant stage
- **Champion post-processing**: `EntityJoinLevelEvent` (LOWEST) → lee stage del player mas cercano via ProgressiveStages → downgrade champions que excedan tier permitido. Cache `Map<UUID, Integer>` actualizado via `StageChangeEvent`. En dungeons, tier basado en llave usada.
- **Deps**: todos los demas (soft), Curios API, ProgressiveStages, Champions Unofficial (API), FTB Quests, Bloo's Gacha Machine

## Estructura Gradle

Multi-project build. Cada subproyecto es un mod NeoForge independiente.
Root project configura runs (client/server) y carga todos los mods en dev.

```
modpack-servo/
+-- settings.gradle          (includes subprojects)
+-- build.gradle             (root: runs, no source code)
+-- gradle.properties        (versiones compartidas)
+-- servo-core/
|   +-- build.gradle
|   +-- gradle.properties    (mod_id, mod_name, etc.)
|   +-- src/main/java/com/servo/core/
|   +-- src/main/templates/META-INF/neoforge.mods.toml
+-- servo-delivery/
+-- servo-cooking/
+-- servo-dungeons/
+-- modpack/                 (KubeJS, configs, mod JARs)
+-- docs/
+-- scripts/
```

## Orden de desarrollo

| Fase | Mod | Estado | Razon |
|------|-----|--------|-------|
| 1 | servo_delivery | **in-progress — scaffold completo** | Loop core del modpack |
| 2 | servo_cooking | pendiente | Contenido para producir -> entregar |
| 3 | servo_dungeons | pendiente | El mas complejo, independiente |
| 4 | servo_core | scaffold | Glue, al final cuando todo existe |

## Patrones compartidos

### Data-driven via tags y datapacks
Cada mod es configurable sin tocar codigo:
- `servo_delivery`: JSON define entregas por capitulo
- `servo_cooking`: TBD
- `servo_dungeons`: .nbt templates, JSON loot tables, boss configs
- `servo_core`: JSON catalogo de PepeMart

### DataComponents (no NBT)
MC 1.21.1 usa DataComponents. Usar para cualquier item con datos custom.

### Sin GUI donde sea posible
Workstations de cocina priorizan interaccion inmersiva (click derecho, items visibles en el mundo).
Excepciones: Terminal de Entrega y PepeMart tienen GUI (necesaria para su funcion).
