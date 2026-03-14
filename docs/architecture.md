# Arquitectura de Mods — Modpack Servo

> Fuente: Sesion 14 (2026-03-12)
> Relacionado: [GDD](gdd-v2.md), [TODO](TODO.md)

## Vision

7 mods separados (JARs independientes). 6 standalone reutilizables + 1 glue del modpack.
Los mods standalone son configurables via JSON/datapacks y no dependen de servo_core.

## Mods

| # | Mod ID | JAR | Standalone | Descripcion |
|---|--------|-----|-----------|-------------|
| 1 | `servo_packaging` | servo-packaging | Si | Cajas de carton, Empacadora. Sin Create dep. |
| 2 | `servo_create` | servo-create | Si | Addon: compat Create <-> packaging (funnels, belts, deployers) |
| 3 | `servo_delivery` | servo-delivery | Si | Terminal de Entrega (Space Elevator), multibloque |
| 4 | `servo_cooking` | servo-cooking | Si | 4 workstations: Blender, Moldes, Drink Maker, Horno Avanzado |
| 5 | `servo_dungeons` | servo-dungeons | Si | Dungeons + Bosses + void instances + ritual altars |
| 6 | `servo_mart` | servo-mart | Si | Tienda/catalogo dinamico configurable |
| 7 | `servo_core` | servo-core | No (glue) | Tokens, accesorios, gacha, progression |

## Grafo de dependencias

```
                    +---------------+
                    |  servo_core   | (tokens, accessories,
                    |               |  gacha, progression)
                    +--+--+--+--+--+
           soft deps   |  |  |  |
        +--------------+  |  |  +--------------+
        v                 |  v                  v
+---------------+ +-------+--------+  +-----------------+
|servo_dungeons | |servo_delivery  |  | servo_cooking   |
|dungeons+bosses| |space elevator  |  | 4 workstations  |
|GeckoLib       | +-------+--------+  +-----------------+
+---------------+         | hard dep
                          v
    +----------+  +----------------+  +-------------+
    |servo_mart|  |servo_packaging |  |servo_create |
    | tienda   |  |cajas de carton |  |Create compat|
    +----+-----+  |PURO, sin Create|  +--+------+---+
         | hard   +----------------+     |hard  |hard
         |               ^               |      |
         +-------+-------+        Create-+      |
                 |                               |
                 +-------------------------------+
```

## Contenido por mod

### servo_packaging
- **Items**: Carton Plano, Caja Abierta, Caja de Envio (DataComponent BoxContents)
- **Bloques**: Empacadora (packing_station) — GUI con 2 slots + barra de progreso (solo dobla carton); Caja Abierta (open_box) — bloque placeable, interaccion inmersiva sin GUI
- **Tags**: `#servo_packaging:packable`, `#servo_packaging:pack_size_1/8/16`, `#servo_packaging:category/food/crops/processed/magic/special`
- **Config**: Cantidades por categoria, items empacables
- **Deps**: ninguna
- **Estado**: CODIGO COMPLETO (v0.3.0)

### servo_create
- **Agrega**: Inventario por caras a Empacadora (funnel/belt I/O), Deployer compat
- **Deps**: servo_packaging (hard), Create (hard)

### servo_delivery
- **Bloques**: Terminal de Entrega, Puerto de Entrega, Base, Antena
- **GUI**: Pantalla de progreso con lista de entregas
- **Config**: Entregas por capitulo via JSON datapack
- **Deps**: servo_packaging (hard — acepta Cajas de Envio)

### servo_cooking
- **Bloques**: Blender, Moldes de Postres, Drink Maker, Horno Avanzado
- **Recipe Types**: 4 tipos custom (JSON)
- **Items**: Moldes (7 tipos x 5 rarezas)
- **Deps**: ninguna

### servo_dungeons
- **Sistema**: Void dimension manager, instance lifecycle
- **Bloques**: Dungeon Pedestal, Dungeon Rune, Boss Altar
- **Items**: 4 Dungeon Keys, 8 Boss Keys, Esencia de Dungeon, Fragmento de Cristal, 8 Boss Drops
- **Entities**: 8 bosses GeckoLib con AI, fases, scaling
- **Proc-gen**: Templates .nbt, 7 tipos de sala
- **Deps**: GeckoLib (hard), FTB Teams (soft)

### servo_mart
- **Bloques**: ServoMart (tablet/catalogo)
- **GUI**: Catalogo con categorias, precios, desbloqueo por stage
- **Config**: Catalogo via JSON datapack (items, precios, moneda, stages)
- **Deps**: servo_packaging (hard — entrega en cajas)

### servo_core (glue)
- **Items**: Pepe Coin
- **Curios**: 4 slots (belt/back/feet/head), ~65 accesorios custom
- **Gacha**: Pity tracker (PlayerCapability)
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
+-- servo-packaging/
|   +-- build.gradle
|   +-- gradle.properties    (mod_id, mod_name, etc.)
|   +-- src/main/java/com/servo/packaging/
|   +-- src/main/templates/META-INF/neoforge.mods.toml
+-- servo-core/
|   +-- (misma estructura)
+-- servo-delivery/
+-- servo-cooking/
+-- servo-create/
+-- servo-dungeons/
+-- servo-mart/
+-- modpack/                 (KubeJS, configs, mod JARs)
+-- docs/
+-- scripts/
```

## Orden de desarrollo

| Fase | Mod | Estado | Razon |
|------|-----|--------|-------|
| 1 | servo_packaging | **COMPLETO (v0.3.0)** | 0 deps, desbloquea 3 mods |
| 2 | servo_delivery | **in-progress — scaffold completo** | Packaging + Delivery = loop core del modpack |
| 3 | servo_cooking | pendiente | Contenido para cocinar -> empacar -> entregar |
| 4 | servo_create | pendiente | Automation: belts + funnels + deployers |
| 5 | servo_mart | pendiente | Tienda, menos urgente |
| 6 | servo_dungeons | pendiente | El mas complejo, independiente |
| 7 | servo_core | scaffold | Glue, al final cuando todo existe |

## Patrones compartidos

### Data-driven via tags y datapacks
Cada mod es configurable sin tocar codigo:
- `servo_packaging`: tags definen que es empacable y cantidades
- `servo_delivery`: JSON define entregas por capitulo
- `servo_cooking`: JSON recipes para cada workstation
- `servo_dungeons`: .nbt templates, JSON loot tables, boss configs
- `servo_mart`: JSON catalogo

### DataComponents (no NBT)
MC 1.21.1 usa DataComponents. Shipping Box usa un DataComponent custom `BoxContents`.

### Sin GUI donde sea posible
Cajas abiertas y workstations de cocina priorizan interaccion inmersiva (click derecho, items visibles en el mundo).
Excepcion: Empacadora tiene GUI de 2 slots (necesaria para el proceso de doblado con barra de progreso).
